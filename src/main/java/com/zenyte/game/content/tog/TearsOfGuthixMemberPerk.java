package com.zenyte.game.content.tog;

import com.zenyte.game.util.TimeUnit;
import com.zenyte.game.world.entity.player.MemberRank;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Chris
 * @since September 13 2020
 */
public enum TearsOfGuthixMemberPerk {
    BRONZE_MEMBER(MemberRank.BRONZE_MEMBER, 5),
    IRON_MEMBER(MemberRank.IRON_MEMBER, 10),
    STEEL_MEMBER(MemberRank.STEEL_MEMBER, 15),
    MITHRIL_MEMBER(MemberRank.MITHRIL_MEMBER, 20),
    ADAMANT_MEMBER(MemberRank.ADAMANT_MEMBER, 25),
    RUNE_MEMBER(MemberRank.RUNE_MEMBER, 30),
    DRAGON_MEMBER(MemberRank.DRAGON_MEMBER, 35);
    private static final List<TearsOfGuthixMemberPerk> PERKS = ObjectLists.unmodifiable(new ObjectArrayList<>(values()));
    /**
     * The member rank a player must have to receive the bonus time.
     */
    private final MemberRank rank;
    /**
     * The amount of extra ticks to append to the Tear Of Guthix timer.
     */
    private final int bonusTime;

    TearsOfGuthixMemberPerk(@NotNull final MemberRank rank, final int bonusSeconds) {
        this.rank = rank;
        this.bonusTime = (int) TimeUnit.SECONDS.toTicks(bonusSeconds);
    }

    /**
     * Returns the extra amount of ticks for the highest member rank a player is eligible for.
     * If player has no member rank then no extra time is .
     */
    public static int ticks(@NotNull final Player player) {
        for (int index = PERKS.size() - 1; index >= 0; index--) {
            final TearsOfGuthixMemberPerk perk = PERKS.get(index);
            if (player.getMemberRank().eligibleTo(perk.rank)) {
                return perk.bonusTime;
            }
        }
        return 0;
    }
}
