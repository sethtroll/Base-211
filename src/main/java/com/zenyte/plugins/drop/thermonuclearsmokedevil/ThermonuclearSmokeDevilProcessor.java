package com.zenyte.plugins.drop.thermonuclearsmokedevil;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ThermonuclearSmokeDevilProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Occult necklace
        appendDrop(new DisplayedDrop(12002, 1, 1, 87));
        //Smoke battlestaff
        appendDrop(new DisplayedDrop(11998, 1, 1, 125));
        //Dragon chainbody
        appendDrop(new DisplayedDrop(3140, 1, 1, 500));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(500) == 0) {
                return new Item(3140);
            }
            if (random(125) == 0) {
                return new Item(11998);
            }
            if (random(87) == 0) {
                return new Item(12002);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{499};
    }
}
