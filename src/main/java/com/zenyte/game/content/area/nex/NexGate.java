package com.zenyte.game.content.area.nex;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 4. sept 2018 : 21:35:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class NexGate implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Pass")) {
            if(player.getPosition().getX() > 2908){
                player.setLocation(new Location(2908, player.getPosition().getY()));
            }else{
                player.setLocation(new Location(2910, player.getPosition().getY()));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.NEX_GATE };
    }
}
