package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 21/04/2019 14:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DraynorSewers extends Area {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {3072, 9728},
                        {3072, 9600},
                        {3136, 9600},
                        {3136, 9728}
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
        return "Draynor Sewers";
    }
}
