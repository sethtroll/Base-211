package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SyncClientVarCache implements GamePacketEncoder {
    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.SYNC_CLIENT_VARCACHE;
        final RSBuffer buffer = new RSBuffer(prot);
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
