package com.zenyte.plugins.dialogue;

import com.zenyte.game.constants.GameConstants;
import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.partyroom.FaladorPartyRoom;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 27 mei 2018 | 15:15:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BankerD extends Dialogue {
    public BankerD(Player player, NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        npc("Good day, how may I help you?");
        final FaladorPartyRoom partyRoom = FaladorPartyRoom.getPartyRoom();
        final int frequency = partyRoom.getAnnouncementFrequency();
        final boolean partyTeleport = frequency > 0 && partyRoom.getVariables().isAnnouncements();
        if (partyTeleport) {
            options(TITLE, "I'd like to access my bank account, please.", "I'd like to collect items.", "What is this place?", "Can you teleport me to the Party room?").onOptionOne(() -> GameInterface.BANK.open(player)).onOptionTwo(() -> GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player)).onOptionThree(() -> setKey(5)).onOptionFour(() -> setKey(25));
            npc(25, "Certainly.").executeAction(() -> TeleportCollection.FALADOR_PARTY_ROOM.teleport(player));
        } else {
            options(TITLE, "I'd like to access my bank account, please.", "I'd like to collect items.", "What is this place?").onOptionOne(() -> GameInterface.BANK.open(player)).onOptionTwo(() -> GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player)).onOptionThree(() -> setKey(5));
        }
        player(5, "What is this place?");
        npc("This is a branch of the Bank of Pharaoh. We have<br>branches in many towns.");
        player("And what do you do?");
        npc("We will look after your items and money for you.<br>Leave your valuables with us if you want to keep them safe.");
    }
}
