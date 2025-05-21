package com.zenyte.discord;

import java.util.Arrays;
import java.util.List;

public class DiscordUtils {

    private static final String[] admins = {
            "1146212289284948058", // ruinous
    };


    public static final List<String> ADMINS = Arrays.asList(admins);

    public static boolean isAdmin(String id) {
        return ADMINS.contains(id);
    }
}
