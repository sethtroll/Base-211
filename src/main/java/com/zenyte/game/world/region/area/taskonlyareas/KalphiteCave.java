package com.zenyte.game.world.region.area.taskonlyareas;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 01/05/2019 16:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KalphiteCave extends Area implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{3284, 9551}, {3268, 9535}, {3268, 9495}, {3264, 9491}, {3264, 9479}, {3271, 9472}, {3328, 9472}, {3350, 9494}, {3350, 9511}, {3310, 9551}})};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Kalphite Cavern: Task only";
    }

    @Override
    public boolean attack(Player player, Entity entity) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (name.startsWith("Kalphite")) {
                if (!player.getSlayer().isCurrentAssignment(entity)) {
                    player.getDialogueManager().start(new Dialogue(player, 491) {
                        @Override
                        public void buildDialogue() {
                            npc("You can only kill these Kalphite while on a slayer task!");
                        }
                    });
                    return false;
                }
            }
        }
        return true;
    }
}
