package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.item.Item;
import com.zenyte.game.tasks.WorldTask;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.entity.player.MemberRank;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.wilderness.WildernessResourceArea;

/**
 * @author Kris | 24. juuni 2018 : 14:52:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ResourceAreaGate implements ObjectAction {

    private static final DiaryReward[] DIARY_REWARDS = { DiaryReward.WILDERNESS_SWORD3, DiaryReward.MORYTANIA_LEGS2 };

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Open")) {
            if (!player.inArea("Wilderness Resource Area")) {
                if (DiaryReward.WILDERNESS_SWORD4.eligibleFor(player) || player.getMemberRank().eligibleTo(MemberRank.RUNE_MEMBER)) {
                    enter(player, object);
                } else {
                    player.getDialogueManager().start(new Dialogue(player) {

                        @Override
                        public void buildDialogue() {
                            int price = 7500;
                            for (int index = 0; index < DIARY_REWARDS.length; index++) {
                                final DiaryReward reward = DIARY_REWARDS[index];
                                if (reward.eligibleFor(player)) {
                                    price *= (index == 0 ? 0.5 : 0.8);
                                    break;
                                }
                            }
                            final int cost = price;
                            options("Do you want to enter? It will cost you " + cost + " coins", "Yes", "No").onOptionOne(() -> {
                                if (player.getInventory().containsItem(995, cost)) {
                                    player.getInventory().deleteItem(new Item(995, cost));
                                    enter(player, object);
                                } else {
                                    setKey(10);
                                }
                            });
                            item(10, new Item(995, 7500), "You don't have enough coins to pay Mandrith.");
                        }
                    });
                }
            } else {
                enter(player, object);
            }
        } else if (option.equals("Peek")) {
            player.sendMessage("You peek through the gate...");
            WorldTasksManager.schedule(() -> {
                final int size = GlobalAreaManager.getArea(WildernessResourceArea.class).getPlayers().size();
                final String auxiliary = size == 1 ? "is" : "are";
                final String label = size == 1 ? "adventurer" : "adventurers";
                player.sendMessage("There " + auxiliary + " " + size + " " + label + " inside the resource area.");
            });
        }
    }

    private static void enter(final Player player, final WorldObject object) {
        player.addWalkSteps(object.getX(), object.getY());
        player.lock(3);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            private WorldObject door;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        door = Door.handleGraphicalDoor(object, null);
                        return;
                    case 1:
                        if (player.inArea("Wilderness Resource Area")) {
                            player.addWalkSteps(door.getX(), door.getY(), 1, false);
                        } else {
                            player.addWalkSteps(object.getX(), object.getY(), 1, false);
                        }
                        return;
                    case 3:
                        Door.handleGraphicalDoor(door, object);
                        stop();
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_26760 };
    }
}
