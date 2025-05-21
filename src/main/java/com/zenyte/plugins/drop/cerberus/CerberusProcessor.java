package com.zenyte.plugins.drop.cerberus;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CerberusProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Primordial crystal
        appendDrop(new DisplayedDrop(13231, 1, 1, 126));
        //Pegasian crystal
        appendDrop(new DisplayedDrop(13229, 1, 1, 126));
        //Eternal crystal
        appendDrop(new DisplayedDrop(13227, 1, 1, 126));
        //Smouldering stone
        appendDrop(new DisplayedDrop(13233, 1, 1, 126));
        //Jar of souls
        appendDrop(new DisplayedDrop(13245, 1, 1, 750));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (Utils.random(750) == 0) {
            npc.dropItem(killer, new Item(13245));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(126)) < 4) {
                return new Item(13227 + (random * 2));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{NpcId.CERBERUS, NpcId.CERBERUS_5863};
    }
}
