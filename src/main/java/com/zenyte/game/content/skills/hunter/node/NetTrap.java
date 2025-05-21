package com.zenyte.game.content.skills.hunter.node;

import com.google.common.base.Preconditions;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.object.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 01/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum NetTrap {
    GREEN(ObjectId.YOUNG_TREE_9341, ObjectId.YOUNG_TREE_9257, TrapType.NET_TRAP_SWAMP_LIZARD),
    ORANGE(ObjectId.YOUNG_TREE_8732, ObjectId.YOUNG_TREE, TrapType.NET_TRAP_ORANGE_SALAMANDER),
    RED(ObjectId.YOUNG_TREE_8990, ObjectId.YOUNG_TREE_8989, TrapType.NET_TRAP_ORANGE_SALAMANDER),
    BLACK(ObjectId.YOUNG_TREE_9000, ObjectId.YOUNG_TREE_8999, TrapType.NET_TRAP_BLACK_SALAMANDER);
    private static final List<NetTrap> values = Collections.unmodifiableList(Arrays.asList(values()));
    private static final Animation treeBendingDownAnimation = new Animation(5266);
    private static final Animation treeRisingUpWithNetAnimation = new Animation(5268);
    private static final Animation treeRisingUpWithoutNetAnimation = new Animation(5270);
    private final int youngTree;
    private final int bentYoungTree;
    private final TrapType type;

    NetTrap(final int youngTree, final int bentYoungTree, final TrapType type) {
        this.youngTree = youngTree;
        this.bentYoungTree = bentYoungTree;
        this.type = type;
    }

    @NotNull
    public static NetTrap findTrap(final int objectId) throws IllegalArgumentException {
        final NetTrap value = Utils.findMatching(values, v -> v.youngTree == objectId || v.bentYoungTree == objectId || v.type.getObjectId() == objectId);
        Preconditions.checkArgument(value != null);
        return value;
    }

    public static List<NetTrap> getValues() {
        return NetTrap.values;
    }

    public static Animation getTreeBendingDownAnimation() {
        return NetTrap.treeBendingDownAnimation;
    }

    public static Animation getTreeRisingUpWithNetAnimation() {
        return NetTrap.treeRisingUpWithNetAnimation;
    }

    public static Animation getTreeRisingUpWithoutNetAnimation() {
        return NetTrap.treeRisingUpWithoutNetAnimation;
    }

    public int getYoungTree() {
        return this.youngTree;
    }

    public int getBentYoungTree() {
        return this.bentYoungTree;
    }

    public TrapType getType() {
        return this.type;
    }
}
