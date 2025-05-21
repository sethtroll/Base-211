package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 09/01/2019 18:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Nexarea extends IceMountainArea implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{{2913, 5215}, {2913, 5191}, {2937, 5191}, {2937, 5215}})
        };
    }

    @Override
    public String restrictionMessage() {
        return "It is not permitted to set up a cannon in the Nex Area.";
    }

    @Override
    public String name() {
        return "Nex";
    }
}
