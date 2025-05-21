package com.zenyte.game.item;

import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 10. nov 2017 : 23:21.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class ItemOnObjectHandler {
    private static final Logger log = LoggerFactory.getLogger(ItemOnObjectHandler.class);
    private static final Long2ObjectOpenHashMap<ItemOnObjectAction> INT_ACTIONS = new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectOpenHashMap<ItemOnObjectAction> ITEM_STRING_ACTIONS = new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectOpenHashMap<ItemOnObjectAction> OBJECT_STRING_ACTIONS = new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectOpenHashMap<ItemOnObjectAction> STRING_ACTIONS = new Long2ObjectOpenHashMap<>();
    private static final Map<Object, ItemOnObjectAction> ALL_ITEMS_USABLE = new HashMap<>();

    public static void add(final Class<?> c) {
        try {
            if (c.isAnonymousClass()) {
                return;
            }
            final Object o = c.newInstance();
            if (!(o instanceof ItemOnObjectAction action)) {
                return;
            }
            if (action.getItems() == null) {
                for (final Object object : action.getObjects()) {
                    if (object instanceof String) {
                        ALL_ITEMS_USABLE.put(object.toString().toLowerCase(), action);
                    } else {
                        ALL_ITEMS_USABLE.put(object, action);
                    }
                }
                return;
            }
            for (final Object item : action.getItems()) {
                for (final Object object : action.getObjects()) {
                    if (item instanceof Integer) {
                        if (object instanceof Integer) {
                            INT_ACTIONS.put((((long) ((Integer) item)) << 32) | ((Integer) object & 4294967295L), action);
                        } else if (object instanceof String) {
                            OBJECT_STRING_ACTIONS.put((((long) (Integer) item) << 32) | (((String) object).toLowerCase().hashCode() & 4294967295L), action);
                        }
                    } else if (item instanceof String) {
                        if (object instanceof String) {
                            STRING_ACTIONS.put((((long) ((String) item).toLowerCase().hashCode()) << 32) | (((String) object).toLowerCase().hashCode() & 4294967295L), action);
                        } else if (object instanceof Integer) {
                            ITEM_STRING_ACTIONS.put((((long) ((String) item).toLowerCase().hashCode()) << 32) | ((Integer) object & 4294967295L), action);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void handleItemOnObject(final Player player, final Item item, int slot, final WorldObject object) {
        if (player.isLocked() || player.isFullMovementLocked()) {
            return;
        }
        player.stopAll(false, true, true);
        final int itemId = item.getId();
        final int objectId = object.getId();
        final String objectName = object.getName(player).toLowerCase();
        ItemOnObjectAction action;
        if ((action = ALL_ITEMS_USABLE.get(objectId)) != null || (action = ALL_ITEMS_USABLE.get(objectName)) != null) {
            action.handle(player, item, slot, object);
            return;
        }
        final int itemHash = item.getName().toLowerCase().hashCode();
        final int objectHash = objectName.hashCode();
        long hash = (((long) itemId) << 32) | (objectId & 4294967295L);
        action = INT_ACTIONS.get(hash);
        if (action == null) {
            hash = (((long) itemId) << 32) | (objectHash & 4294967295L);
            action = OBJECT_STRING_ACTIONS.get(hash);
            if (action == null) {
                hash = (((long) itemHash) << 32) | (objectId & 4294967295L);
                action = ITEM_STRING_ACTIONS.get(hash);
                if (action == null) {
                    hash = (((long) itemHash) << 32) | (objectHash & 4294967295L);
                    action = STRING_ACTIONS.get(hash);
                }
            }
        }
        if (action != null) {
            log.info("[" + action.getClass().getSimpleName() + "] " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + object.getName() + "(" + object.getId() + ")");
            action.handle(player, item, slot, object);
        } else {
            log.info("[ItemOnObject]: " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + object.getName() + "(" + object.getId() + ")");
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
                player.stopAll();
                player.faceObject(object);
                player.sendMessage("Nothing interesting happens.");
            }));
        }
    }
}
