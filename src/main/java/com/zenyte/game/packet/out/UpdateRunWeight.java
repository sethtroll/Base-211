package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 19:03:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class UpdateRunWeight implements GamePacketEncoder {
    private final Player player;

    public UpdateRunWeight(final Player player) {
        this.player = player;
    }

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "Weight: " + ((int) (player.getInventory().getWeight() + player.getEquipment().getWeight())));
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.UPDATE_RUNWEIGHT;
        final RSBuffer buffer = new RSBuffer(prot);
        buffer.writeShort((int) (player.getInventory().getWeight() + player.getEquipment().getWeight()));
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
