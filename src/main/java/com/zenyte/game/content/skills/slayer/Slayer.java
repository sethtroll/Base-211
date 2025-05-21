package com.zenyte.game.content.skills.slayer;

import com.google.common.base.Preconditions;
import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.SkillcapePerk;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.CatacombsOfKourend;
import com.zenyte.game.world.region.area.Keldagrim;
import com.zenyte.plugins.drop.slayer.WildernessAssignmentDropProcessor;
import com.zenyte.plugins.drop.slayer.WildernessAssignmentDropProcessor.Emblem;
import com.zenyte.processor.Listener;
import com.zenyte.processor.Listener.ListenerType;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.enums.Enums;
import mgi.types.config.items.ItemDefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Kris | 22. juuli 2018 : 15:01:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
@StaticInitializer
public class Slayer {
    static final int LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT = 4538;
    static final boolean HALVED_QUANTITIES = true;
    private static final short[] BANNED_SLOT_VARBITS = new short[]{3209, 3210, 3211, 3212, 4441, 5023};
    private static final long FULL_EXTENSION_UNLOCK_HASH;
    private static final int POINTS_REQUIRED_FOR_BLOCKING = 100;
    private static final int POINTS_REQUIRED_FOR_CANCELLATION = 30;
    private static final int SLAYER_POINTS_BIT = 4068;
    private static final int TASK_AMOUNT_VAR = 261;
    private static final int TASK_INDEX_VAR = 262;
    private static final int UNLOCK_REWARDS_FIRST_VARP = 1076;
    private static final int UNLOCK_REWARDS_SECOND_VARP = 1344;
    private static final boolean DOUBLE_POINTS = true;

    static {
        long hash = 0;
        for (final Integer value : Enums.TASK_EXTENSION_ENUM.getValues().values()) {
            hash |= 1L << value;
        }
        FULL_EXTENSION_UNLOCK_HASH = hash;
    }

    private Assignment assignment;
    private Int2ObjectOpenHashMap<RegularTask> bannedTasks;
    private SlayerMaster master;
    private transient Player partner;
    private transient Player player;
    private String lastAssignmentName;

    public Slayer(final Player player) {
        this.player = player;
        bannedTasks = new Int2ObjectOpenHashMap<>(6);
        master = SlayerMaster.TURAEL;
    }

    @Listener(type = ListenerType.LOGOUT)
    private static void onLogout(final Player player) {
        final Slayer slayer = player.getSlayer();
        if (slayer.partner != null) {
            slayer.partner.getSlayer().setPartner(null);
            slayer.partner.sendMessage("Your Slayer partner has logged out.");
        }
    }

    void addSlayerPoints(final int amount) {
        final int currentPoints = getSlayerPoints();
        final int modifiedPoints = currentPoints + amount;
        player.addAttribute("slayer_points", Math.max(0, modifiedPoints));
        refreshSlayerPoints();
    }

    public void checkAssignment(final NPC npc) {
        if (assignment == null) {
            return;
        }
        assignment.checkAssignment(npc.getName(player), npc);
    }

    void confirmFullExtensionUnlock() {
        final int cost = getRemainingExtensionsCost();
        final int currentPoints = getSlayerPoints();
        if (currentPoints < cost) {
            player.sendMessage("You don't have enough Slayer Points to unlock all the extensions. You need " + cost + " Slayer Points.");
            return;
        }
        setUnlocksHash(getUnlocksHash() | FULL_EXTENSION_UNLOCK_HASH);
        addSlayerPoints(-cost);
        refreshRewards();
        player.sendMessage("Congratulations, you've unlocked all the extensions.");
    }

    void confirmPurchase(final int slotId) {
        if (!Enums.TASK_NAMES_ENUM.getValue(slotId).isPresent() || isUnlocked(slotId)) return;
        final int pointsRequired = Enums.TASK_COST_ENUM.getValue(slotId).orElseThrow(Enums.exception());
        final int currentPoints = getSlayerPoints();
        if (currentPoints < pointsRequired) {
            player.sendMessage("You do not have enough Slayer Points to purchase '" + Enums.TASK_NAMES_ENUM.getValue(slotId).orElseThrow(Enums.exception()) + "'.");
            return;
        }
        addSlayerPoints(-pointsRequired);
        setUnlocksHash(getUnlocksHash() | (1L << slotId));
        refreshRewards();
        player.sendMessage("Congratulations, you've unlocked '" + Enums.TASK_NAMES_ENUM.getValue(slotId).orElseThrow(Enums.exception()) + "'.");
    }

    void confirmTaskBlock() {
        if (assignment == null) {
            player.sendMessage("You do not have a Slayer assignment right now.");
            return;
        }
        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_BLOCKING) {
            player.sendMessage("You do not have enough Slayer Points to block your task. You need " + POINTS_REQUIRED_FOR_BLOCKING + " Slayer Points.");
            return;
        }
        final IntAVLTreeSet availableSlots = getAvailableBlockSlots();
        if (availableSlots.isEmpty()) {
            player.sendMessage("You don't have any empty slots to block this task!");
            return;
        }
        RegularTask task;
        if (assignment.getTask() instanceof BossTask) {
            task = RegularTask.BOSS;
        } else {
            task = (RegularTask) assignment.getTask();
        }
        bannedTasks.put(availableSlots.firstInt(), task);
        addSlayerPoints(-POINTS_REQUIRED_FOR_BLOCKING);
        assignment = null;
        lastAssignmentName = null;
        refreshCurrentAssignment();
        refreshBlockedTasks();
    }

    void confirmTaskCancellation() {
        if (assignment == null) {
            player.sendMessage("You do not have a Slayer assignment right now.");
            return;
        }
        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_CANCELLATION && !player.getInventory().containsItem(new Item(ItemId.SLAYER_SKIP_SCROLL, 1))) {
            player.sendMessage("You do not have enough Slayer Points to cancel your task. You need " + POINTS_REQUIRED_FOR_CANCELLATION + " Slayer Points.");
            return;
        }
        assignment = null;
        lastAssignmentName = null;
        if(player.getInventory().containsItem(new Item(ItemId.SLAYER_SKIP_SCROLL, 1))) {
            player.getInventory().deleteItem(ItemId.SLAYER_SKIP_SCROLL, 1);
            player.getInventory().refresh();
            player.sendMessage("You consumed a Task Skip Scroll instead of your slayer points.");
        } else {
            addSlayerPoints(-POINTS_REQUIRED_FOR_CANCELLATION);
        }

        player.sendMessage("Your Slayer assignment has been cancelled.");
        refreshCurrentAssignment();
    }

    public void removeTask() {
        assignment = null;
        lastAssignmentName = null;
        player.sendMessage("Your Slayer assignment has been cancelled.");
    }

    void confirmTaskUnblock(final int slotId) {
        if (!bannedTasks.containsKey(slotId)) {
            player.sendMessage("You don't have a Slayer task blocked in that slot.");
            return;
        }
        bannedTasks.remove(slotId);
        refreshBlockedTasks();
    }

    void disable(final int slotId) {
        final Optional<String> entry = Enums.TASK_NAMES_ENUM.getValue(slotId);
        if (!entry.isPresent()) {
            throw new RuntimeException("Incorrect task index: " + slotId);
        }
        final String name = entry.get();
        if (name.equals("Bigger and Badder")) {
            player.getSettings().toggleSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD);
            return;
        } else if (name.equals("Stop the Wyvern")) {
            player.getSettings().toggleSetting(Setting.STOP_THE_WYVERN_SLAYER_REWARD);
            return;
        }
        if (!Enums.TASK_DISABLE_ENUM.getValue(slotId).isPresent() || !isUnlocked(slotId)) {
            return;
        }
        player.sendMessage("You've disabled the extension '" + name + "'.");
        setUnlocksHash(getUnlocksHash() & ~(1L << slotId));
        refreshRewards();
    }

    public boolean isBiggerAndBadder() {
        return isUnlocked("Bigger and Badder") && !player.getBooleanSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD);
    }

    void finishAssignment() {
        player.sendMessage("You have finished your Slayer assignment. Talk to " + Utils.formatString(master.toString()) + " for a new one.");
        final SlayerMaster master = assignment.getMaster();
        final int completedTasks = player.getNumericAttribute("completed tasks").intValue() + 1;
        player.addAttribute("completed tasks", completedTasks);
        final int completedInARow = player.getNumericAttribute("completed tasks in a row").intValue() + 1;
        player.addAttribute("completed tasks in a row", completedInARow);
        final int multiplier = master.getMultiplier(completedInARow);
        int pointsPerTask = master.getPointsPerTask();
        if (master.equals(SlayerMaster.KONAR_QUO_MATEN) && DiaryReward.RADAS_BLESSING4.eligibleFor(player)) {
            pointsPerTask = 20;
        }
        int amount = master.getPointsPerTask() * multiplier;
        if (DOUBLE_POINTS) {
            amount *= 2;
        }
        if (player.getPerkManager().isValid(PerkWrapper.MASTER_SLAYER)) {
            amount *= 1.15;
        }
        addSlayerPoints(amount);
        if (master.equals(SlayerMaster.KRYSTILIA)) {
            setKrystiliaStreak(getKrystiliaStreak() + 1);
            player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_WILDERNESS_ASSIGNMENTS);
            Emblem emblem = null;
            for (int slot = 0; slot < 28; slot++) {
                final Item item = player.getInventory().getItem(slot);
                if (item == null) {
                    continue;
                }
                final WildernessAssignmentDropProcessor.Emblem e = Emblem.get(item);
                if (e != null && !e.equals(Emblem.T10)) {
                    emblem = e;
                    break;
                }
            }
            final int random = Utils.random(assignment.getInitialAmount() + 55);
            if (emblem != null) {
                if (random > 50) {
                    final int next = emblem.next();
                    final String name = ItemDefinitions.getOrThrow(emblem.getId()).getName();
                    if (next != -1) {
                        player.getInventory().deleteItemsIfContains(new Item[]{new Item(emblem.getId())}, () -> {
                            player.getInventory().addItem(next, 1);
                            player.sendMessage("Krystilia has upgraded your " + name + " as a reward for completing a Wilderness slayer assignment.");
                        });
                    }
                } else {
                    player.sendMessage("Krystilia was unsuccessful in upgrading your emblem. Better luck next time!");
                }
            } else {
                player.sendMessage("No emblems were able to get upgraded when completing the Wilderness slayer assignment.");
            }
        } else if (master.equals(SlayerMaster.NIEVE)) {
            player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_NIEVE_ASSIGNMENTS);
        } else if (master.equals(SlayerMaster.DURADEL)) {
            player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_DURADEL_ASSIGNMENTS);
        } else if (master.equals(SlayerMaster.MAZCHNA)) {
            player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_MAZCHNA_ASSIGNMENTS);
        }
        player.sendMessage(Colour.RED.wrap("You've completed " + completedInARow + " task" + (completedInARow == 1 ? "" : "s") + " and received " + amount + " points, giving you a total of " + getSlayerPoints() + "; return to a Slayer master."));
        if (master.getPointsPerTask() == 0) {
            player.sendMessage(Colour.RED.wrap("Tasks assigned by Turael do not give any slayer points."));
        }
        if (assignment.getTask() instanceof BossTask) {
            player.getSkills().addXp(Skills.SLAYER, 5000);
        }
        assignment = null;
    }

    public List<RegularTask> getAvailableAssignments(final SlayerMaster master) {
        final ArrayList<RegularTask> possibleTasks = new ArrayList<>((int) (RegularTask.VALUES.length / 2.0F));
        final int slayerLevel = player.getSkills().getLevelForXp(Skills.SLAYER);
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        for (final RegularTask $task : RegularTask.VALUES) {
            //Exception as there is no separate spiritual mage in RS; Cannot delete the constant cus it'd throw errors.
            if ($task == RegularTask.SPIRITUAL_MAGE || ($task == RegularTask.SPIRITUAL_CREATURES && bannedTasks.containsValue(RegularTask.SPIRITUAL_MAGE))) {
                continue;
            }
            if (bannedTasks.containsValue($task) || $task == lastAssignment || slayerLevel < $task.getSlayerRequirement() || master != SlayerMaster.KRYSTILIA && this.isCheckingCombat() && player.getSkills().getCombatLevel() < $task.getCombatRequirement()) {
                continue;
            }
            final Task[] $taskSet = $task.getTaskSet();
            for (final Task $set : $taskSet) {
                final Predicate<Player> predicate = $task.getPredicate();
                if ($set.getSlayerMaster() != master || predicate != null && !predicate.test(player)) {
                    continue;
                }
                possibleTasks.add($task);
                break;
            }
        }
        return possibleTasks;
    }

    public Assignment getAssignment(final RegularTask task) {
        if (task == RegularTask.BOSS) {
            return generateBossTask(master);
        }
        return getAssignment(task, master);
    }

    public Assignment generateTask(final SlayerMaster master) {
        int weight = 0;
        int currentWeight = 0;
        final ArrayList<RegularTask> possibleTasks = new ArrayList<>((int) (RegularTask.VALUES.length / 2.0F));
        final int slayerLevel = player.getSkills().getLevelForXp(Skills.SLAYER);
        if (lastAssignmentName != null && SkillcapePerk.SLAYER.isEffective(player) && Utils.random(9) == 0) {
            final SlayerTask task = Assignment.getTask(lastAssignmentName);
            if (task instanceof BossTask) {
                return new Assignment(player, this, task, task.getEnumName(), 0, 0, master);
            }
            final RegularTask regularTask = (RegularTask) task;
            if (!bannedTasks.containsValue(regularTask)) {
                return getAssignment(regularTask, master);
            }
        }
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        for (final RegularTask $task : RegularTask.VALUES) {
            //Exception as there is no separate spiritual mage in RS; Cannot delete the constant cus it'd throw errors.
            if ($task == RegularTask.SPIRITUAL_MAGE || ($task == RegularTask.SPIRITUAL_CREATURES && bannedTasks.containsValue(RegularTask.SPIRITUAL_MAGE))) {
                continue;
            }
            if (bannedTasks.containsValue($task) || lastAssignment == $task || slayerLevel < $task.getSlayerRequirement() || master != SlayerMaster.KRYSTILIA && this.isCheckingCombat() && player.getSkills().getCombatLevel() < $task.getCombatRequirement()) {
                continue;
            }
            final Task[] $taskSet = $task.getTaskSet();
            for (final Task $set : $taskSet) {
                final Predicate<Player> predicate = $task.getPredicate();
                if ($set.getSlayerMaster() != master || predicate != null && !predicate.test(player)) {
                    continue;
                }
                weight += $set.getWeight();
                possibleTasks.add($task);
                break;
            }
        }
        final int $randomTask = Utils.random(weight);
        for (int i = possibleTasks.size() - 1; i >= 0; i--) {
            final RegularTask $task = possibleTasks.get(i);
            final Task $taskSet = $task.getCertainTaskSet(master);
            if ($taskSet == null) continue;
            final int $taskWeight = $taskSet.getWeight();
            if ((currentWeight += $taskWeight) >= $randomTask) {
                if (master.equals(SlayerMaster.VANNAKA)) {
                    player.getAchievementDiaries().update(VarrockDiary.SLAYER_TASK_FROM_VANNAKA);
                } else if (master.equals(SlayerMaster.CHAELDAR)) {
                    player.getAchievementDiaries().update(LumbridgeDiary.GET_SLAYER_TASK_FROM_CHAELDAR);
                } else if (master.equals(SlayerMaster.DURADEL)) {
                    player.getAchievementDiaries().update(KaramjaDiary.SLAYER_TASK_BY_DURADEL);
                } else if (master.equals(SlayerMaster.MAZCHNA)) {
                    player.getAchievementDiaries().update(MorytaniaDiary.GET_A_SLAYER_TASK_FROM_MAZCHNA);
                }
                if ($task == RegularTask.BOSS) {
                    return generateBossTask(master);
                }
                return getAssignment($task, master);
            }
        }
        throw new RuntimeException("Unable to calculate a task for master " + master.toString() + ".");
    }

    public SlayerMaster getAdvisedMaster() {
        final Skills skills = player.getSkills();
        for (int i = SlayerMaster.VALUES.length - 1; i >= 0; i--) {
            final SlayerMaster master = SlayerMaster.VALUES[i];
            if (skills.getCombatLevel() >= master.getCombatRequirement() && skills.getLevelForXp(Skills.SLAYER) >= master.getSlayerRequirement()) {
                return master;
            }
        }
        return SlayerMaster.TURAEL;
    }

    public Assignment getAssignment(final RegularTask task, final SlayerMaster master) {
        final Task taskSet = task.getCertainTaskSet(master);
        Preconditions.checkArgument(taskSet != null);
        int min = taskSet.getMinimumAmount();
        int max = taskSet.getMaximumAmount();
        final RegularTask.Range range = task.getExtendedRange();
        if (range != null) {
            if (this.isUnlocked(range.getExtensionName())) {
                min = range.getMin();
                max = range.getMax();
            }
        }
        final int amount = Utils.random(min, max) / (HALVED_QUANTITIES ? 2 : 1);
        final Class<? extends Area>[] areas = taskSet.getAreas();
        Class<? extends Area> area;
        //Exception for black dragons in Kourend catacombs.
        if (task == RegularTask.BLACK_DRAGONS && areas != null && player.getSkills().getLevel(Skills.SLAYER) < 77) {
            final ObjectArrayList<Class<? extends Area>> areasList = new ObjectArrayList<>();
            for (final Class<? extends Area> a : areas) {
                if (a != CatacombsOfKourend.class) {
                    areasList.add(a);
                }
            }
            area = areasList.isEmpty() ? null : areasList.get(Utils.random(areasList.size() - 1));
        } else {
            area = areas != null ? areas[Utils.random(areas.length - 1)] : null;
        }
        return new Assignment(player, this, task, task.getEnumName(), amount, amount, master, area);
    }

    public int getCompletedTasks() {
        return player.getNumericAttribute("completed tasks").intValue();
    }

    public int getCurrentStreak() {
        return player.getNumericAttribute("completed tasks in a row").intValue();
    }

    public void setCurrentStreak(final int value) {
        player.addAttribute("completed tasks in a row", value);
    }

    public int getKrystiliaStreak() {
        return player.getNumericAttribute("krystilia completed tasks in a row").intValue();
    }

    public void setKrystiliaStreak(final int value) {
        player.addAttribute("krystilia completed tasks in a row", value);
    }

    int getSlayerPoints() {
        return player.getNumericAttribute("slayer_points").intValue();
    }

    public Assignment getTzTokJadAssignment(final SlayerMaster master) {
        return new Assignment(player, this, RegularTask.TZTOK_JAD, RegularTask.TZTOK_JAD.getEnumName(), 1, 1, master);
    }

    public Assignment getTzKalZukAssignment(final SlayerMaster master) {
        return new Assignment(player, this, RegularTask.TZKAL_ZUK, RegularTask.TZKAL_ZUK.getEnumName(), 1, 1, master);
    }

    public void initialize(final Player player, final Player parser) {
        this.player = player;
        bannedTasks = parser.getSlayer().bannedTasks;
        master = parser.getSlayer().master;
        if (parser.getSlayer().assignment != null) {
            assignment = new Assignment();
            assignment.initialize(player, parser.getSlayer().assignment);
        }
    }

    public boolean isAssignable(final SlayerTask task, final SlayerMaster master) {
        final Skills skills = player.getSkills();
        if (skills.getLevel(Skills.SLAYER) < master.getSlayerRequirement() || skills.getCombatLevel() < master.getCombatRequirement()) {
            return false;
        }
        final Predicate<Player> predicate = task.getPredicate();
        return predicate == null || predicate.test(player);
    }

    public boolean isCheckingCombat() {
        return player.getBooleanAttribute("checking combat in slayer");
    }

    public void setCheckingCombat(final boolean value) {
        player.addAttribute("checking combat in slayer", value ? 1 : 0);
    }

    public boolean isCurrentAssignment(final Entity target) {
        if (assignment == null || assignment.getAmount() == 0 || !(target instanceof NPC)) {
            return false;
        }
        return assignment.isValid(player, (NPC) target);
    }

    public boolean isUnlocked(final int index) {
        return ((getUnlocksHash() >> index) & 0x1) == 1;
    }

    public boolean isUnlocked(final String name) {
        if(Enums.TASK_NAMES_ENUM.getKeyIgnoreCase(name).isPresent()){
            if(isUnlocked(Enums.TASK_NAMES_ENUM.getKeyIgnoreCase(name).getAsInt()))
                return true;
            else
                return false;
        }else{
            return false;
        }
    }
    public void openInterface() {
        GameInterface.SLAYER_REWARDS.open(player);
    }

    public void refreshPartnerInterface() {
        final PacketDispatcher dispatch = player.getPacketDispatcher();
        if (partner == null) {
            dispatch.sendClientScript(746, "New partner");
            dispatch.sendComponentText(68, 4, "Current partner: <col=ff0000>(none)</col>");
            dispatch.sendComponentText(68, 5, "Use the button to set yourself a Slayer Partner.<br><br>If your partner's Slayer level is <col=ffffff>as high as yours</col>, whenever a task is <col=ffffff>assigned to them</col>, you'll receive the same task, as long as you are eligible for it.<br><br>If your Slayer level is <col=ffffff>as high as your partner's</col>, whenever a task is <col=ffffff>assigned to you</col>, they'll receive the same task, as long as they are eligible for it.");
        } else {
            dispatch.sendClientScript(746, "Dismiss partner");
            dispatch.sendComponentText(68, 4, "Current partner: <col=ffffff>" + partner.getName() + "</col> (" + (partner.getSkills().getLevelForXp(Skills.SLAYER) + ")"));
            final int playerLevel = player.getSkills().getLevelForXp(Skills.SLAYER);
            final int partnerLevel = partner.getSkills().getLevelForXp(Skills.SLAYER);
            final int difference = Integer.compare(playerLevel, partnerLevel);
            switch (difference) {
                case -1:
                    dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>lower</col> than your partner's.<br><br>When a new task is assigned <col=ffffff>to your partner</col>, if you are<br>eligible for it, and have not blocked it, you will receive<br>it too.<br><br>Your partner will not receive copies of tasks that are<br>assigned <col=ffffff>to you</col> because their Slayer level is higher.");
                    return;
                case 0:
                    dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>the same</col> as your partner's.<br><br>When a new task is assigned <col=ffffff>to either of you</col>, if you are both eligible for it, and have not blocked it, you will both receive it.");
                    return;
                case 1:
                    dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>higher</col> than your partner's.<br><br>When a new task is assigned <col=ffffff>to you</col>, if your partner is<br>eligible for it, and has not blocked it, you will receive<br>it too.<br><br>You will not receive copies of tasks that are assigned <col=ffffff>to your partner</col> because your Slayer level is higher.");
            }
        }
    }

    public void sendTaskInformation() {
        if (assignment == null) {
            player.sendMessage("You need something new to hunt.");
            return;
        }
        final Class<? extends Area> clazz = assignment.getArea();
        Area area = null;
        if (clazz != null) {
            area = GlobalAreaManager.getArea(clazz);
        }
        if (assignment.getMaster().equals(SlayerMaster.KONAR_QUO_MATEN) && area != null) {
            if (area.equals(GlobalAreaManager.getArea(Keldagrim.class))) {
                player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + " in " + area.name() + "; only " + assignment.getAmount() + " more to go.");
            } else {
                player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + " in the " + area.name() + "; only " + assignment.getAmount() + " more to go.");
            }
        } else {
            player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + "; only " + assignment.getAmount() + " more to go.");
        }
    }

    public void setSlayerPoints(final int amount, final boolean refresh) {
        player.addAttribute("slayer_points", amount);
        if (refresh) {
            refreshSlayerPoints();
        }
    }

    private Assignment generateBossTask(final SlayerMaster master) {
        final ArrayList<BossTask> possibleTasks = new ArrayList<>(BossTask.VALUES.length);
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        for (final BossTask $task : BossTask.VALUES) {
            if (master == SlayerMaster.KRYSTILIA && !$task.isAssignableByKrystilia() || !$task.getPredicate().test(player) || lastAssignment == $task) {
                continue;
            }
            possibleTasks.add($task);
        }
        if (possibleTasks.isEmpty()) {
            throw new RuntimeException("Unable to assign any boss task for " + master.toString() + ".");
        }
        final BossTask $task = possibleTasks.get(Utils.random(possibleTasks.size() - 1));
        return new Assignment(player, this, $task, $task.getEnumName(), 0, 0, master);
    }

    private IntAVLTreeSet getAvailableBlockSlots() {
        final IntAVLTreeSet list = new IntAVLTreeSet();
        final int questPoints = player.getQuestPoints();
        for (int i = 0; i < 5; i++) {
            if (questPoints >= ((i + 1) * 50)) {
                list.add(i);
                continue;
            }
            break;
        }
        if (player.getVarManager().getBitValue(LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT) == 1) {
            list.add(5);
        }
        list.removeAll(bannedTasks.keySet());
        return list;
    }

    private int getRemainingExtensionsCost() {
        int cost = 0;
        for (final Integer value : Enums.TASK_EXTENSION_ENUM.getValues().values()) {
            if (isUnlocked(value)) {
                continue;
            }
            cost += Enums.TASK_COST_ENUM.getValue(value).orElseThrow(Enums.exception());
        }
        return (int) (cost * 0.95F);
    }

    private long getUnlocksHash() {
        return player.getNumericAttribute("slayer_unlocked_settings_hash").longValue();
    }

    private void setUnlocksHash(final long value) {
        player.addAttribute("slayer_unlocked_settings_hash", value);
    }

    void refreshBlockedTasks() {
        final VarManager varManager = player.getVarManager();
        for (int i = 0; i < BANNED_SLOT_VARBITS.length; i++) {
            final RegularTask task = bannedTasks.get(i);
            varManager.sendBit(BANNED_SLOT_VARBITS[i], task == null ? 0 : task.getTaskId());
        }
    }

    void refreshCurrentAssignment() {
        final VarManager varManager = player.getVarManager();
        varManager.sendVar(TASK_AMOUNT_VAR, assignment == null ? 0 : assignment.getAmount());
        varManager.sendVar(TASK_INDEX_VAR, assignment == null ? 0 : assignment.getTask().getTaskId());
    }

    void refreshRewards() {
        final VarManager varManager = player.getVarManager();
        final long hash = getUnlocksHash();
        varManager.sendVar(UNLOCK_REWARDS_FIRST_VARP, (int) (hash & 4294967295L));
        varManager.sendVar(UNLOCK_REWARDS_SECOND_VARP, (int) ((hash >> 32) & 4294967295L));
    }

    public void refreshSlayerPoints() {
        player.getVarManager().sendBit(SLAYER_POINTS_BIT, getSlayerPoints());
    }

    public Assignment getAssignment() {
        return this.assignment;
    }

    public void setAssignment(final Assignment assignment) {
        this.assignment = assignment;
        lastAssignmentName = assignment.taskName;
    }

    public SlayerMaster getMaster() {
        return this.master;
    }

    public void setMaster(final SlayerMaster master) {
        this.master = master;
    }

    public Player getPartner() {
        return this.partner;
    }

    public void setPartner(final Player partner) {
        this.partner = partner;
    }

    public String getLastAssignmentName() {
        return this.lastAssignmentName;
    }
}
