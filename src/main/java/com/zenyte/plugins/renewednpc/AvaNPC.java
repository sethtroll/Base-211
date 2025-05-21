package com.zenyte.plugins.renewednpc;

import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.NullNpcID;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 19/04/2019 19:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AvaNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Devices", (player, npc) -> GameInterface.AVAS_DEVICES.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NullNpcID.NULL_4408, NpcId.AVA };
    }
}
