package com.zenyte.game.item;

import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zenyte.game.world.region.area.wilderness.WildernessArea.isWithinWilderness;

/**
 * @author Kris | 15/06/2019 10:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemOnPlayerHandler {
    private static final Logger log = LoggerFactory.getLogger(ItemOnPlayerHandler.class);
    private static final Int2ObjectMap<ItemOnPlayerPlugin> plugins = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<? extends ItemOnPlayerPlugin> clazz) {
        try {
            if (clazz.isAnonymousClass()) {
                return;
            }
            if (clazz.isMemberClass()) {
                return;
            }
            final ItemOnPlayerPlugin o = clazz.newInstance();
            for (final int item : o.getItems()) {
                plugins.put(item, o);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void handleItemOnPlayer(final Player player, final Item item, final int slotId, final Player target) {
        if (player.isLocked() || player.isFullMovementLocked()) {
            return;
        }
        player.stopAll(false, true, true);
        if (player.isFrozen()) {
            player.sendMessage("A magical force stops you from moving.");
            return;
        }

        if (player.isStunned()) {
            player.sendMessage("You're stunned.");
            return;
        }
        if (player.isMovementLocked(true)) {
            return;
        }
        final ItemOnPlayerPlugin action = plugins.get(item.getId());
        if (action != null) {
            log.info("[" + action.getClass().getSimpleName() + "] " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + target);
            action.handle(player, item, slotId, target);
        } else {
            player.setRouteEvent(new EntityEvent(player, new EntityStrategy(target), () -> {
                player.stopAll();
                player.faceEntity(target);
                player.sendMessage("Nothing interesting happens.");
            }, true));
        }
    }
}
