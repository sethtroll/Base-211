package com.zenyte.game.world.entity.player;

import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.world.entity.player.Emote.GIVE_THANKS_VARP;

/**
 * @author Kris | 22. veebr 2018 : 22:43.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class EmotesHandler {

    private final transient Player player;

    EmotesHandler(final Player player) {
        this.player = player;
    }

    /**
     * Unlocks a specific requested emote.
     *
     * @param emote Emote to unlock.
     */
    public void unlock(@NotNull final Emote emote) {
        if (emote.getConfig() == -1) {
            return;
        }
        if (emote.equals(Emote.TRICK)) {
            return;
        }
        player.getVarManager().sendBit(emote.getConfig(), emote == Emote.GOBLIN_BOW || emote == Emote.GOBLIN_SALUTE ? 7 : 1);
    }

    /**
     * Whether the requested emote is unlocked or not.
     *
     * @param emote Emote to check
     * @return unlocked or not.
     */
    public boolean isUnlocked(@NotNull final Emote emote) {
        if (emote == Emote.TRICK) {
            return true;
        } else if (emote == Emote.GIVE_THANKS) {
            return player.getVarManager().getValue(GIVE_THANKS_VARP) == 1;
        }
        return emote.getConfig() == -1 || player.getVarManager().getBitValue(emote.getConfig()) > 0;
    }

}
