package com.zenyte.plugins.object;

import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/01/2019 16:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientPyramidEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Enter")) {
            player.lock(2);
            player.setAnimation(new Animation(844));
            WorldTasksManager.schedule(() -> {
                player.setLocation(new Location(3233, 9312, 0));
                WorldTasksManager.schedule(() -> player.addWalkSteps(3233, 9313));
            });
        }
    }

    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TUNNEL_6481 };
    }
}
