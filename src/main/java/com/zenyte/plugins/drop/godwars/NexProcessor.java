package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 24/11/2018 21:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NexProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Armour
        appendDrop(new DisplayedDrop(26382, 1, 1, 400));
        appendDrop(new DisplayedDrop(26384, 1, 1, 400));
        appendDrop(new DisplayedDrop(26386, 1, 1, 400));
        //Bandos hilt
        appendDrop(new DisplayedDrop(21000, 1, 1, 256));
        appendDrop(new DisplayedDrop(26370, 1, 1, 500));
        //Godsword shards
        appendDrop(new DisplayedDrop(22326, 1, 1, 333));
        appendDrop(new DisplayedDrop(22327, 1, 1, 333));
        appendDrop(new DisplayedDrop(22328, 1, 1, 333));
        appendDrop(new DisplayedDrop(26348, 1, 1, 400));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(1200)) < 3) {
                return new Item(26382 + (random * 2));
            }
            if (random(1150) == 0) {
                return new Item(21000);
            }
            if (random(1100) == 0) {
                return new Item(26348);
            }
            if (random(1050) == 0) {
                return new Item(26370);
            }
            if ((random = random(1000)) < 3) {
                return new Item(22326 + (random * 2));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{11278, 11279, 11280, 11281, 11280 };
    }
}
