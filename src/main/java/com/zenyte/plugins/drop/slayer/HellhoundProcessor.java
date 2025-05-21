package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class HellhoundProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Smouldering stone
        appendDrop(new DisplayedDrop(13233, 1, 1, 32768));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(32768) == 0) {
                return new Item(13233);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{104, 105, 135, 3133, 7256, 7877};
    }
}
