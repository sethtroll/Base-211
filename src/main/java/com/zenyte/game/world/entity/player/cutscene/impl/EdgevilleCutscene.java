package com.zenyte.game.world.entity.player.cutscene.impl;

import com.zenyte.game.HintArrow;
import com.zenyte.game.HintArrowPosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.renewednpc.ZenyteGuide;

/**
 * @author Kris | 26/01/2019 01:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleCutscene extends Cutscene {

    public static final Location START_LOCATION = new Location(3088, 3492, 0);

    @Override
    public void build() {
        player.getPacketDispatcher().sendClientScript(4583, 1);
        addActions(0, () -> player.lock(), () -> player.setViewDistance(30), () -> player.getAppearance().setInvisible(true),
                () -> player.setLocation(START_LOCATION), () -> chat(player, "Very well! Let's begin then.."));
        addActions(6, () -> action(player, "In this Building you will find the <col=00080>Skilling & Food shops</col> incase you want to start your journey just skilling or if you need food when training your combat skills.", 3083, 3515),
                new CameraPositionAction(player, new Location(3088, 3500), 2000, 5, 10),
                new CameraLookAction(player, new Location(3077, 3514, 2), 2000, 5, 10));
        addActions(18, () -> action(player, "And over here you'll find the <col=00080>Ranged armoury & weaponry shops</col> selling all kinds of ranged equipment anywhere from ammunition and (cross)bows to dragonleather equipment.", 3074, 3511),
                new CameraPositionAction(player, new Location(3088, 3500), 2000, 5, 10),
                new CameraLookAction(player, new Location(3077, 3514, 2), 2000, 5, 10));
        addActions(30, () -> action(player, "Over here you can find the <col=00080>Magic armoury & weaponry shops</col> which sell all kinds of runes, staves and robes..", 3076, 3507),
                new CameraPositionAction(player, new Location(3088, 3500), 2000, 5, 10),
                new CameraLookAction(player, new Location(3077, 3514, 2), 2000, 5, 10));
        addActions(42, () -> action(player, " You can also find <col=00080>Perdu</col> where you can repair your damaged equipment.", 3075, 3482),
                new CameraPositionAction(player, new Location(3083, 3469), 2000, 5, 10),
                new CameraLookAction(player, new Location(3083, 3469, 1), 2000, 5, 10));
        addActions(54, () -> action(player, "Here we have a <col=00080>General shop</col> , also <col=00080>Melee armoury & weaponry shops</col> which contain a large variety of melee equipment anywhere from bronze to rune and some dragon weaponry as well!", 3076, 3516),
                new CameraPositionAction(player, new Location(3088, 3500), 2000, 5, 10),
                new CameraLookAction(player, new Location(3077, 3514, 2), 2000, 5, 10));
        addActions(66, () -> action(player, "Here you can start thieving for starter Cash on  <col=00080>Thieving Stalls</col> Bank them via the Deposit Box and sell for Cash.", 3101, 3498),
                new CameraPositionAction(player, new Location(3093, 3492), 1000, 5, 10),
                new CameraLookAction(player, new Location(3101, 3492, 0), 0, 5, 10));
        addActions(78, () -> action(player, "Over here we have a furnace where you can smelt your ores into bars as well as a pottery oven to fire your unfired clay items.", 3092, 3480),
                new CameraPositionAction(player, new Location(3087, 3497), 1000, 5, 10),
                new CameraLookAction(player, new Location(3083, 3475, 0), 0, 5, 10));
        addActions(90, () -> action(player, "And over here you can find <col=00080>Krystilia</col> & <col=00080>Duradel</col> who assign wilderness slayer tasks or Normal slayer tasks. For regular tasks, you can visit each slayer master ", 3112, 3515),
                new CameraPositionAction(player, new Location(3108, 3508), 1000, 5, 10),
                new CameraLookAction(player, new Location(3115, 3515, 0), 0, 5, 10));

        addActions(102, () -> action(player, "In here you can find <col=00080>Adam</col> for all your ironman related inquiries like reverting your ironman mode and claiming armour and the <col=00080>Make-over-mage</col> where you can change your appearance like on the tutorial island.", 3098, 3515),
                new CameraPositionAction(player, new Location(3110, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3098, 3512, 0), 0, 5, 10));

        addActions(114, () -> action(player, "You'll also find myself in case you want to change your experience mode or rewatch this tutorial and " +
                        "<col=00080>Mac</col> which sells skillcapes and of course the Max cape.", 3094, 3512),
                new CameraPositionAction(player, new Location(3110, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3098, 3512, 0), 0, 5, 10));


        addActions(126, () -> action(player, "...and finally, you can find an <col=00080>altar</col> where you're able to change your spellbook and get extra <col=00080>Prayer</col> experience when offering bones.", 3098, 3509),
                new CameraPositionAction(player, new Location(3110, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3098, 3512, 0), 0, 5, 10));


        addActions(138, () -> action(player, "This is the <col=00080>Pharaoh Portal</col> which you can use to teleport all around Pharaoh! Some teleports will be unlocked by default and some require you to visit a certain area first.", 3077, 3500),
                new CameraPositionAction(player, new Location(3093, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3087, 3496, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.FINAL_LOCATION));


        addActions(150, () -> action(player, "And finally over here we have the main hub of the home area where you can find <col=00080>Bankers & Grand Exchange Clerks</col>.", 3087, 3495, HintArrowPosition.EAST),
                new CameraPositionAction(player, new Location(3093, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3087, 3496, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.FINAL_LOCATION));

        addActions(162, () -> action(player, "Introducing Pharaoh, where the spirit of Old School RuneScape comes alive with a perfect blend of Semi-Fast XP Rates and an exhilarating 3x Enhanced Drop Rate, making every loot feel truly worthwhile..", 3087, 3495, HintArrowPosition.EAST),
                new CameraPositionAction(player, new Location(3093, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3087, 3496, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.FINAL_LOCATION));
        addActions(174, () -> action(player, "At Pharaoh, we cater to the dedicated and grind-loving players who seek a nostalgic yet enhanced gaming experience.", 3087, 3495, HintArrowPosition.EAST),
                new CameraPositionAction(player, new Location(3093, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3087, 3496, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.FINAL_LOCATION));
        addActions(186, () -> action(player, "Join us and rediscover the thrill of the grind in a world where every drop is a triumph. Pharaoh - Where the journey is as rewarding as the destination!", 3087, 3495, HintArrowPosition.EAST),
                new CameraPositionAction(player, new Location(3093, 3503), 1000, 5, 10),
                new CameraLookAction(player, new Location(3087, 3496, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.FINAL_LOCATION));


        addActions(198, () -> player.setViewDistance(15), () -> player.getPacketDispatcher().resetHintArrow(), () -> ZenyteGuide.finishTutorial(player),
                new CameraResetAction(player));
    }

    private void action(final Player player, final String message, final int x, final int y) {
        action(player, message, x, y, HintArrowPosition.CENTER);
    }

    private void action(final Player player, final String message, final int x, final int y, final HintArrowPosition position) {
        player.getPacketDispatcher().sendHintArrow(new HintArrow(x, y, (byte) 50, position));
        chat(player, message);
    }

    private void chat(final Player player, final String message) {
        player.getDialogueManager().start(new NPCChat(player, 3308, message, false));
    }
}
