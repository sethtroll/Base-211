package com.zenyte.game.content.boss.smokedevil;

import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;

/**
 * @author Tommeh | 21 aug. 2018 | 16:29:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class SmokeDevil extends NPC implements CombatScript, Spawnable {
    private static final Projectile ATTACK_PROJ = new Projectile(644, 30, 30, 30, 0, 28, 0, 5);
    private static final Projectile PROJECTILE = new Projectile(73, 30, 35, 108, 10);

    public SmokeDevil(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        this.maxDistance = 25;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public int getRespawnDelay() {
        return id == 499 ? 14 : 60;
    }

    @Override
    public int attack(final Entity target) {
        if (!(target instanceof Player player)) {
            return 0;
        }
        setAnimation(getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.FACE_MASK.isWielding(player)) {
            getCombatDefinitions().setAttackStyle("Magic");
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 15, HitType.REGULAR).onLand(hit -> {
                player.getSkills().setLevel(Skills.ATTACK, 0);
                player.getSkills().setLevel(Skills.STRENGTH, 0);
                player.getSkills().setLevel(Skills.RANGED, 0);
                player.getSkills().setLevel(Skills.MAGIC, 0);
                player.getSkills().setLevel(Skills.PRAYER, (int) (player.getSkills().getLevel(Skills.PRAYER) * 0.5064935064935066));
                player.getSkills().setLevel(Skills.DEFENCE, (int) (player.getSkills().getLevel(Skills.DEFENCE) * 0.5064935064935066));
                player.getSkills().setLevel(Skills.AGILITY, (int) (player.getSkills().getLevel(Skills.AGILITY) * 0.5064935064935066));
            }));
        } else {
            delayHit(this, World.sendProjectile(this, target, ATTACK_PROJ), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), isBoss() ? HitType.REGULAR : HitType.RANGED));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean canAttack(final Player player) {
        return player.getSkills().getLevelForXp(Skills.SLAYER) >= 93;
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (isBoss() && source instanceof Player player) {
            player.getAchievementDiaries().update(WesternProvincesDiary.KILL_THERMONUCLEAR_SMOKE_DEVIL);
        }
    }

    private boolean isBoss() {
        return id == 499;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 498 || id == 499;
    }
}
