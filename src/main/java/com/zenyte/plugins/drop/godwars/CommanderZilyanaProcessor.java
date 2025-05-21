package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 17:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CommanderZilyanaProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Saradomin sword
        appendDrop(new DisplayedDrop(11838, 1, 1, 31));
        //Saradomin's light
        appendDrop(new DisplayedDrop(13256, 1, 1, 63));
        //Armadyl crossbow
        appendDrop(new DisplayedDrop(11785, 1, 1, 127));
        //Saradomin hilt
        appendDrop(new DisplayedDrop(11814, 1, 1, 127));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 192));
        appendDrop(new DisplayedDrop(11820, 1, 1, 192));
        appendDrop(new DisplayedDrop(11822, 1, 1, 192));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(576)) < 3) {
                return new Item(11818 + (random * 2));
            }
            if ((random = random(381)) < 2) {
                return new Item(random == 0 ? 11785 : 11814);
            }
            if (random(190) == 0) {
                return new Item(13256);
            }
            if (random(95) == 0) {
                return new Item(11838);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{2205, 6493};
    }
}
