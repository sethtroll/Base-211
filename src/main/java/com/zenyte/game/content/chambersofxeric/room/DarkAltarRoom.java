package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.SkeletalMystic;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 16. nov 2017 : 2:39.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DarkAltarRoom extends RaidArea {

    /**
     * A 2D array containing the spawn locations of the skeletal mages.
     */
    private static final Location[][] spawnLocations = new Location[][] { new Location[] { new Location(3278, 5261, 1), new Location(3286, 5265, 1), new Location(3284, 5271, 1), new Location(3277, 5273, 1), new Location(3273, 5265, 1), new Location(3278, 5270, 1) }, new Location[] { new Location(3315, 5262, 1), new Location(3318, 5259, 1), new Location(3309, 5258, 1), new Location(3301, 5256, 1), new Location(3306, 5264, 1), new Location(3312, 5259, 1) }, new Location[] { new Location(3342, 5262, 1), new Location(3342, 5266, 1), new Location(3340, 5268, 1), new Location(3338, 5274, 1), new Location(3337, 5268, 1), new Location(3338, 5260, 1) } };

    /**
     * An array containing the locations of the mark of power, which blocks the exit.
     */
    private static final Location[] markOfPowerLocations = new Location[] { new Location(3270, 5264, 1), new Location(3309, 5272, 1), new Location(3348, 5268, 1) };

    /**
     * A list of the spawned skeletal mystics.
     */
    private final List<SkeletalMystic> npcs = new ObjectArrayList<>();

    /**
     * The mark of power that's blocking the exit.
     */
    private WorldObject mark;

    public DarkAltarRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendNPCs(npcs);
    }

    @Override
    public void loadRoom() {
        final ObjectArrayList<Location> tiles = new ObjectArrayList<>(Arrays.asList(spawnLocations[index]));
        final int size = raid.getOriginalPlayers().size();
        final int count = size < 3 ? 3 : size < 5 ? 4 : size < 8 ? 5 : 6;
        //Purge the tiles list of the excess tiles.
        for (int i = tiles.size(); i > count; i--) {
            tiles.remove(Utils.random(tiles.size() - 1));
        }
        for (final Location t : tiles) {
            npcs.add((SkeletalMystic) new SkeletalMystic(raid, this, Utils.random(7604, 7606), getLocation(t)).spawn());
        }
        World.spawnObject(mark = new WorldObject(ObjectId.MARK_OF_POWER, 10, getRotation(), getObjectLocation(markOfPowerLocations[index], 2, 2, getRotation())));
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Dark Altar room";
    }

    /**
     * A list of the spawned skeletal mystics.
     */
    public List<SkeletalMystic> getNpcs() {
        return this.npcs;
    }

    /**
     * The mark of power that's blocking the exit.
     */
    public WorldObject getMark() {
        return this.mark;
    }
}
