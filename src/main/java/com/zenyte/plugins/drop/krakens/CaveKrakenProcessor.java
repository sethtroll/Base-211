package com.zenyte.plugins.drop.krakens;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 17:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CaveKrakenProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Uncharged trident
        appendDrop(new DisplayedDrop(11908, 1, 1, 66));
        //Kraken tentacle
        appendDrop(new DisplayedDrop(12004, 1, 1, 300));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(300) == 0) {
                return new Item(12004);
            }
            if (random(66) == 0) {
                return new Item(11908);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{492};
    }
}
