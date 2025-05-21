package com.zenyte.game.world.region;

import com.zenyte.game.item.Item;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.TimeUnit;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.plugins.DropPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 19. juuni 2018 : 15:08:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class DynamicArea extends Area implements RandomEventRestrictionPlugin, DropPlugin {
    private static final Logger log = LoggerFactory.getLogger(DynamicArea.class);
    protected AllocatedArea area;
    protected int chunkX;
    protected int chunkY;
    protected int staticChunkX;
    protected int staticChunkY;
    protected int sizeX;
    protected int sizeY;
    protected boolean constructed;
    private boolean destroyed;

    protected DynamicArea(final AllocatedArea allocatedArea, final int copiedChunkX, final int copiedChunkY) {
        this(allocatedArea.getSizeX(), allocatedArea.getSizeY(), copiedChunkX, copiedChunkY, (allocatedArea.getMinRegionX() << 3) + MapBuilder.PADDING, (allocatedArea.getMinRegionY() << 3) + MapBuilder.PADDING);
        this.area = allocatedArea;
    }

    protected DynamicArea(final int sizeX, final int sizeY, final int staticChunkX, final int staticChunkY, final int chunkX, final int chunkY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.staticChunkX = staticChunkX;
        this.staticChunkY = staticChunkY;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    @Override
    public RSPolygon[] polygons() {
        return null;
    }

    public Location onLoginLocation() {
        return null;
    }

    /**
     * Gets an instanced worldtile based on the coordinates of the static area in the map.
     *
     * @param x      coordinate to the static map location.
     * @param y      coordinate to the static map location.
     * @param height height of the static map location.
     * @return instanced worldtile.
     */
    public Location getLocation(final int x, final int y, final int height) {
        return new Location((chunkX * 8) + (x - (staticChunkX * 8)), (chunkY * 8) + (y - (staticChunkY * 8)), height);
    }

    public int getX(final int x) {
        return (chunkX * 8) + (x - (staticChunkX * 8));
    }

    public int getY(final int y) {
        return (chunkY * 8) + (y - (staticChunkY * 8));
    }

    /**
     * Gets an instanced worldtile based on the coordinates of the static area in the map.
     *
     * @param tile coordinates to the static map location.
     * @return instanced worldtile.
     */
    public Location getLocation(final Location tile) {
        return new Location((chunkX * 8) + (tile.getX() - (staticChunkX * 8)), (chunkY * 8) + (tile.getY() - (staticChunkY * 8)), tile.getPlane());
    }

    public Location getStaticLocation(final Location instanced) {
        final Location instanceBase = new Location(chunkX * 8, chunkY * 8, instanced.getPlane());
        final Location tileBase = new Location(staticChunkX * 8, staticChunkY * 8, instanced.getPlane());
        final int xOff = instanced.getX() - instanceBase.getX();
        final int yOff = instanced.getY() - instanceBase.getY();
        return new Location(tileBase.getX() + xOff, tileBase.getY() + yOff, tileBase.getPlane());
    }

    public int getHash(final int hash) {
        final int x = (hash >> 14) & 16383;
        final int y = hash & 16383;
        final int plane = (hash >> 28) & 3;
        return Location.getHash((chunkX * 8) + (x - (staticChunkX * 8)), (chunkY * 8) + (y - (staticChunkY * 8)), plane);
    }

    /**
     * Constructs the instanced area on the map. Handled on a different thread from the game thread, to avoid problems with load since it
     * takes quite a long time to build.
     */
    public void constructRegion() {
        if (constructed) {
            return;
        }
        GlobalAreaManager.add(this);
        try {
            MapBuilder.copyAllPlanesMap(area, staticChunkX, staticChunkY, chunkX, chunkY, sizeX, sizeY);
        } catch (OutOfBoundaryException e) {
            log.error("", e);
        }
        constructed = true;
        constructed();
    }

    /**
     * Executed after the region has been constructed. Is executed on the main game thread for safety.
     */
    public abstract void constructed();

    /**
     * Destroys the instanced area on the map.
     */
    public void destroyRegion() {
        WorldTasksManager.schedule(() -> {
            if (destroyed || !players.isEmpty()) {
                return;
            }
            destroyed = true;
            GlobalAreaManager.remove(this);
            MapBuilder.destroy(area);
        }, 5);
    }

    /**
     * Executed when the area becomes empty of all players.
     */
    protected void cleared() {
        if (players.isEmpty()) {
            destroyRegion();
        }
    }

    @Override
    public void remove(final Player player, final boolean logout) {
        if (players.remove(player)) {
            leave(player, logout);
            player.getAreaManager().setArea(null);
        }
        cleared();
    }

    /**
     * Constructs a polygon based off of the input points and planes.
     *
     * @param polygonPoints a 2d array containing all of the points of the polygon, which will be shifted based off of the dynamic area.
     * @param planes        an array containing all the planes which are used to determine whether the player is inside the polygon or not.
     * @return the polygon object.
     */
    protected RSPolygon getPolygon(final int[][] polygonPoints, final int... planes) {
        final int[][] adjustedPoints = new int[polygonPoints.length][];
        for (int i = polygonPoints.length - 1; i >= 0; i--) {
            adjustedPoints[i] = new int[]{(chunkX * 8) + (polygonPoints[i][0] - (staticChunkX * 8)), (chunkY * 8) + (polygonPoints[i][1] - (staticChunkY * 8))};
        }
        return new RSPolygon(adjustedPoints, (planes.length == 0 ? new int[]{0, 1, 2, 3} : planes));
    }

    @Override
    public boolean inside(final Location location) {
        if (area == null) return false;
        return area.inside(location);
    }

    @Override
    public int visibleTicks(final Player player, final Item item) {
        return (int) TimeUnit.MINUTES.toTicks(2);
    }

    @Override
    public int invisibleTicks(final Player player, final Item item) {
        return (int) TimeUnit.MINUTES.toTicks(28);
    }

    public AllocatedArea getArea() {
        return this.area;
    }

    public void setArea(final AllocatedArea area) {
        this.area = area;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public void setChunkX(final int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkY() {
        return this.chunkY;
    }

    public void setChunkY(final int chunkY) {
        this.chunkY = chunkY;
    }

    public int getStaticChunkX() {
        return this.staticChunkX;
    }

    public void setStaticChunkX(final int staticChunkX) {
        this.staticChunkX = staticChunkX;
    }

    public int getStaticChunkY() {
        return this.staticChunkY;
    }

    public void setStaticChunkY(final int staticChunkY) {
        this.staticChunkY = staticChunkY;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public void setSizeX(final int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public void setSizeY(final int sizeY) {
        this.sizeY = sizeY;
    }

    public boolean isConstructed() {
        return this.constructed;
    }

    public void setConstructed(final boolean constructed) {
        this.constructed = constructed;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public void setDestroyed(final boolean destroyed) {
        this.destroyed = destroyed;
    }
}
