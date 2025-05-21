package com.zenyte.game.packet.in.decoder;

import com.zenyte.game.constants.ClientProt;
import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.in.event.If1ButtonEvent;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.io.RSBuffer;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 25/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class If1ButtonActionDecoder implements ClientProtDecoder<If1ButtonEvent> {
    public static final int[] OPCODES = {ClientProt.IF1_BUTTON1.getOpcode(), ClientProt.IF1_BUTTON2.getOpcode(), ClientProt.IF1_BUTTON3.getOpcode(), ClientProt.IF1_BUTTON4.getOpcode(), ClientProt.IF1_BUTTON5.getOpcode(), ClientProt.OPOBJ6.getOpcode()};

    @Override
    public If1ButtonEvent decode(Player player, int opcode, RSBuffer buffer) {
        int itemId;
        int slotId;
        int compressed;
        if (opcode == ClientProt.IF1_BUTTON1.getOpcode()) {
            itemId = buffer.readShort() & 65535;
            slotId = buffer.readShort() & 65535;
            compressed = buffer.readInt();
        } else if (opcode == ClientProt.IF1_BUTTON2.getOpcode()) {
            slotId = buffer.readShort128() & 65535;
            itemId = buffer.readShort128() & 65535;
            compressed = buffer.readIntLE();
        } else if (opcode == ClientProt.IF1_BUTTON3.getOpcode()) {
            compressed = buffer.readIntLE();
            itemId = buffer.readShort() & 65535;
            slotId = buffer.readShort() & 65535;
        } else if (opcode == ClientProt.IF1_BUTTON4.getOpcode()) {
            itemId = buffer.readShort() & 65535;
            slotId = buffer.readShortLE() & 65535;
            compressed = buffer.readIntIME();
        } else if (opcode == ClientProt.IF1_BUTTON5.getOpcode()) {
            itemId = buffer.readShortLE128() & 65535;
            slotId = buffer.readShortLE() & 65535;
            compressed = buffer.readInt();
        } else if (opcode == ClientProt.OPOBJ6.getOpcode()) {
            itemId = buffer.readShortLE128();
            return new If1ButtonEvent(1018, 55, -1, itemId, 6);
        } else {
            throw new RuntimeException("opcode: " + opcode);
        }
        final int interfaceId = (compressed >> 16);
        final int componentId = (compressed & 65535);
        final int option = ArrayUtils.indexOf(OPCODES, opcode) + 1;
        return new If1ButtonEvent(interfaceId, componentId, slotId, itemId, option);
    }
}
