package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;

/**
 * @author Kris | 16/01/2019 20:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class JavelinRangedCombat extends RangedCombat {
    public JavelinRangedCombat(final Entity target, final AmmunitionDefinitions defs) {
        super(target, defs);
    }

    @Override
    public void extra() {
        final Projectile projectile = ammunition.getProjectile();
        final int clientCycles = projectile.getProjectileDuration(player.getLocation(), target.getLocation());
        target.setGraphics(new Graphics(344, clientCycles, 146));
    }
}
