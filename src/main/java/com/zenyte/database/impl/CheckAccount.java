package com.zenyte.database.impl;

import com.zenyte.database.DatabaseCredential;
import com.zenyte.database.DatabasePool;
import com.zenyte.database.SQLRunnable;
import com.zenyte.game.util.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Noele | Jun 19, 2018 : 12:14:15 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public class CheckAccount extends SQLRunnable {
    private static final Logger log = LoggerFactory.getLogger(CheckAccount.class);
    /*
     * args[0] - username string
     * args[1] - password string
     */
    private final Object[] args;

    public CheckAccount(Object... args) {
        this.args = args;
    }

    @Override
    public void execute(final DatabaseCredential auth) {
        if (!(args[0] instanceof String) || !(args[1] instanceof String)) return;
        final String user = args[0].toString();
        final String pass = args[1].toString();
        try (
                Connection con = DatabasePool.getConnection(auth, "zenyte_forum");
                PreparedStatement load = con.prepareStatement("SELECT * FROM core_members WHERE name = ?")) {
            load.setString(1, user);
            final ResultSet set = load.executeQuery();
            String hash = null;
            String salt = null;
            while (set.next()) {
                hash = set.getString(set.findColumn("members_pass_hash"));
                salt = set.getString(set.findColumn("members_pass_salt"));
            }
            if (hash == null || salt == null) {
            }
            /* something went wrong! */
            if (BCrypt.hashpw(pass, salt).equals(hash)) {
            }
        } catch (final  /* this should mean the player can login! */ Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void prepare() {
        DatabasePool.submit(this);
    }
}
