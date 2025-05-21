package com.zenyte.game.content.minigame.warriorsguild.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 18. dets 2017 : 2:06.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Anton extends NPC implements Spawnable {

    private static final ForceTalk[] MESSAGES = new ForceTalk[]{
            new ForceTalk("Armours and axes to suit your needs."),
            new ForceTalk("Ow my toe! That armour is heavy."),
            new ForceTalk("Imported weapons from the finest smithys around the lands!"),
            new ForceTalk("A fine selection of blades for you to peruse, come take a look!")
    };
    private long lastAnnouncement;

    public Anton(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (lastAnnouncement < Utils.currentTimeMillis()) {
            lastAnnouncement = Utils.currentTimeMillis() + 15000;
            setForceTalk(MESSAGES[Utils.random(MESSAGES.length - 1)]);
        }
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 2471;
    }

}
