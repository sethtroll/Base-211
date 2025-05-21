package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 19:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CaptainBarnaby extends NPCPlugin {

    private static final Item COST = new Item(995, 30);


    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Right click me and select where you'd like to go.");
            }
        }));

        bind("Brimhaven", (player, npc) -> {
            String departure = player.getArea().name().equals("Rimmington") ? "Rimmington" : "Ardougne";
            travel(player, npc, departure, "Brimhaven");
        });
        bind("Rimmington", (player, npc) -> {
            String departure = player.getArea().name().equals("Brimhaven") ? "Brimhaven" : "Ardougne";
            travel(player, npc, departure, "Rimmington");

        });
        bind("Ardougne", (player, npc) -> {
            String departure = player.getArea().name().equals("Rimmington") ? "Rimmington" : "Brimhaven";
            travel(player, npc, departure, "Ardougne");
        });
    }

    private void travel(Player player, NPC npc, String departure, String destination) {
        if (player.getInventory().containsItem(COST)
                && !player.getInventory().deleteItem(COST).isFailure()) {
            player.sendMessage("You pay " + COST.getAmount() + " coins and board the ship.");
            Sailing.sail(player, departure, destination);
        } else {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("You don't have enough gold on you to go on this trip.");
                }
            });
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.CAPTAIN_BARNABY, NpcId.CAPTAIN_BARNABY_8763, NpcId.CAPTAIN_BARNABY_8764, NpcId.CAPTAIN_BARNABY_9250};
    }
}
