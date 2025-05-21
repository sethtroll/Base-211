package com.zenyte.game.content.skills.mining;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MiningDefinitions {
    public static final Int2ObjectOpenHashMap<OreDefinitions> ores = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectOpenHashMap<PickaxeDefinitions> tools = new Int2ObjectOpenHashMap<>();
    public static final Int2IntOpenHashMap rocks = new Int2IntOpenHashMap();
    public static final Int2ObjectOpenHashMap<PaydirtDefinitions> paydirts = new Int2ObjectOpenHashMap<>();

    public static void load() {
        for (OreDefinitions entry : OreDefinitions.values()) for (int id : entry.getRocks()) ores.put(id, entry);
        for (ShapeDefinitions entry : ShapeDefinitions.values())
            for (int id : entry.getOres()) rocks.put(id, entry.getEmpty());
        for (PickaxeDefinitions entry : PickaxeDefinitions.values()) tools.put(entry.getId(), entry);
        for (final PaydirtDefinitions value : PaydirtDefinitions.values()) paydirts.put(value.getOre(), value);
    }


    public enum PaydirtDefinitions {
        COAL(453, 30, 0),
        GOLD(444, 40, 15),
        MITHRIL(447, 55, 30),
        ADAMANTITE(449, 70, 45),
        RUNITE(451, 85, 75);
        public static final Map<Integer, PaydirtDefinitions> entries = new HashMap<>();
        private final int ore;
        private final int level;
        private final int xp;

        PaydirtDefinitions(final int ore, final int level, final int xp) {
            this.ore = ore;
            this.level = level;
            this.xp = xp;
        }

        public int getOre() {
            return this.ore;
        }

        public int getLevel() {
            return this.level;
        }

        public int getXp() {
            return this.xp;
        }
    }


    @Ordinal
    public enum OreDefinitions {
        /**
         * Ores
         */
        TIN(741600, 438, 1, 1, 2, 17.5, 2, 1, true, ObjectId.ROCKS_11360, ObjectId.ROCKS_11361),
        COPPER(741600, 436, 1, 1, 2, 17.5, 2, 1, true, ObjectId.ROCKS_10943, ObjectId.ROCKS_11161),
        CLAY(741600, 434, 1, 1, 2, 5, 2, 1, true, ObjectId.ROCKS_11362, ObjectId.ROCKS_11363),
        SOFT_CLAY(741600, ItemId.SOFT_CLAY, 70, 1, 67, 5, 2, 1, true, ObjectId.ROCKS_36210),
        BLURITE(741600, 668, 10, 10, 47, 17.5, 5, 1, true, ObjectId.ROCKS_11378, ObjectId.ROCKS_11379),
        IRON(741600, 440, 15, 15, 3, 35, 6, 1, true, ObjectId.ROCKS_11364, ObjectId.ROCKS_11365, ObjectId.ROCKS_11365),
        SILVER(741600, 442, 20, 25, 17, 40, 8, 1, true, ObjectId.ROCKS_11368, ObjectId.ROCKS_11369),
        COAL(296640, 453, 30, 35, 12, 50, 6, 1, true, ObjectId.ROCKS_11366, ObjectId.ROCKS_11367),
        GOLD(296640, 444, 40, 45, 17, 65, 9, 1, true, ObjectId.ROCKS_11370, ObjectId.ROCKS_11371),
        MITHRIL(148320, 447, 55, 65, 33, 80, 12, 1, true, ObjectId.ROCKS_11372, ObjectId.ROCKS_11373, ObjectId.ROCKS_11373),
        LOVAKITE(245562, 13356, 65, 100, 50, 10, 0, 1, true, ObjectId.ROCKS_28596, ObjectId.ROCKS_28597),
        ADAMANTITE(59328, 449, 70, 80, 67, 95, 15, 1, true, ObjectId.ROCKS_11374, ObjectId.ROCKS_11375, ObjectId.ROCKS_11375),
        RUNITE(42377, 451, 85, 99, 150, 125, 18, 1, false, ObjectId.ROCKS_11376, ObjectId.ROCKS_11377),
        /**
         * Motherlode mine
         */
        PAYDIRT(-1, 12011, 30, 50, 150, 60, 0, 0, true, 26661, 26662, 26663, 26664),





        /**
         * Unique
         */
        SANDSTONE(741600, -1, 35, 25, 8, -1, 0, 1, true, ObjectId.ROCKS_11386),
        GRANITE(741600, -1, 45, 35, 8, -1, 0, 1, true, ObjectId.ROCKS_11387),
        GEM(211886, -1, 40, 60, 8, 65, 0, 1, false, ObjectId.ROCKS_11380, ObjectId.ROCKS_11381),
        RUNITE_GOLEM_ROCKS(42377, 451, 85, 99, -1, 125, 0, 1, false),
        ROCKSLIDE(-1, -1, 99, 15, 20, 0, 0, 1, false, 27062),
        ROCKFALL(-1, -1, 30, 20, 30, 10, 0, 1, false, 26679, 26680),
        URT_SALT(-1, 22597, 72, 70, 3, 5, 0, 11, false, 33254),
        EFH_SALT(-1, 22595, 72, 70, 15, 5, 0, 11, false, 33255),
        TE_SALT(-1, 22593, 72, 70, 15, 5, 0, 11, false, 33256),
        BASALT(-1, 22603, 72, 85, 15, 5, 0, 11, false, 33257),
        ESSENCE(-1, 7936, 1, 1, -1, 5, 0, 0, false, ObjectId.RUNE_ESSENCE_34773),
        VOLCANIC_ASH(741600, 21622, 22, 5, 50, 10, 0, 11, false, 30985),
        AMETHYST(46350, 21347, 92, 100, 42, 240, 0, 3, false, ObjectId.CRYSTALS, ObjectId.CRYSTALS_11389),
        /**
         * Castle wars
         */
        CWARS_ROCKS(-1, -1, 1, 35, 100, 0, 0, 1, false, 4437, 4438),
        CWARS_WALL(-1, -1, 1, 35, 100, 0, 0, 1, false, 4448),

        /** Crytal Rocks */
        CRYSTAL_ROCKS(741600, 23866, 94, 45, 125, 350, 0, 6, false, 7465, 7466, 8725),


        ROCK_FORMATION(-1, 23905, 30, 99, 150, 35.2, 0, 0, true, 36193),
        GLOWING_ROCK_FORMATION(-1, 23905, 30, 20, 30, 35.2, 0, 0, true, 36192),
        /** Star Mining */
        FALLING_STAR(741600, 25527, 70, 10, 12000, 80, 0, 600, false, 40040),
        FALLING_STAR1(741600, 25527, 80, 8, 12000, 100, 0, 600, false, 40041),
        FALLING_STAR2(741600, 25527, 90, 5, 12000, 150, 0, 600, false, 40042),

        ;
        private final int baseClueGeodeChance;
        private final int ore;
        private final int level;
        private final int speed;
        private final int time;
        private final int incinerationExperience;
        private final double xp;
        private final int depletionRate;
        private final int[] rocks;
        private final boolean extraOre;

        OreDefinitions(final int baseClueGeodeChance, int ore, int level, final int speed, int time, double xp, int incinerationExperience, int depletionRate, boolean extraOre, int... rocks) {
            this.baseClueGeodeChance = baseClueGeodeChance;
            this.incinerationExperience = incinerationExperience;
            this.ore = ore;
            this.level = level;
            this.speed = speed;
            this.time = time;
            this.xp = xp;
            this.depletionRate = depletionRate;
            this.extraOre = extraOre;
            this.rocks = rocks;
        }

        public static OreDefinitions getDef(int id) {
            return ores.get(id);
        }

        public String getName() {
            return ItemDefinitions.getOrThrow(this.ore).getName().toLowerCase().replace(" ore", "");
        }

        public boolean isSmeltable() {
            return this.equals(BLURITE) || this.equals(IRON) || this.equals(SILVER) || this.equals(GOLD) || this.equals(MITHRIL) || this.equals(ADAMANTITE) || this.equals(RUNITE);
        }

        public int getBaseClueGeodeChance() {
            return this.baseClueGeodeChance;
        }

        public int getOre() {
            return this.ore;
        }

        public int getLevel() {
            return this.level;
        }

        public int getSpeed() {
            return this.speed;
        }

        public int getTime() {
            return this.time;
        }

        public int getIncinerationExperience() {
            return this.incinerationExperience;
        }

        public double getXp() {
            return this.xp;
        }

        public int getDepletionRate() {
            return this.depletionRate;
        }

        public int[] getRocks() {
            return this.rocks;
        }

        public boolean isExtraOre() {
            return this.extraOre;
        }
    }


    public enum PickaxeDefinitions {
        BRONZE(1265, 1, new Animation(625), new Animation(6753), 6),
        IRON(1267, 1, new Animation(626), new Animation(6754), 6),
        STEEL(1269, 6, new Animation(627), new Animation(6755), 5),
        BLACK(12297, 11, new Animation(3873), new Animation(6109), 5),
        MITHRIL(1273, 21, new Animation(629), new Animation(6757), 4),
        ADAMANT(1271, 31, new Animation(628), new Animation(6756), 3),
        RUNE(1275, 41, new Animation(624), new Animation(6752), 2),
        GILDED(23276, 41, new Animation(8313), new Animation(8312), 2),
        DRAGON(11920, 61, new Animation(7139), new Animation(6758), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        DRAGON_OR(12797, 61, new Animation(643), new Animation(335), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        THIRD_AGE(20014, 61, new Animation(7283), new Animation(7282), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        INFERNAL(13243, 61, new Animation(4482), new Animation(4481), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        UNCHARGED_INFERNAL(13244, 61, new Animation(4482), new Animation(4481), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        };
        private static final PickaxeDefinitions[] values = values();
        private final int id;
        private final int level;
        private final Animation anim;
        private final Animation alternateAnimation;
        private final int mineTime;

        PickaxeDefinitions(final int id, final int level, final Animation anim, final Animation alternateAnimation, final int mineTime) {
            this.id = id;
            this.level = level;
            this.anim = anim;
            this.alternateAnimation = alternateAnimation;
            this.mineTime = mineTime;
        }

        public static PickaxeDefinitions getDef(int id) {
            return tools.get(id);
        }

        public static Optional<PickaxeResult> get(final Player player, final boolean checkInventory) {
            final int level = player.getSkills().getLevel(Skills.MINING);
            final Container inventory = player.getInventory().getContainer();
            final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
            for (int i = values.length - 1; i >= 0; i--) {
                final MiningDefinitions.PickaxeDefinitions def = values[i];
                if (level < def.level) continue;
                if (weapon == def.id) {
                    return Optional.of(new PickaxeResult(def, player.getEquipment().getContainer(), 3, player.getWeapon()));
                }
                if (checkInventory) {
                    for (int slot = 0; slot < 28; slot++) {
                        final Item item = inventory.get(slot);
                        if (item == null || item.getId() != def.id) {
                            continue;
                        }
                        return Optional.of(new PickaxeResult(def, player.getInventory().getContainer(), slot, item));
                    }
                }
            }
            return Optional.empty();
        }

        public int getId() {
            return this.id;
        }

        public int getLevel() {
            return this.level;
        }

        public Animation getAnim() {
            return this.anim;
        }

        public Animation getAlternateAnimation() {
            return this.alternateAnimation;
        }

        public int getMineTime() {
            return this.mineTime;
        }

        public static final class PickaxeResult {
            private final PickaxeDefinitions definitions;
            private final Container container;
            private final int slot;
            private final Item item;

            public PickaxeResult(final PickaxeDefinitions definitions, final Container container, final int slot, final Item item) {
                this.definitions = definitions;
                this.container = container;
                this.slot = slot;
                this.item = item;
            }

            public PickaxeDefinitions getDefinitions() {
                return this.definitions;
            }

            public Container getContainer() {
                return this.container;
            }

            public int getSlot() {
                return this.slot;
            }

            public Item getItem() {
                return this.item;
            }
        }
    }


    public enum ShapeDefinitions {
        THREE(ObjectId.ROCKS_11391),
        FOUR(ObjectId.ROCKS_11390, ObjectId.ROCKS_11361,  ObjectId.ROCKS_11387,  ObjectId.ROCKS_11368, ObjectId.ROCKS_11361,  ObjectId.ROCKS_11369,  ObjectId.ROCKS_11360,  ObjectId.ROCKS_11363,  ObjectId.ROCKS_11161, ObjectId.ROCKS_11360, ObjectId.ROCKS_11364, ObjectId.ROCKS_11365,ObjectId.ROCKS_11362, ObjectId.ROCKS_10943, ObjectId.ROCKS_11366, ObjectId.ROCKS_11376, ObjectId.ROCKS_11377, ObjectId.ROCKS_11367, ObjectId.ROCKS_11372, ObjectId.ROCKS_11373, ObjectId.ROCKS_11374, ObjectId.ROCKS_11375, ObjectId.ROCKS_11370, ObjectId.ROCKS_11378, ObjectId.ROCKS_11380, ObjectId.ROCKS_11381, ObjectId.ROCKS_11386, ObjectId.ROCKS_28596),
        ROCKSLIDE(27063, 27062),
        VOLCANIC_ASH(30986, 30985),
        WALL(ObjectId.EMPTY_WALL, ObjectId.CRYSTALS, ObjectId.CRYSTALS_11389),
        SALT_ROCK(33253, 33254, 33255, 33256, 33257);
        private final int empty;
        private final int[] ores;

        ShapeDefinitions(int empty, int... ores) {
            this.empty = empty;
            this.ores = ores;
        }

        public static int getEmpty(int id) {
            return rocks.getOrDefault(id, 7468);
        }

        public int getEmpty() {
            return this.empty;
        }

        public int[] getOres() {
            return this.ores;
        }
    }
}
