package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;


/**
 * @author Kris | 22/03/2019 18:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArdougneCloakFarmingStructure implements TeleportStructure {

    private static final Graphics graphics = new Graphics(1238);
    private static final Animation animation = new Animation(3872);

    @Override
    public Animation getStartAnimation() {
        return animation;
    }

    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }

    @Override
    public void end(Player player, Teleport teleport) {
        TeleportStructure.super.end(player, teleport);
        if (player.getTemporaryAttributes().remove("ardougne farm restricted teleport") != null) {
            final int teleports = player.getVariables().getArdougneFarmTeleports();
            final int limit = player.getNumericTemporaryAttribute("ardougne farm teleport limit").intValue();
            player.sendMessage(Colour.RED.wrap("You have used up " + teleports + "/" + limit + " of your Ardougne Farm teleports for today."));
        }
    }
}