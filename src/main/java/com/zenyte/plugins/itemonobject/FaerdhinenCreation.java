package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.achievementdiary.DiaryReward;
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
public class FaerdhinenCreation implements ItemOnObjectAction {

	private static final Item COINS = new Item(995, 10000000);
	private static final Item BLUEPRINT = new Item(30785);
	private static final Item WEAPON_SEED = new Item(30787);
	private static final Item BOW = new Item(25865);
	private static final Animation finalAnim = new Animation(811);

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if (!player.getInventory().containsItem(COINS)) {
			player.getDialogueManager().start(new ItemChat(player, COINS, "You need at least 10M coins to make a bow."));
			return;
		}
		if (!player.getInventory().containsItem(WEAPON_SEED)) {
			player.getDialogueManager().start(new ItemChat(player, WEAPON_SEED, "You need a enhanced crystal weapon seed to make a bow."));
			return;
		}
		if (!player.getInventory().containsItem(BLUEPRINT)) {
			player.getDialogueManager().start(new ItemChat(player, BLUEPRINT, "You will need a blueprint to know how to do this."));
			return;
		}

		player.getDialogueManager().start(new Dialogue(player) {

			@Override
			public void buildDialogue() {
				options("Combine coins, blueprint and seed to make a bow?", "Yes.", "No.")
						.onOptionOne(() -> {
							player.setAnimation(finalAnim);
							player.getInventory().deleteItemsIfContains(new Item[] { COINS, BLUEPRINT, WEAPON_SEED }, () -> player.getInventory().addItem(BOW));
							setKey(5);
						});
				item(5, BOW, "You combine the coins and weapon seed following the blueprint and make a Bow of Faerdinen.");
			}
		});
	}

	@Override
	public Object[] getItems() {
		return new Object[] { 30785, 995, 30787 };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 50007 };
	}
}
