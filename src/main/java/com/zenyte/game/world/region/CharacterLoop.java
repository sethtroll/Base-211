package com.zenyte.game.world.region;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Kris | 18. sept 2018 : 01:09:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public interface CharacterLoop {
    static <T extends Entity> List<T> find(final Location location, final int radius, final Class<T> clazz, final Predicate<T> predicate) {
        final ArrayList<T> list = new ArrayList<>();
        final int radiusChunkSize = (int) Math.ceil(radius / 8.0F);
        final int fullWidth = radiusChunkSize << 3;
        final int px = location.getX();
        final int py = location.getY();
        final int pz = location.getPlane();
        final boolean checkPlayers = clazz.isAssignableFrom(Player.class);
        final boolean checkNPCs = clazz.isAssignableFrom(NPC.class);
        for (int x = Math.max(0, px - fullWidth), xLength = Math.min(16383, px + fullWidth); x <= xLength; x += 8) {
            for (int y = Math.max(0, py - fullWidth), yLength = Math.min(16383, py + fullWidth); y <= yLength; y += 8) {
                final int hash = Chunk.getChunkHash(x >> 3, y >> 3, pz);
                final Chunk chunk = World.getChunk(hash);
                if (checkPlayers) {
                    addCharactersFromList(radius, predicate, chunk.getPlayers(), location, list);
                }
                if (checkNPCs) {
                    addCharactersFromList(radius, predicate, chunk.getNPCs(), location, list);
                }
            }
        }
        return list;
    }

    static <T extends Entity> void forEach(final Location location, final int radius, final Class<T> clazz, final Function<T> function) {
        final int radiusChunkSize = (int) Math.ceil(radius / 8.0F);
        final int fullWidth = radiusChunkSize << 3;
        final int px = location.getX();
        final int py = location.getY();
        final int pz = location.getPlane();
        final boolean checkPlayers = clazz.isAssignableFrom(Player.class);
        final boolean checkNPCs = clazz.isAssignableFrom(NPC.class);
        for (int x = Math.max(0, px - fullWidth), xLength = Math.min(16383, px + fullWidth); x <= xLength; x += 8) {
            for (int y = Math.max(0, py - fullWidth), yLength = Math.min(16383, py + fullWidth); y <= yLength; y += 8) {
                final int hash = Chunk.getChunkHash(x >> 3, y >> 3, pz);
                final Chunk chunk = World.getChunk(hash);
                if (checkPlayers) {
                    forEach(radius, chunk.getPlayers(), location, function);
                }
                if (checkNPCs) {
                    forEach(radius, chunk.getNPCs(), location, function);
                }
            }
        }
    }

    static <T extends Entity> void forEach(final int radius, final List<? extends Entity> entities, final Location location, final Function<T> function) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            final T loopedEntity = (T) entities.get(i);
            if (loopedEntity.isFinished() || !loopedEntity.isRunning() || !loopedEntity.getPosition().withinDistance(location, radius)) {
                continue;
            }
            function.apply(loopedEntity);
        }
    }

    static <T extends Entity> void populateEntityList(final Collection<Entity> list, final Location location, final int radius, final Class<T> clazz, final Predicate<Entity> predicate) {
        final int radiusChunkSize = (int) Math.ceil(radius / 8.0F);
        final int fullWidth = radiusChunkSize << 3;
        final int px = location.getX();
        final int py = location.getY();
        final int pz = location.getPlane();
        final boolean checkPlayers = clazz.isAssignableFrom(Player.class);
        final boolean checkNPCs = clazz.isAssignableFrom(NPC.class);
        for (int x = Math.max(0, px - fullWidth), xLength = Math.min(16384, px + fullWidth); x <= xLength; x += 8) {
            for (int y = Math.max(0, py - fullWidth), yLength = Math.min(16384, py + fullWidth); y <= yLength; y += 8) {
                final int hash = Chunk.getChunkHash(x >> 3, y >> 3, pz);
                final Chunk chunk = World.getChunk(hash);
                if (checkPlayers) {
                    addEntitiesWithinRadius(radius, chunk.getPlayers(), location, list, predicate);
                }
                if (checkNPCs) {
                    addEntitiesWithinRadius(radius, chunk.getNPCs(), location, list, predicate);
                }
            }
        }
    }

    static void forEachChunk(final int regionId, final Function<Chunk> function) {
        final int rx = (regionId >> 8) << 6;
        final int ry = (regionId & 255) << 6;
        for (int z = 0; z < 4; z++) {
            for (int x = rx, xLength = (rx + 64); x <= xLength; x += 8) {
                for (int y = ry, yLength = (ry + 64); y <= yLength; y += 8) {
                    final int hash = Chunk.getChunkHash(x >> 3, y >> 3, z);
                    final Chunk chunk = World.getChunk(hash);
                    function.apply(chunk);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Entity> void addCharactersFromList(final int radius, final Predicate<T> predicate, final List<? extends Entity> entities, final Location location, final List<T> list) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            final T loopedEntity = (T) entities.get(i);
            if (loopedEntity.isFinished() || !loopedEntity.isRunning() || !loopedEntity.getPosition().withinDistance(location, radius) || !predicate.test(loopedEntity)) {
                continue;
            }
            list.add(loopedEntity);
        }
    }

    static <T extends Entity> void addEntitiesWithinRadius(final int radius, final List<T> entities, final Location location, final Collection<Entity> list, final Predicate<Entity> predicate) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            final T loopedEntity = entities.get(i);
            if (loopedEntity.isFinished() || !loopedEntity.isRunning() || !loopedEntity.getPosition().withinDistance(location, radius) || !predicate.test(loopedEntity)) {
                continue;
            }
            list.add(loopedEntity);
        }
    }

    /**
     * Loops over all the nearby chunks and finds all entities of the parametrized type within the specified distance, adds them to the list
     * if predicate runs true on them.
     *
     * @param <T>       the type of the retrieved entities, must be an extension of {@link Entity}
     * @param radius    the maximum allowed radius.
     * @param clazz     the class of the return type.
     * @param predicate the predicate upon which to test.
     * @return a list of entities which match the predicate and requirements.
     */
    default <T extends Entity> List<T> findCharacters(final int radius, final Class<T> clazz, final Predicate<T> predicate) {
        return find(((Entity) this).getLocation(), radius, clazz, predicate);
    }

    @FunctionalInterface
    interface Function<T> {
        void apply(final T t);
    }
}
