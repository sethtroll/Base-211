package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.PairedItemOnItemPlugin;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;

/**
 * @author Christopher
 * @since 1/24/2020
 */
public class UnpoweredSymbolBlessingAction implements PairedItemOnItemPlugin {
    private static final Animation unholyBlessingAction = new Animation(1336);
    private static final Animation balanceBlessingAction = new Animation(1337);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().checkLevel(Skills.PRAYER, 50, "bless the unholy symbol.")) {
            final Item book = from.getId() != ItemId.UNPOWERED_SYMBOL ? from : to;
            player.setAnimation(book.getId() == ItemId.UNHOLY_BOOK ? unholyBlessingAction : balanceBlessingAction);
            WorldTasksManager.schedule(() -> {
                final int slot = from.getId() == ItemId.UNPOWERED_SYMBOL ? fromSlot : toSlot;
                player.getInventory().replaceItem(ItemId.HOLY_SYMBOL, 1, slot);
                player.getPrayerManager().drainPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.1 + 2));
                player.sendFilteredMessage("You bless the unholy symbol.");
            }, 5);
        }
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[]{ItemPair.of(ItemId.UNPOWERED_SYMBOL, ItemId.UNHOLY_BOOK), ItemPair.of(ItemId.UNPOWERED_SYMBOL, ItemId.BOOK_OF_BALANCE)};
    }
}
