package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.IfButtonTEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;

public class IfButtonTDecoder implements ClientProtDecoder<IfButtonTEvent> {
    @Override
    public IfButtonTEvent decode(Player player, int opcode, RSBuffer buffer) {
        final int toSlot = buffer.readShortLE();
        final int fromItemId = buffer.readShortLE128();
        final int fromCompressed = buffer.readIntME();
        final int toItemId = buffer.readShortLE();
        final int fromSlot = buffer.readShort128();
        final int toCompressed = buffer.readIntME();
        final int fromInterfaceId = fromCompressed >> 16;
        final int fromComponentId = fromCompressed & 65535;
        final int toInterfaceId = toCompressed >> 16;
        final int toComponentId = toCompressed & 65535;
        return new IfButtonTEvent(fromInterfaceId, fromComponentId, toInterfaceId, toComponentId, fromSlot, toSlot, fromItemId, toItemId);
    }
}
