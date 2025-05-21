package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 19:05:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class UpdateZonePartialFollows implements GamePacketEncoder {
    private final int x;
    private final int y;
    private final Player player;

    public UpdateZonePartialFollows(final int x, final int y, final Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    private static int getLocal(int abs, int chunk) {
        return abs - 8 * (chunk - 6);
    }

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "X: " + x + ", y: " + y);
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.UPDATE_ZONE_PARTIAL_FOLLOWS;
        final RSBuffer buffer = new RSBuffer(prot);
        final int localX = getLocal(((x >> 3) << 3), player.getLastLoadedMapRegionTile().getChunkX());
        final int localY = getLocal(((y >> 3) << 3), player.getLastLoadedMapRegionTile().getChunkY());
        buffer.writeByteC(localY);
        buffer.writeByte(localX);
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
