package com.zenyte.game.content.skills.fletching.actions;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.plugins.dialogue.DoubleItemChat;

import static com.zenyte.game.content.skills.fletching.FletchingDefinitions.*;

/**
 * @author Tommeh | 25 aug. 2018 | 19:15:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class LogsFletching extends Action {
    private final int category;
    private final int slotId;
    private final int amount;
    private int cycle;
    private int ticks;

    public LogsFletching(final int category, final int slotId, final int amount) {
        this.category = category;
        this.slotId = slotId;
        this.amount = amount;
    }

    @Override
    public boolean start() {
        if (player.getSkills().getLevel(Skills.FLETCHING) < LEVELS[category][slotId]) {
            final String message = slotId == 0 ? "You need a Fletching level of " + LEVELS[category][slotId] + " to make arrow shafts from " + MATERIALS[category][slotId].getDefinitions().getName().toLowerCase() + "." : "You need a Fletching level of " + LEVELS[category][slotId] + " to make a " + PRODUCTS[category][slotId].getDefinitions().getName().replace(" (u)", "").toLowerCase() + ".";
            player.getDialogueManager().start(new DoubleItemChat(player, MATERIALS[category][slotId], PRODUCTS[category][slotId], message));
            return false;
        }
        if (!player.getInventory().containsItem(MATERIALS[category][slotId])) {
            player.sendMessage("You don't have all the required items.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().containsItem(MATERIALS[category][slotId])) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        final Item product = PRODUCTS[category][slotId];
        final String name = PRODUCTS[category][slotId].getDefinitions().getName().toLowerCase();
        final boolean vowel = name.startsWith("a") || name.startsWith("o") || name.startsWith("u") || name.startsWith("i") || name.startsWith("e");
        if (ticks == 0) {
            player.setAnimation(ANIMATION);
        } else if (ticks == 2) {
            player.getInventory().deleteItemsIfContains(new Item[]{MATERIALS[category][slotId]}, () -> {
                if (product.getId() == 9452) {
                    player.getAchievementDiaries().update(ArdougneDiary.MAKE_RUNE_CROSSBOW, 1);
                } else if (product.getId() == 66) {
                    player.getAchievementDiaries().update(KandarinDiary.CREATE_YEW_LONGBOW, 2);
                } else if (product.getId() == 72) {
                    player.getDailyChallengeManager().update(SkillingChallenge.FLETCH_MAGIC_SHORTBOWS);
                }
                player.getInventory().addItem(product);
                player.getSkills().addXp(Skills.FLETCHING, EXPERIENCE[category][slotId]);
                player.sendFilteredMessage("You carefully cut the wood into" + (product.getAmount() > 1 ? " " + product.getAmount() + " " : vowel ? " an " : " a ") + name + (product.getAmount() > 1 ? "s" : "") + ".");
                cycle++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }
}
