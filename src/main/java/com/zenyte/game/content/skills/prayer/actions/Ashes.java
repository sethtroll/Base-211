package com.zenyte.game.content.skills.prayer.actions;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matt | 2. Oct 2021
 */
public enum Ashes {

	ASHES(20, new Item(592)),
	FIENDISH_ASHES(20, new Item(31038)),
	VILE_ASHES(50, new Item(31041)),
	MALICIOUS_ASHES(120, new Item(31044)),
	ABYSSAL_ASHES(160, new Item(31047)),
	INFERNAL_ASHES(200, new Item(30065));

	private final Item[] ashes;
	private final double xp;
	private final String name;

	Ashes(final double xp, final Item... item) {
		this.xp = xp;
		this.ashes = item;
		this.name = name().toLowerCase().replace("_", " ");
	}

	public static final Ashes[] VALUES = values();
	private static final Map<Integer, Ashes> ASHES_MAP = new HashMap<Integer, Ashes>(VALUES.length);
	private static final Animation BURY_ANIMATION = new Animation(1979);

	static {
		for (Ashes ashes : VALUES) {
			for (Item ash : ashes.ashes)
				ASHES_MAP.put(ash.getId(), ashes);
		}
	}

	public static Ashes getAsh(int id) {
		return ASHES_MAP.get(id);
	}

	public static void bury(final Player player, final Ashes ash, final Item item, final int slot) {
		if (player.isDead() || player.isFinished()) {
			return;
		}
		if (player.getNumericTemporaryAttribute("BoneBuryDelay").longValue() > Utils.currentTimeMillis()) {
			return;
		}
		player.lock(1);
		player.setAnimation(BURY_ANIMATION);
		double xp = ash.getXp();
		player.getSkills().addXp(Skills.PRAYER, xp);
		player.getInventory().deleteItem(slot, item);
		player.sendSound(2738);
		player.sendMessage("You scatter the " + ash.name + ".");
		player.getTemporaryAttributes().put("BoneBuryDelay", Utils.currentTimeMillis() + 1000);
	}

	public Item[] getBones() {
		return this.ashes;
	}

	public Item[] getAshes() {
		return this.ashes;
	}

	public double getXp() {
		return this.xp;
	}

	public String getName() {
		return this.name;
	}
}