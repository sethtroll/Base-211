package com.zenyte.game.content.skills.fishing;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 04/03/2019 22:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FishingTool {
    LOBSTER_POT(new Tool(new Animation(619), 301)),
    SMALL_FISHING_NET(new Tool(new Animation(621), 303)),
    BIG_FISHING_NET(new Tool(new Animation(620), 305)),
    FISHING_ROD(new Tool(new Animation(622), 307)),
    FLY_FISHING_ROD(new Tool(new Animation(623), 309)),
    OILY_FISHING_ROD(new Tool(new Animation(623), 1585)),
    HARPOON(new Tool(new Animation(618), 311), new Tool(new Animation(618), 10129), new Tool(new Animation(7401), 21028, true), new Tool(new Animation(7402), 21033, true), new Tool(new Animation(7402), 21031, true)),
    BARBARIAN_ROD(new Tool(new Animation(623), 11323)),
    KARAMBWAN_VESSEL(new Tool(new Animation(1193), 3157));

    @NotNull
    final Tool[] tools;

    FishingTool(@NotNull final Tool... tools) {
        this.tools = tools;
    }

    public Optional<Tool> getTool(@NotNull final Player player) {
        final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
        final Inventory inventory = player.getInventory();
        for (int i = tools.length - 1; i >= 0; i--) {
            final FishingTool.Tool tool = tools[i];
            if (weapon == tool.id || inventory.containsItem(tool.id, 1)) {
                return Optional.of(tool);
            }
        }
        return Optional.empty();
    }


    public static class Tool {
        final Animation animation;
        final int id;
        final boolean increasedSpeed;

        Tool(final Animation animation, final int id) {
            this(animation, id, false);
        }

        Tool(final Animation animation, final int id, final boolean increasedSpeed) {
            this.animation = animation;
            this.id = id;
            this.increasedSpeed = increasedSpeed;
        }

        public boolean isIncreasedSpeed() {
            return this.increasedSpeed;
        }
    }
}
