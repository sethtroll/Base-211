package com.zenyte.game.packet.out;

import com.zenyte.game.content.grandexchange.ExchangeOffer;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:15:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GrandExchangeOffer implements GamePacketEncoder {
    private final ExchangeOffer offer;

    public GrandExchangeOffer(final ExchangeOffer offer) {
        this.offer = offer;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Slot: " + offer.getSlot() + ", stage: " + offer.getStage() + ", id: " + offer.getItem().getId() + ", item amount: " + offer.getItem().getAmount() + ", price: " + offer.getPrice() + ", total amount: " + offer.getAmount() + ", total price: " + offer.getTotalPrice());
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.TRADING_POST_UPDATE;
        final RSBuffer buffer = new RSBuffer(prot);
        buffer.writeByte(offer.getSlot());
        buffer.writeByte(offer.getStage());
        buffer.writeShort(offer.getItem().getId());
        buffer.writeInt(offer.getPrice());
        buffer.writeInt(offer.getItem().getAmount());
        buffer.writeInt(offer.getAmount());
        buffer.writeInt(offer.getTotalPrice());
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
