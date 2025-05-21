package com.zenyte.game.content.bloodfury;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Matt
 *
 */
public class BloodFuryChargingFury implements PairedItemOnItemPlugin {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        Inventory inventory = player.getInventory();
        inventory.deleteItemsIfContains(new Item[] { from, to }, () -> {
            inventory.addOrDrop(new Item(50499));
            player.sendMessage("You combine the blood shard and the amulet of fury together to make a amulet of blood fury.");
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(24777, 6585)
        };
    }
}
