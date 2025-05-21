package com.zenyte.plugins.item;

import com.zenyte.game.item.pluginextensions.ItemPlugin;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Tommeh | 07/06/2019 | 17:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class PkSupplysfood extends ItemPlugin {
    @Override
    public void handle() {
        bind("Open", (player, item, slotId) -> {
            final PkSupplysfood.ItemSet set = ItemSet.get(item.getId());
            if (set == null) {
                return;
            }
            if (!player.getInventory().checkSpace(set.getItems().length)) {
                return;
            }
            player.getInventory().deleteItem(item);
            for (final int id : set.getItems()) {
                player.getInventory().addItem(id, 500);
            }
        });
    }

    @Override
    public int[] getItems() {
        return ItemSet.SETS.keySet().toIntArray();
    }


    public enum ItemSet {
        PK_SUPPLYS(50906, 13442, 3145, 9075, 557, 565, 555, 560 );
        private final int id;
        private final int[] items;
        public static final ItemSet[] all = values();
        public static final Int2ObjectOpenHashMap<ItemSet> SETS = new Int2ObjectOpenHashMap(all.length);

        static {
            for (final PkSupplysfood.ItemSet set : all) {
                SETS.put(set.id, set);
            }
        }

        ItemSet(final int id, final int... items) {
            this.id = id;
            this.items = items;
        }

        public static ItemSet get(final int id) {
            return SETS.get(id);
        }

        public int getId() {
            return this.id;
        }

        public int[] getItems() {
            return this.items;
        }
    }
}
