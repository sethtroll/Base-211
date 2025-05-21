package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
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
public class Secbox extends ItemPlugin {

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
            player.getInventory().deleteItem(item);
            final StringBuilder builder = new StringBuilder();
            final List<Secbox.MysteryItem> rewards = getRewards(player);
            for (final Secbox.MysteryItem rewardItem : rewards) {
                final Item reward = new Item(rewardItem.id, Utils.random(rewardItem.minAmount, rewardItem.maxAmount));
                builder.append(reward.getAmount()).append(" x ").append(reward.getName()).append(", ");
                player.getInventory().addOrDrop(reward);
                if (rewardItem.credits >= 300) {
                   // WorldBroadcasts.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, rewardItem.id);
                }
            }
            builder.delete(builder.length() - 2, builder.length());
            player.getDialogueManager().start(new ItemChat(player, item, "You open the mystery box and find " + builder + "!"));
        });
    }

    private final List<MysteryItem> getRewards(@NotNull final Player player) {
        final ArrayList<Secbox.MysteryItem> list = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            list.add(generateItem(player));
        }
        return list;
    }

    @Override
    public int[] getItems() {
        return new int[]{60011};
    }

    public enum MysteryItem {

        //All items above this line are rolled within the two supply rolls. An additional roll will be done for the entire table afterwards.


        LIMPWURT_ROOT(226, 15, 50, 10000, 100),
        WHITE_BERRIES(240, 15, 50, 10000, 100),
        POTATO_CACTUS(3139, 15, 50, 10000, 100),
        WINE_OF_ZAMORAK(246, 15, 50, 10000, 100),
        SNAPE_GRASS(232, 15, 50, 10000, 100),
        MORT_MYRE_FUNGUS(2971, 15, 50, 10000, 100),
        CRUSHED_NEST(6694, 15, 50, 10000, 100),
        RED_SPIDERS_EGGS(224, 15, 50, 10000, 100);

        public static final MysteryItem[] values = values();
        private static int total;

        static {
            for (final Secbox.MysteryItem reward : values) {
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
            for (final Secbox.MysteryItem it : values) {
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
