package com.zenyte.game.world.entity;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

public final class ForceTalk {

    private static final Object2ObjectMap<String, ForceTalk> cachedForceChats = new Object2ObjectOpenHashMap<>();
    private final String text;

    public ForceTalk(String text) {
        this.text = text;
    }

    public static ForceTalk get(@NotNull final String string) {
        ForceTalk cached = cachedForceChats.get(string);
        if (cached == null) {
            cached = new ForceTalk(string);
            cachedForceChats.put(string, cached);
        }
        return cached;
    }

    public String getText() {
        return text;
    }
}