package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 31-1-2019 | 19:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ElvenKeyPlugin implements ItemOnObjectAction {
    private static final Item CRYSTAL_SHARDS = new Item(23866, 150);
    private static final Item CRYSTAL_KEY = new Item(989, 1);
    private static final Item CRYSTAL_KEY_ENCHANTMENT = new Item(32012, 1);

    private static final Item ENHANCED_CRYSTAL_KEY = new Item(23951);
    private static final Animation finalAnim = new Animation(811);

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getInventory().containsItem(CRYSTAL_SHARDS)) {
            player.getDialogueManager().start(new ItemChat(player, CRYSTAL_SHARDS, "You need at least 150 crystal shards."));
            return;
        }
        if (!player.getInventory().containsItem(CRYSTAL_KEY)) {
            player.getDialogueManager().start(new ItemChat(player, CRYSTAL_KEY, "You will need a Key and shards to know how to do this."));
            return;
        }
        if (!player.getInventory().containsItem(CRYSTAL_KEY_ENCHANTMENT)) {
            player.getDialogueManager().start(new ItemChat(player, CRYSTAL_KEY_ENCHANTMENT, "You will need a Blueprint to do this."));
            return;
        }

        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Combine coins, Key to make a new Key", "Yes.", "No.")
                        .onOptionOne(() -> {
                            player.setAnimation(finalAnim);
                            player.getInventory().deleteItemsIfContains(new Item[] { CRYSTAL_SHARDS, CRYSTAL_KEY, CRYSTAL_KEY_ENCHANTMENT }, () -> player.getInventory().addItem(ENHANCED_CRYSTAL_KEY));
                            setKey(5);
                        });
                item(5, ENHANCED_CRYSTAL_KEY, "You combine the Shards and Key and make a new Key.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 989, 32012, 23866, 23951 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 50008 };
    }
}
