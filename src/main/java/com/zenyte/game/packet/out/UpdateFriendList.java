package com.zenyte.game.packet.out;

import com.zenyte.Constants;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.ServerProt;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.network.game.packet.GamePacketOut;
import com.zenyte.network.io.RSBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Tommeh | 28 jul. 2018 | 18:55:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class UpdateFriendList implements GamePacketEncoder {
    private final Player player;
    private final List<FriendEntry> friends;

    public UpdateFriendList(final Player player, final List<FriendEntry> friends) {
        this.player = player;
        this.friends = friends;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot = ServerProt.UPDATE_FRIENDLIST;
        final RSBuffer buffer = new RSBuffer(prot);
        for (final UpdateFriendList.FriendEntry entry : friends) {
            final String username = entry.username;
            final String displayname = Utils.formatString(entry.username);
            final Optional<Player> friend = World.getPlayer(username);
            int world = 0;
            if (friend.isPresent()) {
                Player friendPlayer = friend.get();
                if (!friendPlayer.isFinished() && player.getSocialManager().isVisible(friendPlayer)) {
                    world = Constants.WORLD_PROFILE.getNumber();
                }
            }
            buffer.writeByte(entry.added ? 1 : 0);
            buffer.writeString(displayname);
            buffer.writeString("");
            buffer.writeShort(world);
            buffer.writeByte(ClanManager.getRank(player, username).getId());
            buffer.writeByte(0);
            if (world > 0) {
                buffer.writeString("");
                buffer.writeByte(0);
                buffer.writeInt(0);
            }
            buffer.writeString("");
        }
        return new GamePacketOut(prot, buffer);
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public static final class FriendEntry {
        private final String username;
        private final boolean added;

        public FriendEntry(final String username, final boolean added) {
            this.username = username;
            this.added = added;
        }
    }
}
