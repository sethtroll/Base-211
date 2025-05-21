package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 19/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ForceTeleport implements Teleport {

    private final Location location;

    public ForceTeleport(@NotNull final Location location) {
        this.location = location;
    }

    @Override
    public TeleportType getType() {
        return TeleportType.INSTANT_UNSAFE;
    }

    @Override
    public Location getDestination() {
        return location;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return 0;
    }

    @Override
    public Item[] getRunes() {
        return null;
    }

    @Override
    public int getWildernessLevel() {
        return 0;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }
}
