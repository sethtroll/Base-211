package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Swifty | 18/04/2024 | 21:20
 */
public class FeroxBarrier implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Pass-Through")) {
            if(player.getPosition().getX() == 3122 && (player.getPosition().getY() == 3628 || player.getPosition().getY() == 3629)){
                player.setLocation(new Location(3123, player.getPosition().getY()));
            }if(player.getPosition().getX() == 3123 && (player.getPosition().getY() == 3628 || player.getPosition().getY() == 3629)){
                player.setLocation(new Location(3122, player.getPosition().getY()));
            }

            if(player.getPosition().getX() == 3154 && (player.getPosition().getY() == 3635 || player.getPosition().getY() == 3634)){
                player.setLocation(new Location(3155, 3635));
            }if(player.getPosition().getX() == 3155 && (player.getPosition().getY() == 3635 || player.getPosition().getY() == 3634)){
                player.setLocation(new Location(3154, 3635));
            }

            if(player.getPosition().getX() == 3135 && (player.getPosition().getY() == 3617 || player.getPosition().getX() == 3134)){
                player.setLocation(new Location(3135, 3616));
            }if(player.getPosition().getX() == 3135 && (player.getPosition().getY() == 3616 || player.getPosition().getY() == 3134)){
                player.setLocation(new Location(3134, 3617));
            }

            if(player.getPosition().getX() == 3135 && (player.getPosition().getY() == 3640 || player.getPosition().getX() == 3134)){
                player.setLocation(new Location(3135, 3639));
            }if(player.getPosition().getX() == 3135 && (player.getPosition().getY() == 3639 || player.getPosition().getY() == 3134)){
                player.setLocation(new Location(3135, 3640));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { NullObjectID.NULL_39656, 39653, 39652};
    }
}
