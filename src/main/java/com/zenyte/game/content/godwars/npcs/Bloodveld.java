package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/08/2019 00:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bloodveld extends KillcountNPC implements Spawnable, CombatScript {
    private static final SoundEffect attackSound = new SoundEffect(312, 10, 0);

    public Bloodveld(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean isTolerable() {
        return true;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("bloodveld");
    }

    @Override
    public int attack(final Entity target) {
        final Bloodveld npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, MAGIC, target), HitType.MELEE));
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (!(source instanceof Player player)) {
            return;
        }
        player.getAchievementDiaries().update(WildernessDiary.KILL_A_BLOODVELD);
    }
}
