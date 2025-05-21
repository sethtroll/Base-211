package com.zenyte.game.world.entity.npc.drop.viewerentry;

/**
 * @author Tommeh | 05/10/2019 | 19:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface DropViewerEntry {
    int getMinAmount();

    int getMaxAmount();

    double getRate();

    String getInfo();

    boolean isPredicated();
}
