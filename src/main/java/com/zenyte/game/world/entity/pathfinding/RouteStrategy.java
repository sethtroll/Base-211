package com.zenyte.game.world.entity.pathfinding;

/**
 * Class, controlling the exit point of a route.
 */
public abstract class RouteStrategy {
    public static final int BLOCK_FLAG_NORTH = 1;
    public static final int BLOCK_FLAG_EAST = 2;
    public static final int BLOCK_FLAG_SOUTH = 4;
    public static final int BLOCK_FLAG_WEST = 8;
    protected int distance;

    public RouteStrategy(final int distance) {
        this.distance = distance;
    }

    /**
     * Check's if we can interact wall decoration from current position.
     */
    protected static boolean checkWallDecorationInteract(final int[][] clip, final int currentX, final int currentY, final int sizeXY, final int targetX, final int targetY, final int targetType, int targetRotation) {
        if (sizeXY == 1) {
            if (currentX == targetX && currentY == targetY) {
                return true;
            }
            if (targetType == 6 || targetType == 7) {
                if (targetType == 7) {
                    targetRotation = targetRotation + 2 & 3;
                }
                if (targetRotation == 0) {
                    if (currentX == (targetX + 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALL_WEST) == 0) {
                        return true;
                    }
                    return currentX == targetX && currentY == (targetY - 1) && (clip[currentX][currentY] & Flags.WALL_NORTH) == 0;
                } else if (targetRotation == 1) {
                    if (currentX == (targetX - 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALL_EAST) == 0) {
                        return true;
                    }
                    return currentX == targetX && currentY == (targetY - 1) && (clip[currentX][currentY] & Flags.WALL_NORTH) == 0;
                } else if (targetRotation == 2) {
                    if (currentX == (targetX - 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALL_EAST) == 0) {
                        return true;
                    }
                    return currentX == targetX && currentY == (targetY + 1) && (clip[currentX][currentY] & Flags.WALL_SOUTH) == 0;
                } else if (targetRotation == 3) {
                    if (currentX == (targetX + 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALL_WEST) == 0) {
                        return true;
                    }
                    return currentX == targetX && currentY == (targetY + 1) && (clip[currentX][currentY] & Flags.WALL_SOUTH) == 0;
                }
            } else if (targetType == 8) {
                if (currentX == targetX && currentY == (targetY + 1) && (clip[currentX][currentY] & Flags.WALL_SOUTH) == 0) {
                    return true;
                }
                if (currentX == targetX && currentY == (targetY - 1) && (clip[currentX][currentY] & Flags.WALL_NORTH) == 0) {
                    return true;
                }
                if (currentX == (targetX - 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALL_EAST) == 0) {
                    return true;
                }
                return currentX == (targetX + 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALL_WEST) == 0;
            }
        } else {
            final int i_66_ = currentX + sizeXY - 1;
            final int i_67_ = currentY + sizeXY - 1;
            if (targetX >= currentX && targetX <= i_66_ && targetY >= currentY && targetY <= i_67_) {
                return true;
            }
            if (targetType == 6 || targetType == 7) {
                if (targetType == 7) {
                    targetRotation = targetRotation + 2 & 3;
                }
                if (targetRotation == 0) {
                    if (currentX == (targetX + sizeXY) && targetY >= currentY && targetY <= i_67_ && (clip[targetX][targetY] & Flags.WALL_WEST) == 0) {
                        return true;
                    }
                    return currentX >= targetX && targetX <= i_66_ && currentY == (targetY - sizeXY) && (clip[targetX][targetY] & Flags.WALL_NORTH) == 0;
                } else if (targetRotation == 1) {
                    if (currentX == (targetX - sizeXY) && currentY >= targetY && targetY <= i_67_ && (clip[targetX][targetY] & Flags.WALL_EAST) == 0) {
                        return true;
                    }
                    return targetX >= currentX && targetX <= i_66_ && currentY == (targetY - sizeXY) && (clip[targetX][targetY] & Flags.WALL_NORTH) == 0;
                } else if (targetRotation == 2) {
                    if (currentX == (targetX - sizeXY) && targetY >= currentY && targetY <= i_67_ && (clip[targetX][targetY] & Flags.WALL_EAST) == 0) {
                        return true;
                    }
                    return targetX >= currentX && targetX <= i_66_ && currentY == (targetY + sizeXY) && (clip[targetX][targetY] & Flags.WALL_SOUTH) == 0;
                } else if (targetRotation == 3) {
                    if (currentX == (targetX + sizeXY) && currentY >= targetY && targetY <= i_67_ && (clip[targetX][targetY] & Flags.WALL_WEST) == 0) {
                        return true;
                    }
                    return currentX >= targetX && targetX <= i_66_ && currentY == (targetY + sizeXY) && (clip[targetX][targetY] & Flags.WALL_SOUTH) == 0;
                }
            } else if (targetType == 8) {
                if (currentX >= (targetX - sizeXY) && currentX <= (targetX + sizeXY) && currentY == (targetY + sizeXY) && (clip[targetX][targetY] & Flags.WALL_SOUTH) == 0) {
                    return true;
                }
                if (currentX >= (targetX - sizeXY) && currentX <= (targetX + sizeXY) && currentY == (targetY - sizeXY) && (clip[targetX][targetY] & Flags.WALL_NORTH) == 0) {
                    return true;
                }
                if (currentX == (targetX - sizeXY) && currentY >= (targetY - sizeXY) && currentY <= (targetY + sizeXY) && (clip[targetX][targetY] & Flags.WALL_EAST) == 0) {
                    return true;
                }
                return currentX == (targetX + sizeXY) && currentY >= (targetY - sizeXY) && currentY <= (targetY + sizeXY) && (clip[targetX][targetY] & Flags.WALL_WEST) == 0;
            }
        }
        return false;
    }

    /**
     * Check's if we can interact wall object from current position.
     */
    protected static boolean checkWallInteract(final int[][] clips, final int currentX, final int currentY, final int sizeXY, final int targetX, final int targetY, final int targetType, final int targetRotation) {
        // TODO refactor
        if (sizeXY == 1) {
            if (currentX == targetX && currentY == targetY) {
                return true; // we are inside the object
            }
        } else if (targetX >= currentX && targetX <= currentX + sizeXY - 1 && targetY <= targetY + sizeXY - 1) {
            return true; // we are inside the object bounds , though no y check?
        }
        if (sizeXY == 1) {
            if (targetType == 0) {
                if (targetRotation == 0) {
                    if (targetX - 1 == currentX && currentY == targetY) {
                        return true;
                    }
                    if (currentX == targetX && targetY + 1 == currentY && (clips[currentX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (targetX == currentX && currentY == targetY - 1 && (clips[currentX][currentY] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 1) {
                    if (currentX == targetX && targetY + 1 == currentY) {
                        return true;
                    }
                    if (currentX == targetX - 1 && targetY == currentY && (clips[currentX][currentY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX + 1 == currentX && currentY == targetY && (clips[currentX][currentY] & 2883968) == 0) {
                        return true;
                    }
                } else if (targetRotation == 2) {
                    if (targetX + 1 == currentX && currentY == targetY) {
                        return true;
                    }
                    if (targetX == currentX && currentY == targetY + 1 && (clips[currentX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (targetX == currentX && currentY == targetY - 1 && (clips[currentX][currentY] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 3) {
                    if (currentX == targetX && targetY - 1 == currentY) {
                        return true;
                    }
                    if (targetX - 1 == currentX && currentY == targetY && (clips[currentX][currentY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY == currentY && (clips[currentX][currentY] & 2883968) == 0) {
                        return true;
                    }
                }
            }
            if (targetType == 2) {
                if (targetRotation == 0) {
                    if (currentX == targetX - 1 && currentY == targetY) {
                        return true;
                    }
                    if (targetX == currentX && targetY + 1 == currentY) {
                        return true;
                    }
                    if (currentX == targetX + 1 && targetY == currentY && (clips[currentX][currentY] & 2883968) == 0) {
                        return true;
                    }
                    if (targetX == currentX && targetY - 1 == currentY && (clips[currentX][currentY] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 1) {
                    if (targetX - 1 == currentX && targetY == currentY && (clips[currentX][currentY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX == currentX && targetY + 1 == currentY) {
                        return true;
                    }
                    if (targetX + 1 == currentX && currentY == targetY) {
                        return true;
                    }
                    if (targetX == currentX && currentY == targetY - 1 && (clips[currentX][currentY] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 2) {
                    if (targetX - 1 == currentX && currentY == targetY && (clips[currentX][currentY] & 2883848) == 0) {
                        return true;
                    }
                    if (currentX == targetX && currentY == targetY + 1 && (clips[currentX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (currentX == targetX + 1 && targetY == currentY) {
                        return true;
                    }
                    if (currentX == targetX && targetY - 1 == currentY) {
                        return true;
                    }
                } else if (targetRotation == 3) {
                    if (targetX - 1 == currentX && currentY == targetY) {
                        return true;
                    }
                    if (targetX == currentX && targetY + 1 == currentY && (clips[currentX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (currentX == targetX + 1 && targetY == currentY && (clips[currentX][currentY] & 2883968) == 0) {
                        return true;
                    }
                    if (currentX == targetX && targetY - 1 == currentY) {
                        return true;
                    }
                }
            }
            if (targetType == 9) {
                if (targetX == currentX && targetY + 1 == currentY && (clips[currentX][currentY] & 32) == 0) {
                    return true;
                }
                if (currentX == targetX && targetY - 1 == currentY && (clips[currentX][currentY] & 2) == 0) {
                    return true;
                }
                if (currentX == targetX - 1 && currentY == targetY && (clips[currentX][currentY] & 8) == 0) {
                    return true;
                }
                return currentX == targetX + 1 && currentY == targetY && (clips[currentX][currentY] & 128) == 0;
            }
        } else {
            final int i_66_ = currentX + sizeXY - 1;
            final int i_67_ = sizeXY + currentY - 1;
            if (targetType == 0) {
                if (targetRotation == 0) {
                    if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1 && (clips[targetX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY && (clips[targetX][i_67_] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 1) {
                    if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY) {
                        return true;
                    }
                    if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 2883968) == 0) {
                        return true;
                    }
                } else if (targetRotation == 2) {
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1 && (clips[targetX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY && (clips[targetX][i_67_] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 3) {
                    if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY) {
                        return true;
                    }
                    if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 2883968) == 0) {
                        return true;
                    }
                }
            }
            if (targetType == 2) {
                if (targetRotation == 0) {
                    if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 2883968) == 0) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY && (clips[targetX][i_67_] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 1) {
                    if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY && (clips[targetX][i_67_] & 2883842) == 0) {
                        return true;
                    }
                } else if (targetRotation == 2) {
                    if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 2883848) == 0) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY && (clips[targetX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY) {
                        return true;
                    }
                } else if (targetRotation == 3) {
                    if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1 && (clips[targetX][currentY] & 2883872) == 0) {
                        return true;
                    }
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 2883968) == 0) {
                        return true;
                    }
                    if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY) {
                        return true;
                    }
                }
            }
            if (targetType == 9) {
                if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY && (clips[targetX][currentY] & 2883872) == 0) {
                    return true;
                }
                if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY && (clips[targetX][i_67_] & 2883842) == 0) {
                    return true;
                }
                if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 2883848) == 0) {
                    return true;
                }
                return currentX == targetX + 1 && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 2883968) == 0;
            }
        }
        return false;
    }

    /**
     * Check's if we can interact filled rectangular (Might be ground object or npc or player etc) from current position.
     */
    protected static boolean checkFilledRectangularInteract(final int[][] clip, final int currentX, final int currentY, final int sizeX, final int sizeY, final int targetX, final int targetY, final int targetSizeX, final int targetSizeY, final int accessBlockFlag) {
        // TODO refactor
        final int srcEndX = currentX + sizeX;
        final int srcEndY = currentY + sizeY;
        final int destEndX = targetX + targetSizeX;
        final int destEndY = targetY + targetSizeY;
        if (destEndX == currentX && (accessBlockFlag & 2) == 0) {
            // can we enter from east ?
            int i_12_ = currentY > targetY ? currentY : targetY;
            for (final int i_13_ = srcEndY < destEndY ? srcEndY : destEndY; i_12_ < i_13_; i_12_++) {
                if (((clip[destEndX - 1][i_12_]) & 8) == 0) {
                    return true;
                }
            }
        } else if (srcEndX == targetX && (accessBlockFlag & 8) == 0) {
            // can we enter from west ?
            int i_14_ = currentY > targetY ? currentY : targetY;
            for (final int i_15_ = srcEndY < destEndY ? srcEndY : destEndY; i_14_ < i_15_; i_14_++) {
                if (((clip[targetX][i_14_]) & 128) == 0) {
                    return true;
                }
            }
        } else if (currentY == destEndY && (accessBlockFlag & 1) == 0) {
            // can we enter from north?
            int i_16_ = currentX > targetX ? currentX : targetX;
            for (final int i_17_ = srcEndX < destEndX ? srcEndX : destEndX; i_16_ < i_17_; i_16_++) {
                if (((clip[i_16_][destEndY - 1]) & 2) == 0) {
                    return true;
                }
            }
        } else if (targetY == srcEndY && (accessBlockFlag & 4) == 0) {
            // can we enter from south?
            int i_18_ = currentX > targetX ? currentX : targetX;
            for (final int i_19_ = srcEndX < destEndX ? srcEndX : destEndX; i_18_ < i_19_; i_18_++) {
                if (((clip[i_18_][targetY]) & 32) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Whether we can exit at specific x and y.
     */
    public abstract boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY);

    /**
     * Get's approximated destination position X.
     */
    public abstract int getApproxDestinationX();

    /**
     * Get's approximated destination position Y.
     */
    public abstract int getApproxDestinationY();

    /**
     * Get's approximated destination size X.
     */
    public abstract int getApproxDestinationSizeX();

    /**
     * Get's approximated destination size Y.
     */
    public abstract int getApproxDestinationSizeY();

    /**
     * Whether this strategy equals to other object.
     */
    @Override
    public abstract boolean equals(Object other);

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(final int distance) {
        this.distance = distance;
    }
}
