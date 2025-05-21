package com.zenyte.game.content.tournament.preset;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tournament.preset.component.EquipmentComponent;
import com.zenyte.game.content.tournament.preset.component.EquipmentComponent.EquipmentComponentBuilder;
import com.zenyte.game.content.tournament.preset.component.InventoryComponent;
import com.zenyte.game.content.tournament.preset.component.InventoryComponent.InventoryComponentBuilder;
import com.zenyte.game.content.tournament.preset.component.RunePouchComponent;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 25/05/2019 | 15:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum TournamentPreset {
    DHAROKS(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 4716, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 20366, true).put(EquipmentSlot.WEAPON, 12006, true).put(EquipmentSlot.PLATE, 4720, true).put(EquipmentSlot.SHIELD, 22322, true).put(EquipmentSlot.LEGS, 4722, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 13239, true).put(EquipmentSlot.RING, 11773, true).build(),
            new InventoryComponentBuilder().add(12695, true).add(6685, true).add(10925, true).add(13441, true).add(6685, true).add(10925, true).add(13441, 2, true).add(3144, 4, true).add(13441, 9, true).add(4718, true).add(11802, true).add(13441, 4, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Dharok's";
        }
    },
    KARILS_DHAROKS(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 4732, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 12853, true).put(EquipmentSlot.WEAPON, 4734, true).put(EquipmentSlot.PLATE, 4736, true).put(EquipmentSlot.LEGS, 4738, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 13239, true).put(EquipmentSlot.RING, 11773, true).put(EquipmentSlot.AMMUNITION, 4740, 10000, true).build(),
	/*.add(9075, 1000, true)
                    .add(560, 1000, true)
                    .add(557, 1000, true)*/
            new InventoryComponentBuilder().add(4716, true).add(4720, true).add(12695, true).add(2444, true).add(4718, true).add(4722, true).add(6685, true).add(4153, true).add(3144, 2, true).add(10925, 2, true).add(13441, 15, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Karil's/Dharok's";
        }
    },
    MELEE_PURE(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 4502, true).put(EquipmentSlot.CAPE, 6570, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 12006, true).put(EquipmentSlot.PLATE, 12235, true).put(EquipmentSlot.SHIELD, 3842, true).put(EquipmentSlot.LEGS, 2497, true).put(EquipmentSlot.HANDS, 11133, true).put(EquipmentSlot.BOOTS, 3105, true).put(EquipmentSlot.RING, 6737, true).put(EquipmentSlot.AMMUNITION, 20235, true).build(), new InventoryComponentBuilder().add(12695, true).add(6685, true).add(10925, true).add(13441, true).add(6685, true).add(10925, true).add(13441, 2, true).add(3144, 4, true).add(13441, 9, true).add(13652, true).add(21003, true).add(13441, 5, true).build(), Spellbook.NORMAL, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC, Prayer.SMITE),
    MAX_MELEE(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 13197, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 12006, true).put(EquipmentSlot.PLATE, 11832, true).put(EquipmentSlot.SHIELD, 22322, true).put(EquipmentSlot.LEGS, 11834, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 11840, true).put(EquipmentSlot.RING, 11773, true).put(EquipmentSlot.AMMUNITION, 20232, true).build(),
	/*.add(9075, 1000, true)
                    .add(560, 1000, true)
                    .add(557, 1000, true)*/
            new InventoryComponentBuilder().add(12695, true).add(10925, 2, true).add(6685, true).add(12695, true).add(11802, true).add(4153, true).add(3144, 4, true).add(13441, 16, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 1, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC),
    ZERKER_MID_LEVEL(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 3751, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 4587, true).put(EquipmentSlot.PLATE, 10551, true).put(EquipmentSlot.SHIELD, 8850, true).put(EquipmentSlot.LEGS, 2625, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 4131, true).put(EquipmentSlot.RING, 11773, true).put(EquipmentSlot.AMMUNITION, 20235, true).build(),
	/*.add(9075, 1000, true)
                    .add(560, 1000, true)
                    .add(557, 1000, true)*/
            new InventoryComponentBuilder().add(13652, true).add(4153, true).add(12695, true).add(6685, true).add(10925, 2, true).add(3144, 4, true).add(13441, 17, true).add(30006, false).build(),Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC, Prayer.SMITE) {
        @Override
        public String toString() {
            return "Zerker Mid-Lvl";
        }
    },
    ZERKER_HIGH_LVL(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 3751, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 6585, true).put(EquipmentSlot.WEAPON, 12788, true).put(EquipmentSlot.PLATE, 10551, true).put(EquipmentSlot.LEGS, 3477, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 4131, true).put(EquipmentSlot.RING, 11773, true).put(EquipmentSlot.AMMUNITION, 21326, 10000, true).build(),
	/*.add(9075, 1000, true)
                    .add(560, 1000, true)
                    .add(557, 1000, true)*/
            new InventoryComponentBuilder().add(11802, true).add(4153, true).add(2444, true).add(12695, true).add(6685, true).add(10925, 2, true).add(3144, 4, true).add(6685, true).add(13441, 15, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC, Prayer.SMITE) {
        @Override
        public String toString() {
            return "Zerker High-Lvl";
        }
    },
    WELFARE_BRID(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 10828, true).put(EquipmentSlot.CAPE, 21795, true).put(EquipmentSlot.AMULET, 6585, true).put(EquipmentSlot.WEAPON, 4675, true).put(EquipmentSlot.PLATE, 4091, true).put(EquipmentSlot.SHIELD, 12831, true).put(EquipmentSlot.LEGS, 4093, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 11840, true).put(EquipmentSlot.RING, 11773, true).put(EquipmentSlot.AMMUNITION, 21326, 10000, true).build(),
	/*.add(555, 1000, true)
                    .add(560, 1000, true)
                    .add(565, 1000, true)*/
            new InventoryComponentBuilder().add(4720, true).add(12006, true).add(21295, true).add(13441, true).add(4759, true).add(22322, true).add(13441, 2, true).add(4736, true).add(5698, true).add(13441, 2, true).add(6685, 3, true).add(13441, true).add(12695, true).add(10925, 2, true).add(13441, 8, true).add(30006, false).build(), Spellbook.ANCIENT, RunePouchComponent.ICE_BARRAGE, 3, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC),
    TRIBRID(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 10828, false).put(EquipmentSlot.CAPE, 21791, false).put(EquipmentSlot.AMULET, 6585, false).put(EquipmentSlot.WEAPON, 22296, false).put(EquipmentSlot.PLATE, 4111, false).put(EquipmentSlot.SHIELD, 12831, false).put(EquipmentSlot.LEGS, 4113, false).put(EquipmentSlot.HANDS, 7462, false).put(EquipmentSlot.BOOTS, 11840, false).put(EquipmentSlot.RING, 11770, false).put(EquipmentSlot.AMMUNITION, 21932, 10000, false).build(), new InventoryComponentBuilder().add(4736, false).add(21902, false).add(12006, false).add(13441, true).add(4759, false).add(22109, false).add(22322, false).add(13441, true).add(2444, true).add(11802, true).add(13441, 2, true).add(10925, 2, true).add(12695, true).add(13441, true).add(6685, 3, true).add(13441, 8, true).add(30006, false).build(), Spellbook.ANCIENT, RunePouchComponent.ICE_BARRAGE, 3),
    VOID_RANGED(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 11664, true).put(EquipmentSlot.CAPE, 22109, true).put(EquipmentSlot.AMULET, 22249, true).put(EquipmentSlot.WEAPON, 5667, 1000, true).put(EquipmentSlot.PLATE, 13072, true).put(EquipmentSlot.SHIELD, 21000, true).put(EquipmentSlot.LEGS, 13073, true).put(EquipmentSlot.HANDS, 8842, true).put(EquipmentSlot.BOOTS, 13237, true).put(EquipmentSlot.RING, 11771, true).put(EquipmentSlot.AMMUNITION, 21946, 10000, true).build(),

            new InventoryComponentBuilder().add(11235, true).add(11212, 200, true).add(20849, 200, true).add(11785, true).add(2444, true).add(10925, 2, true).add(6685, true).add(3144, 4, true).add(13441, 15, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC),
    OBSIDIAN(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 21298, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 11128, true).put(EquipmentSlot.WEAPON, 6523, true).put(EquipmentSlot.PLATE, 21301, true).put(EquipmentSlot.SHIELD, 19722, true).put(EquipmentSlot.LEGS, 21304, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 11840, true).put(EquipmentSlot.RING, 11773, true).build(),
	/*.add(9075, 1000, true)
                    .add(560, 1000, true)
                    .add(557, 1000, true)*/
            new InventoryComponentBuilder().add(12695, true).add(6685, true).add(10925, 2, true).add(6528, true).add(12848, true).add(3144, 4, true).add(13441, 17, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC),
    VESTA(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 10548, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 12006, true).put(EquipmentSlot.PLATE, 10551, true).put(EquipmentSlot.SHIELD, 22322, true).put(EquipmentSlot.LEGS, 22619, true).put(EquipmentSlot.HANDS, 7462, true).put(EquipmentSlot.BOOTS, 13239, true).put(EquipmentSlot.RING, 11773, true).build(),
	/*.add(9075, 1000, true)
                    .add(560, 1000, true)
                    .add(557, 1000, true)*/
            new InventoryComponentBuilder().add(12695, true).add(6685, 2, true).add(13441, true).add(10925, 2, true).add(3144, true).add(13441, true).add(3144, true).add(13441, true).add(3144, true).add(13441, 2, true).add(3144, true).add(13441, 7, true).add(22613, true).add(5698, true).add(13441, 4, true).add(30006, false).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC),
    PureNHBrid(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 20595, true).put(EquipmentSlot.CAPE, 21795, true).put(EquipmentSlot.AMULET, 12002, true).put(EquipmentSlot.WEAPON, 12904, true).put(EquipmentSlot.PLATE, 20517, true).put(EquipmentSlot.SHIELD, 3842, true).put(EquipmentSlot.LEGS, 20520, true).put(EquipmentSlot.HANDS, 7458, true).put(EquipmentSlot.BOOTS, 3105, true).put(EquipmentSlot.RING, 11773, true).build(),  //anguish 1
            new InventoryComponentBuilder().add(19547, true).add(2497, true).add(12695, true).add(6685, true).add(11785, true).add(22109, true).add(2444, true).add(6685, true).add(19553, true).add(21295, true).add(10925, 2, true).add(4587, true).add(13652, true).add(3144, 2, true).add(13441, 10, true).add(30006, true).build(), Spellbook.ANCIENT, RunePouchComponent.ICE_BARRAGE, 3, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Elder Pure Brid";
        }
    },
    GraniteSLAB(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 10589, true).put(EquipmentSlot.CAPE, 21295, true).put(EquipmentSlot.AMULET, 6585, true).put(EquipmentSlot.WEAPON, 21742, true).put(EquipmentSlot.PLATE, 10564, true).put(EquipmentSlot.SHIELD, 3122, true).put(EquipmentSlot.LEGS, 6809, true).put(EquipmentSlot.HANDS, 21736, true).put(EquipmentSlot.BOOTS, 21643, true).put(EquipmentSlot.RING, 12691, true).build(),  //super combat pot
            new InventoryComponentBuilder().add(12695, true).add(6685, true).add(6685, true).add(6685, true).add(10925, 2, true).add(6969, 4, true).add(3144, 5, true).add(4153, true).add(6969, 12, true).add(30006, false).build(), Spellbook.NORMAL, RunePouchComponent.VENGEANCE, 1, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Granite Slab";
        }
    },
    BISSTAB(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 10548, true).put(EquipmentSlot.CAPE, 13280, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 22324, true).put(EquipmentSlot.PLATE, 11832, true).put(EquipmentSlot.SHIELD, 22322, true).put(EquipmentSlot.LEGS, 11834, true).put(EquipmentSlot.HANDS, 22981, true).put(EquipmentSlot.BOOTS, 13239, true).put(EquipmentSlot.RING, 12692, true).put(EquipmentSlot.AMMUNITION, 20220, true).build(),  //super combat pot
            new InventoryComponentBuilder().add(12695, true).add(6685, true).add(6685, true).add(6685, true).add(10925, 2, true).add(13441, 4, true).add(3144, 5, true).add(22613, true).add(13441, 12, true).add(30006, true).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Best In Slot - Stab";
        }
    },
    BISSLASH(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 3245, true).put(EquipmentSlot.CAPE, 13280, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 12006, true).put(EquipmentSlot.PLATE, 11832, true).put(EquipmentSlot.SHIELD, 22322, true).put(EquipmentSlot.LEGS, 11834, true).put(EquipmentSlot.HANDS, 22981, true).put(EquipmentSlot.BOOTS, 13239, true).put(EquipmentSlot.RING, 11772, true).put(EquipmentSlot.AMMUNITION, 20220, true).build(),  //super combat pot

            new InventoryComponentBuilder().add(12695, true).add(6685, true).add(6685, true).add(6685, true).add(10925, 2, true).add(13441, 4, true).add(3144, 5, true).add(11802, true).add(13441, 12, true).add(30006, true).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Best In Slot - Slash";
        }
    },
    BISSMASH(new EquipmentComponentBuilder().put(EquipmentSlot.HELMET, 11200, true).put(EquipmentSlot.CAPE, 13280, true).put(EquipmentSlot.AMULET, 19553, true).put(EquipmentSlot.WEAPON, 13576, true).put(EquipmentSlot.PLATE, 11832, true).put(EquipmentSlot.SHIELD, 22322, true).put(EquipmentSlot.LEGS, 11834, true).put(EquipmentSlot.HANDS, 22981, true).put(EquipmentSlot.BOOTS, 13239, true).put(EquipmentSlot.RING, 12691, true).put(EquipmentSlot.AMMUNITION, 20220, true).build(),  //super combat pot

            new InventoryComponentBuilder().add(12695, true).add(6685, true).add(6685, true).add(6685, true).add(10925, 2, true).add(13441, 4, true).add(3144, 5, true).add(21003, true).add(13441, 12, true).add(30006, true).build(), Spellbook.LUNAR, RunePouchComponent.VENGEANCE, 2, Prayer.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC) {
        @Override
        public String toString() {
            return "Best In Slot - Smash";
        }
    };

    public static final Set<TournamentPreset> values = EnumSet.allOf(TournamentPreset.class);
    private static final Map<Integer, TournamentPreset> presets = new HashMap<>();

    static {
        for (final TournamentPreset preset : values) {
            presets.put(preset.ordinal(), preset);
        }
    }

    private final EquipmentComponent equipment;
    private final InventoryComponent inventory;
    private final Spellbook spellbook;
    private final RunePouchComponent runePouch;
    private final int maximumBrews;
    private final Prayer[] disabledPrayers;

    TournamentPreset(final EquipmentComponent equipment, final InventoryComponent inventory, final Spellbook spellbook, final RunePouchComponent runePouch, final int maximumBrews, final Prayer... disabledPrayers) {
        this.equipment = equipment;
        this.inventory = inventory;
        this.spellbook = spellbook;
        this.runePouch = runePouch;
        this.maximumBrews = maximumBrews;
        this.disabledPrayers = disabledPrayers;
    }

    TournamentPreset(final EquipmentComponent equipment, final InventoryComponent inventory, final Spellbook spellbook, final int maximumBrews, final Prayer... disabledPrayers) {
        this(equipment, inventory, spellbook, null, maximumBrews, disabledPrayers);
    }

    public static TournamentPreset get(final int index) {
        return presets.get(index);
    }

    public void apply(final Player player) {
        player.getInventory().clear();
        player.getEquipment().clear();
        for (final BooleanEntry<Item> item : inventory.getItems()) {
            player.getInventory().addItem(item.getT());
        }
        for (final Map.Entry<Integer, BooleanEntry<Item>> entry : equipment.getItems().entrySet()) {
            player.getEquipment().set(entry.getKey(), entry.getValue() == null ? null : entry.getValue().getT());
        }
        player.getInventory().refreshAll();
        player.getEquipment().refreshAll();
        if (runePouch != null) {
            int slot = 0;
            for (final RuneEntry entry : runePouch.getEntries()) {
                player.getSecondaryRunePouch().getContainer().set(slot, new Item(entry.getRune().getId(), entry.getAmount()));
                slot++;
            }
            player.getSecondaryRunePouch().getContainer().refresh(player);
        }
        player.getCombatDefinitions().setSpellbook(spellbook, true);
        player.getCombatDefinitions().refresh();
    }

    @Override
    public String toString() {
        return Utils.formatString(name().replaceAll("_", " ").toLowerCase());
    }

    public EquipmentComponent getEquipment() {
        return this.equipment;
    }

    public InventoryComponent getInventory() {
        return this.inventory;
    }


    public Spellbook getSpellbook() {
        return this.spellbook;
    }

    public RunePouchComponent getRunePouch() {
        return this.runePouch;
    }

    public int getMaximumBrews() {
        return this.maximumBrews;
    }

    public Prayer[] getDisabledPrayers() {
        return this.disabledPrayers;
    }
}
