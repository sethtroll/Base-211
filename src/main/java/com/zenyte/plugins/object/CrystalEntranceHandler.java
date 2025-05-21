package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 4. sept 2018 : 21:34:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class CrystalEntranceHandler implements ObjectAction {
    private static final int INSIDE_DOOR =50009;
    private static final int OUTSIDE_DOOR =2236;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Climb-down")) {
            player.setLocation(new Location(2763, 9377, 0));
            player.getDialogueManager().start(new PlainChat(player, "You squeeze through the hole and find some rocks a few feet down leading into the Crystal cave."));
        } else {
            if (option.equals("Climb")) {
                player.setLocation(new Location(2322, 3189, 0));
                //player.getDialogueManager().start(new PlainChat(player, "You squeeze through the hole and find a ladder a few feet down leading into the Stronghold of Security."));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{INSIDE_DOOR, OUTSIDE_DOOR};
    }


    @Override
    public int getDelay() {
        return 1;
    }
}