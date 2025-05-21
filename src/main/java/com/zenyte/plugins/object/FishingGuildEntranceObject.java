package com.zenyte.plugins.object;

import com.zenyte.game.tasks.WorldTask;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 6 jun. 2018 | 17:49:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FishingGuildEntranceObject implements ObjectAction {

    private static final WorldObject OPENED_DOOR = new WorldObject(ObjectId.DOOR_20925, 0, 0, new Location(2611, 3394, 0));

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getSkills().getLevel(Skills.FISHING) < 68) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Fishing level of at least 68 to enter the guild."));
            return;
        }
        object.setLocked(true);
        World.spawnGraphicalDoor(OPENED_DOOR);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        player.addWalkSteps(player.getX(), player.getY() <= 3393 ? 3394 : 3393, 1, false);
                        break;
                    case 1:
                        World.spawnGraphicalDoor(World.getObjectWithType(OPENED_DOOR, object.getType()));
                        break;
                    case 2:
                        object.setLocked(false);
                        stop();
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_20925 };
    }
}
