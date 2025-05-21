package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.FriendSetRankEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;

/**
 * @author Tommeh | 2 dec. 2017 : 22:21:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FriendSetRankDecoder implements ClientProtDecoder<FriendSetRankEvent> {
    @Override
    public FriendSetRankEvent decode(Player player, int opcode, RSBuffer buffer) {
        final byte rank = buffer.read128Byte();
        final String name = buffer.readString();
        return new FriendSetRankEvent(name, rank);
    }
}
