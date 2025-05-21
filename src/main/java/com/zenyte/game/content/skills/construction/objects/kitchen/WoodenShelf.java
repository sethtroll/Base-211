package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Kris | 24. veebr 2018 : 15:52.04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WoodenShelf implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHELVES_13545, ObjectId.SHELVES_13546, ObjectId.SHELVES_13554 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        final int objId = object.getId();
        player.getDialogueManager().start(new OptionDialogue(player, "What would you like to take?", new String[] { "A kettle.", "A teapot.", objId == 13554 ? "A porcelain cup." : "A clay cup.", objId == 13545 ? null : "An empty beer glass.", objId == 13554 ? "A cake tin." : null }, new Runnable[] { () -> addItem(player, 0, objId), () -> addItem(player, 1, objId), () -> addItem(player, 2, objId), () -> addItem(player, 3, objId), () -> addItem(player, 4, objId) }));
    }

    private void addItem(final Player player, final int option, final int objectId) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some more free inventory space to take this.");
            return;
        }
        player.sendInputInt("How many would you like to take?", amount -> {
            if (amount > player.getInventory().getFreeSlots()) {
                amount = player.getInventory().getFreeSlots();
            }
            if (amount == 0) {
                player.sendMessage("You don't have enough free space to take this.");
                return;
            }
            switch(option) {
                case 0:
                    player.getInventory().addItem(7688, amount);
                    break;
                case 1:
                    player.getInventory().addItem(objectId == 13554 ? 7714 : 7702, amount);
                    break;
                case 2:
                    player.getInventory().addItem(objectId == 13554 ? 7732 : 7728, amount);
                    break;
                case 3:
                    player.getInventory().addItem(1919, amount);
                    break;
                case 4:
                    player.getInventory().addItem(1887, amount);
                    break;
            }
        });
    }
}
