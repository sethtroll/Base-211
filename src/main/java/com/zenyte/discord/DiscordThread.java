package com.zenyte.discord;

import com.zenyte.game.util.Utils;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static com.zenyte.discord.DiscordConstants.DISCORD_TOKEN;
import static com.zenyte.discord.DiscordConstants.DROPS_CHANNEL;

public final class DiscordThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DiscordThread.class);
    private static DiscordApi api;
    @Override
    public void run() {
        try {
            init();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    private void init() {
        api = new DiscordApiBuilder()
                .setToken(DISCORD_TOKEN)
                .login()
                .join();
        final JavacordHandler handler = new JavacordHandler(api);
        registerCommands(handler);
    }

    private void registerCommands(final JavacordHandler handler) {
        try {
            final Class<?>[] classes = Utils.getClasses("com.zenyte.discord.commands");
            for (final Class<?> c : classes) {
                if (c.isAnonymousClass() || c.isMemberClass()) continue;

                final Object o = c.getDeclaredConstructor().newInstance();
                if (!(o instanceof CommandExecutor command)) continue;

                handler.registerCommand(command);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }


    public static void sendDropMessage(String message){
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Rare Drop").setDescription(message).setColor(new Color(64, 0, 64));
        if(api.getTextChannelById(DROPS_CHANNEL).isPresent()) {
            api.getTextChannelById(DROPS_CHANNEL).get().sendMessage(embed);
        }
    }
}
