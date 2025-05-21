package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see com.zenyte.game.content.godwars.npcs.combat.BloodveldCombat for combat script.
 */
public class InsatiableMutatedBloodveld extends SuperiorNPC {
    public InsatiableMutatedBloodveld(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7398, tile);
    }
}
