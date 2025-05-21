package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:50.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetNightmareD extends Dialogue {

    public PetNightmareD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        player("Not sure if im Awake... Am i asleep?");
        npc("You are awake sir playing Pharaoh...");
        player("Is this a nightmare?");
    }

}
