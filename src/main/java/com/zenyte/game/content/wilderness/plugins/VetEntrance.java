package com.zenyte.game.content.wilderness.plugins;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16/03/2019 20:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VetEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if ("Jump-Down".equals(option)) {
            if (object.getX() == 3221 && object.getY() == 3787) {
                player.useStairs(1148, new Location(3295, 10192, 1), 1, 2, null, true);
            }
        }

    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CREVICE_46995 };
    }
}