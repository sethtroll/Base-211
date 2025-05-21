package com.zenyte.plugins.drop.corporealbeast;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CorporealBeastProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Spirit shield
        appendDrop(new DisplayedDrop(12829, 1, 1, 22));
        //Holy elixir
        appendDrop(new DisplayedDrop(12833, 1, 1, 57));
        //Spectral sigil
        appendDrop(new DisplayedDrop(12823, 1, 1, 342));
        //Arcane sigil
        appendDrop(new DisplayedDrop(12827, 1, 1, 342));
        //Elysian sigil
        appendDrop(new DisplayedDrop(12819, 1, 1, 1025));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(1025) == 0) {
                return new Item(12819);
            }
            int random;
            if ((random = random(342)) < 2) {
                return new Item(12823 + (random * 4));
            }
            if (random(57) == 0) {
                return new Item(12833);
            }
            if (random(22) == 0) {
                return new Item(12829);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{319};
    }
}
