package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;

import java.util.Objects;

/**
 * @author Tommeh | 30 mrt. 2018 : 23:13:42
 * @author Kris | 05/08/2020
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * Revised by Kris.
 */
public class DemonicGorilla extends CrashSiteGorilla implements Spawnable {
    private static final int MAX_DAMAGE_THRESHOLD = 50;
    private static final int MELEE_GORILLA = NpcId.DEMONIC_GORILLA;
    private static final int RANGED_GORILLA = NpcId.DEMONIC_GORILLA_7145;
    private static final int MAGIC_GORILLA = NpcId.DEMONIC_GORILLA_7146;
    private int hitThreshold;

    public DemonicGorilla(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
    }

    @Override
    int failedHitsUntilSwitch() {
        return 3;
    }

    @Override
    public NPC spawn() {
        hitThreshold = 0;
        return super.spawn();
    }

    @Override
    public void processNPC() {
        final Entity target = combat.getTarget();
        if (target != null) {
            if (target instanceof Player) {
                if (Objects.equals(target.getTemporaryAttributes().get("demonic_gorillas_debug"), "true")) {
                    setForceTalk(new ForceTalk("Hits threshold: " + hitThreshold + ", Attacks counter: " + missedHits + ", Melee delay: " + meleeMovementDelay + ", Style: " + combatDefinitions.getAttackType()));
                }
            }
        }
        super.processNPC();
    }

    @Override
    public float getXpModifier(final Hit hit) {
        final Object weapon = hit.getWeapon();
        if (weapon != null) {
            if ("Dwarf Multicannon".equals(weapon.toString())) {
                hit.setDamage(0);
                return 0;
            }
        }
        final HitType hitType = hit.getHitType();
        if (id == MELEE_GORILLA && hitType.equals(HitType.MELEE) || id == RANGED_GORILLA && hitType.equals(HitType.RANGED) || id == MAGIC_GORILLA && hitType.equals(HitType.MAGIC)) {
            hit.setDamage(0);
            return 0;
        }
        final Entity source = hit.getSource();
        //Poison and things like this do not count so if the source is null, let's ignore it.
        if (source != null && (hitThreshold += hit.getDamage()) >= MAX_DAMAGE_THRESHOLD) {
            final int next = hitType.equals(HitType.MELEE) ? MELEE_GORILLA : hitType.equals(HitType.RANGED) ? RANGED_GORILLA : MAGIC_GORILLA;
            final AttackType style = combatDefinitions.getAttackStyle();
            setTransformation(next);
            setAnimation(boulderAnimation);
            combatDefinitions.setAttackStyle(style);
            if (combat.getCombatDelay() < 5) {
                combat.setCombatDelay(5);
            }
            hitThreshold = 0;
            source.cancelCombat();
        }
        return 1;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id >= NpcId.DEMONIC_GORILLA && id <= NpcId.DEMONIC_GORILLA_7146;
    }
}
