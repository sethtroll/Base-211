package com.zenyte.plugins.dialogue.home;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Privilege;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.renewednpc.Aubury;


public class PkMaster extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    {
                        options("Hello adventurer what can I help you with im the Pk Master?", new DialogueOption("How can I become a Pk master?", key(100)), new DialogueOption("How do I Buy Pk Master Armour?", key(200)),
                                new DialogueOption("How do i Sell my Pk Armour?", key(300)), new DialogueOption("Can i sell 1$ Tickets for real Money?", key(400)));
                    }

                    {
                        player(100, "How can I become a Pk master?");
                        npc("To become a pk master Create an account with PK at the start of ur username...");
                        npc("A Pk master stats will be removed everytime you leave the wilderness so training is pointless....");
                        npc("Anyone found trying to abuse these accounts outside wilderness will be banned!. ");
                        npc("Once your a Pk master account whenever you cross the ditch ur stats will go to 99. ");
                        npc("Remember any Fights/Gambling Must be recorded to Complain so i can actually see what went on.. ");
                        npc("All Pk armour and weapons will leave an overlay on floor until log out so i can log the fights.. ");
                        player("Thank you!");
                    }

                    {
                        player(200, "How do I Buy Pk Master Armour and weapons?");
                        npc("You can Collect 1$ Fragments from level 200+ Npcs around Pharaohs and use them on Upgrade rack Misc Section...");
                        npc("You can also buy them from Pharaoh on ::discord no one else!! only via Crypto USDT or Paypal...");
                        npc("The best way to obtain Pk armour and weapons is right click me and buy with 1$ tickets...");
                        npc("1$ Tickets can only be bought via Crypto and Paypal and we will split fees.");
                        npc("You can make money by selling things you get to other players also");
                        player("Thank you!");
                    }

                    {
                        player(300, "How do I Sell Pk Master Armour?");
                        npc("To sell your Pk armour right click me and click sell...");
                        npc("Before we carry on Remember only Pharaoh on discord can help you cash these out...");
                        npc("Armour and weapons are sellable to myself for 50% of the Buy value items will stay on floor when picked up for log...");
                        npc("Pk armour or 1$ tickets can be freely traded and the armour and weapons can only be used in the wilderness..");
                        npc("Anyone caught trying ot use this armour outside the wilderness will have it removed.");
                        npc("I wish you all the luck on your journey to make $ while playing Pharaoh.");
                        player("Thank you!");


                    }

                    {
                        player(400, "Can i sell 1$ Tickets for real Money?");
                        npc("yes, you can cash your 1$ Tickets when you have 50...");
                        npc("To sell them speak with Pharaoh on discord in our discord channel ::discord");
                        npc("You can sell any amount of tickets above 50 you basically get 50% of the purchased value...");
                        npc("You can either Pk other for there armour worth $ or get them as random drops.");
                        npc("Hopefully this helps and if you have any questions feel free to ask a member of the Staff Team for help only! ");
                        player("Thank you!");
                    }
                }
            });
        });

        bind("Buy", (player, npc) -> {
            if (player.getPrivilege().eligibleTo(Privilege.MEMBER)) {
                player.openShop("Pk Master Buy store");
            }

        });

        bind("Sell", (player, npc) -> {

            player.openShop("Pk Master Sell store");

        });

        bind("Set Pk Master Account", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    {
                        options("Hello adventurer you wish to be a Pk Master?", new DialogueOption("Yes id like to be a Pk master (NO RETURN IF CONTINUE)?", key(100)), new DialogueOption("No, let me have a think about it", key(200)));
                    }

                    {
                        player(100, "Yes id like to be a Pk master (I understand this will reset all my stats any time i leave the wilderness)?");
                        npc("If you are sure you want to be a Pk Master press below there is no return! (walk away to cancel action)...");
                        npc("Relog for Account to Update after we have spoke (walk away to cancel action)...");
                        npc("Last Chance to walk away!. (walk away to cancel action)...");
                        npc("Last Chance to walk away!.. (walk away to cancel action)...");
                        npc("Last Chance to walk away!... (walk away to cancel action)...");
                        npc("Last Chance to walk away!.... (walk away to cancel action)...");
                        npc("Last Chance to walk away!..... (walk away to cancel action)...");
                        npc("Last Chance to walk away!...... (walk away to cancel action)...");
                        npc("There you go!").executeAction(() -> player.setPrivilege(Privilege.MEMBER));
                    }
                    {
                        player(200, "No, let me have a think about it");
                        npc("Ok ill be right here if you change ur mind...");
                        player("Thank you!");
                    }

                }
            });
        });

    }
    @Override
    public int[] getNPCs() {
        return new int[]{3307};
    }
}
