package com.zenyte;

import com.zenyte.api.client.APIClient;
import com.zenyte.api.client.query.ApiPing;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.constants.GameConstants;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.grandexchange.GrandExchangeHandler;
import com.zenyte.game.content.multicannon.DwarfMulticannon;
import com.zenyte.game.content.skills.agility.AgilityManager;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.parser.impl.NPCExamineLoader;
import com.zenyte.game.shop.Shop;
import com.zenyte.game.ui.testinterfaces.DropViewerInterface;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.game.world.flooritem.GlobalItem;
import com.zenyte.game.world.info.WorldProfile;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectExamineLoader;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.XTEALoader;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.MultiwayArea;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.tools.jagcached.cache.Cache;
import mgi.types.Definitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * A class to start both the file and game servers.
 *
 * @author Tom
 */
public class GameEngine {
    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);
    public static final long SERVER_START_TIME = System.nanoTime();
    private static boolean loaded;
    private static final List<Runnable> postServerLoadTasks = new ArrayList<>();

    public static void appendPostLoadTask(final Runnable task) {
        if (loaded) {
            throw new IllegalStateException();
        }
        postServerLoadTasks.add(task);
    }

    public static final Logger logger = LoggerFactory.getLogger("Default logger");

    public static long serverStartTime;

    /**
     * The entry point of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(final String[] args) {
        serverStartTime = System.nanoTime();
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        log.info("Starting " + GameConstants.SERVER_NAME + "...");
        try {
            if (args.length > 0) {
                Constants.WORLD_PROFILE = new WorldProfile(args[0]);
            } else {
                Constants.WORLD_PROFILE = new WorldProfile("offline_dev");
            }
            log.info("Loaded world: '" + Constants.WORLD_PROFILE.getKey() + "'");
            log.info("  name: " + Constants.WORLD_PROFILE.getKey());
            log.info("  host: " + Constants.WORLD_PROFILE.getHost());
            log.info("  port: " + Constants.WORLD_PROFILE.getPort());
            log.info("  activity: \"" + Constants.WORLD_PROFILE.getActivity() + "\"");
            log.info("  private: " + Constants.WORLD_PROFILE.isPrivate());
            log.info("  development: " + Constants.WORLD_PROFILE.isDevelopment());
            log.info("  verify passwords: " + Constants.WORLD_PROFILE.isVerifyPasswords());
            log.info("  location: " + Constants.WORLD_PROFILE.getLocation());
            log.info("  flags: " + Constants.WORLD_PROFILE.getFlags());
            log.info("  api:");
            log.info("    enabled: " + Constants.WORLD_PROFILE.getApi().isEnabled());
            log.info("    scheme: " + Constants.WORLD_PROFILE.getApi().getScheme());
            log.info("    host: " + Constants.WORLD_PROFILE.getApi().getHost());
            log.info("    port: " + Constants.WORLD_PROFILE.getApi().getPort());
            log.info("    token: " + Constants.WORLD_PROFILE.getApi().getToken().substring(0, 7) + "...");
        } catch (IOException e) {
            log.error("Failed to load world profile!", e);
            return;
        }
        try (final ForkJoinPool fork = ForkJoinPool.commonPool()) {
            final ArrayList<Callable<Void>> list = new ArrayList<>();
            Server.PORT = Constants.WORLD_PROFILE.getPort();
            CoresManager.init();
            try {
                Game.load(Cache.openCache("./data/cache/", true));
                ItemDefinitions.loadDefinitions(fork, () -> {}, () -> {});
            } catch (Exception e) {
                log.error("Failure submitting cache loading.", e);
                System.exit(-1);
                return;
            }
            log.info("Creating game engine...");
            list.add(callable(World::init));
            list.add(callable(Consumable::initialize));
            list.add(callable(GlobalItem::load));
            fork.invokeAll(list);
            list.clear();
            log.info("Loading game.");
            load();
            PluginManager.post(new ServerLaunchEvent());
            loaded = true;
            for (final Runnable task : postServerLoadTasks) {
                task.run();
            }
            if (Constants.WORLD_PROFILE.getApi().isEnabled()) {
                final boolean successful = new ApiPing().execute();
                if (successful) {
                    log.info("Received ping response from api server.");
                    log.info("Starting api tasks...");
                    APIClient.startTasks();
                } else {
                    log.error("Failed to ping api server!");
                    System.exit(-1);
                }
            }
            log.info("Server took " + (Utils.nanoToMilli(System.nanoTime() - serverStartTime)) + " milliseconds to launch.");
            log.info("Ready. Server is listening on " + Server.PORT + ".");
            fork.submit(() -> {
                final File file = new File("data/logs/error.log");
                if (!file.exists()) {
                    return;
                }
                if (file.length() > 0) {
                    System.err.println("Some errors from previous session(s) are logged at /data/logs/error.log; review and delete them.");
                }
            });
        }
    }

    private static void load() {
        try {
            final ForkJoinPool pool = ForkJoinPool.commonPool();
            log.info("Submitting binding task; Binding to port: " + Server.PORT);
            final ForkJoinTask<?> bindTask = pool.submit(() -> {
                try {
                    Server.bind(Server.PORT);
                    log.info("Bound to port: " + Server.PORT);
                } catch (final Exception e) {
                    log.error("Error starting " + GameConstants.SERVER_NAME + ".", e);
                    System.exit(1);
                }
            });
            final ArrayList<Callable<Void>> list = new ArrayList<>();
            for (final Class<?> clazz : Definitions.lowPriorityDefinitions) {
                list.add(callable(Definitions.load(clazz)));
            }
            pool.invokeAll(list);
            list.clear();
            log.info("Loading npc combat definitions.");
            pool.invokeAll(Arrays.asList(callable(NPCCDLoader::parse), callable(MiningDefinitions::load), callable(DwarfMulticannon::init), callable(AgilityManager::init), callable(NPCSpawnLoader::parseDefinitions)));
            log.info("Loading common world tasks.");
            World.initTasks();
            log.info("Loading shops, examines, drops and grand exchange.");
            pool.invokeAll(Arrays.asList(callable(Shop::load), callable(NPCExamineLoader::loadExamines), callable(ObjectExamineLoader::loadExamines), callable(NPCDrops::init), callable(GrandExchangeHandler::init), callable(Door::load)));
            try {
                XTEALoader.load("data/objects/xteas.json");
            } catch (Throwable throwable) {
                log.error("Failure to load XTEAs.", throwable);
            }
            log.info("Loading plugins.");
            new Scanner().scan();
            log.info("Loading area inheritance.");
            GlobalAreaManager.setInheritance();
            log.info("Loading NPC spawns.");
            pool.invokeAll(Arrays.asList(callable(NPCDefinitions::filter), callable(NPCSpawnLoader::loadNPCSpawns)));
            pool.submit(DropViewerInterface::populateDropViewerData);
            log.info("Submitting multiway area mapping and area intersection verification.");
            pool.submit(MultiwayArea::loadAndMap);
            pool.submit(GlobalAreaManager::checkIntersections);
            pool.submit(GlobalAreaManager::map);
            log.info("Launching login manager.");
//            CoresManager.getBackupManager().launch();
            CoresManager.getLoginManager().launch();
            if (!bindTask.isDone()) {
                try {
                    bindTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Failure binding the port", e);
                    System.exit(-1);
                    return;
                }
            }
        } catch (final Exception e) {
            log.error("Exception loading game", e);
        }
        if (Constants.CYCLE_DEBUG) {
            //TODO
            final IntOpenHashSet regionList = new IntOpenHashSet(2000);
            /*val index = Game.getLibrary().getIndex(5);
            for (int rx = 0; rx < 100; rx++) {
                for (int ry = 0; ry < 256; ry++) {
                    val id = index.getArchiveId("m" + (rx) + "_" + ry);
                    if (id != -1) {
                        regionList.add(rx << 8 | ry);
                    }
                }
            }*/
            final ArrayList<Callable<Void>> taskList = new ArrayList<>(2000);
            for (final Integer region : regionList) {
                taskList.add(() -> {
                    World.loadRegion(region);
                    return null;
                });
            }
            ForkJoinPool.commonPool().invokeAll(taskList);
        }
    }

    private static Callable<Void> callable(final Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error("Failure loading callable: ", e);
            }
            return null;
        };
    }
}
