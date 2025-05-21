package com.zenyte.discord;

import com.zenyte.game.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class CommandLog {

    private static final int DELAY = 10; // time between commands
    public static Map<String, Long> LOGS = new HashMap<>();

    public static void log(String id, long time) {
        LOGS.put(id, time);
    }

    public static boolean check(String id) {
        if (DiscordUtils.isAdmin(id))
            return false;
        return time(id) < DELAY;
    }

    public static int time(String id) {
        return (int) (Utils.currentTimeMillis() - LOGS.get(id)) / 1000;
    }

    public static String warn(String id) {
        return "```You must wait at least " + (DELAY - time(id)) + "s before using another command.\n\nDo not attempt to spam or you will be blacklisted!```";
    }

}
