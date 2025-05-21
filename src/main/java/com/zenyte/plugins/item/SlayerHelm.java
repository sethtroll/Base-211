package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.plugins.itemonitem.SlayerHelmItemCreation;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 25. aug 2018 : 22:47:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SlayerHelm extends ItemPlugin {
    public static final Item BLACK_MASK_I = new Item(11784);

    @Override
    public void handle() {
        bind("Disassemble", (player, item, slotId) -> {
            if (item.getId() == 11864 || item.getId() == 11865) {
                if (player.getInventory().getFreeSlots() < SlayerHelmItemCreation.REQUIRED_ITEMS.length + 1) {
                    player.sendMessage("You need more inventory space to disassemble your slayer helmet.");
                    return;
                }
                player.getInventory().deleteItem(item);
                for (final Item component : SlayerHelmItemCreation.REQUIRED_ITEMS) {
                    if (component.getId() == 8921 && item.getName().endsWith("(i)")) {
                        player.getInventory().addItem(BLACK_MASK_I);
                    } else {
                        player.getInventory().addItem(component);
                    }
                }
            } else {
                if (player.getInventory().getFreeSlots() < SlayerHelmItemCreation.REQUIRED_ITEMS.length + 2) {
                    player.sendMessage("You need more inventory space to disassemble your slayer helmet.");
                    return;
                }
                player.getInventory().deleteItem(item);
                for (final Item component : SlayerHelmItemCreation.REQUIRED_ITEMS) {
                    if (component.getId() == 8921 && item.getName().endsWith("(i)")) {
                        player.getInventory().addItem(BLACK_MASK_I);
                    } else {
                        player.getInventory().addItem(component);
                    }
                }
                for (final SlayerHelm.SlayerHelmRecolour recolour : SlayerHelmRecolour.values) {
                    if (item.getId() == recolour.getHelm() + (item.getName().endsWith("(i)") ? 2 : 0)) {
                        player.getInventory().addItem(new Item(recolour.getBase()));
                        break;
                    }
                }
            }
        });
        bind("Check", (player, item, slotId) -> player.getSlayer().sendTaskInformation());
        bind("Log", (player, item, slotId) -> player.getNotificationSettings().sendKillLog(NotificationSettings.SLAYER_NPC_NAMES, true));
        bind("Partner", (player, item, slotId) -> {
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 68);
            player.getSlayer().refreshPartnerInterface();
        });
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final SlayerHelm.SlayerHelmRecolour recolour : SlayerHelmRecolour.values) {
            list.add(recolour.getHelm()); //regular
            list.add(recolour.getHelm() + 2); //imbued
        }
        list.add(ItemId.SLAYER_HELMET);
        list.add(ItemId.SLAYER_HELMET_I);
        return list.toArray(new int[list.size()]);
    }

    public enum SlayerHelmRecolour {
        BLACK(ItemId.KBD_HEADS, ItemId.BLACK_SLAYER_HELMET, "King black bonnet"),
        GREEN(ItemId.KQ_HEAD, ItemId.GREEN_SLAYER_HELMET, "Kalphite khat"),
        RED(ItemId.ABYSSAL_HEAD, ItemId.RED_SLAYER_HELMET, "Unholy helmet"),
        PURPLE(ItemId.DARK_CLAW, ItemId.PURPLE_SLAYER_HELMET, "Dark Mantle"),
        TURQOISE(ItemId.VORKATHS_HEAD_21907, ItemId.TURQUOISE_SLAYER_HELMET, "Undead head"),
        HYDRA(ItemId.ALCHEMICAL_HYDRA_HEADS, ItemId.HYDRA_SLAYER_HELMET, "Use more head");
        public static final SlayerHelmRecolour[] values = values();
        private final int base;
        private final int helm;
        private final String slayerReward;

        SlayerHelmRecolour(final int base, final int helm, final String slayerReward) {
            this.base = base;
            this.helm = helm;
            this.slayerReward = slayerReward;
        }

        public int getBase() {
            return this.base;
        }

        public int getHelm() {
            return this.helm;
        }

        public String getSlayerReward() {
            return this.slayerReward;
        }
    }
}
