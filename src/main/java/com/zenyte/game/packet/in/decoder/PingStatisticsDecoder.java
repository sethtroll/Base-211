package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.PingStatisticsEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;

/**
 * @author Kris | 1. apr 2018 : 22:05.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class PingStatisticsDecoder implements ClientProtDecoder<PingStatisticsEvent> {
    @Override
    public PingStatisticsEvent decode(Player player, int opcode, RSBuffer buffer) {
        final long end = System.nanoTime();

        final long start1 = buffer.readIntME() & 0xFFFF_FFFFL;
        final long start2 = buffer.readIntLE() & 0xFFFF_FFFFL;
        final long start = (start1 << 32) + start2;

        final int fps = buffer.read128Byte() & 0xFF;
        final int gc = buffer.readByte() & 0xFF;

        return new PingStatisticsEvent(gc, fps, end - start);
    }
}
