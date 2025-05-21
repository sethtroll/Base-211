package com.zenyte.game.content.skills.magic.spells.regular;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.item.BonesTablet;

/**
 * @author Kris | 8. jaan 2018 : 1:16.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BonesToBananas implements DefaultSpell {
    @Override
    public int getDelay() {
        return 1000;
    }

    @Override
    public boolean spellEffect(final Player player, final int optionId, final String option) {
        final int amount = BonesTablet.convertBones(player, BonesTablet.BONES);
        if (amount > 0) {
            player.setAnimation(BonesTablet.animation);
            player.setGraphics(BonesTablet.graphics);
            player.sendSound(BonesTablet.sound);
            this.addXp(player, 25);
            player.getInventory().refreshAll();
            player.sendMessage("You convert " + amount + " x bones to bananas.");
        } else {
            player.getDialogueManager().start(new PlainChat(player, "You aren't holding any bones!"));
            return false;
        }
        return true;
    }

    @Override
    public Spellbook getSpellbook() {
        return Spellbook.NORMAL;
    }
}
