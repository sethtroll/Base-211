package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.skills.prayer.actions.Ashes;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.PairedItemOnItemPlugin;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * @author Kris | 24/06/2019 13:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AshSanctifier extends ItemPlugin implements PairedItemOnItemPlugin {

    public static int getCharges(@NotNull final Player player) {
        return player.getNumericAttribute("AshSanctifier charges").intValue();
    }

    public static void addCharges(@NotNull final Player player, final int amount) {
        player.addAttribute("AshSanctifier charges", getCharges(player) + amount);
    }

    public static boolean enabled(@NotNull final Player player) {
        return !player.getBooleanSetting(Setting.ASH_SANCTIFIER_INACTIVE);
    }

    private static void applyExperience(@NotNull final Player player, final Ashes ash) {
        addCharges(player, -1);
        player.getSkills().addXp(Skills.PRAYER, (DiaryReward.MORYTANIA_LEGS4.eligibleFor(player) ? 1.0F : 0.5F) * ash.getXp());
    }

    public static void restorePrayer(@NotNull final Player player, final Ashes ashes) {
        if (ashes.equals(Ashes.ASHES) || ashes.equals(Ashes.FIENDISH_ASHES)) {
            player.getPrayerManager().restorePrayerPoints(1);
        } else if (ashes.equals(Ashes.VILE_ASHES)) {
            player.getPrayerManager().restorePrayerPoints(2);
        } else if (ashes.equals(Ashes.MALICIOUS_ASHES)) {
            player.getPrayerManager().restorePrayerPoints(4);
        } else if (ashes.equals(Ashes.INFERNAL_ASHES)) {
            player.getPrayerManager().restorePrayerPoints(5);
        }
    }

    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int charges = player.getNumericAttribute("AshSanctifier charges").intValue();
            player.sendMessage("Your AshSanctifier has " + charges + " charge" + (charges == 1 ? "" : "s") + " remaining.");
        });
        bind("Uncharge", (player, item, container, slotId) -> {
            final int charges = getCharges(player);
            final int ectotokens = (int) (charges / 5.0F);
            if (ectotokens > 0) {
                player.getInventory().addOrDrop(new Item(560, ectotokens));
                player.sendMessage("You uncharge the AshSanctifier and receive " + ectotokens + " Death Runes.");
            } else {
                player.sendMessage("Your AshSanctifier has no charges remaining.");
            }
            addCharges(player, -charges);
        });
        bind("Activity", (player, item, container, slotId) -> {
            player.getSettings().toggleSetting(Setting.ASH_SANCTIFIER_INACTIVE);
            player.sendMessage(!enabled(player) ? "Your AshSanctifier is no longer scattering ashes." : "Your AshSanctifier is now scattering ashes.");
        });
    }

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item tokens = from.getId() == 560 ? from : to;
        int eligibleCharges = tokens.getAmount() * 5;
        final int crusherCharges = getCharges(player);
        if (crusherCharges + eligibleCharges < 0) {
            eligibleCharges = Integer.MAX_VALUE - crusherCharges;
            eligibleCharges -= eligibleCharges % 5;
        }
        if (eligibleCharges <= 0) {
            player.sendMessage("Your AshSanctifier can't hold anymore charges.");
            return;
        }
        player.getInventory().deleteItem(new Item(tokens.getId(), eligibleCharges / 5));
        addCharges(player, eligibleCharges);
        player.sendMessage("You add " + eligibleCharges + " charges in your AshSanctifier. It now holds " + getCharges(player) + " charges total.");
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.ASH_SANCTIFIER};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[]{ItemPair.of(ItemId.DEATH_RUNE, ItemId.ASH_SANCTIFIER)};
    }

    public enum CrusherType {
        BONECRUSHER_NECKLACE(player -> player.getInventory().containsItem(ItemId.BONECRUSHER_NECKLACE, 1) || player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.BONECRUSHER_NECKLACE, (player, bone, burying) -> {
            if (burying) {
                if (getCharges(player) <= 0) {
                    return false;
                }
                restorePrayer(player, bone);
                return true;
            }
            if (!enabled(player) || getCharges(player) <= 0) {
                return false;
            }
            if (player.inArea("Catacombs of Kourend") || (player.getNumericTemporaryAttribute("bonecrusher_necklace_effect_delay").longValue() <= Utils.currentTimeMillis() && player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.BONECRUSHER_NECKLACE) || player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.DRAGONBONE_NECKLACE) {
                restorePrayer(player, bone);
            }
            applyExperience(player, bone);
            return true;
        }),
        ASH_SANCTIFIER(player -> player.getInventory().containsItem(ItemId.ASH_SANCTIFIER, 1), (player, bone, burying) -> {
            if (!enabled(player) || getCharges(player) <= 0) {
                return false;
            }
            if (player.inArea("Catacombs of Kourend") || player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.DRAGONBONE_NECKLACE) {
                restorePrayer(player, bone);
            }
            applyExperience(player, bone);
            return true;
        }),
        DRAGONBONE_NECKLACE(player -> player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.DRAGONBONE_NECKLACE, (player, bone, burying) -> {
            restorePrayer(player, bone);
            return true;
        });
        private static final CrusherType[] values = values();
        private final Predicate<Player> predicate;
        private final CrushEffect effect;

        CrusherType(final Predicate<Player> predicate, final CrushEffect effect) {
            this.predicate = predicate;
            this.effect = effect;
        }

        public static CrusherType get(final Player player) {
            for (final AshSanctifier.CrusherType type : values) {
                if (!type.getPredicate().test(player)) {
                    continue;
                }
                return type;
            }
            return null;
        }

        public Predicate<Player> getPredicate() {
            return this.predicate;
        }

        public CrushEffect getEffect() {
            return this.effect;
        }

        public interface CrushEffect {
            boolean crush(final Player player, final Ashes bone, final boolean burying);
        }
    }
}
