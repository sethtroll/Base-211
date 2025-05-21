package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.PotteryFiringData;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.skills.PotteryFiringD;

import java.util.ArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:52.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PotteryFiringObjectAction implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getDialogueManager().start(new PotteryFiringD(player));
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<>();
        for (final CraftingDefinitions.PotteryFiringData data : PotteryFiringData.VALUES_ARR) {
            list.add(data.getMaterial().getId());
        }
        return list.toArray(new Object[list.size()]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{"Pottery Oven"};
    }
}
