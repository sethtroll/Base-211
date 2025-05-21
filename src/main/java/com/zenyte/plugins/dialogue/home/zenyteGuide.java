package com.zenyte.plugins.dialogue.home;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class zenyteGuide extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
           player.getDialogueManager().start(new Dialogue(player, npc) {
               @Override
               public void buildDialogue() {
                   {
                       options("Hello adventurer what can I help you with?", new DialogueOption("How can I start training?", key(100)), new DialogueOption("How do I start making money?", key(200)),
                               new DialogueOption("What is the Starter Island?", key(300)), new DialogueOption("How can I keep updated with the server and know what is going on?", key(400)));
                   }

                   {
                       player(100, "How can I start training?");
                       npc("You will want to start by killing Chicken then move onto the Yak Near by ...");
                       npc("Did you know The chicken here drop runes and a staff....");
                       npc("Also the Yak Drop Adamant Armour Rune Scim and Dragon also but rare!. ");
                       player("Thank you!");
                   }

                   {
                       player(200, "How do I start making money?");
                       npc("The best way to obtain starter cash is by Starting Slayer on the main land...");
                       npc("But there is also the Agility pyramid 75k a lap! thieving also... ");
                       npc("You can make money by selling things you get to other players");
                       npc("at the start its more about bringing items into the eco...");
                       player("Thank you!");
                   }

                   {
                       player(300, "What is this area Starter Island?");
                       npc("Here on starter island is the perfect place to begin your journey on Pharaoh Let me begin...");
                       npc("Before we carry on Remember Once you have Finished here use your home teleport to get to the mainland...");
                       npc("Upstairs is an anvil and furnace.. Also there is Women to Pickpocket over there..");
                       npc("Just to the East is ore to mine North Food Potato and Cabbage heal 5 here!! Npc to kill to the North East");
                       npc("Oh yea.. A cooking range North and Fishing spots east of there! you can also Chop logs and fletch North East");
                       player("Thank you!");


                   }

                   {
                       player(400, "How can I keep updated with the server and know what is going on?");
                       npc("If you would like to stay up to date I would recommend joining the discord...");
                       npc("You can find the link discord by doing the command ::discord..");
                       npc("scroll down to \"Useful links\" and click \"Website\" or \"Discord\" ");
                       npc("We have several channels on discord such as #updates and #announcements... ");
                       npc("We also have forum sections for suggestions and feedback so feel free to take a look at those..");
                       npc("Hopefully this helps and if you have any questions feel free to ask a member of the Staff Team! ");
                       player("Thank you!");
                   }
               }
           });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[]{13035};
    }
}
