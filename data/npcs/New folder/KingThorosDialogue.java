package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KingThorosDialogue extends Dialogue {

    private static final int COIN_ID = 995;
    private static final int BET_AMOUNT = 10_000_000;
    public KingThorosDialogue(Player player, NPC npc, boolean b) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        if (player.getInventory().containsItem(COIN_ID, BET_AMOUNT)) {
            npc(String.format("Hello, %s! Do you wish to test your luck against my grand gamble? 10 million coins for a chance at unimaginable wealth!", player.getPlayerInformation().getDisplayname()));
            options("Dare you gamble with King Thoros?", "Yes, I feel lucky", "Seems risky, no thanks.").onOptionOne(() -> setKey(5));
            player(5, "Yes, I feel lucky");
            npc(String.format("I wish you the best of luck, %s!", player.getPlayerInformation().getDisplayname())).executeAction(this::gambleCoins);
        } else {
            npc("You don't have enough coins to gamble. Please come back with at least 10 million.");
        }
    }

    private void gambleCoins() {
        if (rollForWin()) {
            LootResult lootResult = getLootFromTable();
            int misfortuneAmount = calculateMisfortuneAmount();
            player.getInventory().deleteItem(COIN_ID, misfortuneAmount);
            player.getInventory().addItem(lootResult.getItemId(), lootResult.getQuantity());
            player.sendMessage(String.format("Fortune favors you! You've won %d of item ID: %d", lootResult.getQuantity(), lootResult.getItemId()));
            npc("Splendid! The gods of luck smile upon you, adventurer. Enjoy your reward!");
            WorldBroadcasts.broadcast(player, BroadcastType.GAMBLE_KING_THOROS, String.format("Congratulations to %s for winning at King Thoros's gamble!", player.getName()));
        } else {
            player.getInventory().deleteItem(COIN_ID, BET_AMOUNT);
            player.sendMessage("Alas, luck was not on your side this time.");
            npc("Ah, such is the fickle nature of fortune. Perhaps another time, brave soul.");
        }
    }

    private boolean rollForWin() {
        int randomNumber = Utils.random(1, 10);
        return randomNumber <= 3;
    }

    private int calculateMisfortuneAmount() {
        int randomPercentage = ThreadLocalRandom.current().nextInt(70, 81);
        return (int) (BET_AMOUNT * randomPercentage / 100.0);
    }

    private LootResult getLootFromTable() {
        List<ItemWithWeightAndQuantity> lootTable = buildLootTable();
        int totalWeight = lootTable.stream().mapToInt(ItemWithWeightAndQuantity::getWeight).sum();
        int index = ThreadLocalRandom.current().nextInt(totalWeight);
        int currentSum = 0;
        for (ItemWithWeightAndQuantity item : lootTable) {
            currentSum += item.getWeight();
            if (index < currentSum) {
                int quantity = Utils.random(item.getMinQuantity(), item.getMaxQuantity());
                return new LootResult(item.getItemId(), quantity);
            }
        }
        return new LootResult(-1, 0); // Fallback case
    }

    private static List<ItemWithWeightAndQuantity> buildLootTable() {
        return Stream.of(
                new ItemWithWeightAndQuantity(30017, 10, 1, 1),
                new ItemWithWeightAndQuantity(11824, 30, 1, 1),
                new ItemWithWeightAndQuantity(11826, 30, 1, 1),
                new ItemWithWeightAndQuantity(11828, 30, 1, 1),
                new ItemWithWeightAndQuantity(11830, 30, 1, 1),
                new ItemWithWeightAndQuantity(11832, 30, 1, 1),
                new ItemWithWeightAndQuantity(11834, 30, 1, 1),
                new ItemWithWeightAndQuantity(11836, 30, 1, 1),
                new ItemWithWeightAndQuantity(22322, 30, 1, 1),
                new ItemWithWeightAndQuantity(22323, 30, 1, 1),
                new ItemWithWeightAndQuantity(22324, 20, 1, 1),
                new ItemWithWeightAndQuantity(22326, 30, 1, 1),
                new ItemWithWeightAndQuantity(22327, 30, 1, 1),
                new ItemWithWeightAndQuantity(22328, 30, 1, 1),
                new ItemWithWeightAndQuantity(19707, 200, 1, 1),
                new ItemWithWeightAndQuantity(6739, 200, 1, 1),
                new ItemWithWeightAndQuantity(11840, 200, 1, 1),
                new ItemWithWeightAndQuantity(6573, 200, 1, 1),
                new ItemWithWeightAndQuantity(11862, 6, 1, 1),
                new ItemWithWeightAndQuantity(962, 6, 1, 1),
                new ItemWithWeightAndQuantity(6585, 200, 1, 1),
                new ItemWithWeightAndQuantity(6889, 200, 1, 1),
                new ItemWithWeightAndQuantity(23866, 200, 3000, 6000),
                new ItemWithWeightAndQuantity(11284, 100, 1, 1),
                new ItemWithWeightAndQuantity(20724, 50, 1, 1),
                new ItemWithWeightAndQuantity(24664, 10, 1, 1),
                new ItemWithWeightAndQuantity(24666, 10, 1, 1),
                new ItemWithWeightAndQuantity(24668, 10, 1, 1),
                new ItemWithWeightAndQuantity(24271, 20, 1, 1),
                new ItemWithWeightAndQuantity(20997, 6, 1, 1),
                new ItemWithWeightAndQuantity(22486, 6, 1, 1),
                new ItemWithWeightAndQuantity(27226, 6, 1, 1),
                new ItemWithWeightAndQuantity(27229, 6, 1, 1),
                new ItemWithWeightAndQuantity(27232, 6, 1, 1),
                new ItemWithWeightAndQuantity(26382, 6, 1, 1),
                new ItemWithWeightAndQuantity(26386, 6, 1, 1),
                new ItemWithWeightAndQuantity(2638, 6, 1, 1)
        ).collect(Collectors.toList());
    }

    private static class ItemWithWeightAndQuantity {
        private final int itemId, weight, minQuantity, maxQuantity;

        ItemWithWeightAndQuantity(int itemId, int weight, int minQuantity, int maxQuantity) {
            this.itemId = itemId;
            this.weight = weight;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
        }

        public int getItemId() {
            return itemId;
        }

        public int getWeight() {
            return weight;
        }

        public int getMinQuantity() {
            return minQuantity;
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }
    }

    private static class LootResult {
        private final int itemId, quantity;

        LootResult(int itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public int getItemId() {
            return itemId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}