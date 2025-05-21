package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Useful_herb_drop_table>Useful herb drop table</a>
 * @since 24/11/2019
 */
public class UsefulHerbTable {

    public static final DropTable table = new DropTable();

    static {
        table.append(ItemDefinitions.getOrThrow(ItemId.GRIMY_AVANTOE).getNotedId(), 15)
                .append(ItemDefinitions.getOrThrow(ItemId.GRIMY_SNAPDRAGON).getNotedId(), 12)
                .append(ItemDefinitions.getOrThrow(ItemId.GRIMY_RANARR_WEED).getNotedId(), 12)
                .append(ItemDefinitions.getOrThrow(ItemId.GRIMY_TORSTOL).getNotedId(), 9);
    }

    public static Item roll() {
        return table.rollItem();
    }

}
