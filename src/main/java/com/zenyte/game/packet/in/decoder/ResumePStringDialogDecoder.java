package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.ResumePStringDialogEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 20:13:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePStringDialogDecoder implements ClientProtDecoder<ResumePStringDialogEvent> {
    @Override
    public ResumePStringDialogEvent decode(Player player, int opcode, RSBuffer buffer) {
        final String string = buffer.readString();
        return new ResumePStringDialogEvent(string);
    }
}
