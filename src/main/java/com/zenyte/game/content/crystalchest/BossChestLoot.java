package com.zenyte.game.content.crystalchest;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Kris | 04/04/2019 12:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BossChestLoot {
    UNCUT_DIAMOND(12, new Item(1618, 50 )),
    UNCUT_RUBY(12, new Item(1620, 50 )),
    UNCUT_EMERALD(12, new Item(1622, 4 )),
    COAL(12, new Item(454, 460 )),
    GOLD_ORE(15, new Item(445, 460 )),
    ADAMANTITE_ORE(15, new Item(450, 150)),
    RUNITE_ORE(15, new Item(452, 80 )),
    DRAGON_ARROWTIPS(15, new Item(11237, 150)),
    DRAGON_BOLTS(15, new Item(21905, 150)),
    DRAGON_DART_TIP(15, new Item(11232, 200)),
    COINS(20, new Item(995, 100000)),
    STEEL_BAR(20, new Item(2354, 250)),
    MAGIC_LOGS(30, new Item(1514, 160)),
    RANARR_WEED(40, new Item(258, 40)),
    SNAPDRAGON(30, new Item(3001, 30)),
    TORSTOL(30, new Item(270, 30)),
    PALM_TREE_SEED(60, new Item(5289, 6)),
    MAGIC_SEED(100, new Item(5316, 4)),
    CELASTRUS_SEED(60, new Item(22869, 4)),
    DRAGONFRUIT_TREE_SEED(60, new Item(22877, 4)),
    REDWOOD_TREE_SEED(60, new Item(22871, 2)),
    TORSTOL_SEED(100, new Item(5304, 6)),
    SNAPDRAGON_SEED(100, new Item(5300, 12)),
    RANARR_SEED(100, new Item(5295, 12)),
    MORRIGANS_THROWING_AXE(6, new Item(22634)),
    VESTA_SPEAR(3, new Item(22610)),
    VETSA_LEGS(3, new Item(22619)),
    VESTA_BODY(3, new Item(22616)),
    VESTA_LONG(3, new Item(22613)),
    MORR_JAVS(6, new Item(22636, 50)),
    MORR_CHAPS(3, new Item(22644)),
    MORR_BODY(3, new Item(22641)),
    MORR_COIF(3, new Item(22638)),
    STAT_HAMMER(3, new Item(22622)),
    STAT_LEGS(3, new Item(22631)),
    STAT_BODY(3, new Item(22628)),
    STAT_HELM(3, new Item(22625)),
    ZURIAL_STAFF(3, new Item(22647)),
    ZURLIAL_LEGS(3, new Item(22656)),
    ZURIALS_BODY(3, new Item(22653)),
    ZURIALS_HELM(3, new Item(22650));
    private static final BossChestLoot[] values = values();
    private static final int TOTAL_WEIGHT;

    static {
        int weight = 0;
        for (final BossChestLoot value : values) {
            weight += value.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    private final int weight;
    private final Item[] loot;
    private final BiFunction<Player, Item, Item> lootFunction;

    BossChestLoot(final double percentage, final Item... loot) {
        this(percentage, null, loot);
    }

    BossChestLoot(final double percentage, final BiFunction<Player, Item, Item> lootFunction, final Item... loot) {
        this.weight = (int) Math.floor(32767.0F * percentage / 100.0F);
        this.lootFunction = lootFunction;
        this.loot = loot;
    }

    public static List<Item> get(@NotNull final Player player) {
        final ObjectArrayList<Item> list = new ObjectArrayList<>();
        list.add(new Item(1631));
        final int roll = Utils.random(TOTAL_WEIGHT);
        int current = 0;
        for (final BossChestLoot loot : values) {
            if ((current += loot.weight) >= roll) {
                for (final Item item : loot.loot) {
                    list.add(loot.lootFunction == null ? item : loot.lootFunction.apply(player, item));
                }
                break;
            }
        }
        return list;
    }

    public int getWeight() {
        return this.weight;
    }

    public Item[] getLoot() {
        return this.loot;
    }

    public BiFunction<Player, Item, Item> getLootFunction() {
        return this.lootFunction;
    }
}
