package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 31/01/2019 01:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleDungeonMonkeyBars implements Shortcut {
    private final RenderAnimation render = new RenderAnimation(745, 745, 744, 745, 745, 745, 744);
    private final Animation start = new Animation(742);
    private final Animation end = new Animation(743);

    @Override
    public int getLevel(WorldObject object) {
        return 15;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{23566};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 5;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final Location destination = new Location(object.getX() + 1, object.getY() == 9964 ? 9969 : 9964, object.getPlane());
        player.setFaceLocation(destination);
        player.getAppearance().setRenderAnimation(render);
        player.setAnimation(start);
        player.addWalkSteps(destination.getX(), destination.getY(), 5, false);
    }

    @Override
    public void endSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        player.setAnimation(end);
        WorldTasksManager.schedule(() -> player.getAppearance().resetRenderAnimation());
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return new Location(object.getX() + 1, object.getY(), object.getPlane());
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 20;
    }
}
