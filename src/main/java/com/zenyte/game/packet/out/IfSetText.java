package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:26:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IfSetText implements GamePacketEncoder {
    private final int interfaceId;
    private final int componentId;
    private final String text;

    public IfSetText(final int interfaceId, final int componentId, final String text) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.text = text;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", text: " + text);
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.IF_SETTEXT;
        final RSBuffer buffer = new RSBuffer(prot);
        buffer.writeString(text);
        buffer.writeIntME(interfaceId << 16 | componentId);
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
