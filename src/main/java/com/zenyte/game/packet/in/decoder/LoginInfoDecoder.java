package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.LoginInfoEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:53:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LoginInfoDecoder implements ClientProtDecoder<LoginInfoEvent> {

    @Override
    public LoginInfoEvent decode(Player player, int opcode, RSBuffer buffer) {
        return new LoginInfoEvent();
    }
}
