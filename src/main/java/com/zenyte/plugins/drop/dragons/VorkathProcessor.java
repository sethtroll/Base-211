package com.zenyte.plugins.drop.dragons;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VorkathProcessor extends DropProcessor {

    @Override
    public void onDeath(NPC npc, Player killer) {
        if (killer.getKillcount(npc) == 49) {
            npc.dropItem(killer, new Item(21907));
        }
    }

    @Override
    public void attach() {
        //Vorkath's head
        appendDrop(new DisplayedDrop(21907, 1, 1, 33));
        //Dragonbone necklace
        appendDrop(new DisplayedDrop(22111, 1, 1, 333));
        //Skeletal visage
        appendDrop(new DisplayedDrop(22006, 1, 1, 833));
        //Jar of decay
        appendDrop(new DisplayedDrop(22106, 1, 1, 1000));

        put(21907, new PredicatedDrop("Vorkath's head is always dropped on the 50th Vorkath kill."));
        put(1751, new PredicatedDrop("Vorkath always rolls twice on the main drop table.<br>The below drop rates are for a single roll."));
    }

    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            //All drop rates are doubled in here because vorkath rolls on the table twice every kill.
            int random;
            if ((random = random(3333)) < 2) {
                return new Item(random == 0 ? 11286 : 22006);
            }
            if (random(2000) == 0) {
                return new Item(22106);
            }
            if (random(750) == 0) {
                return new Item(22111);
            }
            if (random(33) == 0) {
                return new Item(21907);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{8058, 8059, 8060, 8061};
    }
}
