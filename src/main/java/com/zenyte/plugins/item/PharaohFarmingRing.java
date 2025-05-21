package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.OptionsMenuD;

import static com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport.*;

/**
 * @author Tommeh | 29-1-2019 | 17:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PharaohFarmingRing extends ItemPlugin {

    private static final ItemTeleport[] TELEPORTS = {Canifis, Weiss, Trollheim, Farming_Guild, Catherby, Ardougne, Hosidius, Harmony_Island, Falador_patch};



    static void sendPOHPortalTeleports(final Player player) {
        player.getDialogueManager().start(new OptionsMenuD(player, "House portal teleports") {

            @Override
            public void handleClick(int slotId) {

            }

            @Override
            public boolean cancelOption() {
                return false;
            }
        });
    }

    @Override
    public void handle() {

                bind("Rub", (player, item, slotId) -> {
                    player.getDialogueManager().start(new OptionsMenuD(player, "Select a destination", "Canifis", "Weiss", "Trollheim", "Farming Guild", "Catherby", "Ardougne", "Hosidius", "Harmony Island", "Falador patch") {
                        @Override
                        public void handleClick(int slotId) {
                            if (slotId <= 4) {
                                final ItemTeleport teleport = TELEPORTS[slotId];
                                teleport(player, teleport);
                            } else if (slotId == 5) {
                                final ItemTeleport teleport = TELEPORTS[slotId];
                                teleport(player, teleport);
                            } else if (slotId == 6) {
                                final ItemTeleport teleport = TELEPORTS[slotId];
                                teleport(player, teleport);
                            } else if (slotId == 7) {
                            final ItemTeleport teleport = TELEPORTS[slotId];
                            teleport(player, teleport);
                        } else if (slotId == 8) {
                            final ItemTeleport teleport = TELEPORTS[slotId];
                            teleport(player, teleport);
                        }
                        }


                        @Override
                        public boolean cancelOption() {
                            return false;
                        }
                    });
                });
            }


            private void teleport(final Player player, final Teleport teleport) {
                teleport.teleport(player);
            }



            private enum POHPortal implements Teleport {
                HOME(null),
                RIMMINGTON(new Location(2954, 3224, 0)),
                TAVERLEY(new Location(2894, 3465, 0)),
                POLLNIVNEACH(new Location(3340, 3004, 0)),
                HOSIDIUS(new Location(1743, 3517, 0)),
                RELLEKKA(new Location(2670, 3632, 0)),
                BRIMHAVEN(new Location(2758, 3178, 0)),
                YANILLE(new Location(2544, 3095, 0));

                //private static final MaxCape.POHPortal[] values = values();
                //private static final Map<String, MaxCape.POHPortal> map = new Object2ObjectLinkedOpenHashMap<>();


                private final String title;
                private final Location destination;

                POHPortal(final Location destination) {
                    this.destination = destination;
                    this.title = Utils.formatString(name());
                }

                @Override
                public TeleportType getType() {
                    return TeleportType.REGULAR_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return destination;
                }

                @Override
                public int getLevel() {
                    return 0;
                }

                @Override
                public double getExperience() {
                    return 0;
                }

                @Override
                public int getRandomizationDistance() {
                    return 2;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return WILDERNESS_LEVEL;
                }

                @Override
                public boolean isCombatRestricted() {
                    return false;
                }
            }
    @Override
    public int[] getItems() {
        return new int[] { ItemId.PHARAOH_FARMING_RING };
    }
}
