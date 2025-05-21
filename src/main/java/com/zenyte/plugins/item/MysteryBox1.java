package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kris | 10/06/2019 06:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MysteryBox1 extends ItemPlugin {

    @NotNull
    public static MysteryItem generateItem(@NotNull final Player player) {
        MysteryItem rewardItem;
        while (true) {
            rewardItem = MysteryItem.generate();
           // if (rewardItem == MysteryItem.FIGHTER_TORSO || rewardItem == MysteryItem.FIRE_CAPE || rewardItem == MysteryItem.DRAGON_DEFENDER) {
               // if (player.containsItem(rewardItem.id)) {
                 //   continue;
               // }
           // }
            break;
        }
        return Objects.requireNonNull(rewardItem);
    }

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> {
            player.lock(5);
            player.getInventory().deleteItem(item);
            final StringBuilder builder = new StringBuilder();
            final List<MysteryBox1.MysteryItem> rewards = getRewards(player);
            for (final MysteryBox1.MysteryItem rewardItem : rewards) {
                final Item reward = new Item(rewardItem.id, Utils.random(rewardItem.minAmount, rewardItem.maxAmount));
                builder.append(reward.getAmount()).append(" x ").append(reward.getName()).append(", ");
                player.getInventory().addOrDrop(reward);
                if (rewardItem.credits >= 250) {
                    WorldBroadcasts.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, rewardItem.id);
                }
            }
            builder.delete(builder.length() - 2, builder.length());
            player.getDialogueManager().start(new ItemChat(player, item, "You open the Donator Mystery box and find " + builder + "!"));
        });
    }

    private final List<MysteryItem> getRewards(@NotNull final Player player) {
        final ArrayList<MysteryBox1.MysteryItem> list = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            list.add(generateItem(player));
        }
        return list;
    }

    @Override
    public int[] getItems() {
        return new int[]{60001};
    }

    public enum MysteryItem {

        //All items above this line are rolled within the two supply rolls. An additional roll will be done for the entire table afterwards.


        AMULET_OF_ETERNAL_GLORY(19707, 1, 1, 500, 0),
        DRAGON_AXE(6739, 1, 1, 1000, 0),
        DRAGON_BOOTS(11840, 1, 1, 1000, 50),
        ONYX(6573, 1, 1, 1000, 150),

        //BARROWS
        GUTHANS_SET(ItemId.GUTHANS_ARMOUR_SET, 1, 1, 900, 0),

        VERACS_SET(ItemId.VERACS_ARMOUR_SET, 1, 1, 800, 0),

        DHAROKS_SET(ItemId.DHAROKS_ARMOUR_SET, 1, 1, 700, 0),

        TORAGS_SET(ItemId.TORAGS_ARMOUR_SET, 1, 1, 1000, 0),

        AHRIMS_SET(ItemId.AHRIMS_ARMOUR_SET, 1, 1, 70, 0),

        KARILS_SET(ItemId.KARILS_ARMOUR_SET, 1, 1, 1000, 0),

        AMULET_OF_THE_DAMNED(12851, 1, 1, 700, 100),

        //Donation Store Items
        BLACK_PARTYHAT(11862, 1, 1, 50, 3000),
        CHRISTMAS_CRACKER(962, 1, 1, 50, 2000),
        ZAMORAKIAN_SPEAR(11824, 1, 1, 1000, 300),
        AMULET_OF_FURY(6585, 1, 1, 1000, 75),
        MAGES_BOOK(6889, 1, 1, 1000, 75),
        CRYSTAL_KEY(990, 25, 30, 1000, 10),
        CRYSTAL_SHARDS(23866, 1000, 3000, 1000, 0),

        //Extra
        DRAGONFIRE_SHIELD(11284, 1, 1, 500, 350),
        IMBUED_HEART(20724, 1, 1, 500, 350),
        TWISTED_ANCESTRAL_HAT(24664, 1, 1, 250, 350),
        TWISTED_ANCESTRAL_ROBE_TOP(24666, 1, 1, 250, 350),
        TWISTED_ANCESTRAL_ROBE_BOTTOM(24668, 1, 1, 250, 350),
        NEITIZNOT_FACEGUARD(24271, 1, 1, 50, 350),
        TWISTED_BOW(ItemId.TWISTED_BOW, 1, 1, 50, 350),
        SCYTHE_OF_VITUR(22486, 1, 1, 50, 350),
        MASORI_MASK(27226, 1, 1, 75, 350),
        MASORI_BODY(27229, 1, 1, 75, 350),
        MASORI_CHAPS(27232, 1, 1, 75, 350),
        TORVA_FULL_HELM(26382, 1, 1, 75, 350),
        TORVA_PLATELEGS(26386, 1, 1, 75, 350),
        TORVA_PLATEBODY(26384, 1, 1, 75, 350);
        public static final MysteryItem[] values = values();
        private static int total;

        static {
            for (final MysteryBox1.MysteryItem reward : values) {
                total += reward.weight;
            }
        }

        private final int id;
        private final int minAmount;
        private final int maxAmount;
        private final int weight;
        private final int credits;

        MysteryItem(final int id, final int minAmount, final int maxAmount, final int weight, final int credits) {
            this.id = id;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.weight = weight;
            this.credits = credits;
        }

        private static MysteryItem generate() {
            final int random = Utils.random(total);
            int current = 0;
            for (final MysteryBox1.MysteryItem it : values) {
                if ((current += it.weight) >= random) {
                    return it;
                }
            }
            return null;
        }

        public int getId() {
            return this.id;
        }

        public int getMinAmount() {
            return this.minAmount;
        }

        public int getMaxAmount() {
            return this.maxAmount;
        }

        public int getWeight() {
            return this.weight;
        }

        public int getCredits() {
            return this.credits;
        }
    }
}
