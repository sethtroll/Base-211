package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 03/05/2019 00:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WaterfallDungeon extends Area {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {2560, 9920},
                        {2560, 9856},
                        {2624, 9856},
                        {2624, 9920}
                })
        };
    }

    @Override
    public void enter(final Player player) {

    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Waterfall Dungeon";
    }
}
