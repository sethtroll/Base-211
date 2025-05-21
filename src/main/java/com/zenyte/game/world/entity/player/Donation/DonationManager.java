package com.zenyte.game.world.entity.player.Donation;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.zenyte.Constants;
import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.LogoutEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class DonationManager {
    public static final int Donation_POINTS_PER_INTERVAL = 0;
    public static final int Donation_POINTS_INTERVAL_MINUTES = 30000;
    public static final long Donation_POINTS_INTERVAL_TICKS = (long) (TimeUnit.MINUTES.toMillis(Donation_POINTS_INTERVAL_MINUTES) / Constants.TICK);

    @Nullable
    private final List<DonationSession> sessions = new LinkedList<>();

    @NotNull
    private final transient Player player;

    @Nullable
    private transient Date login;

    public DonationManager(@NotNull final Player player) {
        this.player = player;
    }


    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final DonationManager currentDonation = player.getDonationManager();
        currentDonation.login = Date.from(Instant.now());
        final Player saved = event.getSavedPlayer();
        final DonationManager DonationManager = saved.getDonationManager();
        if (DonationManager == null) {
            return;
        }
        if (DonationManager.sessions == null) {
            return;
        }
        Objects.requireNonNull(currentDonation.sessions).addAll(DonationManager.sessions);
    }

    @Subscribe
    public static void onLogout(final LogoutEvent event) {
        final Player player = event.getPlayer();
        final DonationManager Donation = player.getDonationManager();
        Objects.requireNonNull(Donation.sessions).add(new DonationSession(Donation.login, Date.from(Instant.now())));
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        final Date minimumDate = calendar.getTime();
        Objects.requireNonNull(Donation.sessions).removeIf(session -> session.getLogout().before(minimumDate));
    }


    private static float multiplier(final int iteration) {
        if (iteration % 20 == 0) {
            return 5;
        }
        if (iteration % 10 == 0) {
            return 3;
        }
        if (iteration % 5 == 0) {
            return 1.5F;
        }
        return 1;
    }


    public int getConsecutiveDaysLoggedIn(@NotNull final LocalDate from, @NotNull final LocalDate until, final int requiredDurationPerDay, @NotNull final TimeUnit timeUnit) {
        final long startDay = from.toEpochDay();
        final long endDay = until.toEpochDay();
        Preconditions.checkArgument(endDay > startDay, "End date cannot be after start date.");
        final long necessaryDuration = timeUnit.toMillis(requiredDurationPerDay);
        int count = 0;
        for (long epochDay = endDay; epochDay >= startDay; epochDay--) {
            final LocalDate date = LocalDate.ofEpochDay(epochDay);
            final long duration = getLoggedInDuration(date);
            if (duration < necessaryDuration) {
                break;
            }
            count++;
        }
        return count;
    }


    private long getLoggedInDuration(@NotNull final LocalDate day) {
        final Date date = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Date endDate = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        endDate.setTime(endDate.getTime() + TimeUnit.DAYS.toMillis(1));
        long totalMilliseconds = 0;
        for (final DonationSession session : Objects.requireNonNull(this.sessions)) {
            final Date login = session.getLogin();
            final Date logout = session.getLogout();
            if (logout.before(date) || login.after(endDate)) {
                continue;
            }
            final long start = (date.before(login) ? login : date).getTime();
            final long end = (logout.after(endDate) ? endDate : logout).getTime();
            final long difference = end - start;
            Preconditions.checkArgument(difference > 0);
            totalMilliseconds += difference;
        }
        return totalMilliseconds;
    }


    public void informSession(final int count) {
        if (player.getTemporaryAttributes().get("User deemed inactive") != null) {
            return;
        }
        final int totalMinutes = count * Donation_POINTS_INTERVAL_MINUTES;
        final int hours = totalMinutes / 60;
        final int remainingMinutes = totalMinutes % 60;
        final String time = hours == 0 ? remainingMinutes + " minutes" : (hours + (hours == 1 ? " hour " : " hours ") + (remainingMinutes != 0 ? "and " : "") + (remainingMinutes == 0 ? "" : (remainingMinutes + " minutes")));
        final int points = (int) (Donation_POINTS_PER_INTERVAL * multiplier(count));
        setDonationPoints(getDonationPoints() + points);
        player.sendMessage(Colour.RS_GREEN.wrap("You receive " + points + " Donation points for " + time.trim() + " of consecutive playtime. You now have " + getDonationPoints() + " Donation points."));
    }

    public int getDonationPoints() {
        return player.getNumericAttribute("Donation points").intValue();
    }

    public void setDonationPoints(final int value) {
        player.addAttribute("Donation points", value);
        GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("Donation points"), "Donation points: <col=ffffff>" + player.getDonationManager().getDonationPoints() + "</col>"));
    }
}


