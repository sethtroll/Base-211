package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 21/04/2019 14:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MountainDwarves extends Area {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {2816, 9890},
                        {2816, 9856},
                        {2880, 9856},
                        {2880, 9890}
                }, 0)
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
        return "Taverley Underground: Mountain Dwarves";
    }
}
