package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.smithing.DragonfireWardCreationAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 22 jul. 2018 | 17:20:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DragonfireWardCreationObjectAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new DragonfireWardCreationAction());
    }

    @Override
    public Object[] getItems() {
        return new Object[]{1540, 22006};
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{"Anvil"};
    }

}
