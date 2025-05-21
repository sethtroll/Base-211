package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/03/2019 23:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UpdateSiteSettings implements GamePacketEncoder {
    private final String settings;

    public UpdateSiteSettings(final String settings) {
        this.settings = settings;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Settings: " + settings);
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.FRIENDLIST_LOADED;
        final RSBuffer buffer = new RSBuffer(prot);
        buffer.writeString(settings);
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
