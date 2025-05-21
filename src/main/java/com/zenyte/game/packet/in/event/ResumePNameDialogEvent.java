package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.NameDialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePNameDialogEvent implements ClientProtEvent {
    private String name;

    public ResumePNameDialogEvent(final String name) {
        this.name = name;
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Value: " + name);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        name = Utils.convertToNameFormat(name);
        if (name.isEmpty()) {
            return;
        }
        final Object input = player.getTemporaryAttributes().get("interfaceInput");
        if (input instanceof NameDialogue dialogue) {
            dialogue.execute(player, name);
        }
    }
}
