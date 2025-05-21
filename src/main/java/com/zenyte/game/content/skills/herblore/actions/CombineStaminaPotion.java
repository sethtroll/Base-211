package com.zenyte.game.content.skills.herblore.actions;

import com.zenyte.game.content.consumables.drinks.Potion;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Skills;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author Tommeh | 11 jun. 2018 | 21:47:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CombineStaminaPotion extends Action {
    // Keep ordered from low dose to high
    public static final Int2IntOpenHashMap POTS = new Int2IntOpenHashMap(new int[]{3022, 3020, 3018, 3016}, new int[]{12631, 12629, 12627, 12625});
    private final int amount;
    private final int potionToUpgrade;
    private final int upgradedPotion;
    private final int dose;
    private int completed;

    public CombineStaminaPotion(int amount, int potionToUpgrade) {
        this.amount = amount;
        this.potionToUpgrade = potionToUpgrade;
        this.upgradedPotion = POTS.get(potionToUpgrade);
        this.dose = Potion.STAMINA_POTION.getDoses(upgradedPotion);
    }

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        final Item potion = player.getInventory().getAny(potionToUpgrade);
        player.getInventory().deleteItem(ItemId.AMYLASE_CRYSTAL, dose);
        player.getInventory().deleteItem(potion);
        player.getInventory().addOrDrop(upgradedPotion, 1);
        player.getSkills().addXp(Skills.HERBLORE, 25.5 * dose);
        completed++;
        return 1;
    }

    private boolean check() {
        if (completed >= amount) {
            return false;
        }
        return player.carryingItem(potionToUpgrade) && player.getInventory().getAmountOf(ItemId.AMYLASE_CRYSTAL) >= dose;
    }
}
