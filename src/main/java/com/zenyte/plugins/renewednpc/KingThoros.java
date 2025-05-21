package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.KingThorosDialogue;

import static com.zenyte.game.world.entity.npc.NpcId.KING_THOROS;

public class KingThoros extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new KingThorosDialogue(player, npc, false)));
        bind("Gamble", (player, npc) -> player.getDialogueManager().start(new KingThorosDialogue(player, npc, true)));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{KING_THOROS};
    }


}