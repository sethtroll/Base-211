package com.zenyte.game.content.bloodfury;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Matt
 *
 */
public class BloodFuryChargingFull implements PairedItemOnItemPlugin {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.sendMessage("This Amulet of blood fury is already full of charges.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(50499, 24777)
        };
    }
}