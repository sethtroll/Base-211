package com.zenyte.game.content.minigame.warriorsguild.shotput;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 23:04.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShotTakeD extends Dialogue {

    private final Item item;

    public ShotTakeD(final Player player, final int npcId, final Item item) {
        super(player, npcId);
        this.item = item;
    }

    @Override
    public void buildDialogue() {
        NPC ref = World.getNPC(item.getId() == ShotputArea.SHOT_18LB_ITEM.getId() ? 6073 : 6074, 11319);
        if (ref != null)
            ref.setFaceLocation(new Location(player.getLocation()));
        npc("Hey! You can't take that, it's guild propery. Take one from the pile.");
    }

}
