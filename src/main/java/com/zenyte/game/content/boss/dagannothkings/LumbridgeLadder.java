package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 18/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LumbridgeLadder implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
            case "Climb-down":
                player.useStairs(828, new Location(3210, 9616, 0), 1, 2);
                break;



        }
    }



@Override
public Object[] getObjects() {
    return new Object[] { ObjectId.TRAPDOOR_14880 };
}
}