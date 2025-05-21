package com.zenyte.game.content.skills.agility.alkharidrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourse;
import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 26 feb. 2018 : 17:07:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class AlKharidRooftopCourse implements AgilityCourse {

    public static final Location[] MARK_LOCATIONS = {
            new Location(3275, 3186, 3), new Location(3267, 3170, 3),
            new Location(3290, 3162, 3), new Location(3317, 3161, 1),
            new Location(3317, 3177, 2), new Location(3303, 3189, 3)};

    @Override
    public Class<?>[] getObstacles() {
        return new Class<?>[]{RoughWall.class, FirstTightRope.class, Cable.class, ZipLine.class, TropicalTree.class, RooftopBeams.class, SecondTightRope.class, Gap.class};
    }

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}
