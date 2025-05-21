package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.NoTimeOutEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 19:51:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class NoTimeOutDecoder implements ClientProtDecoder<NoTimeOutEvent> {

    @Override
    public NoTimeOutEvent decode(Player player, int opcode, RSBuffer buffer) {
        return new NoTimeOutEvent();
    }
}
