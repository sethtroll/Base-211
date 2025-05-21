package com.zenyte.game.world.region.area.kourend;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 13/10/2019 | 18:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KebosSwamp extends GreatKourend {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{
                {1199, 3572},
                {1199, 3576},
                {1191, 3585},
                {1194, 3592},
                {1213, 3600},
                {1224, 3595},
                {1228, 3595},
                {1232, 3598},
                {1237, 3604},
                {1247, 3610},
                {1257, 3611},
                {1262, 3608},
                {1257, 3606},
                {1255, 3600},
                {1258, 3598},
                {1263, 3600},
                {1265, 3600},
                {1269, 3594},
                {1279, 3589},
                {1280, 3590},
                {1283, 3586},
                {1287, 3589},
                {1291, 3588},
                {1294, 3600},
                {1301, 3594},
                {1307, 3596},
                {1312, 3589},
                {1319, 3589},
                {1322, 3596},
                {1327, 3596},
                {1329, 3590},
                {1332, 3589},
                {1334, 3589},
                {1333, 3594},
                {1333, 3598},
                {1340, 3605},
                {1336, 3620},
                {1341, 3635},
                {1338, 3641},
                {1348, 3645},
                {1348, 3649},
                {1332, 3656},
                {1260, 3655},
                {1261, 3658},
                {1263, 3660},
                {1264, 3660},
                {1266, 3662},
                {1268, 3662},
                {1269, 3664},
                {1260, 3668},
                {1248, 3667},
                {1241, 3661},
                {1234, 3660},
                {1214, 3665},
                {1206, 3662},
                {1199, 3650},
                {1200, 3644},
                {1190, 3639},
                {1180, 3648},
                {1169, 3647},
                {1160, 3623},
                {1166, 3610},
                {1177, 3607},
                {1174, 3587},
                {1187, 3575}
        })};
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Kebos Swamp";
    }
}
