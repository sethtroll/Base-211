package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.Room;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 22. nov 2017 : 5:22.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RemoveObjectD extends Dialogue {

    private final WorldObject object;

    public RemoveObjectD(Player player, final WorldObject object) {
        super(player);
        this.object = object;
    }

    @Override
    public void buildDialogue() {
        options("Remove the " + object.getName() + "?", "Remove it.", "No, keep it.")
                .onOptionOne(() -> {
                    finish();
                    final RoomReference reference = player.getConstruction().getReference(object);
                    if (reference == null)
                        return;
                    if (reference.getRoom() == Room.QUEST_HALL_DS || reference.getRoom() == Room.SKILL_HALL_DS || reference.getRoom() == Room.DUNGEON_STAIRS_ROOM) {
                        player.getConstruction().removeStaircase(reference, object);
                        return;
                    }
                    player.getConstruction().removeObject(reference, object);
                }).onOptionTwo(() -> finish());
    }

}
