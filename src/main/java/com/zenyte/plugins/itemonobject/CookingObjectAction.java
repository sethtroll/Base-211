package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.cooking.CookingDefinitions;
import com.zenyte.game.content.skills.cooking.CookingDefinitions.CookingData;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.skills.CookingD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11. nov 2017 : 0:40.38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class CookingObjectAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        final CookingData[] food = CookingData.isCooking(player, item, object.getName().toLowerCase().contains("fire"));
        final ArrayList<Item> list = new ArrayList<>(2);
        final ArrayList<CookingDefinitions.CookingData> cookableList = new ArrayList<>();
        for (final CookingDefinitions.CookingData it : food) {
            if (CookingData.hasRequirements(player, it)) {
                list.add(new Item(it.getCooked()));
                cookableList.add(it);
            }
        }
        if (!list.isEmpty()) {
            player.getDialogueManager().start(new CookingD(player, object, false, cookableList, list.toArray(new Item[0])));
        }
        /*if (food != null && CookingData.hasRequirements(player, food)) {
			final CookingData[] cookable = CookingData.getDataByProduct(items[slotId], object);
			player.getDialogueManager().start(new CookingD(player, object, new Item(food.getCooked())));
			return;
		}*/
    }

    @Override
    public Object[] getItems() {
        final List<Object> list = new ArrayList<>();
        for (final CookingData data : CookingData.values) {
            list.add(data.getRaw());
        }
        return list.toArray(new Object[list.size()]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Clay oven", "Fire", ObjectId.BONFIRE, /* wintertodt camp bonfire */
        "Cooking range", "Range", "Stove", "Sulphur vent", "Cooking pot" };
    }
}
