package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.boss.zulrah.combat.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.tasks.WorldTask;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatType;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.zenyte.game.content.boss.zulrah.ZulrahPosition.*;

/**
 * @author Kris | 28. jaan 2018 : 18:03.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class ZulrahNPC extends NPC {

    public static final int RANGED = 2042;

    public static final int MELEE = 2043;

    public static final int MAGIC = 2044;

    private static final Sequence[][][] sequences = new Sequence[][][] { /* First rotation */
    new Sequence[][] { new Sequence[] { new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3070, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2263, 3070, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2273, 3075, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new DiveSequence(CENTER, MAGIC) }, new Sequence[] { new MagicSequence(4), new DiveSequence(SOUTH, RANGED) }, new Sequence[] { new RangedSequence(5), new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new CloudsSequence(new Location(2263, 3070, 0), new Location(2266, 3069, 0)), new CloudsSequence(new Location(2272, 3069, 0), new Location(2273, 3072, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3077, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new DiveSequence(WEST, MAGIC) }, new Sequence[] { new MagicSequence(5), new DiveSequence(SOUTH, RANGED) }, new Sequence[] { new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3069, 0)), new CloudsSequence(new Location(2263, 3070, 0), new Location(2266, 3069, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2272, 3071, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3077, 0)), new SnakelingSequence(new Location(2273, 3072, 0)), new DiveSequence(SOUTH, MAGIC) }, new Sequence[] { new MagicSequence(5), new SnakelingSequence(new Location(2263, 3070, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2269, 3069, 0)), new SnakelingSequence(new Location(2263, 3076, 0)), new CloudsSequence(new Location(2272, 3069, 0), new Location(2273, 3072, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new DiveSequence(WEST, RANGED) }, new Sequence[] { new FlickingSequence(RANGED), new CloudsSequence(new Location(2263, 3070, 0), new Location(2266, 3069, 0)), new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3069, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2273, 2075, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new DiveSequence(CENTER, RANGED) } }, /* Second rotation */
    new Sequence[][] { new Sequence[] { new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3070, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2263, 3070, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2273, 3075, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new DiveSequence(CENTER, MAGIC) }, new Sequence[] { new MagicSequence(4), new DiveSequence(WEST, RANGED) }, new Sequence[] { new CloudsSequence(new Location(2273, 3072, 0), new Location(2272, 3069, 0)), new CloudsSequence(new Location(2273, 3075, 0), new Location(2273, 3078, 0)), new CloudsSequence(new Location(2269, 3069, 0), new Location(2266, 3069, 0)), new SnakelingSequence(new Location(2266, 3069, 0)), new SnakelingSequence(new Location(2263, 3070, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new SnakelingSequence(new Location(2263, 3076, 0)), new DiveSequence(SOUTH, MAGIC) }, new Sequence[] { new MagicSequence(5), new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new CloudsSequence(new Location(2263, 3070, 0), new Location(2266, 3069, 0)), new CloudsSequence(new Location(2272, 3069, 0), new Location(2273, 3072, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3077, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new DiveSequence(EAST, RANGED) }, new Sequence[] { new RangedSequence(5), new DiveSequence(SOUTH, MAGIC) }, new Sequence[] { new MagicSequence(5), new SnakelingSequence(new Location(2263, 3070, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2269, 3069, 0)), new SnakelingSequence(new Location(2263, 3076, 0)), new CloudsSequence(new Location(2272, 3069, 0), new Location(2273, 3072, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new DiveSequence(WEST, RANGED) }, new Sequence[] { new FlickingSequence(RANGED), new CloudsSequence(new Location(2263, 3070, 0), new Location(2266, 3069, 0)), new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3069, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2273, 3075, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new DiveSequence(CENTER, RANGED) } }, /* Third rotation */
    new Sequence[][] { new Sequence[] { new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3070, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2263, 3070, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2273, 3075, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new DiveSequence(EAST, RANGED) }, new Sequence[] { new RangedSequence(5), new SnakelingSequence(new Location(2273, 3078, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3072, 0)), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new CloudsSequence(new Location(2273, 3078, 0), new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3072, 0)), new CloudsSequence(new Location(2272, 3070, 0), new Location(2269, 3069, 0)), new SnakelingSequence(new Location(2266, 3069, 0)), new CloudsSequence(new Location(2263, 3070, 0), new Location(2263, 3073, 0)), new SnakelingSequence(new Location(2263, 3076, 0)), new MeleeSequence(), new DiveSequence(WEST, MAGIC) }, new Sequence[] { new MagicSequence(5), new DiveSequence(SOUTH, RANGED) }, new Sequence[] { new RangedSequence(5), new DiveSequence(EAST, MAGIC) }, new Sequence[] { new MagicSequence(5), new DiveSequence(CENTER, RANGED) }, new Sequence[] { new CloudsSequence(new Location(2273, 3078, 0), new Location(2273, 3075, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2272, 3069, 0)), new CloudsSequence(new Location(2269, 3069, 0), new Location(2266, 3069, 0)), new SnakelingSequence(new Location(2263, 3070, 0)), new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new DiveSequence(WEST, RANGED) }, new Sequence[] { new RangedSequence(5), new DiveSequence(CENTER, MAGIC) }, new Sequence[] { new MagicSequence(5), new CloudsSequence(new Location(2263, 3076, 0), new Location(2263, 3073, 0)), new CloudsSequence(new Location(2263, 3070, 0), new Location(2266, 3069, 0)), new SnakelingSequence(new Location(2269, 3069, 0)), new SnakelingSequence(new Location(2272, 3069, 0)), new SnakelingSequence(new Location(2273, 3072, 0)), new DiveSequence(EAST, RANGED) }, new Sequence[] { new FlickingSequence(MAGIC), new DiveSequence(CENTER, MAGIC) }, new Sequence[] { new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3070, 0)), new SnakelingSequence(new Location(2263, 3072, 0)), new SnakelingSequence(new Location(2273, 3078, 0)), new DiveSequence(CENTER, RANGED) } }, /* Fourth rotation */
    new Sequence[][] { new Sequence[] { new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3070, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2263, 3070, 0)), new CloudsSequence(new Location(2273, 3072, 0), new Location(2273, 3075, 0)), new CloudsSequence(new Location(2263, 3073, 0), new Location(2263, 3076, 0)), new DiveSequence(EAST, MAGIC) }, new Sequence[] { new SnakelingSequence(new Location(2272, 3069, 0)), new SnakelingSequence(new Location(2273, 3078, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3072, 0)), new MagicSequence(5), new DiveSequence(SOUTH, RANGED) }, new Sequence[] { new RangedSequence(4), new CloudsSequence(new Location(2263, 3070, 0), new Location(2269, 3069, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2272, 3069, 0)), new DiveSequence(WEST, MAGIC) }, new Sequence[] { new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new SnakelingSequence(new Location(2266, 3069, 0)), new SnakelingSequence(new Location(2269, 3069, 0)), new MagicSequence(4), new DiveSequence(CENTER, MELEE) }, new Sequence[] { new MeleeSequence(), new CloudsSequence(new Location(2263, 3070, 0), new Location(2269, 3069, 0)), new CloudsSequence(new Location(2266, 3069, 0), new Location(2272, 3069, 0)), new DiveSequence(EAST, RANGED) }, new Sequence[] { new RangedSequence(4), new DiveSequence(SOUTH, RANGED) }, new Sequence[] { new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new SnakelingSequence(new Location(2263, 3070, 0)), new SnakelingSequence(new Location(2273, 3072, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3078, 0)), new CloudsSequence(new Location(2273, 3075, 0), new Location(2273, 3078, 0)), new CloudsSequence(new Location(2272, 3069, 0), new Location(2273, 3072, 0)), new CloudsSequence(new Location(2269, 3069, 0), new Location(2266, 3069, 0)), new DiveSequence(WEST, MAGIC) }, new Sequence[] { new MagicSequence(5), new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new SnakelingSequence(new Location(2263, 3070, 0)), new SnakelingSequence(new Location(2266, 3069, 0)), new DiveSequence(CENTER, RANGED) }, new Sequence[] { new RangedSequence(5), new DiveSequence(CENTER, MAGIC) }, new Sequence[] { new MagicSequence(4), new CloudsSequence(new Location(2263, 3073, 0), new Location(2266, 3069, 0)), new CloudsSequence(new Location(2263, 3070, 0), new Location(2263, 3076, 0)), new CloudsSequence(new Location(2269, 3069, 0), new Location(2272, 3069, 0)), new DiveSequence(EAST, RANGED) }, new Sequence[] { new FlickingSequence(MAGIC), new DiveSequence(CENTER, MAGIC) }, new Sequence[] { new SnakelingSequence(new Location(2263, 3076, 0)), new SnakelingSequence(new Location(2263, 3073, 0)), new SnakelingSequence(new Location(2273, 3075, 0)), new SnakelingSequence(new Location(2273, 3078, 0)) } } };

    private static final Animation SPAWN = new Animation(5071);

    private static final SoundEffect SOUND_EFFECT_IMPACT = new SoundEffect(790, 15);

    private static final SoundEffect DISSIPATE_A = new SoundEffect(796, 15);

    private static final SoundEffect DISSIPATE_B = new SoundEffect(795, 15);

    private final ZulrahInstance instance;

    private final List<Location> venomClouds;

    private final List<SnakelingNPC> snakelings;

    private final transient Predicate<Hit> hitPredicate = hit -> {
        final Predicate<Hit> predicate = hit.getPredicate();
        if (predicate != null) {
            if (predicate.test(hit)) {
                return true;
            }
        }
        this.postProcessHit(hit);
        processHit(hit);
        return true;
    };

    private Player player;

    private int rotation;

    private int sequence;

    private int phase;

    private boolean stopped;

    private boolean initialStop;

    public ZulrahNPC(final Location tile, final ZulrahInstance instance) {
        super(RANGED, tile, Direction.SOUTH, 0);
        this.instance = instance;
        player = instance.getPlayer();
        setSpawned(true);
        setForceMultiArea(true);
        setDamageCap(50);
        setAnimation(SPAWN);
        venomClouds = new ArrayList<>();
        snakelings = new ArrayList<>();
        rotation = Utils.random(3);
        lock(9);
        setCantInteract(true);
        //initialStop = true;
        WorldTasksManager.schedule(() -> {
            player.getBossTimer().startTracking("Zulrah");
            player.getPacketDispatcher().resetCamera();
            setCantInteract(false);
        }, 4);
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        return false;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean checkProjectileClip(final Player player) {
        return false;
    }

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        stopped = false;
        initialStop = false;
        if (hit.getDamage() >= 50) {
            hit.setDamage(Utils.random(45, 50));
        }
        if (hit.getHitType() == HitType.MELEE) {
            hit.setDamage(0);
        }
    }

    @Override
    protected boolean preserveStatsOnTransformation() {
        return true;
    }

    @Override
    public void autoRetaliate(final Entity source) {
        super.autoRetaliate(source);
        stopped = false;
        initialStop = false;
    }

    /**
     * Adds the venom cloud to the requested tile.
     * The cloud only remains for 18 seconds AKA 30 ticks.
     * The tile requested should be the center of the cloud,
     * the tile on which the cloud is spawned on is directly SW of that.
     *
     * @param tile  the center of the cloud.
     * @param delay the delay in ticks until the clouds spawning.
     */
    public void addCloud(final Location tile, final int delay) {
        WorldTasksManager.schedule(new WorldTask() {

            private VenomCloud cloud;

            private int ticks;

            @Override
            public void run() {
                if (ZulrahNPC.this.isDead() || ZulrahNPC.this.isFinished()) {
                    if (cloud != null) {
                        World.removeObject(cloud);
                    }
                    stop();
                    return;
                }
                if (cloud == null) {
                    World.sendSoundEffect(tile, SOUND_EFFECT_IMPACT);
                    World.spawnObject(cloud = new VenomCloud(tile));
                } else {
                    if (ticks == 30) {
                        World.removeObject(cloud);
                        stop();
                        World.sendSoundEffect(tile, Utils.random(2) == 0 ? DISSIPATE_A : DISSIPATE_B);
                    } else {
                        if (player.getLocation().withinDistance(tile, 1)) {
                            player.applyHit(new Hit(Utils.random(1, 4), HitType.VENOM));
                        }
                    }
                    ticks++;
                }
            }
        }, delay, 0);
    }

    @Override
    public void processReceivedHits() {
        if (receivedHits.isEmpty())
            return;
        receivedHits.removeIf(hitPredicate);
    }

    @Override
    public void processNPC() {
        if (initialStop || stopped || isLocked() || isDead()) {
            return;
        }
        if (!player.getLocation().withinDistance(getLocation(), 50)) {
            finish();
            return;
        }
        if (isCancelled(false)) {
            return;
        }
        final Sequence sequence = getNextSequence();
        sequence.attack(this, instance, player);
    }

    public boolean isCancelled(boolean ignoreTeleportation) {
        if (isFinished()) {
            return true;
        }
        if (player.isNulled() || player.isDead() || player.isFinished() || (!ignoreTeleportation && player.isTeleported())) {
            return true;
        }
        return !player.getLocation().withinDistance(getLocation(), 50);
    }

    /**
     * Schedules a hit from Zulrah. Additionally, if the hit is successful,
     * applies venom on the target.
     *
     * @param delay the delay in ticks until the hit.
     * @param hit   the hit being applied on the player.
     */
    public void delayHit(final int delay, final Hit hit) {
        CombatUtilities.delayHit(ZulrahNPC.this, delay, player, hit.onLand(h -> {
            if (hit.getDamage() > 0) {
                player.getToxins().applyToxin(ToxinType.VENOM, 6);
            }
        }));
    }

    /**
     * Gets the next Sequence in the current rotation, in the current sequence array.
     * Sets the position of the sequence to the next one.
     *
     * @return the next Sequence object to execute.
     */
    private Sequence getNextSequence() {
        Sequence[] seq = sequences[rotation][sequence];
        if (phase >= seq.length) {
            phase = 0;
            if (++sequence >= sequences[rotation].length) {
                sequence = 0;
                rotation = Utils.random(3);
            }
            seq = sequences[rotation][sequence];
        }
        return seq[phase++];
    }

    @Override
    public boolean startAttacking(final Player source, final CombatType type) {
        if (type == CombatType.MELEE) {
            if (!PlayerCombat.isExtendedMeleeDistance(source)) {
                player.sendMessage("You cannot reach that!");
                return false;
            }
        }
        return super.startAttacking(source, type);
    }

    @Override
    public void dropItem(final Player killer, final Item item, final Location tile, boolean guaranteedDrop) {
        //Zulrah always drops loot underneath the player.
        tile.setLocation(killer.getLocation());
        super.dropItem(killer, item, tile, guaranteedDrop);
    }

    @Override
    protected void drop(final Location tile) {
        final Player killer = instance.getPlayer();
        onDrop(killer);
        final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
        if (drops == null) {
            return;
        }
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }
        NPCDrops.forEach(drops, drop -> dropItem(killer, drop, tile));
        NPCDrops.forEach(drops, drop -> {
            if (!drop.isAlways())
                dropItem(killer, drop, tile);
        });
    }

    @Override
    public void sendDeath() {
        final Player source = getMostDamagePlayerCheckIronman();
        final NPCCombatDefinitions defs = getCombatDefinitions();
        final String name = getDefinitions().getName().toLowerCase();
        resetWalkSteps();
        combat.removeTarget();
        setAnimation(null);
        onDeath(getMostDamagePlayerCheckIronman());
        for (final SnakelingNPC snake : snakelings) {
            snake.applyHit(new Hit(1, HitType.REGULAR));
        }
        snakelings.clear();
        WorldTasksManager.schedule(new WorldTask() {

            private int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setAnimation(defs.getSpawnDefinitions().getDeathAnimation());
                } else if (loop == getDeathDelay()) {
                    if (player.getLocation().withinDistance(ZulrahNPC.this.getLocation(), 50)) {
                        drop(new Location(player.getLocation()));
                        sendTeleportTab();
                    } else {
                        drop(getMiddleLocation());
                    }
                    if (source != null) {
                        if (NotificationSettings.isKillcountTracked(name)) {
                            source.getNotificationSettings().increaseKill(name);
                            if (NotificationSettings.BOSS_NPC_NAMES.contains(name)) {
                                source.getNotificationSettings().sendBossKillCountNotification(name);
                            }
                        }
                    }
                    player.getBossTimer().finishTracking("Zulrah");
                    reset();
                    finish();
                    stop();
                    return;
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player player) {
            player.getAchievementDiaries().update(WesternProvincesDiary.KILL_ZULRAH);
        }
    }

    /**
     * Sends the teleport tablet object next to player, if possible, otherwise
     * under the player.
     */
    private void sendTeleportTab() {
        final int py = player.getY();
        final int px = player.getX();
        for (int y = py + 1; y >= py - 1; y--) {
            for (int x = px + 1; x >= px - 1; x--) {
                if (y == py && x == px) {
                    continue;
                }
                if (World.isTileFree(x, y, player.getPlane(), 1)) {
                    World.spawnObject(new WorldObject(ObjectId.ZULANDRA_TELEPORT, 10, 0, x, y, player.getPlane()));
                    return;
                }
            }
        }
        World.spawnObject(new WorldObject(ObjectId.ZULANDRA_TELEPORT, 10, 0, px, py, player.getPlane()));
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(final int rotation) {
        this.rotation = rotation;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(final int sequence) {
        this.sequence = sequence;
    }

    public int getPhase() {
        return this.phase;
    }

    public void setPhase(final int phase) {
        this.phase = phase;
    }

    public ZulrahInstance getInstance() {
        return this.instance;
    }

    public List<Location> getVenomClouds() {
        return this.venomClouds;
    }

    public List<SnakelingNPC> getSnakelings() {
        return this.snakelings;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void setStopped(final boolean stopped) {
        this.stopped = stopped;
    }
}
