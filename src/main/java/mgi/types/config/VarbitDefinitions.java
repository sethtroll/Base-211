package mgi.types.config;

import com.zenyte.Game;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

/**
 * @author Kris | 6. apr 2018 : 21:40.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class VarbitDefinitions implements Definitions {
    public static VarbitDefinitions[] definitions;
    private static final Int2IntMap varbit2varpmap = new Int2IntOpenHashMap();

    @Override
    public void load() {
        final Cache cache = Game.getCacheMgi();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group varbits = configs.findGroupByID(GroupType.VARBIT);
        definitions = new VarbitDefinitions[varbits.getHighestFileId()];
        for (int id = 0; id < varbits.getHighestFileId(); id++) {
            final File file = varbits.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions[id] = new VarbitDefinitions(id, buffer);
            varbit2varpmap.put(id, definitions[id].getBaseVar());
        }
    }

    private final int id;
    private int baseVar;
    private int startBit;
    private int endBit;

    public VarbitDefinitions(final int baseVar, final ByteBuffer buffer) {
        this.id = baseVar;
        decode(buffer);
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            decode(buffer, opcode);
        }
    }

    @Override
    public void decode(final ByteBuffer buffer, final int opcode) {
        if (opcode == 1) {
            baseVar = buffer.readUnsignedShort();
            startBit = buffer.readUnsignedByte();
            endBit = buffer.readUnsignedByte();
        }
    }

    @Override
    public void pack() {
        final Archive archive = Game.getCacheMgi().getArchive(ArchiveType.CONFIGS);
        final Group varbits = archive.findGroupByID(GroupType.VARBIT);
        varbits.addFile(new File(id, encode()));
        //Game.getLibrary().getIndex(ArchiveType.CONFIGS.getId()).getArchive(GroupType.VARBIT.getId()).addFile(id, encode().array());
    }

    public static VarbitDefinitions get(final int id) {
        if (id < 0 || id >= definitions.length) {
            return null;
        }
        return definitions[id];
    }

    public static OptionalInt findVarp(final int varbit) {
        final int varpId = varbit2varpmap.getOrDefault(varbit, -1);
        return varpId == -1 ? OptionalInt.empty() : OptionalInt.of(varpId);
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(6);
        buffer.writeByte(1);
        buffer.writeShort(baseVar);
        buffer.writeByte(startBit);
        buffer.writeByte(endBit);
        buffer.writeByte(0);
        return buffer;
    }

    public VarbitDefinitions() {
        this.id = 0;
    }

    @NotNull
    @Override
    public String toString() {
        return "VarbitDefinitions(id=" + this.getId() + ", baseVar=" + this.getBaseVar() + ", startBit=" + this.getStartBit() + ", endBit=" + this.getEndBit() + ")";
    }

    public int getId() {
        return this.id;
    }

    public int getBaseVar() {
        return this.baseVar;
    }

    public int getStartBit() {
        return this.startBit;
    }

    public int getEndBit() {
        return this.endBit;
    }

    public void setBaseVar(final int baseVar) {
        this.baseVar = baseVar;
    }

    public void setStartBit(final int startBit) {
        this.startBit = startBit;
    }

    public void setEndBit(final int endBit) {
        this.endBit = endBit;
    }
}
