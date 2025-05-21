package com.zenyte.tools;

import com.zenyte.Constants;
import com.zenyte.game.util.Utils;
import mgi.Indice;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author Kris | 5. march 2018 : 17:38.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ItemExtractor implements Extractor {
    private static final Logger log = LoggerFactory.getLogger(ItemExtractor.class);

    @Override
    public void extract() {
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + Constants.REVISION + " item-defs.txt")));
            //final int len = CacheUtil.getStore().getFilesSystem(2).findFolderByID(10).filesCount();//Cache.STORE.getIndexes()[2].getLastFileId(10);
            final int len = Utils.getIndiceSize(Indice.ITEM_DEFINITIONS);
            for (int i = 0; i < len; i++) {
                final ItemDefinitions defs = ItemDefinitions.get(i);
                if (defs == null) {
                    continue;
                }
                if (defs.getName().equals("null")) {
                    continue;
                }
                writer.write(i + ": " + defs.getName() + (defs.isNoted() ? "(noted)" : ""));
                if (i < len) {
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }
}
