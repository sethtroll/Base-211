package com.zenyte.game.ui.testinterfaces.advancedsettings;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 10/06/2022
 */
class SliderPosition {
    private final Setting slider;
    private final int startingPosition;

    public SliderPosition(@NotNull Setting slider, int startingPosition) {
        this.slider = slider;
        this.startingPosition = startingPosition;
    }

    public Setting getSlider() {
        return slider;
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SliderPosition that = (SliderPosition) o;

        if (startingPosition != that.startingPosition) return false;
        return slider.equals(that.slider);
    }

    @Override
    public int hashCode() {
        int result = slider.hashCode();
        result = 31 * result + startingPosition;
        return result;
    }

    @Override
    public String toString() {
        return "SliderPosition{" +
                "slider=" + slider +
                ", startingPosition=" + startingPosition +
                '}';
    }
}
