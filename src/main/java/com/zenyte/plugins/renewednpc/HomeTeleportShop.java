package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Matt
 */
public class HomeTeleportShop extends NPCPlugin {
    @Override
    public void handle() {

        bind("Trade", (player, npc) -> player.openShop("Teleport Shop"));
    }
    @Override
    public int[] getNPCs() {
        return new int[] {13082};
    }
}
