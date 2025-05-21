package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.room.TektonRoom;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.util.TimeUnit;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 04:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TektonFire implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, TektonRoom.class, room -> {
            if (room.isFinished()) {
                player.sendMessage("The fire is about to go out!");
                return;
            }
            if (player.getNumericTemporaryAttribute("tendril_cox_delay").longValue() > System.currentTimeMillis()) {
                return;
            }
            player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(15));
            player.lock();
            player.setRunSilent(true);
            final boolean damage = !room.getTekton().isDead() && room.getTekton().isApproached();
            if (damage) {
                final Hit hit = new Hit((int) ((player.getHitpoints() * 0.15F) + (player.getHitpoints() * 0.075F)), HitType.REGULAR);
                hit.setExecuteIfLocked();
                player.applyHit(hit);
            }
            switch(object.getRotation()) {
                case 0:
                case 2:
                    if (player.getY() < object.getY()) {
                        player.addWalkSteps(player.getX(), object.getY() + 2, 3, false);
                    } else {
                        player.addWalkSteps(player.getX(), object.getY() - 1, 3, false);
                    }
                    break;
                default:
                    if (player.getX() < object.getX()) {
                        player.addWalkSteps(object.getX() + 2, player.getY(), 3, false);
                    } else {
                        player.addWalkSteps(object.getX() - 1, player.getY(), 3, false);
                    }
                    break;
            }
            WorldTasksManager.schedule(() -> {
                player.setRunSilent(false);
                player.unlock();
                player.getTemporaryAttributes().put("tendril_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(2));
                if (damage) {
                    player.sendMessage("You get burnt by the fire.");
                }
            }, 2);
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FIRE_30021 };
    }
}
