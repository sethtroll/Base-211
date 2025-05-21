package com.zenyte.game.content.WellOfExchange;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class WellOfExchangeShop implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.openShop("Burn Points System Sell Price Viewer");
    }

    @Override
        public Object[] getObjects() {
            return new Object[]{ObjectId.FIRE_OF_DEHUMIDIFICATION};
        }
    }
