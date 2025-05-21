package com.zenyte.game.world.entity.player;

import com.zenyte.api.model.Role;
import com.zenyte.game.util.Utils;
import com.zenyte.utils.Ordinal;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 5-4-2019 | 16:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@Ordinal
public enum MemberRank {

    NONE(31, null, "000000", -1),
    BRONZE_MEMBER(44, Role.SAPPHIRE_MEMBER, "2188ff", 200),  //10 min
    IRON_MEMBER(45, Role.EMERALD_MEMBER, "34d058", 100),  //8 min
    STEEL_MEMBER(46, Role.RUBY_MEMBER, "ea4a5a", 75),  // 6 min
    MITHRIL_MEMBER(47, Role.DIAMOND_MEMBER, "e1e4e8", 50),  // 4 min
    ADAMANT_MEMBER(48, Role.DRAGONSTONE_MEMBER, "9673d7", 25),  //2 min
    RUNE_MEMBER(49, Role.ONYX_MEMBER, "46505c", 8),  //1 min
    DRAGON_MEMBER(50, Role.ZENYTE_MEMBER, "f3ae59", 0);
    private static final Set<MemberRank> ALL = EnumSet.allOf(MemberRank.class);
    private static final Map<Integer, MemberRank> RANKS = new HashMap<>();

    static {
        for (final MemberRank rank : ALL) {
            RANKS.put(rank.ordinal(), rank);
        }
    }

    //30 sec
    private final int icon;
    private final Role apiRole;
    private final String yellColor;
    private final int yellDelay;

    MemberRank(final int icon, final Role apiRole, final String yellColor, final int yellDelay) {
        this.icon = icon;
        this.apiRole = apiRole;
        this.yellColor = yellColor;
        this.yellDelay = yellDelay;
    }

    public static MemberRank get(final int index) {
        return RANKS.get(index);
    }
    public boolean eligibleTo(final Privilege p) {
        return ordinal() >= p.ordinal();
    }
    public String getCrown() {
        return this.equals(NONE) ? "" : "<img=" + icon + ">";
    }

    public boolean eligibleTo(final MemberRank member) {
        return ordinal() >= member.ordinal();
    }

    @Override
    public String toString() {
        return Utils.formatString(name().toLowerCase().replace("_", " "));
    }

    public int getIcon() {
        return this.icon;
    }

    public Role getApiRole() {
        return this.apiRole;
    }

    public String getYellColor() {
        return this.yellColor;
    }

    public int getYellDelay() {
        return this.yellDelay;
    }

}
