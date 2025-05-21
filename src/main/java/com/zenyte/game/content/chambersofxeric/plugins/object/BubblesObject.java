package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.skills.RaidFishing;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 04:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BubblesObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> player.getActionManager().setAction(new RaidFishing(object)));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BUBBLES_29889 };
    }
}
