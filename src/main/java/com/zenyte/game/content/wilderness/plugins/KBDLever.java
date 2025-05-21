package com.zenyte.game.content.wilderness.plugins;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.boss.kingblackdragon.KingBlackDragonInstance;
import com.zenyte.game.content.skills.magic.spells.teleports.structures.LeverTeleport;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 16/03/2019 20:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KBDLever implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(KBDLever.class);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch(option) {
            case "Pull":
                new LeverTeleport(KingBlackDragonInstance.insideTile, object, "... and teleport into the Dragon's lair.", () -> player.getAchievementDiaries().update(WildernessDiary.ENTER_KING_BLACK_DRAGON_LAIR)).teleport(player);
                break;
            case "Commune":
                final int playerCount = GlobalAreaManager.get("King Black Dragon Lair").getPlayers().size();
                player.sendMessage("The Lever magically communicates with you, saying there " + (playerCount == 0 ? "are no adventurers" : (playerCount == 1 ? "is 1 adventurer" : "are " + playerCount + " adventurers")) + " inside the lair.");
                break;
            case "Private":
                if (!player.getInventory().containsItem(KingBlackDragonInstance.price)) {
                    player.sendMessage("A private King Black Dragon lair costs " + Utils.format(KingBlackDragonInstance.price.getAmount()) + " coins. The lever cannot take funds from your bank.");
                    return;
                }
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        options("Enter a private King Black Dragon lair?", "Pay " + Utils.format(KingBlackDragonInstance.price.getAmount()) + " coins.", "Cancel.").onOptionOne(() -> {
                            try {
                                if (!player.getInventory().containsItem(KingBlackDragonInstance.price)) {
                                    player.sendMessage("Not enough coins in your inventory.");
                                    return;
                                }
                                final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                                final KingBlackDragonInstance instance = new KingBlackDragonInstance(player, area, 281, 584);
                                instance.constructRegion();
                                player.getInventory().deleteItem(KingBlackDragonInstance.price);
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        });
                    }
                });
                break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LEVER_1816 };
    }
}
