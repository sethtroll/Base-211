package com.zenyte.plugins.drop.wilderness;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VenenatisProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dragon pickaxe
        appendDrop(new DropProcessor.DisplayedDrop(11920, 1, 1, 128));
        //Dragon 2h sword
        appendDrop(new DropProcessor.DisplayedDrop(7158, 1, 1, 192));
        //Treasonous ring ring
        appendDrop(new DropProcessor.DisplayedDrop(12605, 1, 1, 384));
        appendDrop(new DropProcessor.DisplayedDrop(27681, 1, 1, 286));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(384) == 0) {
                return new Item(12605);
            }
            if (random(192) == 0) {
                return new Item(7158);
            }
            if (random(128) == 0) {
                return new Item(11920);
            }
            if (random(286) == 0) {
                return new Item(27681);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{6504, 6610};
    }
}
