package com.zenyte.game.world.entity.player.Pharaoh;

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

/**
 * @author Kris | 29/04/2019 13:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class PharaohManager {
    public static final int PHARAOH_POINTS_PER_INTERVAL = 10;
    public static final int PHARAOH_POINTS_INTERVAL_MINUTES = 30;
    public static final long PHARAOH_POINTS_INTERVAL_TICKS = (long) (TimeUnit.MINUTES.toMillis(PHARAOH_POINTS_INTERVAL_MINUTES) / Constants.TICK);
    /**
     * A linked list of sessions. This set will be cleansed of entries older than a month ago on logout.
     * List is defined as nullable because existing players will have forceset the variable to nullable due
     * to reflection loading.
     */
    @Nullable
    private final List<PharaohSession> sessions = new LinkedList<>();
    /**
     * The player who owns this Pharaoh manager.
     */
    @NotNull
    private final transient Player player;

    @Nullable
    private transient Date login;

    public PharaohManager(@NotNull final Player player) {
        this.player = player;
    }

    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final PharaohManager currentPharaoh = player.getPharaohManager();
        currentPharaoh.login = Date.from(Instant.now());
        final Player saved = event.getSavedPlayer();
        final PharaohManager PharaohManager = saved.getPharaohManager();
        if (PharaohManager == null) {
            return;
        }
        if (PharaohManager.sessions == null) {
            return;
        }
        Objects.requireNonNull(currentPharaoh.sessions).addAll(PharaohManager.sessions);
    }

    @Subscribe
    public static void onLogout(final LogoutEvent event) {
        final Player player = event.getPlayer();
        final PharaohManager Pharaoh = player.getPharaohManager();
        Objects.requireNonNull(Pharaoh.sessions).add(new PharaohSession(Pharaoh.login, Date.from(Instant.now())));
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        final Date minimumDate = calendar.getTime();
        Objects.requireNonNull(Pharaoh.sessions).removeIf(session -> session.getLogout().before(minimumDate));
    }

    /**
     * The multiplier for the logged in session duration.
     *
     * @return the multiplier - 1.5X every 2.5 hours, 3x every 5 hours & 5x every 10 hours.
     */
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

    /**
     * Gets the consecutive number of days logged in between(and including) the two dates specifified. Starts counting from the end, if it runs into a day that hasn't been properly
     * fulfilled, breaks out of the counting process and returns the number of days that had consecutive logins.
     *
     * @param from                   the date from when to start counting the consecutive logins.
     * @param until                  the date until when to count the days.
     * @param requiredDurationPerDay the time spent online necessarily to count a day as a successful consecutive login day.
     * @param timeUnit               the time unit in which the aforementioned time is represented in.
     * @return the number of days that the player has consecutively logged in for.
     */
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

    /**
     * Gets the duration the player has been in-game for during the requested day.
     *
     * @param day the day which to count.
     * @return the amount of milliseconds the player was online for during that day. All sessions are added together.
     */
    private long getLoggedInDuration(@NotNull final LocalDate day) {
        final Date date = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Date endDate = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        endDate.setTime(endDate.getTime() + TimeUnit.DAYS.toMillis(1));
        long totalMilliseconds = 0;
        for (final PharaohSession session : Objects.requireNonNull(this.sessions)) {
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
        final int totalMinutes = count * PHARAOH_POINTS_INTERVAL_MINUTES;
        final int hours = totalMinutes / 60;
        final int remainingMinutes = totalMinutes % 60;
        final String time = hours == 0 ? remainingMinutes + " minutes" : (hours + (hours == 1 ? " hour " : " hours ") + (remainingMinutes != 0 ? "and " : "") + (remainingMinutes == 0 ? "" : (remainingMinutes + " minutes")));
        final int points = (int) (PHARAOH_POINTS_PER_INTERVAL * multiplier(count));
        setPharaohPoints(getPharaohPoints() + points);
        player.sendMessage(Colour.RS_GREEN.wrap("You receive " + points + " Pharaoh points for " + time.trim() + " of consecutive playtime. You now have " + getPharaohPoints() + " Pharaoh points."));
    }

    public int getPharaohPoints() {
        return player.getNumericAttribute("Pharaoh points").intValue();
    }

    public void setPharaohPoints(final int value) {
        player.addAttribute("Pharaoh points", value);
        GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("Pharaoh points"), "Pharaoh points: <col=ffffff>" + player.getPharaohManager().getPharaohPoints() + "</col>"));
    }
}
