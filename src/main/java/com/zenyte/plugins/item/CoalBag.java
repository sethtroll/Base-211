package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemOnItemAction;
import com.zenyte.game.item.SkillcapePerk;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 03/07/2019 00:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CoalBag extends ItemPlugin implements ItemOnItemAction {
    public static final Item ITEM = new Item(12019, 1);

    public static void emptyBag(final Item coalBag, final Player player) {
        final int coal = coalBag.getNumericAttribute("coal").intValue();
        if (coal <= 0) {
            player.sendMessage("Your coal bag is empty.");
            return;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("Your inventory is full.");
            return;
        }
        final int amount = player.getInventory().addItem(new Item(453, coal)).getSucceededAmount();
        coalBag.setAttribute("coal", coal - amount);
        player.sendMessage("You withdraw " + amount + " coal from the coal bag.");
    }

    public static void emptyBagToBank(final Item coalBag, final Player player) {
        final int coal = coalBag.getNumericAttribute("coal").intValue();
        if (coal <= 0) {
            player.sendMessage("Your coal bag is empty.");
            return;
        }
        if (!player.getBank().hasFreeSlots()) {
            player.sendMessage("Your inventory is full.");
            return;
        }
        final int amount = player.getBank().add(new Item(453, coal)).getSucceededAmount();
        coalBag.setAttribute("coal", coal - amount);
        player.sendMessage("You withdraw " + amount + " coal from the coal bag.");
        player.getBank().getContainer().refresh(player);
    }

    private void fillBag(final Item coalBag, final Player player) {
        final int coal = coalBag.getNumericAttribute("coal").intValue();
        final boolean cape = SkillcapePerk.SMITHING.isEffective(player);
        final int limit = cape ? 36 : 27;
        if (coal >= limit) {
            player.sendMessage("Your coal bag cannot hold any more coal.");
            return;
        }
        final int amount = player.getInventory().deleteItem(new Item(453, limit - coal)).getSucceededAmount();
        coalBag.setAttribute("coal", coal + amount);
        if (amount > 0) {
            player.sendMessage("You add " + amount + " coal into the coal bag.");
        } else {
            player.sendMessage("You have no coal to add to the coal bag.");
        }
    }

    @Override
    public void handle() {
        bind("Fill", (player, item, container, slotId) -> fillBag(item, player));
        bind("Check", (player, item, container, slotId) -> player.sendMessage("Your coal bag currently holds " + item.getNumericAttribute("coal").intValue() + " coal."));
        bind("Empty", (player, item, container, slotId) -> emptyBag(item, player));
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (to.getId() != ITEM.getId()) {
            return;
        }
        fillBag(to, player);
    }

    @Override
    public int[] getItems() {
        return new int[]{ITEM.getId(), Smelting.COAL.getId()};
    }
}
