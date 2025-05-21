package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.tasks.WorldTask;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class MortMyreSteppingStone implements Shortcut {

    private static final Location WEST = new Location(3417, 3325, 0);
    private static final Location EAST = new Location(3422, 3325, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean direction = player.getX() < object.getX();
        player.setFaceLocation(object);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 1) {
                    player.setLocation(object);
                } else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, direction ? EAST : WEST, 35, direction ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 3) {
                    player.setLocation(direction ? EAST : WEST);
                    stop();
                }
                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 50;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{13504};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 4;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.getX() < object.getX() ? WEST : EAST;
    }

}
