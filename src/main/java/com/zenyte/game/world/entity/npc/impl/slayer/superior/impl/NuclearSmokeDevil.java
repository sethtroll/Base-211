package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NuclearSmokeDevil extends SuperiorNPC implements CombatScript {
    private static final Projectile ATTACK_PROJ = new Projectile(644, 50, 30, 30, 0, 28, 0, 5);
    private static final Projectile PROJECTILE = new Projectile(73, 50, 35, 108, 10);

    public NuclearSmokeDevil(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7406, tile);
    }

    @Override
    public int attack(final Entity target) {
        if (target instanceof NPC) {
            return -1;
        }
        final Player player = (Player) target;
        setAnimation(getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.FACE_MASK.isWielding(player)) {
            getCombatDefinitions().setAttackStyle("Magic");
            World.sendProjectile(this, target, PROJECTILE);
            WorldTasksManager.schedule(() -> {
                player.getSkills().setLevel(Skills.ATTACK, 0);
                player.getSkills().setLevel(Skills.STRENGTH, 0);
                player.getSkills().setLevel(Skills.RANGED, 0);
                player.getSkills().setLevel(Skills.MAGIC, 0);
                player.getSkills().setLevel(Skills.PRAYER, (int) (player.getSkills().getLevel(Skills.PRAYER) * 0.5064935064935066));
                player.getSkills().setLevel(Skills.DEFENCE, (int) (player.getSkills().getLevel(Skills.DEFENCE) * 0.5064935064935066));
                player.getSkills().setLevel(Skills.AGILITY, (int) (player.getSkills().getLevel(Skills.AGILITY) * 0.5064935064935066));
                delayHit(this, 0, player, new Hit(this, 15, HitType.REGULAR));
            }, PROJECTILE.getTime(this, target));
        } else {
            World.sendProjectile(this, target, ATTACK_PROJ);
            delayHit(this, ATTACK_PROJ.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
