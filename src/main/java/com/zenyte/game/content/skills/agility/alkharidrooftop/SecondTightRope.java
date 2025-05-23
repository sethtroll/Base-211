package com.zenyte.game.content.skills.agility.alkharidrooftop;

import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.content.skills.agility.Obstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 26 feb. 2018 : 21:11:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class SecondTightRope implements Obstacle {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
    private static final Location START_LOC = new Location(3314, 3186, 3);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20);
        player.addWalkSteps(3313, 3186, -1, false);
        player.addWalkSteps(3302, 3186, -1, false);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 10;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.TIGHTROPE_14409};
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 15;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 12;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

}
