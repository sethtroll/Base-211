package com.zenyte.game.content.theatreofblood.reward;

import com.zenyte.game.item.Item;

/**
 * @author Cresinkel
 */
public enum TobRareReward {
    AVERNIC_DEFENDER_HILT(new Item(22477), 8), JUSTICIAR_FACEGUARD(new Item(22326), 2), JUSTICIAR_CHESTGUARD(new Item(22327), 2), JUSTICIAR_LEGGUARDS(new Item(22328), 2), SANGUINESTI_STAFF(new Item(22481), 2), GHRAZI_RAPIER(new Item(22324), 2), SCYTHE_OF_VITUR(new Item(22486), 1);
    public static final TobRareReward[] values = values();
    public static final int TOTAL_WEIGHT;

    static {
        int weight = 0;
        for (final var value : values) {
            weight += value.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    private final Item item;
    private final int weight;

    public Item getItem() {
        return this.item;
    }

    public int getWeight() {
        return this.weight;
    }

    private TobRareReward(final Item item, final int weight) {
        this.item = item;
        this.weight = weight;
    }
}
