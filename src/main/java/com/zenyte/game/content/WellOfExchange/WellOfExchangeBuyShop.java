package com.zenyte.game.content.WellOfExchange;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class WellOfExchangeBuyShop implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.openShop("Burn Points Store");
    }

    @Override
        public Object[] getObjects() {
            return new Object[]{ObjectId.OPEN_CHEST_379};
        }
    }
