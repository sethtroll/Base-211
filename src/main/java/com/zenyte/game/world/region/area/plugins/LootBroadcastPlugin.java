package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.GlobalAreaManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 30/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface LootBroadcastPlugin {
    static void fireEvent(@NotNull final String name, @NotNull final Item loot, @NotNull final Location location, boolean guaranteedDrop) {
        fireEvent(name, loot, location, guaranteedDrop, true);
    }

    static void fireEvent(@NotNull final String name, @NotNull final Item loot, @NotNull final Location location, boolean guaranteedDrop, boolean informSelf) {
        final Area area = GlobalAreaManager.getArea(location);
        if (area instanceof LootBroadcastPlugin) {
            ((LootBroadcastPlugin) area).broadcast(name, loot, location, guaranteedDrop, informSelf);
        }
    }

    /**
     * Broadcasts the message to all the people in the area, capped to a maximum distance of 25 tiles by default.
     *
     * @param name           the name of the user who received the drop.
     * @param loot           the item that dropped.
     * @param location       the location of the item.
     * @param guaranteedDrop whether or not the drop is dropped every kill.
     */
    default void broadcast(@NotNull final String name, @NotNull final Item loot, @NotNull final Location location, final boolean guaranteedDrop, final boolean informSelf) {
        if (!(this instanceof Area area)) {
            throw new IllegalStateException();
        }
        if (!acceptDropBroadcast(loot, guaranteedDrop)) {
            return;
        }
        final int maximumDistance = maximumDistance();
        final String message = Colour.RS_GREEN.wrap(Utils.formatString(name) + " received a drop: " + loot.getAmount() + " x " + loot.getName());
        final String username = Utils.formatUsername(name);
        for (final Player player : area.getPlayers()) {
            if (player.getLocation().getTileDistance(location) > maximumDistance || (player.getUsername().equals(username) && (!informSelf || player.getNotificationSettings().shouldNotifyRareDrop(loot)))) {
                continue;
            }
            player.sendMessage(message);
        }
    }

    default int maximumDistance() {
        return 25;
    }

    default int broadcastValueThreshold() {
        return 0;
    }

    default boolean acceptDropBroadcast(@NotNull final Item item, final boolean guaranteedDrop) {
        if (guaranteedDrop) {
            return false;
        }
        return (long) item.getSellPrice() * item.getAmount() >= broadcastValueThreshold();
    }
}
