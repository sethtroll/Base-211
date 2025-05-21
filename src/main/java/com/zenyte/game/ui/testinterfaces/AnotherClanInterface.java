package com.zenyte.game.ui.testinterfaces;

import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.model.ui.chat_channel.ChatChannelPlayerExtKt.getChatChannelInterfaceType;

/**
 * @author Tommeh | 27-10-2018 | 19:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AnotherClanInterface extends Interface {

    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        getChatChannelInterfaceType(player).sendTabInterface(player, getInterface());
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ANOTHER_CLAN_TAB;
    }
}
