package com.zenyte.game.content.WellOfExchange;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.constants.GameInterface.UPGRADE_INTERFACE;

public class UpgradeRack implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getInterfaceHandler().sendInterface(UPGRADE_INTERFACE);
    }

    @Override
        public Object[] getObjects() {
            return new Object[]{ObjectId.WEAPON_RACK_33020};
        }
    }
