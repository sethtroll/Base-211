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
public class BonusCoxManager implements ScheduledExternalizable {
    private static final Logger log = LoggerFactory.getLogger(BonusCoxManager.class);
    public static long expirationDateCox;

    public static final void set(final long time) {
        expirationDateCox = time;
        GameNoticeboardInterface.refreshBonusCox();
    }

    public static final void checkIfFlip() {
        if (Constants.BOOSTED_COX) {
            if (expirationDateCox < System.currentTimeMillis()) {
                Constants.BOOSTED_COX = false;
                expirationDateCox = 0;
                GameNoticeboardInterface.refreshBonusCox();
                for (final Player player : World.getPlayers()) {
                    player.sendMessage("<col=FF0000><shad=000000>Chambers of Xeric is no longer boosted!</col></shad>");
                    player.getVarManager().sendVar(3804, 0);
                }
            }
        } else {
            if (expirationDateCox > 0) {
                Constants.BOOSTED_COX = true;
                GameNoticeboardInterface.refreshBonusCox();
                final String date = new Date(BonusTobManager.expirationDateCox).toString();
                for (final Player player : World.getPlayers()) {
                    player.sendMessage("<col=00FF00><shad=000000>Chambers of Xeric is boosted until " + date + "!</col></shad>");
                    player.getVarManager().sendVar(3804, Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(BonusCoxManager.expirationDateCox - System.currentTimeMillis())));
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
        calendar.setTimeInMillis(expirationDateCox);
        out(getGSON().toJson(calendar));
    }

    @Override
    public String path() {
        return "data/bonuscoxinfo.json";
    }
}
