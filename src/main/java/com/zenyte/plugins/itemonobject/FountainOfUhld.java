package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemOnObjectAction;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FountainOfUhld implements ItemOnObjectAction {

    private static final Animation ANIM = new Animation(832);

    private static final int BASIC_CHARGED_GLORY = 1712;

    private static final int TRIMMED_CHARGED_GLORY = 10354;

    private static final int CHARGED_SKILLS_NECKLACE = 11105;

    private static final int CHARGED_COMBAT_BRACELET = 11118;

    private static final int[] BASIC_GLORIES = new int[] { 1704, 1706, 1708, 1710 };

    private static final int[] TRIMMED_GLORIES = new int[] { 10362, 10360, 10358, 10356 };

    private static final int[] SKILLS_NECKLACES = new int[] { 11113, 11111, 11109, 11107 };

    private static final int[] COMBAT_BRACELETS = new int[] { 11126, 11124, 11122, 11120 };

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        player.lock(1);
        player.setAnimation(ANIM);
        WorldTasksManager.schedule(() -> {
            final Container container = player.getInventory().getContainer();
            container.setFullUpdate(true);
            for (int i = container.getContainerSize(); i >= 0; i--) {
                final Item containerItem = container.get(i);
                if (containerItem == null) {
                    continue;
                }
                final int id = containerItem.getId();
                final boolean isUnchargedGlory = ArrayUtils.contains(BASIC_GLORIES, id);
                final boolean isTrimmedUnchargedGlory = ArrayUtils.contains(TRIMMED_GLORIES, id);
                final boolean isSkillsNecklace = ArrayUtils.contains(SKILLS_NECKLACES, id);
                final boolean isCombatBracelet = ArrayUtils.contains(COMBAT_BRACELETS, id);
                if (isUnchargedGlory || isTrimmedUnchargedGlory || isSkillsNecklace || isCombatBracelet) {
                    containerItem.setId(isSkillsNecklace ? CHARGED_SKILLS_NECKLACE : isCombatBracelet ? CHARGED_COMBAT_BRACELET : isUnchargedGlory ? BASIC_CHARGED_GLORY : TRIMMED_CHARGED_GLORY);
                }
            }
            container.refresh(player);
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    item(item, "You feel a power emanating from the fountain as it recharges your jewellery.");
                }
            });
        });
    }

    @Override
    public Object[] getItems() {
        final IntArrayList list = new IntArrayList();
        list.addAll(new IntArrayList(BASIC_GLORIES));
        list.addAll(new IntArrayList(TRIMMED_GLORIES));
        list.addAll(new IntArrayList(SKILLS_NECKLACES));
        list.addAll(new IntArrayList(COMBAT_BRACELETS));
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FOUNTAIN_OF_UHLD };
    }
}
