package com.zenyte.game.ui.testinterfaces;

import com.google.common.eventbus.Subscribe;
import com.zenyte.Constants;
import com.zenyte.GameEngine;
import com.zenyte.game.BonusCoxManager;
import com.zenyte.game.BonusTobManager;
import com.zenyte.game.BonusXpManager;
import com.zenyte.game.GameClock;
import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.event.DoubleDropsManager;
import com.zenyte.game.ui.Interface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.TimeUnit;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Privilege;
import com.zenyte.game.world.entity.player.SocialManager;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.LogoutEvent;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Tommeh | 2-12-2018 | 16:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GameNoticeboardInterface extends Interface {

    private static final Logger logger = LoggerFactory.getLogger(GameNoticeboardInterface.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final Privilege[] STAFF = {
            Privilege.SPAWN_ADMINISTRATOR, Privilege.ADMINISTRATOR,
            Privilege.GLOBAL_MODERATOR, Privilege.MODERATOR, Privilege.SUPPORT,
            Privilege.FORUM_MODERATOR
    };

    @Subscribe
    public static void onLogin(final LoginEvent event) {
        final Player p = event.getPlayer();
        if (increasesStaffCount(p)) staffCount.getAndIncrement();
        p.getPacketDispatcher().sendClientScript(3501, 162 << 16 | 2, 701 << 16 | 11, 701 << 16 | 16, 701 << 16 | 31, 701 << 16 | 32, 701 << 16 | 33, 701 << 16 | 43);
        p.getVarManager().sendVar(3500, (int) TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - GameEngine.SERVER_START_TIME));
        p.getVarManager().sendVar(3501, (int) (p.getVariables().getPlayTime() * 0.6));
        p.getVarManager().sendVar(3506, Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(BonusXpManager.expirationDate - System.currentTimeMillis())));
        p.getVarManager().sendVar(3507, (int) (p.getVariables().getRaidsBoost() * 0.6F));
        p.getVarManager().sendVar(3801, (int) (p.getVariables().getBonusXP() * 0.6F));
        p.getVarManager().sendVar(3804, Constants.BOOSTED_COX ? (int) TimeUnit.MILLISECONDS.toSeconds(BonusCoxManager.expirationDateCox - System.currentTimeMillis()) : 0);
        p.getVarManager().sendVar(3510, (int) (p.getVariables().getTobBoost() * 0.6F));
        p.getVarManager().sendVar(3805, Constants.BOOSTED_TOB ? (int) TimeUnit.MILLISECONDS.toSeconds(BonusTobManager.expirationDateTob - System.currentTimeMillis()) : 0);
        refreshAllCounters();
        p.getPacketDispatcher().sendClientScript(20586,
                162 << 16 | 2,
                10005 << 16 | 11,
                10005 << 16 | 16);
        p.getVariables().synchNoticeboardVars();
        GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin ->
                p.getPacketDispatcher().sendComponentText(
                        plugin.getInterface(),
                        plugin.getComponent("Time"),
                        "Time: " + Colour.WHITE.wrap(GameClock.gameTime())));

        refreshAllCounters();
    }

    public static boolean increasesStaffCount(final Player p) {
        return !isHidden(p) && p.getPrivilege().eligibleTo(Privilege.FORUM_MODERATOR);
    }

    public static void refreshXericsWisdom(@NotNull final Player player) {
        player.getVarManager().sendVar(3806, (int) (player.getVariables().getRaidsBoost() * 0.6F));
    }
    public static  void refreshBonusCox() {
        for (Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3804, Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(BonusCoxManager.expirationDateCox - System.currentTimeMillis())));
        }
    }

    public static  void refreshBonusTob() {
        for (Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3805, Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(BonusTobManager.expirationDateTob - System.currentTimeMillis())));
        }
    }

    public static void refreshBonusXP() {
        final int bonusXPSecondsLeft = Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(
                BonusXpManager.expirationDate - System.currentTimeMillis())
        );
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3805, bonusXPSecondsLeft);
        }
    }

    public static final AtomicInteger staffCount = new AtomicInteger();
    public static final AtomicInteger wildernessCount = new AtomicInteger();

    public static void refreshAllCounters() {
        final int playerCount = World.getPlayers().size();
        final int staffCount = GameNoticeboardInterface.staffCount.get();
        final int wildernessCount = GameNoticeboardInterface.wildernessCount.get();
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3803, playerCount);
            player.getVarManager().sendVar(3804, staffCount);
            //player.getVarManager().sendVar(3508, mobile.intValue());
            player.getVarManager().sendVar(3807, wildernessCount);
        }
    }

    public static void refreshStaffCounters(final int staffCount) {
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3804, staffCount);
        }
    }

    public static void refreshWildernessCounters(final int wildernessCount) {
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3807, wildernessCount);
        }
    }


    public static void refreshVerziksWill(@NotNull final Player player) {
        player.getVarManager().sendVar(3510, (int) (player.getVariables().getTobBoost() * 0.6F));
    }




    @Subscribe
    public static void onLogout(final LogoutEvent event) {
        final Player p = event.getPlayer();
        if (increasesStaffCount(p)) staffCount.getAndDecrement();

        refreshAllCounters();
    }

    private static List<Player> getStaff(final Player requester, final Privilege privilege) {
        final boolean requesterIsStaff = requester.getPrivilege().eligibleTo(Privilege.FORUM_MODERATOR);
        return World.getPlayers()
                .stream()
                .filter(p -> privilege.equals(p.getPrivilege())
                        && (requesterIsStaff || !isHidden(p)))
                .collect(Collectors.toList());
    }

    public static void showStaffOnline(final Player player) {
        final ArrayList<String> lines = new ArrayList<>();
        int count = 0;
        for (final Privilege privilege : STAFF) {
            final List<Player> members = getStaff(player, privilege);
            count += members.size();
            lines.add(privilege.getCrown() + " <col=00080>" + privilege + (privilege == Privilege.SPAWN_ADMINISTRATOR ? "" : "s") + "</col>");
            if (members.isEmpty()) {
                lines.add("- Nobody");
            } else {
                members.forEach(p -> lines.add(p.getName() + (isHidden(p) ? " (" + Colour.MAROON.wrap("Hidden") + ")" : "")));
            }
            lines.add("\n");
        }
        Diary.sendJournal(player, "Staff online: " + count, lines);
    }

    private static boolean isHidden(final Player player) {
        return !SocialManager.PrivateStatus.ALL.equals(player.getSocialManager().getStatus());
    }

    public static void refreshCounters(boolean b) {
    }

    @Override
    protected void attach() {
        put(8, "Players online");
        put(9, "Staff online");
        put(10, "Wilderness players");
        put(11, "Up-time");
        put(12, "Time");
        put(14, "Donation points");
        put(15, "XP rate");
        put(16, "Time played");
        put(17, "Register date");
        put(18, "Privilege");
        put(19, "Game Mode");
        put(20, "Member Rank");
        put(21, "Pharaoh points");
        put(22, "Total donated");
        put(23, "Vote credits");
        put(25, "Game Settings");
        put(27, "Drop Viewer");
        put(29, "Daily Challenges");
        put(32, "Website");
        put(34, "Forums");
        put(36, "Discord");
        put(38, "Store");
        put(39, "Bonus XP");
        put(40, "CoX Boost");
        put(41, "Tob Boost");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Time"), "Time: <col=ffffff>" + GameClock.gameTime());
        // player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("2FA"), "Two-Factor Authentication");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("XP rate"), "XP: <col=ffffff>" + ((player.getSkillingXPRate() == 1) ? "-" : (player.getCombatXPRate() + "x Combat & " + player.getSkillingXPRate() + "x Skilling</col>")));
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Register date"), "Registered on: <col=ffffff>" + FORMATTER + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Privilege"), "Privilege: <col=ffffff>" + player.getPrivilege().getCrown() + player.getPrivilege().toString() + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Game Mode"), "Mode: <col=ffffff>" + player.getGameMode().getCrown() + player.getGameMode().toString() + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Member Rank"), "Member: <col=ffffff>" + player.getMemberRank().getCrown() + player.getMemberRank().toString().replace(" Member", "") + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Pharaoh points"), "Pharaoh points: <col=ffffff>" + player.getPharaohManager().getPharaohPoints() + "</col>");
        final String totalDonated = "Total donated: <col=ffffff>$" + player.getNumericAttribute("total donated online").doubleValue() + "</col>";
        player.log(LogLevel.INFO, totalDonated);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Total donated"), totalDonated);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Donation points"), "Donation points: <col=ffffff>" + player.getDonationManager().getDonationPoints() + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Vote credits"), "Vote credits: <col=ffffff>" +
                player.getNumericAttribute("vote_points").intValue() + "</col>");


        try {
            final long time = player.getNumericAttribute("forum registration date").longValue();
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC);
            final String formatted = FORMATTER.format(zdt);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Register date"), "Registered" +
                    " on: <col=ffffff>" + formatted + "</col>");
            refreshCounters(true);
        } catch (Exception e) {
            logger.error("Failed to send register date", e);
        }
    }
    public static void showDoubleDropsList(final Player p) {
        ArrayList<String> statuses = new ArrayList<String>();
        DoubleDropsManager.DOUBLE_DROPS.forEach((id, doubled) -> {
            statuses.add(id  + " (" + ItemDefinitions.get(id).getName() + ") : " + (doubled ? Colour.RS_GREEN.wrap("doubled") : Colour.RED.wrap("not doubled")));
        });
        Diary.sendJournal(p, "Double drops: " + DoubleDropsManager.DOUBLE_DROPS.size(), statuses);
    }


    public static void updateDropRate(Player player) {

    }

    @Override
    protected void build() {
        bind("Staff online", GameNoticeboardInterface::showStaffOnline);
        bind("Game Settings", player -> {
            GameInterface.GAME_SETTINGS.open(player);
            //Analytics.flagInteraction(player, Analytics.InteractionType.GAME_SETTINGS);
        });
        bind("Drop Viewer", GameInterface.DROP_VIEWER::open);
        bind("Daily Challenges", GameInterface.DAILY_CHALLENGES_OVERVIEW::open);
        bind("Donation points", player -> player.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/"));
        bind("Website", player -> player.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/"));
        bind("Forums", player -> player.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/"));
        bind("Discord", player -> player.getPacketDispatcher().sendURL("https://discord.gg/pharaohrsps"));
        bind("Store", player -> player.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/storev2/"));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GAME_NOTICEBOARD;
    }
}
