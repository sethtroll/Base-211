package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;

import static com.zenyte.game.util.TimeUnit.MILLISECONDS;

/**
 * @author Kris | 24/03/2019 13:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VolcanicMineStructure extends TabletStructure {
    private static final Animation BREAK_ANIM = new Animation(7741);
    private static final SoundEffect SOUND = new SoundEffect(965, 5, 0);
    private static final Graphics SHRINK_GFX = new Graphics(1409);

    @Override
    public Animation getStartAnimation() {
        return BREAK_ANIM;
    }

    @Override
    public void start(final Player player, final Teleport teleport) {
        final double experience = teleport.getExperience();
        final Animation startAnimation = Utils.getOrDefault(getStartAnimation(), Animation.STOP);
        final Graphics startGraphics = Utils.getOrDefault(getStartGraphics(), Graphics.RESET);
        World.sendSoundEffect(player, SOUND);
        player.lock();
        teleport.onUsage(player);
        if (experience != 0) {
            player.getSkills().addXp(Skills.MAGIC, experience);
        }
        player.setAnimation(startAnimation);
        player.setGraphics(startGraphics);
        final int breakDuration = (int) MILLISECONDS.toTicks(BREAK_ANIM.getCeiledDuration());
        player.setGraphics(SHRINK_GFX);
        WorldTasksManager.scheduleOrExecute(() -> end(player, teleport), breakDuration);
    }

    @Override
    public void end(final Player player, final Teleport teleport) {
        if (isTeleportPrevented(player, teleport) || isAreaPrevented(player, teleport) || isRestricted(player, teleport)) {
            stop(player, teleport);
            return;
        }
        final Animation endAnimation = Utils.getOrDefault(getEndAnimation(), Animation.STOP);
        final Graphics endGraphics = Utils.getOrDefault(getEndGraphics(), Graphics.RESET);
        final Location location = getRandomizedLocation(player, teleport);
        verifyLocation(player, location);
        teleport.onArrival(player);
        player.setLocation(location);
        player.setAnimation(endAnimation);
        player.setGraphics(endGraphics);
        player.getInterfaceHandler().closeInterfaces();
        WorldTasksManager.scheduleOrExecute(() -> stop(player, teleport), (int) MILLISECONDS.toTicks(endAnimation.getCeiledDuration()) + EXTRA_DELAY - 1);
        updateDiaries(player, teleport);
    }
}
