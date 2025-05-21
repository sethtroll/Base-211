package com.zenyte.game.content.bloodfury;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Matt, redone by Cresinkel
 *
 */
public class BloodFuryCharging implements PairedItemOnItemPlugin {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        Inventory inventory = player.getInventory();
        inventory.deleteItemsIfContains(new Item[] { from, to }, () -> {
            inventory.addOrDrop(new Item(50499));
            player.sendMessage("You combine the blood shard and the amulet of blood fury together to provide more charges");
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(50500, 24777), ItemPair.of(50502, 24777),
                ItemPair.of(50504, 24777), ItemPair.of(50506, 24777)
        };
    }
}
