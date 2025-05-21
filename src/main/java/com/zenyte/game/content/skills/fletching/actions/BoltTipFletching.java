package com.zenyte.game.content.skills.fletching.actions;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions.BoltTipFletchingData;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Skills;

/**
 * @author Tommeh | 25 aug. 2018 | 19:02:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class BoltTipFletching extends Action {
    private final BoltTipFletchingData data;
    private final int amount;
    private int cycle;
    private int ticks;

    public BoltTipFletching(final BoltTipFletchingData data, final int amount) {
        this.data = data;
        this.amount = amount;
    }

    @Override
    public boolean start() {
        return player.getInventory().containsItem(data.getMaterial());
    }

    @Override
    public boolean process() {
        if (!player.getInventory().containsItem(data.getMaterial())) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        if (ticks == 0) {
            player.setAnimation(data.getAnimation());
        } else if (ticks == 3) {
            player.getInventory().deleteItemsIfContains(new Item[]{data.getMaterial()}, () -> {
                player.getInventory().addItem(data.getProduct());
                player.sendFilteredMessage("You use your chisel to fletch small bolt tips.");
                player.getSkills().addXp(Skills.FLETCHING, data.getXp());
                cycle++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }
}
