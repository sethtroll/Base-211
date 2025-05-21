package com.zenyte.game.world.entity.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class Notification {

    /**
     *
     */
    @Getter
    private final String title;

    @Getter
    private final String message;

    @Getter
    private final int colour;

    public Notification(String title, String message, int colour) {
        this.title = title;
        this.message = message;
        this.colour = colour;
    }
}