package com.zenyte.game.content.skills.agility.pyramid.area;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.plugins.events.ServerLaunchEvent;

import java.util.EnumMap;
import java.util.Map;

public enum MovingBlock {
    FIRST_LEVEL_BLOCK(5788, 10872, new ImmutableLocation(3372, 2847, 1), Direction.EAST),
    THIRD_LEVEL_BLOCK(5788, 10873, new ImmutableLocation(3366, 2845, 3), Direction.NORTH);
    public static final MovingBlock[] values = values();
    private static final EnumMap<MovingBlock, MovingBlockNPC> map = new EnumMap<>(MovingBlock.class);
    private final int npcId;
    private final int objectId;
    private final Location spawn;
    private final Direction direction;

    MovingBlock(final int npcId, final int objectId, final Location spawn, final Direction direction) {
        this.npcId = npcId;
        this.objectId = objectId;
        this.spawn = spawn;
        this.direction = direction;
    }

    @Subscribe
    public static void onServerLaunch(final ServerLaunchEvent event) {
        for (final MovingBlock block : values()) {
            final MovingBlockNPC npc = new MovingBlockNPC(block.npcId, block.spawn, block.direction, 0);
            npc.spawn();
            map.put(block, npc);
        }
    }

    static void moveBlocks() {
        for (final Map.Entry<MovingBlock, MovingBlockNPC> entry : map.entrySet()) {
            final MovingBlock key = entry.getKey();
            final MovingBlockNPC value = entry.getValue();
            value.slide(key.direction);
            World.sendSoundEffect(key.spawn, new SoundEffect(1395, 5));
            WorldTasksManager.schedule(() -> value.slide(key.direction.getCounterClockwiseDirection(4)), 6);
        }
    }

    public int getNpcId() {
        return this.npcId;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
