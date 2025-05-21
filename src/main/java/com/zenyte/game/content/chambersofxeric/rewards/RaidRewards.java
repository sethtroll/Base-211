package com.zenyte.game.content.chambersofxeric.rewards;

import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.TimeUnit;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Kris | 12. mai 2018 : 22:56:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidRewards {
    private static final Logger log = LoggerFactory.getLogger(RaidRewards.class);
    /**
     * The max number of points that can be rolled against per roll(80% chance).
     */
    private static final float MAXIMUM_POINT_ROLL = 570000.0F;
    /**
     * The max number of points to achieve 100% chance of getting reward based on {@link RaidRewards#MAXIMUM_POINT_ROLL}
     */
    private static final float MAXIMUM_POINT_ROLL_TOTAL = 867500.0F;
    /**
     * The raid for which we generate the rewards.
     */
    private final Raid raid;
    /**
     * A map of all the containers for each player.
     */
    private final Map<Player, Container> rewardMap;
    private final Map<String, List<Item>> originalRewards;
    /**
     * A duplicate list of raid players that's modified if a rare drop is given.
     */
    private final Set<Player> players;

    public RaidRewards(final Raid raid) {
        this.raid = raid;
        final int size = raid.getPlayers().size();
        rewardMap = new Object2ObjectOpenHashMap<>(size);
        players = new ObjectOpenHashSet<>(size);
        originalRewards = new Object2ObjectOpenHashMap<>();
    }

    /**
     * Generates the rewards for all players, based on their and the raid's total points.
     */
    public void generate() {
        players.clear();
        players.addAll(raid.getPlayers());
        rewardMap.clear();
        raid.complete();
        final int ticks = raid.getDuration();
        final long seconds = TimeUnit.TICKS.toSeconds(ticks) % 60;
        final long minutes = TimeUnit.TICKS.toMinutes(ticks);
        raid.getLevelCompletionMessages().put(0, Pair.of("Olm level: ", TimeUnit.TICKS.toMillis(ticks)));
        final String message = Colour.RS_PINK + "Congratulations - your raid is complete! Duration: " + Colour.RED + Utils.formatTime(minutes, seconds) + Colour.END;
        recordBottom();
        final boolean inCMTime = raid.isChallengeMode() && raid.isMetamorphicDustEligible();
        for (final Player p : players) {
            if (inCMTime) {
                p.sendMessage("Your team beat the challenge target time and you earned an extra 10,000 points.");
                raid.addPoints(p, 10000, true);
            }
            p.sendMessage(message);
            if (!raid.isChallengeMode()) {
                p.addAttribute("chambersofxeric", p.getNumericAttribute("chambersofxeric").intValue() + 1);
                p.sendMessage("Your completed Chambers of Xeric count is: " + Colour.RED + p.getNumericAttribute("chambersofxeric").intValue() + Colour.END + ".");
            } else {
                p.addAttribute("challengechambersofxeric", p.getNumericAttribute("challengechambersofxeric").intValue() + 1);
                p.sendMessage("Your completed challenge mode Chambers of Xeric count is: " + Colour.RED + p.getNumericAttribute("challengechambersofxeric").intValue() + Colour.END + ".");
            }
            rewardMap.put(p, new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.RAID_REWARDS, Optional.of(p)));
        }
        if (raid.isChallengeMode()) {
            if (raid.isMetamorphicDustEligible()) {
                for (final Player player : players) {
                    if (Utils.secureRandom(199) == 0) {
                        final Item item = new Item(ItemId.METAMORPHIC_DUST);
                        WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, item, "Challenge Mode Chambers of Xeric");
                        addReward(player, item);
                    }
                }
            }
        }
        rollRareRewards();
        for (final Player player : players) {
            addRandomRewards(player);
            final Container rewards = rewardMap.get(player);
            if (rewards != null && !rewards.isEmpty()) {
                player.getVarManager().sendBit(5456, 1);
            }
        }
        players.addAll(raid.getPlayers());
        for (final Map.Entry<Player, Container> reward : rewardMap.entrySet()) {
            final List<Item> list = originalRewards.computeIfAbsent(reward.getKey().getUsername(), __ -> new ObjectArrayList<>());
            for (final Int2ObjectMap.Entry<Item> itemEntry : reward.getValue().getItems().int2ObjectEntrySet()) {
                list.add(new Item(itemEntry.getValue()));
            }
        }
    }

    /**
     * Records the bottom floor completion time into the logger.
     */
    private void recordBottom() {
        final long millis = Utils.currentTimeMillis() - raid.getStartTime();
        final String prefix = "Bottom";
        raid.getLevelCompletionMessages().put(0, Pair.of(prefix + " level: ", millis));
    }

    /**
     * Adds random non-rare rewards for the player requested.
     *
     * @param player the player whose container to fill with random rewards.
     */
    private void addRandomRewards(final Player player) {
        final boolean tablet = !player.containsItem(21046) && !player.getAttributes().containsKey("xeric's honour") && Utils.secureRandom(10) == 0;
        if (tablet) {
            addReward(player, new Item(21046));
        }
        final int points = raid.getPoints(player);
        final int size = 2 - (tablet ? 1 : 0);
        for (int i = 0; i < size; i++) {
            final RaidReward reward = raid.isChallengeMode() ? ChallengeRaidNormalReward.random() : RaidNormalReward.random();
            final float modifier = 131070.0F / reward.getMaximumAmount();
            final int amount = Math.max(1, (int) (points / modifier));
            assert amount <= 65535;
            addReward(player, new Item(ItemDefinitions.getOrThrow(reward.getId()).getNotedOrDefault(), amount));
        }
        if (Utils.secureRandom(11) == 0) {
            addReward(player, new Item(ClueItem.ELITE.getScrollBox()));
        }
    }

    /**
     * Opens the rewards interface that's filled with their personal rewards.
     *
     * @param player the player who's opening the interface.
     */
    public void open(final Player player) {
        final Container container = rewardMap.get(player);
        if (container.isEmpty()) {
            player.getDialogueManager().start(new PlainChat(player, "The chest is empty."));
            return;
        }
        player.getVarManager().sendBit(5457, !player.containsItem(20899) && players.contains(player) ? 1 : 0);
        container.setFullUpdate(true);
        container.refresh(player);
        GameInterface.RAID_REWARDS.open(player);
    }

    /**
     * Attempts to roll for rare rewards using OSRS formulas.
     */
    private void rollRareRewards() {
        int points = (int) Math.min(raid.getTotalPoints(), MAXIMUM_POINT_ROLL * 3);
        final int length = (int) Math.ceil(points / MAXIMUM_POINT_ROLL);
        boolean specialLoot = false;
        for (int i = 0; i < length; i++) {
            if (players.isEmpty()) {
                break;
            }
            final int totalPoints = getTotalPoints();
            final float currentPoints = Math.min(MAXIMUM_POINT_ROLL, points);
            final float percentage = currentPoints / MAXIMUM_POINT_ROLL_TOTAL;
            points -= currentPoints;
            if (Utils.SECURE_RANDOM.nextDouble() > percentage) {
                continue;
            }
            final int roll = Utils.secureRandom(totalPoints);
            try {
                final Player player = getPlayerAtPoints(roll);
                if (!specialLoot) {
                    specialLoot = true;
                    for (final Player p : raid.getPlayers()) {
                        p.sendMessage(Colour.RS_PINK.wrap("Special loot:"));
                    }
                }
                player.getVarManager().sendBit(5456, 2);
                final Item rareLoot = getRareReward();
                addReward(player, rareLoot);
                for (final Player p : raid.getPlayers()) {
                    p.sendMessage(player.getName() + " - " + Colour.RED.wrap(rareLoot.getName()));
                }
                final Item unnotedItem = new Item(rareLoot.getDefinitions().getUnnotedOrDefault(), rareLoot.getAmount());
                WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, unnotedItem, raid.isChallengeMode() ? "Challenge Mode Chambers of Xeric" : "Chambers of Xeric");
                final boolean pet = BossPet.OLMLET.roll(player, 53);
                if (pet) {
                    originalRewards.computeIfAbsent(player.getUsername(), __ -> new ObjectArrayList<>()).add(new Item(ItemId.OLMLET));
                }
                players.remove(player);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    /**
     * Adds a reward to the requested player's container.
     *
     * @param player the player whose container to enqueue the reward to.
     * @param reward the item to enqueue to the container.
     */
    private void addReward(final Player player, final Item reward) {
        final Container container = rewardMap.get(player);
        final ContainerResult result = container.add(reward);
        final Item unnoted = new Item(reward.getDefinitions().getUnnotedOrDefault(), reward.getAmount());
        player.getCollectionLog().add(unnoted);
        if (result.getResult() != RequestResult.SUCCESS) {
            System.err.println("Failure to successfully add reward: " + reward + "\n" + result);
        }
    }

    /**
     * Gets the player at the randomly rolled points index. The roll value will be between 0 and total points of the raid. It will loop over
     * the players until the current roll stack exceeds or equals the roll.
     *
     * @param roll the number rolled, from 0 to total points in raid.
     * @return the lucky player.
     */
    private Player getPlayerAtPoints(final int roll) {
        int currentRoll = 0;
        for (final Player player : players) {
            if ((currentRoll += raid.getPoints(player)) < roll) {
                continue;
            }
            return player;
        }
        throw new IllegalStateException();
    }

    /**
     * Gets the total points of all the players remaining in the set combined.
     *
     * @return total points combined.
     */
    private int getTotalPoints() {
        int amount = 0;
        for (final Player player : players) {
            amount += raid.getPoints(player);
        }
        return amount;
    }

    /**
     * Gets a random rare reward based on their own individual weights defined in RaidReward enum.
     *
     * @return a random rare reward.
     */
    private Item getRareReward() {
        final int random = Utils.secureRandom(RaidRareReward.TOTAL_WEIGHT);
        int roll = 0;
        for (final RaidRareReward reward : RaidRareReward.values) {
            if ((roll += reward.getWeight()) < random) {
                continue;
            }
            final Item item = reward.getItem();
            return new Item(item.getDefinitions().getNotedOrDefault(), item.getAmount());
        }
        throw new IllegalStateException();
    }

    /**
     * A map of all the containers for each player.
     */
    public Map<Player, Container> getRewardMap() {
        return this.rewardMap;
    }

    public Map<String, List<Item>> getOriginalRewards() {
        return this.originalRewards;
    }

    /**
     * A duplicate list of raid players that's modified if a rare drop is given.
     */
    public Set<Player> getPlayers() {
        return this.players;
    }
}
