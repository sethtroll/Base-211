package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 2-12-2018 | 17:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpenUrl implements GamePacketEncoder {
    private final String url;

    public OpenUrl(final String url) {
        this.url = url;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "URL: " + url);
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.OPEN_URL;
        final RSBuffer buffer = new RSBuffer(prot);
        buffer.writeString(url);
        return new GamePacketOut(prot, buffer) {
            @Override
            public boolean encryptBuffer() {
                return true;
            }
        };
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }
}
