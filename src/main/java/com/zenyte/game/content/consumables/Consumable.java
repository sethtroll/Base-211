package com.zenyte.game.content.consumables;

import com.zenyte.game.content.consumables.drinks.*;
import com.zenyte.game.content.consumables.edibles.Food;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.SkillcapePerk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/11/2018 22:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface Consumable {
    Int2ObjectOpenHashMap<Consumable> consumables = new Int2ObjectOpenHashMap<>(500);
    Int2ObjectOpenHashMap<Consumable> food = new Int2ObjectOpenHashMap<>((int) (Food.values.length / 0.75F));
    Int2ObjectOpenHashMap<Consumable> gourdDrinks = new Int2ObjectOpenHashMap<>(500);

    /**
     * Initializes all of the consumable items in the game; this is necessary to be done manually
     * because static blocks are only initialized when the respective class is referenced, and they are not
     * allowed inside interfaces, so therefore if we add static blocks in classes like Food and others, and
     * reference {@link Consumable#consumables} through that, those static blocks would not be initialized,
     * as those classes aren't actually referenced, returning us with an empty map.
     */
    static void initialize() {
        for (final Food food : Food.values) {
            consumables.put(food.getId(), food);
            Consumable.food.put(food.getId(), food);
        }
        for (final Ale ale : Ale.values) {
            consumables.put(ale.getId(), ale);
        }
        for (final Drink drink : Drink.values) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
            }
        }
        for (final Potion drink : Potion.values) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
            }
        }
        for (final BarbarianMix drink : BarbarianMix.values) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
            }
        }
        for (final GourdPotion drink : GourdPotion.values) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
                gourdDrinks.put(id, drink);
            }
        }
        for (final KegAle drink : KegAle.values) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
            }
        }
    }

    int healedAmount(Player player);

    void heal(final Player player);

    Item leftoverItem(int id);

    Boost[] boosts();

    Animation animation();

    int delay();

    String startMessage();

    String endMessage(Player player);

    void onConsumption(final Player player);

    @SuppressWarnings("all")
    boolean canConsume(final Player player);

    void consume(final Player player, final Item item, final int slotId);

    void applyEffects(final Player player);

    class Restoration extends Boost {
        public Restoration(final int skill, final float percentage, final int amount) {
            super(skill, percentage, amount);
        }

        @Override
        public void apply(final Player player) {
            final int realLevel = getBaseLevel(player);
            final int currentLevel = getBoostedLevel(player);
            if (currentLevel >= realLevel) return;
            float boostedPercentage = percentage;
            if (skill == Skills.PRAYER && hasPrayerBoostItem(player)) {
                boostedPercentage += 0.02F;
            }
            final int boostedLevel = (int) (currentLevel + amount + (realLevel * boostedPercentage));
            player.getSkills().setLevel(skill, Math.min(realLevel, boostedLevel));
        }

        private boolean hasPrayerBoostItem(@NotNull final Player player) {
            return player.carryingItem(ItemId.HOLY_WRENCH) || player.carryingItem(ItemId.RING_OF_THE_GODS_I) || SkillcapePerk.PRAYER.isCarrying(player);
        }
    }


    class Debuff extends Boost {
        public Debuff(final int skill, final float percentage, final int amount) {
            super(skill, percentage, amount);
        }

        @Override
        public void apply(final Player player) {
            int level = getBoostedLevel(player);
            final int boostedLevel = (int) (level - amount - Math.floor(level * percentage));
            player.getSkills().setLevel(skill, Math.max(0, boostedLevel));
        }
    }


    class Boost {
        final int skill;
        final float percentage;
        final int amount;

        public Boost(final int skill, final float percentage, final int amount) {
            this.skill = skill;
            this.percentage = percentage;
            this.amount = amount;
        }

        public void apply(final Player player) {
            final int realLevel = getBaseLevel(player);
            final int currentLevel = getBoostedLevel(player);
            final int boostedLevel = (int) (Math.min(currentLevel, realLevel) + amount + (realLevel * percentage));
            if (boostedLevel < currentLevel) return;
            player.getSkills().setLevel(skill, boostedLevel);
        }

        int getBaseLevel(final Player player) {
            return player.getSkills().getLevelForXp(skill);
        }

        int getBoostedLevel(final Player player) {
            return player.getSkills().getLevel(skill);
        }
    }
}
