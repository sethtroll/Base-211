package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kris | 15. jaan 2018 : 21:16.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * The acid pool object that Olm shoots out through {@link com.zenyte.game.content.chambersofxeric.greatolm.scripts.AcidSpray}
 * & {@link com.zenyte.game.content.chambersofxeric.greatolm.scripts.AcidDrip} attacks.
 */
public final class AcidPool extends WorldObject {
    /**
     * The duration of the acid pool; By nature, the pools only remain on the ground for 23 ticks.
     */
    private int ticks = 17;

    public AcidPool(final Location tile) {
        super(30032, 10, 0, tile);
    }

    public boolean process() {
        switch (--ticks) {
            case 16:
                World.spawnObject(this);
                return true;
            case 0:
                World.removeObject(this);
                return false;
            default:
                return true;
        }
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (o == this) return true;
        if (!(o instanceof AcidPool other)) return false;
        if (!other.canEqual(this)) return false;
        if (!super.equals(o)) return false;
        return this.getTicks() == other.getTicks();
    }

    protected boolean canEqual(@Nullable final Object other) {
        return other instanceof AcidPool;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        result = result * PRIME + this.getTicks();
        return result;
    }

    /**
     * The duration of the acid pool; By nature, the pools only remain on the ground for 23 ticks.
     */
    public int getTicks() {
        return this.ticks;
    }
}
