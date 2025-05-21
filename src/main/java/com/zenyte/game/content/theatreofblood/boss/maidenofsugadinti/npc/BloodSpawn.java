package com.zenyte.game.content.theatreofblood.boss.maidenofsugadinti.npc;

import com.zenyte.game.content.theatreofblood.boss.maidenofsugadinti.MaidenOfSugadintiRoom;
import com.zenyte.game.content.theatreofblood.boss.maidenofsugadinti.object.BloodTrail;
import com.zenyte.game.content.theatreofblood.plugin.entity.TheatreNPC;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;

/**
 * @author Corey
 * @since 30/05/2020
 */
public class BloodSpawn extends TheatreNPC<MaidenOfSugadintiRoom> {
    private final MaidenOfSugadinti maiden;
    private final Location spawnLocation;

    public BloodSpawn(final MaidenOfSugadinti maiden, final Location tile) {
        super(maiden.getRaid(), maiden.getRoom(), NpcId.BLOOD_SPAWN, tile);
        this.maiden = maiden;
        this.spawnLocation = tile;
        this.radius = 10;
    }

    @Override
    public void processNPC() {
        if (maiden.dead()) {
            return;
        }
        final var lastTile = lastLocation == null ? getLocation() : lastLocation;
        addTrail(lastTile);
        if (shouldNotWalk()) {
            addTrail(location);
        }
        // custom walk behaviour
        if (!isFrozen() && Utils.random(2) == 0) {
            final var moveX = Utils.random(-radius, radius);
            final var moveY = Utils.random(-radius, radius);
            final var respawnX = spawnLocation.getX();
            final var respawnY = spawnLocation.getY();
            addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius, getSize(), true);
        }
    }

    private void addTrail(final Location tile) {
        if (!maiden.splatExists(tile)) {
            final var loc = new Location(tile);
            maiden.addSplat(loc);
            final var t = new BloodTrail(maiden, loc);
            maiden.getBloodTrails().add(t);
            t.process();
        } else if (!shouldNotWalk()) {
            // resets timer for existing splat
            for (BloodTrail trail : maiden.getBloodTrails()) {
                if (trail.getPositionHash() == tile.getPositionHash()) {
                    trail.resetTimer();
                    break;
                }
            }
        }
    }

    private boolean shouldNotWalk() {
        return isFrozen() || isStunned();
    }

    @Override
    protected void setStats() {
    }

    @Override
    public void autoRetaliate(final Entity source) {
    }
}
