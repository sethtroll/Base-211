package com.zenyte.game.content.skills.hunter.node;

import com.zenyte.game.item.ImmutableItem;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.zenyte.game.content.skills.hunter.node.PreyObject.*;
import static com.zenyte.game.content.skills.hunter.node.TrapType.*;

/**
 * @author Kris | 27/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum TrapPrey implements HunterPrey {
    CRIMSON_SWIFT(100, 32, 5549, 1, 34, BIRD_SNARE, CRIMSON_SWIFT_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(ItemId.RAW_BIRD_MEAT), new ImmutableItem(ItemId.RED_FEATHER, 5, 10)),
    GOLDEN_WARBLER(100, 37, 5551, 5, 47, BIRD_SNARE, GOLDEN_WARBLER_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(ItemId.RAW_BIRD_MEAT), new ImmutableItem(ItemId.YELLOW_FEATHER, 5, 10)),
    COPPER_LONGTAIL(100, 41, 5552, 9, 61, BIRD_SNARE, COPPER_LONGTAIL_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(ItemId.RAW_BIRD_MEAT), new ImmutableItem(ItemId.ORANGE_FEATHER, 5, 10)),
    CERULEAN_TWITCH(100, 43, 5550, 11, 64.5, BIRD_SNARE, CERULEAN_TWITCH_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(ItemId.RAW_BIRD_MEAT), new ImmutableItem(ItemId.BLUE_FEATHER, 5, 10)),
    TROPICAL_WAGTAIL(100, 51, 5548, 19, 95.2, BIRD_SNARE, TROPICAL_WAGTAIL_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(ItemId.RAW_BIRD_MEAT), new ImmutableItem(ItemId.STRIPY_FEATHER, 5, 10)),
    FERRET(145, 72, 1505, 27, 115, BOX_TRAP, FERRET_OBJECT, new ImmutableItem(10092)),
    CHINCHOMPA(145, 94, 2910, 53, 198.25, BOX_TRAP, CHINCHOMPA_OBJECT, new ImmutableItem(10033)),
    CARNIVOROUS_CHINCHOMPA(115, 107, 2911, 63, 265, BOX_TRAP, CARNIVOROUS_CHINCHOMPA_OBJECT, new ImmutableItem(10034)),
    BLACK_CHINCHOMPA(115, 107, 2912, 73, 315, BOX_TRAP, BLACK_CHINCHOMPA_OBJECT, new ImmutableItem(11959)),
    WILD_KEBBIT(115, 60, 1349, 23, 128, DEADFALL, WILD_KEBBIT_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(10113)),
    BARB_TAILED_KEBBIT(115, 70, 1348, 33, 168, DEADFALL, BARB_TAILED_KEBBIT_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(10129)),
    PRICKLY_KEBBIT(115, 75, 1346, 37, 204, DEADFALL, PRICKLY_KEBBIT_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(10105)),
    SABRE_TOOTHED_KEBBIT(115, 90, 1347, 51, 200, DEADFALL, SABRE_TOOTHED_KEBBIT_OBJECT, new ImmutableItem(ItemId.BONES), new ImmutableItem(10109)),
    /*MANIACAL_MONKEY(115, 95, 7212, 60, 1000, DEADFALL, MANIACAL_MONNKEY_OBJECT,
            new ImmutableItem(ItemId.DAMAGED_MONKEY_TAIL)),*/
    SPINED_LARUPIA(145, 68, 2908, 31, 180, PITFALL, null, new ImmutableItem(ItemId.BIG_BONES), new ImmutableItem(10093), new ImmutableItem(10095)),
    HORNED_GRAAHK(145, 80, 2909, 41, 240, PITFALL, null, new ImmutableItem(ItemId.BIG_BONES), new ImmutableItem(10097), new ImmutableItem(10099)),
    SABRE_TOOTHED_KYATT(145, 97, 2907, 55, 300, PITFALL, null, new ImmutableItem(ItemId.BIG_BONES), new ImmutableItem(10101), new ImmutableItem(10103)),
    SWAMP_LIZARD(115, 74, 2906, 29, 152, NET_TRAP_SWAMP_LIZARD, SWAMP_LIZARD_OBJECT, new ImmutableItem(10149)),
    ORANGE_SALAMANDER(115, 88, 2903, 47, 224, NET_TRAP_ORANGE_SALAMANDER, ORANGE_SALAMANDER_OBJECT, new ImmutableItem(10146)),
    RED_SALAMANDER(115, 91, 2904, 59, 272, NET_TRAP_RED_SALAMANDER, RED_SALAMANDER_OBJECT, new ImmutableItem(10147)),
    BLACK_SALAMANDER(115, 107, 2905, 67, 319.5, NET_TRAP_BLACK_SALAMANDER, BLACK_SALAMANDER_OBJECT, new ImmutableItem(10148));
    private static final List<TrapPrey> values = Collections.unmodifiableList(Arrays.asList(values()));
    private final int baseCatchRate;
    private final int neverFailLevel;
    private final int npcId;
    private final int level;
    private final double experience;
    @NotNull
    private final TrapType trap;
    @Nullable
    private final PreyObject object;
    @NotNull
    private final ImmutableItem[] items;
    @NotNull
    private final String formattedName;

    TrapPrey(final int baseCatchRate, final int neverFailLevel, final int npcId, final int level, final double experience, @NotNull final TrapType trap, @Nullable final PreyObject object, @NotNull final ImmutableItem... items) {
        this.baseCatchRate = baseCatchRate;
        this.neverFailLevel = neverFailLevel;
        this.npcId = npcId;
        this.level = level;
        this.experience = experience;
        this.trap = trap;
        this.object = object;
        this.items = items;
        this.formattedName = name().toLowerCase().replace("_", " ");
    }

    /**
     * Finds the first entry of {@link TrapPrey} which matches the requested npc id.
     *
     * @param npcId the npc id to seek.
     * @return an optional entry of the {@link TrapPrey}.
     */
    @NotNull
    public static Optional<TrapPrey> get(final int npcId) {
        return Optional.ofNullable(Utils.findMatching(values, value -> value.npcId == npcId));
    }

    public static boolean contains(final int id, @NotNull final TrapType trap) {
        final Optional<TrapPrey> optionalTrapPrey = get(id);
        if (optionalTrapPrey.isPresent()) {
            final TrapPrey prey = optionalTrapPrey.get();
            return prey.getTrap().equals(trap);
        }
        return false;
    }

    public static List<TrapPrey> getValues() {
        return TrapPrey.values;
    }

    @Override
    public String toString() {
        return formattedName;
    }

    @Override
    public int baseCatchRate() {
        return baseCatchRate;
    }

    @Override
    public int baseRequirement() {
        return level;
    }

    @Override
    public int neverFailLevel() {
        return neverFailLevel;
    }

    public int getBaseCatchRate() {
        return this.baseCatchRate;
    }

    public int getNeverFailLevel() {
        return this.neverFailLevel;
    }

    public int getNpcId() {
        return this.npcId;
    }

    public int getLevel() {
        return this.level;
    }

    public double getExperience() {
        return this.experience;
    }

    @NotNull
    public TrapType getTrap() {
        return this.trap;
    }

    @Nullable
    public PreyObject getObject() {
        return this.object;
    }

    @NotNull
    public ImmutableItem[] getItems() {
        return this.items;
    }

    @NotNull
    public String getFormattedName() {
        return this.formattedName;
    }
}
