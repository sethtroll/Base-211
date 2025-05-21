package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BugReportEvent implements ClientProtEvent {
    private final String instructions;
    private final String description;
    private final int bit;

    public BugReportEvent(final String instructions, final String description, final int bit) {
        this.instructions = instructions;
        this.description = description;
        this.bit = bit;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Instructions: " + instructions + ", description: " + description + ", bit: " + bit);
    }

    @Override
    public void handle(Player player) {
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }
}
