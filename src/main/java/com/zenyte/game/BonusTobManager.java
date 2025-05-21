package com.zenyte.game;

import com.zenyte.Constants;
import com.zenyte.game.parser.scheduled.ScheduledExternalizable;
import com.zenyte.game.ui.testinterfaces.GameNoticeboardInterface;
import com.zenyte.game.util.TimeUnit;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Cresinkel
 */
public class BonusTobManager implements ScheduledExternalizable {
    private static final Logger log = LoggerFactory.getLogger(BonusTobManager.class);

    public static long expirationDateCox;
    public static long expirationDateTob;

    public static final void set(final long time) {
        expirationDateTob = time;
        GameNoticeboardInterface.refreshBonusTob();
    }

    public static final void checkIfFlip() {
        if (Constants.BOOSTED_TOB) {
            if (expirationDateTob < System.currentTimeMillis()) {
                Constants.BOOSTED_TOB = false;
                expirationDateTob = 0;
                GameNoticeboardInterface.refreshBonusTob();
                for (final Player player : World.getPlayers()) {
                    player.sendMessage("<col=FF0000><shad=000000>Theatre of Blood is no longer boosted!</col></shad>");
                    player.getVarManager().sendVar(3805, 0);
                }
            }
        } else {
            if (expirationDateTob > 0) {
                Constants.BOOSTED_TOB = true;
                GameNoticeboardInterface.refreshBonusTob();
                final String date = new Date(BonusTobManager.expirationDateTob).toString();
                for (final Player player : World.getPlayers()) {
                    player.sendMessage("<col=00FF00><shad=000000>Theatre of Blood is boosted until " + date + "!</col></shad>");
                    player.getVarManager().sendVar(3805, Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(BonusTobManager.expirationDateTob - System.currentTimeMillis())));
                }
            }
        }
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 5;
    }

    @Override
    public void read(BufferedReader reader) {
        final Calendar expirationDate = World.getGson().fromJson(reader, Calendar.class);
        BonusXpManager.expirationDate = expirationDate.getTimeInMillis();
    }

    @Override
    public void ifFileNotFoundOnRead() {
        write();
    }

    @Override
    public void write() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(expirationDateTob);
        out(getGSON().toJson(calendar));
    }

    @Override
    public String path() {
        return "data/bonustobinfo.json";
    }
}
