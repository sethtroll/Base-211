package com.zenyte.plugins.renewednpc;

import com.zenyte.Constants;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsShopNPC extends NPCPlugin {

    @Override
    public void handle() {
        if (!Constants.CASTLE_WARS) {
            return;
        }
        bind("Talk-to", (player, npc) -> player.openShop("Castle Wars Store"));
        bind("Trade", (player, npc) -> player.openShop("Castle Wars Store"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.LANTHUS };
    }
}
