package com.zenyte.network.game.packet;

import com.zenyte.game.packet.ServerProt;
import com.zenyte.network.PacketIn;
import com.zenyte.network.io.RSBuffer;

/**
 * @author Tommeh | 28 jul. 2018 | 12:39:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GamePacketOut implements PacketIn {
    private final ServerProt packet;
    private final RSBuffer buffer;

    public GamePacketOut(final ServerProt packet, final RSBuffer buffer) {
        this.packet = packet;
        this.buffer = buffer;
    }

    public boolean encryptBuffer() {
        return false;
    }

    public ServerProt getPacket() {
        return this.packet;
    }

    public RSBuffer getBuffer() {
        return this.buffer;
    }
}
