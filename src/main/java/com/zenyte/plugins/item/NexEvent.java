package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

import static com.zenyte.game.world.entity.player.GameCommands.event_started;
import static com.zenyte.game.world.entity.player.GameCommands.nex_started;

/**
 * @author Kris | 30/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NexEvent extends ItemPlugin {
    private static final Animation READ_ANIM = new Animation(7403);

    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> {
            final String name = item.getName();
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "You can make out some faded words on the ancient parchment. It appears to be an invitation to a god! Would you like start the Event?");
                    options("Will you consume the scroll. (Use outside nex only!)", new DialogueOption("Use " + name + ".", () -> readScroll(player, item, slotId)), new DialogueOption("Cancel."));
                }
            });
        });
    }

    private final void readScroll(final Player player, final Item item, final int slotId) {
        final Inventory inventory = player.getInventory();
        final Item inSlot = inventory.getItem(slotId);
        if (inSlot != item) {
            return;
        }
        final String name = inSlot.getName();
        player.lock(5);
        player.setAnimation(READ_ANIM);
        inventory.deleteItem(50750,1);
        if (nex_started == true ) {
            player.sendMessage("The Nex event has already started.");
            inventory.addItem(50750,1);
            return;
        }
        int NM_NPC = 11278;
        Location START = new Location(2925, 5203, 0);
        nex_started = true;
        World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=00FF00><shad=000000>" + player + "</col></shad> <col=FF0040>has started a Fight with Nex come help</col> "+ player +".");
        World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=FF0000>Buy 250x Nex scroll from a Slayer Store 100Pts and the Teleport from Npc at home 10M to join!!</col>");
        World.spawnNPC(NM_NPC, START, Direction.SOUTH, 0);
        player.getDialogueManager().start(new ItemChat(player, item, "You study the scroll and start the event: <col=FF0040>" + name + "</col>"));
    }

    @Override
    public int[] getItems() {
        return new int[]{50750};
    }
}
