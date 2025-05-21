package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.clans.ClanRank;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.zenyte.game.content.boss.dagannothkings.DagannothKingsInstanceConstants.entranceCoordinates;

/**
 * @author Kris | 25/04/2019 00:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WaterbirthDungeonCrack implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(WaterbirthDungeonCrack.class);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Peek")) {
            player.sendMessage("You peek through the crack...");
            WorldTasksManager.schedule(() -> {
                final int playerCount = GlobalAreaManager.get("Dagannoth Kings Lair").getPlayers().size();
                player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
            }, 2);
        } else if (option.equals("Private")) {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.sendMessage("You need to be in a clan chat channel to start or join an instance.");
                return;
            }
            final Optional<DagannothKingInstance> instance = DagannothKingsInstanceManager.getManager().findInstance(player);
            if (instance.isPresent()) {
                player.lock(1);
                player.setLocation(instance.get().getLocation(entranceCoordinates));
                return;
            }
            final ClanRank rank = ClanManager.getRank(player, channel);
            if (rank.getId() < channel.getKickRank().getId()) {
                player.sendMessage("Clan members ranked as " + Utils.formatString(channel.getKickRank().toString()) + " or above can only start a clan instance.");
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("Pay " + Utils.format(DagannothKingsInstanceConstants.COST) + " to start an instance for your clan?");
                    options(new DialogueOption("Yes.", () -> {
                        final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
                        final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
                        if ((long) amountInBank + amountInInventory >= DagannothKingsInstanceConstants.COST) {
                            if (DagannothKingsInstanceManager.getManager().findInstance(player).isPresent()) {
                                setKey(75);
                                return;
                            }
                            player.lock(1);
                            player.getInventory().deleteItem(new Item(ItemId.COINS_995, DagannothKingsInstanceConstants.COST)).onFailure(remainder -> player.getBank().remove(remainder));
                            try {
                                final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(6, 6);
                                final DagannothKingInstance area = new DagannothKingInstance(channel.getOwner(), allocatedArea, 361, 553);
                                area.constructRegion();
                                player.setLocation(area.getLocation(entranceCoordinates));
                            } catch (OutOfSpaceException e) {
                                log.error("", e);
                            }
                            return;
                        }
                        setKey(50);
                    }), new DialogueOption("No."));
                    plain(50, "You don't have enough coins with you or in your bank.");
                    plain(75, "Someone in your clan has already initiated an instance.");
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CRACK_30169 };
    }
}
