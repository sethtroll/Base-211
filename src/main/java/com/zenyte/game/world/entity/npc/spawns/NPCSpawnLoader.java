package com.zenyte.game.world.entity.npc.spawns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenyte.game.parser.Parse;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.impl.Crab;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.GlobalAreaManager;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.types.config.npcs.NPCDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NPCSpawnLoader implements Parse {
    public static final IntSet dropViewerNPCs = IntSets.synchronize(new IntOpenHashSet());
    public static final IntSet ignoredMonsters = IntSets.synchronize(new IntOpenHashSet());
    /**
     * A mapping of the definitions to their item id.
     */
    public static final List<NPCSpawn> DEFINITIONS = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(NPCSpawnLoader.class);
    private static final Int2ObjectMap<Set<String>> npcAreaMap = new Int2ObjectOpenHashMap<>();
    private static final int MAXIMUM_AREA_CHECK_RADIUS = 25;
    private static final Map<Location, String> areaMap = new Object2ObjectOpenHashMap<>();
    private static final List<NPCSpawn> artificialSpawns = new ArrayList<>();
    private static final Int2IntMap npcTransformers = new Int2IntOpenHashMap();

    static {
        //Skotizo
        artificialSpawns.add(new NPCSpawn(7286, 1698, 9886, 0, Direction.SOUTH, 5));
        //Alchemical Hydra
        artificialSpawns.add(new NPCSpawn(8621, 1364, 10265, 0, Direction.SOUTH, 5));
        //Dusk (Grotesque Guardians)
        artificialSpawns.add(new NPCSpawn(7888, 3426, 3542, 2, Direction.SOUTH, 5));
        //Obor
        artificialSpawns.add(new NPCSpawn(7416, 3095, 9832, 0, Direction.SOUTH, 5));
        //Bryophyta
        artificialSpawns.add(new NPCSpawn(8195, 3174, 9900, 0, Direction.SOUTH, 5));
        //Zulrah
        artificialSpawns.add(new NPCSpawn(2042, 2267, 3072, 0, Direction.SOUTH, 5));
        //Vorkath
        artificialSpawns.add(new NPCSpawn(8061, 2269, 4062, 0, Direction.SOUTH, 5));
        //Cerberus
        artificialSpawns.add(new NPCSpawn(5862, 1239, 1250, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(5862, 1303, 1313, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(5862, 1367, 1249, 0, Direction.SOUTH, 5));
        //Corporeal beast
       artificialSpawns.add(new NPCSpawn(319, 2990, 4381, 2, Direction.SOUTH, 5));
        //Scavenger beasts
        artificialSpawns.add(new NPCSpawn(7548, 3279, 5225, 1, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(7549, 3279, 5225, 1, Direction.SOUTH, 5));
        //Mogre
        artificialSpawns.add(new NPCSpawn(2592, 2993, 3108, 0, Direction.SOUTH, 5));
        //The Mimic
        artificialSpawns.add(new NPCSpawn(8633, 2719, 4318, 1, Direction.SOUTH, 5));
        dropViewerNPCs.addAll(SuperiorMonster.superiorMonsters);
        //Armadylian guardian
        dropViewerNPCs.add(6587);
        //Bandosian guardian
        dropViewerNPCs.add(6587);
        //Brassican mage
        dropViewerNPCs.add(7310);
        //Ancient wizard
        dropViewerNPCs.add(7307);
        //nex
        dropViewerNPCs.add(11278);
        //Krakens
        dropViewerNPCs.add(494);
        dropViewerNPCs.add(492);
        npcTransformers.put(493, 492);
        npcTransformers.put(496, 494);
        //Werewolves
        npcTransformers.put(2631, 2593);
        //All kinds of crabs.
        npcTransformers.putAll(Crab.rocks2AliveMap);
        //Wall beast
        dropViewerNPCs.add(476);
        npcTransformers.put(475, 476);
        //Zygomites
        dropViewerNPCs.add(537);
        dropViewerNPCs.add(474);
        npcTransformers.put(471, 473);
        npcTransformers.put(472, 474);
        dropViewerNPCs.add(7797);
        npcTransformers.put(7798, 7797);
        //Tree spirits
        dropViewerNPCs.add(1163);
        dropViewerNPCs.add(1861);
        dropViewerNPCs.add(1862);
        dropViewerNPCs.add(1863);
        dropViewerNPCs.add(1864);
        dropViewerNPCs.add(1865);
        dropViewerNPCs.add(1866);
        dropViewerNPCs.add(6380);
        //Locost riders
        dropViewerNPCs.add(795);
        dropViewerNPCs.add(796);
        dropViewerNPCs.add(800);
        dropViewerNPCs.add(801);
        //Brutal green dragons
        dropViewerNPCs.add(2918);
        dropViewerNPCs.add(8081);
        dropViewerNPCs.add(8583);
        //Godwars sergeants
        artificialSpawns.add(new NPCSpawn(2216, 2868, 5362, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2217, 2872, 5354, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2218, 2871, 5359, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2206, 2901, 5264, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2207, 2897, 5263, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2208, 2895, 5265, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3130, 2929, 5327, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3131, 2921, 5327, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3132, 2923, 5324, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3163, 2834, 5297, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3164, 2827, 5299, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3165, 2829, 5300, 2, Direction.SOUTH, 5));
        dropViewerNPCs.addAll(npcTransformers.values());
        artificialSpawns.forEach(spawn -> dropViewerNPCs.add(spawn.getId()));
//        //homemobs
//        artificialSpawns.add(new NPCSpawn(2108,3206,3215,0,Direction.EAST,0));
//        artificialSpawns.add(new NPCSpawn(5215,3206,3222,0,Direction.EAST,0));
    }

    public static Set<String> getFoundLocations(final int npcId) {
        return npcAreaMap.get(npcId);
    }

    public static void populateAreaMap() {
        final Int2ObjectMap<Set<String>> npcAreaMap = new Int2ObjectOpenHashMap<>();
        final ArrayList<NPCSpawn> spawns = new ArrayList<>();
        spawns.addAll(artificialSpawns);
        spawns.addAll(new ArrayList<>(DEFINITIONS));
        for (final NPCSpawn spawn : spawns) {
            final Location tile = new Location(spawn.getX(), spawn.getY(), spawn.getZ());
            final Area area = GlobalAreaManager.getArea(tile);
            if (area != null) {
                areaMap.put(tile, area.name());
            }
        }
        for (final NPCSpawn spawn : spawns) {
            final Location tile = new Location(spawn.getX(), spawn.getY(), spawn.getZ());
            final Area area = GlobalAreaManager.getArea(tile);
            String name = area == null ? "Undefined area" : area.name();
            if (area == null) {
                for (final Map.Entry<Location, String> entry : areaMap.entrySet()) {
                    final Location t = entry.getKey();
                    if (t.withinDistance(tile, MAXIMUM_AREA_CHECK_RADIUS)) {
                        name = entry.getValue();
                        break;
                    }
                }
            }
            npcAreaMap.computeIfAbsent(npcTransformers.getOrDefault(spawn.getId(), spawn.getId()), a -> new ObjectOpenHashSet<>()).add(name);
        }
        npcAreaMap.forEach((id, set) -> NPCSpawnLoader.npcAreaMap.put(id.intValue(), set));
    }

    public static void parseDefinitions() {
        try {
            new NPCSpawnLoader().parse();
        } catch (Throwable throwable) {
            log.error("", throwable);
        }
    }

    public static void loadNPCSpawns() {
        try {
            WorldTasksManager.schedule(() -> DEFINITIONS.forEach(v -> {
                dropViewerNPCs.add(v.getId());
                final Location tile = new Location(v.getX(), v.getY(), v.getZ());
                World.getChunk(tile.getChunkHash());
                World.spawnNPC(v, v.getId(), tile, v.getDirection(), v.getRadius());
            }));
        } catch (final
        //DEFINITIONS.clear();
        Throwable e) {
            log.error("", e);
        }
    }

    public static void save() {
        final String toJson = World.getGson().toJson(DEFINITIONS);
        try {
            final PrintWriter pw = new PrintWriter("data/npcs/spawns.json", StandardCharsets.UTF_8);
            pw.println(toJson);
            pw.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void parse() throws Throwable {
        if (false) {
            NewNPCSpawnLoader.load();
        } else {
            final BufferedReader br = new BufferedReader(new FileReader("data/npcs/spawns.json"));
            final NPCSpawn[] item_definitions = World.getGson().fromJson(br, NPCSpawn[].class);
            for (final NPCSpawn def : item_definitions) {
                if (def != null) {
                    DEFINITIONS.add(def);
                }
            }
        }
    }

    @Deprecated
    public void parseNew() {
        final Map<Integer, Spawn> KRIS_SPAWNS = new HashMap<>();
        final Map<Integer, Spawn> MTARIK_SPAWNS = new HashMap<>();
        final Map<Integer, Spawn> spawns = new HashMap<>();
        try {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final BufferedReader reader = new BufferedReader(new FileReader("data/npcs/NPC Spawns.json"));
            final NPCSpawnLoader.Spawn[] definitions = gson.fromJson(reader, Spawn[].class);
            for (final NPCSpawnLoader.Spawn spawn : definitions) {
                KRIS_SPAWNS.put(spawn.index, spawn);
            }
            final BufferedReader mtarikreader = new BufferedReader(new FileReader("data/npcs/MTARIK_NPC Spawns.json"));
            final NPCSpawnLoader.Spawn[] mtarikdefinitions = gson.fromJson(mtarikreader, Spawn[].class);
            for (final NPCSpawnLoader.Spawn spawn : mtarikdefinitions) {
                MTARIK_SPAWNS.put(spawn.index, spawn);
            }
            for (final Map.Entry<Integer, NPCSpawnLoader.Spawn> entry : KRIS_SPAWNS.entrySet()) {
                final Integer index = entry.getKey();
                final NPCSpawnLoader.Spawn spawn = entry.getValue();
                if (!MTARIK_SPAWNS.containsKey(index)) {
                    spawns.put(index, spawn);
                    continue;
                }
            }
            for (final Map.Entry<Integer, NPCSpawnLoader.Spawn> entry : MTARIK_SPAWNS.entrySet()) {
                final Integer index = entry.getKey();
                final NPCSpawnLoader.Spawn spawn = entry.getValue();
                if (!KRIS_SPAWNS.containsKey(index)) {
                    spawns.put(index, spawn);
                    continue;
                }
            }
            for (final Map.Entry<Integer, NPCSpawnLoader.Spawn> entry : KRIS_SPAWNS.entrySet()) {
                final Integer index = entry.getKey();
                final NPCSpawnLoader.Spawn spawn = entry.getValue();
                if (MTARIK_SPAWNS.containsKey(index)) {
                    final NPCSpawnLoader.Spawn comparableSpawn = MTARIK_SPAWNS.get(index);
                    if (comparableSpawn.minX < spawn.minX) {
                        spawn.minX = comparableSpawn.minX;
                    }
                    if (comparableSpawn.maxX > spawn.maxX) {
                        spawn.maxX = comparableSpawn.maxX;
                    }
                    if (comparableSpawn.minY < spawn.minY) {
                        spawn.minY = comparableSpawn.minY;
                    }
                    if (comparableSpawn.minY > spawn.minY) {
                        spawn.minY = comparableSpawn.minY;
                    }
                    spawns.put(index, spawn);
                }
                //spawns.put(index, spawn);
                //continue;
            }
            for (final NPCSpawnLoader.Spawn spawn : spawns.values()) {
                final NPCDefinitions defs = NPCDefinitions.get(spawn.id);
                if (defs == null) {
                    continue;
                }
                final String defName = defs.getName();
                if (spawn.id == 324 || spawn.id == 2779 || defName.startsWith("Reanimated") || defName.startsWith("Animated") && !defName.endsWith("spade") || defs.containsOption("Dismiss") || defs.isFamiliar()) {
                    continue;
                }
                if (defName.toLowerCase().contains("crab")) {
                    switch (spawn.id) {
                        case 100:
                        case 5935:
                        case 7206:
                        case 2261:
                        case 5940:
                        case 7799:
                            spawn.id++;
                            spawn.minX = spawn.maxX = ((spawn.minX + spawn.maxX) / 2);
                            spawn.minY = spawn.maxY = ((spawn.minY + spawn.maxY) / 2);
                            break;
                    }
                }
                if (defName.equals("Rocks")) {
                    spawn.minX = spawn.maxX = ((spawn.minX + spawn.maxX) / 2);
                    spawn.minY = spawn.maxY = ((spawn.minY + spawn.maxY) / 2);
                }
                if (defName.toLowerCase().contains("fishing spot")) {
                    spawn.minX = spawn.maxX;
                    spawn.minY = spawn.maxY;
                }
                final NPCSpawn s = new NPCSpawn();
                s.setId(spawn.id);
                s.setDirection(Direction.npcMap.get(spawn.direction));
                final int x = (spawn.getMaxX() + spawn.getMinX()) / 2;
                final int y = (spawn.getMaxY() + spawn.getMinY()) / 2;
                int radius = Math.max(spawn.getMaxX() - spawn.getMinX(), spawn.getMaxY() - spawn.getMinY());
                //s.setKnownIndex(spawn.getIndex());
                s.setX(x);
                s.setY(y);
                if (radius > 0) {
                    radius = Math.max(radius, 4);
                }
                if (radius == 0) {
                    if (defs.containsOption("Attack")) {
                        radius = 4;
                    }
                }
                s.setRadius(radius);
                s.setZ(spawn.z);
                if (radius == 0) {
                    s.setRadius(null);
                }
                if (s.getDirection() == Direction.SOUTH || radius > 0) {
                    s.setDirection(null);
                }
                DEFINITIONS.add(s);
            }
            final String toJson = gson.toJson(DEFINITIONS);
            try {
                final PrintWriter pw = new PrintWriter("data/npcs/spawns.json", StandardCharsets.UTF_8);
                pw.println(toJson);
                pw.close();
            } catch (final Exception e) {
                log.error("", e);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }


    @Deprecated
    private static final class Spawn {
        private String name;
        private int id;
        private int index;
        private int direction;
        private int minX;
        private int minY;
        private int maxX;
        private int maxY;
        private int z;

        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public void setId(final int id) {
            this.id = id;
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(final int index) {
            this.index = index;
        }

        public int getDirection() {
            return this.direction;
        }

        public void setDirection(final int direction) {
            this.direction = direction;
        }

        public int getMinX() {
            return this.minX;
        }

        public void setMinX(final int minX) {
            this.minX = minX;
        }

        public int getMinY() {
            return this.minY;
        }

        public void setMinY(final int minY) {
            this.minY = minY;
        }

        public int getMaxX() {
            return this.maxX;
        }

        public void setMaxX(final int maxX) {
            this.maxX = maxX;
        }

        public int getMaxY() {
            return this.maxY;
        }

        public void setMaxY(final int maxY) {
            this.maxY = maxY;
        }

        public int getZ() {
            return this.z;
        }

        public void setZ(final int z) {
            this.z = z;
        }
    }
}
