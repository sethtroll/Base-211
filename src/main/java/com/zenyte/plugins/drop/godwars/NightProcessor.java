package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 24/11/2018 21:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NightProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Armour
        appendDrop(new DisplayedDrop(6199, 1, 1, 100));
        //Bandos hilt
        appendDrop(new DisplayedDrop(7478, 300, 1000, 256));
        //Godsword shards
        appendDrop(new DisplayedDrop(30051, 1, 1, 250));
        appendDrop(new DisplayedDrop(24491, 1, 1, 400));
        appendDrop(new DisplayedDrop(24420, 1, 1, 296));
        appendDrop(new DisplayedDrop(24421, 1, 1, 296));
        appendDrop(new DisplayedDrop(24419, 1, 1, 296));
        appendDrop(new DisplayedDrop(24417, 1, 1, 296));

    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(100)) == 0) {
                return new Item(6199);
            }
            if (random(1200) == 0) {
                return new Item(7478);
            }
            if (random(1150) == 0) {
                return new Item(24491);
            }
            if (random(1100) == 0) {
                return new Item(24417);
            }
            if (random(1050) == 0) {
                return new Item(24419);
            }
            if (random(1040) == 0) {
                return new Item(24420);
            }
            if (random(1030) == 0) {
                return new Item(24421);
            }
            if ((random = random(1020)) == 0) {
                return new Item(3051);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{NpcId.PHOSANIS_NIGHTMARE_9416, 9416 };
    }
}
