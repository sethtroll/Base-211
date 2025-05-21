package com.zenyte.game.world.entity.player;

import com.google.common.collect.ObjectArrays;
import com.zenyte.Constants;
import com.zenyte.api.client.query.DiscordVerificationPost;
import com.zenyte.api.client.webhook.GlobalBroadcastWebhook;
import com.zenyte.cores.CoresManager;
import com.zenyte.database.impl.Store;
import com.zenyte.database.impl.Vote;
import com.zenyte.game.BonusXpManager;
import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.Book;
import com.zenyte.game.content.achievementdiary.AchievementDiaries;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.dialogue.RaidFloorOverviewD;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.grandexchange.GrandExchangePriceManager;
import com.zenyte.game.content.minigame.barrows.Barrows;
import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.InfernoWave;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk;
import com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities;
import com.zenyte.game.content.minigame.wintertodt.Wintertodt;
import com.zenyte.game.content.partyroom.BirthdayEventRewardList;
import com.zenyte.game.content.partyroom.PartyRoomVariables;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.actions.Teleother;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.content.skills.slayer.*;
import com.zenyte.game.content.tog.juna.JunaEnterDialogue;
import com.zenyte.game.content.tournament.Tournament;
import com.zenyte.game.content.tournament.plugins.TournamentLobby;
import com.zenyte.game.content.tournament.preset.TournamentPreset;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.degradableitems.DegradableItem;
import com.zenyte.game.item.enums.RareDrop;
import com.zenyte.game.packet.out.Heatmap;
import com.zenyte.game.packet.out.PingStatisticsRequest;
import com.zenyte.game.tasks.WorldTasksManager;
import com.zenyte.game.ui.InterfaceHandler;
import com.zenyte.game.ui.InterfacePosition;
import com.zenyte.game.util.*;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.TriviaBroadcasts;
import com.zenyte.game.world.entity.AnimationMap;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.login.InvitedPlayersList;
import com.zenyte.game.world.entity.player.perk.Perk;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.entity.player.punishments.Punishment;
import com.zenyte.game.world.entity.player.punishments.PunishmentManager;
import com.zenyte.game.world.entity.player.punishments.PunishmentType;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.Area;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.EvilBobIsland;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.dialogue.CountDialogue;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.item.DiceItem;
import com.zenyte.tools.AnimationExtractor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.Indice;
import mgi.types.component.ComponentDefinitions;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.zenyte.game.world.entity.player.Emote.GIVE_THANKS_VARP;
import static com.zenyte.game.world.entity.player.MessageType.GLOBAL_BROADCAST;

/**
 * @author Tom
 */
public final class GameCommands {
    public static boolean event_started = false;
    public static boolean nex_started = false;
    private static final Logger log = LoggerFactory.getLogger(GameCommands.class);
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        new Command(Privilege.ADMINISTRATOR, "ugm", "Opens the upgrade interface.", (p, args) -> GameInterface.UPGRADE_INTERFACE.open(p));
        new Command(Privilege.ADMINISTRATOR, "test", TempCommand::run);
        new Command(Privilege.ADMINISTRATOR, "addspins", (p, args) -> {
            p.getWheelOfFortune().setSpins(p.getWheelOfFortune().getSpins() + Integer.parseInt(args[0]));
        });
        new Command(Privilege.GLOBAL_MODERATOR, "addbroadcast", (p, args) -> {
            final int id = Integer.parseInt(args[0]);
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options("Add " + ItemDefinitions.getOrThrow(id).getName() + " to broadcasts?", new DialogueOption("Yes", () -> {
                        RareDrop.add(id);
                        p.sendMessage("Added " + ItemDefinitions.getOrThrow(id).getName() + " to custom broadcasts.");
                    }), new DialogueOption("No"));
                }
            });
        });
        new Command(Privilege.GLOBAL_MODERATOR, "removebroadcast", (p, args) -> {
            final IntArrayList broadcasts = RareDrop.getDynamicIds();
            if (broadcasts.isEmpty()) {
                p.sendMessage("No dynamic broadcasts present.");
                return;
            }
            final ObjectArrayList<String> options = new ObjectArrayList<>();
            for (final Integer bc : broadcasts) {
                options.add(ItemDefinitions.getOrThrow(bc).getName());
            }
            options.add("All broadcasts");
            p.getDialogueManager().start(new OptionsMenuD(p, "Select broadcast to remove", options.toArray(new String[0])) {
                @Override
                public void handleClick(int slotId) {
                    if (slotId >= broadcasts.size()) {
                        for (final Integer bc : broadcasts) {
                            RareDrop.remove(bc);
                        }
                        p.sendMessage("Wiped custom broadcasts.");
                        return;
                    }
                    final int id = broadcasts.getInt(slotId);
                    RareDrop.remove(id);
                    p.sendMessage("Removed " + ItemDefinitions.getOrThrow(id).getName() + " from broadcasts.");
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.PLAYER, "item",
                "(Only works in beta world) Spawns an item in your inventory. If undefined, amount is set to 1 and charges are set to the default of said item. Arguments: id "
                        +
                        "<Optional>amount <Optional>charges", (p, args) -> {
            if(!Constants.WORLD_PROFILE.isBeta()) {
                if(!p.getPrivilege().eligibleTo(Privilege.SPAWN_ADMINISTRATOR)) {
                    p.sendMessage("This command does not work outside of beta worlds.");
                    return;
                }
            }

            final int itemId = Integer.parseInt(args[0]);
            final int amount = args.length > 1 ? Integer.parseInt(args[1]) : 1;
            final int charges = args.length > 2 ? Integer.parseInt(args[2])
                    : Math.max(0, DegradableItem.getDefaultCharges(itemId, 0));
            p.getInventory().addItem(itemId, amount, charges);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "skotaltar", (p, args) -> {
            p.setLocation(new Location(1665, 10048, 0));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "skotroom", (p, args) -> {
            p.setLocation(new Location(1693, 9886, 0));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "dmgzuk", (p, args) -> {
            final Area area = p.getArea();
            if (area instanceof Inferno inferno) {
                final TzKalZuk zuk = inferno.getNPCs(TzKalZuk.class).get(0);
                zuk.applyHit(new Hit(p, Integer.parseInt(args[0]), HitType.REGULAR));
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "skipwave", (p, args) -> {
            if (!p.inArea(p.getName() + "'s Inferno Instance")) {
                p.sendMessage("You must be in the Inferno to do this.");
                return;
            }
            final Inferno inferno = (Inferno) p.getArea();
            final InfernoWave wave = InfernoWave.get(Integer.parseInt(args[0]));
            p.sendMessage("Skipped to wave " + wave.getWave() + ".");
            inferno.skip(wave);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "inferno", (p, args) -> {
            p.setLocation(new Location(2496, 5115, 0));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "bonusxp", (p, args) -> p.sendInputString("Enter bonus xp expiration date: (format: YYYY/MM/DD/HH)", value -> {
            final String[] split = value.split("/");
            if (split.length != 4) {
                p.sendMessage("Invalid format.");
                return;
            }
            final Calendar instance = Calendar.getInstance();
            instance.set(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]), Integer.parseInt(split[3]), 0, 0);
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options("Set bonus experience expiration date to <br>" + instance.getTime() + "?", new DialogueOption("Yes.", () -> BonusXpManager.set(instance.getTimeInMillis())), new DialogueOption("No."));
                }
            });
        }));
        new Command(Privilege.ADMINISTRATOR, "objs", (p, args) -> {
            final WorldObject[] objects = World.getRegion(p.getLocation().getRegionId()).getObjects(p.getPlane(), p.getX() & 63, p.getY() & 63);
            if (objects == null) {
                p.sendMessage("No objects detected on this location.");
                return;
            }
            for (final WorldObject object : objects) {
                if (object == null) {
                    continue;
                }
                p.sendMessage("Object: " + object.getId() + ", type: " + object.getType() + ", rotation: " + object.getRotation() + ", location: " + object.getX() + ", " + object.getY() + ", " + object.getPlane() + ".");
            }
        });
        new Command(Privilege.ADMINISTRATOR, "gc", (p, args) -> {
            if (!Constants.WORLD_PROFILE.isDevelopment()) {
                return;
            }
            System.gc();
        });
        new Command(Privilege.ADMINISTRATOR, "memory", (p, args) -> {
            final Runtime runtime = Runtime.getRuntime();
            final long totalMem = runtime.totalMemory();
            final long freeMem = runtime.freeMemory();
            final long maxMem = runtime.maxMemory();
            p.sendMessage("Memory specifications: ");
            p.sendMessage("Free memory: " + Utils.format(freeMem));
            p.sendMessage("Used memory: " + Utils.format(totalMem - freeMem));
            p.sendMessage("Total memory: " + Utils.format(totalMem));
            p.sendMessage("Max memory: " + Utils.format(maxMem));
        });
        new Command(Privilege.ADMINISTRATOR, "disablehydra", (p, args) -> {
            Constants.ALCHEMICAL_HYDRA = !Constants.ALCHEMICAL_HYDRA;
            p.sendMessage("Alchemical Hydra: " + Constants.ALCHEMICAL_HYDRA);
        });
        new Command(Privilege.ADMINISTRATOR, "disableraids", (p, args) -> {
            Constants.CHAMBERS_OF_XERIC = !Constants.CHAMBERS_OF_XERIC;
            p.sendMessage("Chambers of Xeric: " + Constants.CHAMBERS_OF_XERIC);
        });
        new Command(Privilege.SUPPORT, "timeout", "Toggles staff logout-timer for current session.", (p, args) -> {
            p.getTemporaryAttributes().put("staff timeout disabled", p.getNumericTemporaryAttribute("staff timeout disabled").intValue() == 1 ? 0 : 1);
            p.sendMessage("Timeout setting: " + (p.getNumericTemporaryAttribute("staff timeout disabled").intValue() == 1 ? "enabled" : "disabled") + ".");
        });
        new Command(Privilege.MODERATOR, "bosstimers", "Opens boss spawn timer menu.", (p, args) -> BossRespawnTimer.open(p));
        new Command(Privilege.ADMINISTRATOR, "disablediscordbroadcast", "Toggles discord broadcasts.", (p, args) -> {
            GlobalBroadcastWebhook.setDisabled(!GlobalBroadcastWebhook.isDisabled());
            p.sendMessage("Discord broadcasts disabled: " + GlobalBroadcastWebhook.isDisabled());
        });

        new Command (Privilege.MODERATOR, "clearnex", "Reset nex scrolls", (p, args) -> {
            nex_started = false;

        });
        new Command(Privilege.ADMINISTRATOR, "disablewintertodt", "Toggles access to the Wintertodt's prison.", (p, args) -> {
            Wintertodt.setDisabled(!Wintertodt.isDisabled());
            p.sendMessage("Wintertodt disabled: " + Wintertodt.isDisabled());
        });
        new Command(Privilege.PLAYER, new String[]{"gambling", "gamble", "dice", "dicing", "fp", "flowerpoker", "flower"}, (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            p.getDialogueManager().start(new PlainChat(p, DiceItem.GAMBLE_WARNING));
        });
        new Command(Privilege.ADMINISTRATOR, "tempattr", (p, args) -> p.sendInputString("Enter name of the temporary attribute", key -> {
            p.getDialogueManager().finish();
            p.sendInputInt("Enter value of the temporary attribute", value -> {
                p.getTemporaryAttributes().put(key, value);
                p.sendMessage("Temporary attribute " + Colour.RS_RED.wrap(key) + " value set to " + Colour.RS_RED.wrap(Integer.toString(value)));
            });
        }));
        new Command(Privilege.ADMINISTRATOR, "forceprice", "Initiates forcing a price change for an item.", (p, args) -> {
            p.sendInputItem("What item's price would you like to change?", item -> p.sendInputInt("What price would you like to set to " + item.getName() + "?", value -> {
                final int existingPrice = ItemDefinitions.getSellPrice(item.getId());
                GrandExchangePriceManager.forcePrice(item.getId(), value);
                p.sendMessage("Price of " + item.getName() + " changed from " + Utils.format(existingPrice) + " to " + Utils.format(value) + ".");
            }));
        });
        new Command(Privilege.ADMINISTRATOR, "invite", "Grant beta access to a user. Usage: ::invite player_name", (p, args) -> {
            final String name = Utils.formatUsername(StringUtilities.compile(args, 0, args.length, '_'));
            if (name.length() >= 1 && name.length() <= 12) {
                InvitedPlayersList.invitedPlayers.add(name);
                p.sendMessage("Granted beta access to " + name + ".");
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "gg", (p, args) -> {
            p.setLocation(GrotesqueGuardiansInstance.OUTSIDE_LOCATION);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "gargtask", (p, args) -> {
            p.getSlayer().setAssignment(p.getSlayer().getAssignment(RegularTask.GARGOYLES, SlayerMaster.NIEVE));
            p.getSlayer().sendTaskInformation();
        });
        new Command(Privilege.ADMINISTRATOR, "uninvite", "Revoke beta access of a user. Usage: ::uninvite player_name", (p, args) -> {
            final String name = Utils.formatUsername(StringUtilities.compile(args, 0, args.length, '_'));
            if (name.length() >= 1 && name.length() <= 12) {
                if (ArrayUtils.contains(Constants.owners, name)) {
                    p.sendMessage("You cannot uninvite owners.");
                    return;
                }
                InvitedPlayersList.invitedPlayers.remove(name);
                p.sendMessage("Revoked beta access from " + name + ".");
            }
        });

        new Command(Privilege.PLAYER, "donorzone", "dz", (p, args) -> {
            if(p.isLocked()) {
                return;
            }
            if (!p.getMemberRank().eligibleTo(MemberRank.BRONZE_MEMBER)) {
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.ZENYTE_PORTAL_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(1909, 4269, 0);
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
                    return 3;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return p.getPrivilege().eligibleTo(Privilege.ADMINISTRATOR) ? 100 : 20;
                }

                @Override
                public boolean isCombatRestricted() {
                    return false;
                }
            };
            teleport.teleport(p);
        });


        new Command(Privilege.PLAYER, "99construction", "Sets your Construction level to 99.", (p, args) -> {
            if (p.getNumericAttribute("first_99_skill").intValue() == -1) {
                p.addAttribute("first_99_skill", 0);
            }
            for (int i = 22; i < 23; i++) {
                p.getSkills().setSkill(i, 99, 13034431);
            }
            p.getAppearance().resetRenderAnimation();
        });

        new Command(Privilege.PLAYER, new String[] {"ans", "answer", "trivia"},
                "Use this command to provide an answer to an active trivia question.", (p, args) -> {
            if(!TriviaBroadcasts.getCurrentTriviaQuestion().equals("")) {
                String answer = StringUtilities.compile(args, 0, args.length, ' ');
                if(args.length == 0) {
                    p.sendMessage("You have to give an answer!");
                    return;
                }
                if(TriviaBroadcasts.isCorrectAnswer(answer)) {
                    switch(TriviaBroadcasts.getTriviaWinners().size()) {
                        case 0:
                            TriviaBroadcasts.getTriviaWinners().add(p.getUsername());
                            p.sendMessage("You have answered correctly!");
                            break;
                        case 1:
                        case 2:
                        case 3:
                            if(TriviaBroadcasts.getTriviaWinners().contains(p.getUsername())) {
                                p.sendMessage("You can't win twice!");
                                return;
                            }
                            for(int i = 0; i < TriviaBroadcasts.getTriviaWinners().size(); i++) {
                                if(!World.getPlayer(TriviaBroadcasts.getTriviaWinners().get(i)).isPresent()) {
                                    continue;
                                }
                                if(p.getIP().equals(
                                        World.getPlayer(TriviaBroadcasts.getTriviaWinners().get(i)).get().getIP())) {
                                    p.sendMessage("Only one submission per IP address!");
                                    return;
                                }
                            }

                            p.sendMessage("You have answered correctly!");
                            TriviaBroadcasts.getTriviaWinners().add(p.getUsername());
                            if(TriviaBroadcasts.getTriviaWinners().size() == 4) {
                                TriviaBroadcasts.announceWinners();
                                TriviaBroadcasts.payWinners();
                                TriviaBroadcasts.reset();
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    p.sendMessage(answer + " is not a correct answer, try again.");
                }
            } else {
                p.sendMessage("There isn't a currently active trivia question.");
            }
        });
//
//        new Command(Privilege.PLAYER, "claim", (c, args) -> {
//            new Thread(() -> {
//                try {
//                    Donation[] donations = Donation.donations("bvhAnkghmrx9J7qIWrKY4LFqP6pcM11qZeJgEP9Vtgas9zvMDYxOong0D7v0rq4tlhBhp2fY",
//                            c.getUsername());
//                    if (donations.length == 0) {
//                        c.sendMessage("You currently don't have any items waiting. You must donate first!");
//                        return;
//                    }
//                    if (donations[0].message != null) {
//                        c.sendMessage(donations[0].message);
//                        return;
//                    }
//                    for (Donation donate: donations) {
//                        c.getInventory().addItem(donate.product_id, donate.product_amount);
//                    }
//                    c.sendMessage("Thank you for donating!");
//                } catch (Exception e) {
//                    c.sendMessage("Api Services are currently offline. Please check back shortly");
//                    e.printStackTrace();
//                }
//            }).start();
//        });

        new Command(Privilege.PLAYER, "claim", (c, args) -> {
            new Thread(new Store(c)).start();
        });

//        new Command(Privilege.PLAYER, "voted", (c, args) -> {
//            new Thread(new Vote(c)).start();
//        });

        new Command(Privilege.PLAYER, "voted","reward 1", (c, args) -> {
            final String playerName = c.getUsername().replace('_', ' ');
            final String id = "1";
            final String amount = "all";

            com.everythingrs.vote.Vote.service.execute(() -> {
                try {
                    com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("bvhAnkghmrx9J7qIWrKY4LFqP6pcM11qZeJgEP9Vtgas9zvMDYxOong0D7v0rq4tlhBhp2fY",
                            playerName, id, amount);
                    if (reward[0].message != null) {
                        c.sendMessage(reward[0].message);
                        return;
                    }
                    if (reward[0].reward_id == -1) {
                        int points = reward[0].give_amount;
                        String name = playerName;
                        World.getPlayer(name).ifPresent(a -> {
                            a.addAttribute("vote_points", a.getNumericAttribute("vote_points").intValue() + points);
                            GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(
                                    plugin -> a.getPacketDispatcher().sendComponentText(GameInterface.GAME_NOTICEBOARD,
                                            plugin.getComponent("Vote credits"),
                                            "Vote credits: <col=ffffff>" + a.getNumericAttribute("vote_points").intValue()
                                                    + "</col>"));
                        });
                    } else {
                        c.getInventory().addItem(reward[0].reward_id, reward[0].give_amount);
                    }
                    c.sendMessage(
                            "Thank you for voting!");
                } catch (Exception e) {
                    c.sendMessage("Api Services are currently offline. Please check back shortly");
                    e.printStackTrace();
                }
            });
        });

        new Command(Privilege.PLAYER, "reward", (c, args) -> {
            new Thread(new Vote(c)).start();
        });
        new Command(Privilege.MODERATOR, "related", "See others users with the same ip or mac address. Usage: ::related player name", (p, args) -> World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
            final String ip = target.getIP();
            final String mac = target.getMACAddress();
            final HashMap<String, String> foundPlayers = new HashMap<>(); // username, reason
            for (final Player player : World.getPlayers()) {
                final String name = player.getName();
                if (!ip.isEmpty() && player.getIP().equals(ip)) {
                    foundPlayers.put(name, "Matched IP Address");
                }
                if (!mac.isEmpty() && player.getMACAddress().equals(mac)) {
                    if (foundPlayers.containsKey(name)) {
                        foundPlayers.put(name, "Matched IP and Mac");
                    } else {
                        foundPlayers.put(name, "Matched Mac Address");
                    }
                }
            }
            if (foundPlayers.size() == 1) {
                // if the only result was the player itself
                p.sendMessage("No related players found.");
            } else {
                final ArrayList<String> entries = new ArrayList<>(foundPlayers.size());
                for (final Map.Entry<String, String> entry : foundPlayers.entrySet()) {
                    entries.add(Colour.BLUE.wrap(entry.getKey()) + " - reason: " + Colour.RS_GREEN.wrap(entry.getValue()));
                }
                Diary.sendJournal(p, "Related players: " + (entries.size() - 1), entries);
            }
        }));
        new Command(Privilege.SUPPORT, "clearnullednpcs", (p, args) -> {
            //Clears the npcs which are visible in the player's viewport and have died - if they're still in dead status 10 ticks after the check.
            CharacterLoop.forEach(p.getLocation(), p.getViewDistance(), NPC.class, npc -> {
                if (npc.isDead()) {
                    p.sendMessage("Dead NPC: " + npc.getName(p) + ": " + npc.getLocation());
                    WorldTasksManager.schedule(() -> {
                        if (npc.isDead() && !npc.isFinished()) {
                            npc.setRespawnTask();
                        }
                    }, 10);
                }
            });
        });

        new Command(Privilege.MODERATOR, "clearnullednpcs", (p, args) -> {
            //Clears the npcs which are visible in the player's viewport and have died - if they're still in dead status 10 ticks after the check.
            CharacterLoop.forEach(p.getLocation(), p.getViewDistance(), NPC.class, npc -> {
                if (npc.isDead()) {
                    p.sendMessage("Dead NPC: " + npc.getName(p) + ": " + npc.getLocation());
                    WorldTasksManager.schedule(() -> {
                        if (npc.isDead() && !npc.isFinished()) {
                            npc.setRespawnTask();
                        }
                    }, 10);
                }
            });
        });
        new Command(Privilege.ADMINISTRATOR, "randomfrequency", (p, args) -> {
            final int value = Integer.parseInt(args[0]);
            Constants.randomEvent = (int) TimeUnit.HOURS.toTicks(value);
            p.sendMessage("Random events are on average now occuring every " + value + " hours.");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "resetecto", (p, args) -> World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(user -> {
            user.addAttribute("ectofuntus bone status", 0);
            user.addAttribute("ectofuntus grinded bone", 0);
            p.sendMessage(user.getName() + "'s ectofuntus settings reset.");
        }));
        new Command(Privilege.MODERATOR, "checkrandom", (p, args) -> {
            if (Constants.WORLD_PROFILE.isBeta() && !Constants.isOwner(p)) {
                return;
            }
            if (!p.isLocked() && !p.isFinished() && !p.isDead()) {
                final Area area = p.getArea();
                if (!(area instanceof RandomEventRestrictionPlugin)) {
                    p.getAttributes().put("evil bob complete", true);
                    p.getAttributes().put("observing random event", true);
                    EvilBobIsland.teleport(p);
                } else {
                    p.sendMessage("You cannot teleport to the random event island from here.");
                }
            } else {
                p.sendMessage("You can't do that right now.");
            }
        });
        new Command(Privilege.MODERATOR, "random", "Initiate a random event for a user. Usage: ::random player name", (p, args) -> World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
            if (Constants.WORLD_PROFILE.isBeta() && !Constants.isOwner(p)) {
                return;
            }
            final long lastEvent = target.getNumericAttribute("last random event").longValue();
            if (!p.getPrivilege().eligibleTo(Privilege.ADMINISTRATOR) && lastEvent + TimeUnit.MINUTES.toMillis(45) > System.currentTimeMillis()) {
                p.sendMessage("That user has already played through a random event within the past 45 minutes.");
                return;
            }
            target.log(LogLevel.INFO, "Forced random event by " + p.getName() + ".");
            EvilBobIsland.teleport(target);
        }));
        new Command(Privilege.MODERATOR, "movehome", "Moves another user home if they accept the request. Usage: ::movehome player name", (p, args) -> World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
            if (p == target) {
                p.sendMessage("You can't teleport yourself.");
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.ZENYTE_PORTAL_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    int baseX = 3087;
                    int baseY = 3490;
                    int baseZ = 0;

                    // Add variation to the coordinates
                    int variationRange = 3;
                    Random rand = new Random();

                    int newX = baseX + rand.nextInt(variationRange * 2 + 1) - variationRange;
                    int newY = baseY + rand.nextInt(variationRange * 2 + 1) - variationRange;
                    int newZ = baseZ + rand.nextInt(variationRange * 2 + 1) - variationRange;

                    return new Location(newX, newY, newZ);
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
                    return 3;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return 0;
                }

                @Override
                public boolean isCombatRestricted() {
                    return false;
                }

                @Override
                public String toString() {
                    return "Home";
                }
            };
            Teleother.request(p, target, teleport);
        }));
        new Command(Privilege.SUPPORT, "clearfriendlist", "Remove all entries from friend list.", (p, args) -> {
            p.getSocialManager().getFriends().clear();
            p.sendMessage("Relog to refresh your friends list.");
        });
        new Command(Privilege.PLAYER, "ccban", "Ban given user from your clan chat. Usage: ::ccban player name", (p, args) -> ClanManager.permban(p, StringUtilities.compile(args, 0, args.length, ' ')));
        new Command(Privilege.PLAYER, "ccunban", "Unban given user from your clan chat. Usage: ::ccunban player name", (p, args) -> ClanManager.permunban(p, StringUtilities.compile(args, 0, args.length, ' ')));
        new Command(Privilege.MODERATOR, "observe", (p, args) -> {
            if (Constants.WORLD_PROFILE.isBeta() && !Constants.isOwner(p)) {
                return;
            }
            if (args.length == 0 || p.getTemporaryAttributes().get("observee") != null) {
                final Object observee = p.getTemporaryAttributes().remove("observee");
                if (observee instanceof Player) {
                    ((Player) observee).getBotObservers().remove(p);
                    p.sendMessage("No longer observing " + ((Player) observee).getName());
                }
                return;
            }
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(user -> {
                p.getTemporaryAttributes().put("observee", user);
                user.getBotObservers().add(p);
            });
        });
        new Command(Privilege.ADMINISTRATOR, "fixtournament", (p, args) -> {
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(user -> user.getAttributes().remove("was inside tournament lobby"));
        });
        new Command(Privilege.ADMINISTRATOR, "npcinfo", (p, args) -> {
            p.sendMessage("Currently " + World.getNPCs().size() + " in the game.");
        });
        new Command(Privilege.SUPPORT, "checkinv", "Check inv of given player. Usage: ::checkinv player name", (p, args) -> {
            final String target = StringUtilities.compile(args, 0, args.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(target);
            if (!targetPlayer.isPresent()) {
                p.sendMessage(target + " is not online.");
                return;
            }
            final Player tp = targetPlayer.get();
            if (tp == p) {
                p.sendMessage("You can't do this on yourself.");
                return;
            }
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 12);
            p.getPacketDispatcher().sendUpdateItemContainer(tp.getInventory().getContainer(), ContainerType.BANK);
            p.getPacketDispatcher().sendComponentSettings(12, 13, 0, 1000, AccessMask.CLICK_OP10);
        });
        new Command(Privilege.SUPPORT, "checkbank", "Check bank of given player. Usage: ::checkbank player name", (p, args) -> {
            final String target = StringUtilities.compile(args, 0, args.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(target);
            if (!targetPlayer.isPresent()) {
                p.sendMessage(target + " is not online.");
                return;
            }
            final Player tp = targetPlayer.get();
            if (tp == p) {
                p.sendMessage("You can't do this on yourself.");
                return;
            }
            p.getTemporaryAttributes().put("viewing another bank", true);
            p.setCloseInterfacesEvent(() -> p.getTemporaryAttributes().remove("viewing another bank"));
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 12);
            p.getPacketDispatcher().sendUpdateItemContainer(tp.getBank().getContainer(), ContainerType.BANK);
            tp.getBank().refreshBankSizes(p);
            p.getPacketDispatcher().sendComponentSettings(12, 13, 0, 1000, AccessMask.CLICK_OP10);
        });
        new Command(Privilege.SUPPORT, "checkgear", "Check current worn equipment of a player. Usage: ::checkgear player name", (p, args) -> {
            final String target = StringUtilities.compile(args, 0, args.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(target);
            if (!targetPlayer.isPresent()) {
                p.sendMessage(target + " is not online.");
                return;
            }
            final Player tp = targetPlayer.get();
            if (tp == p) {
                p.sendMessage("You can't do this on yourself.");
                return;
            }
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 12);
            p.getPacketDispatcher().sendUpdateItemContainer(tp.getEquipment().getContainer(), ContainerType.BANK);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "antiknox", (p, args) -> {
            Constants.ANTIKNOX = !Constants.ANTIKNOX;
            p.sendMessage("Antiknox: " + Constants.ANTIKNOX);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "purgechunks", (p, args) -> {
            Constants.PURGING_CHUNKS = !Constants.PURGING_CHUNKS;
            p.sendMessage("Purging chunks: " + Constants.PURGING_CHUNKS);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "huntercheck", (p, args) -> {
            Constants.CHECK_HUNTER_TRAPS_QUANTITY = !Constants.CHECK_HUNTER_TRAPS_QUANTITY;
            p.sendMessage("Checking hunter trap quantity: " + Constants.CHECK_HUNTER_TRAPS_QUANTITY);
        });
        new Command(Privilege.ADMINISTRATOR, "duelarena", "Toggle duel arena access.", (p, args) -> {
            Constants.DUEL_ARENA = !Constants.DUEL_ARENA;
            p.sendMessage("Duel Arena: " + Constants.DUEL_ARENA);
        });
        new Command(Privilege.ADMINISTRATOR, "grots", "Toggle grotesque guardians.", (p, args) -> {
            Constants.GROTESQUE_GUARDIANS = !Constants.GROTESQUE_GUARDIANS;
            p.sendMessage("Grotesque Guardians: " + Constants.GROTESQUE_GUARDIANS);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "whitelisting", (p, args) -> {
            Constants.WHITELISTING = !Constants.WHITELISTING;
            p.sendMessage("Whitelisting: " + Constants.WHITELISTING);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "whitelist", (p, args) -> {
            Constants.whitelistedUsernames.add(Utils.formatUsername(StringUtilities.compile(args, 0, args.length, ' ')));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "teleparty", (p, args) -> {
            if (!Constants.isOwner(p)) {
                return;
            }
            final Area area = p.getArea();
            if (!(area instanceof RaidArea raidArea)) {
                return;
            }
            final Raid raid = raidArea.getRaid();
            final Set<Player> members = raid.getPlayers();
            for (final Player member : members) {
                member.setLocation(p.getLocation());
            }
        });

        new Command(Privilege.SPAWN_ADMINISTRATOR, "addrp",
                "Give vote points to a player. Usage: ::addrp points player name", (p, args) -> {
            int points = Integer.valueOf(args[0]);
            String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                a.addAttribute("Pharaoh points", a.getNumericAttribute("Pharaoh points").intValue() + points);
                GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(
                        plugin -> a.getPacketDispatcher().sendComponentText(GameInterface.GAME_NOTICEBOARD,
                                plugin.getComponent("Pharaoh points"),
                                "Pharaoh points: <col=ffffff>" + a.getNumericAttribute("Pharaoh points").intValue()
                                        + "</col>"));
                p.sendMessage("Added Pharaoh points to user " + name + "; Amount: " + points);
            });
        });




        new Command(Privilege.SPAWN_ADMINISTRATOR, "tourneyall", (p, args) -> {
            if (Tournament.tournaments.isEmpty() || !Constants.isOwner(p)) {
                return;
            }
            final ArrayList<Tournament> tournaments = new ArrayList<>(Tournament.tournaments);
            for (final Tournament tournament : tournaments) {
                if (!tournament.expired()) {
                    for (final Player player : World.getPlayers()) {
                        tournament.getLobby().teleportPlayer(player);
                    }
                    break;
                }
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "starttournament", (p, args) -> {
            final ArrayList<TournamentPreset> presets = new ArrayList<>(TournamentPreset.values);
            final ArrayList<String> presetNameList = new ArrayList<>(presets.size());
            for (final TournamentPreset preset : presets) {
                presetNameList.add(preset.toString());
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select a preset", presetNameList.toArray(new String[0])) {
                @Override
                public void handleClick(final int slotId) {
                    final TournamentPreset preset = presets.get(slotId);
                    p.getDialogueManager().finish();
                    p.sendInputString("When to start the tournament?(MM DD HH MM)", value -> {
                        final SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH mm");
                        final Date date = format.parse((Calendar.getInstance().get(Calendar.YEAR) + " ") + value, new ParsePosition(0));
                        final long milliseconds = date.toInstant().toEpochMilli();
                        final long currentTime = System.currentTimeMillis();
                        if (currentTime > milliseconds) {
                            p.sendMessage("Cannot schedule a tournament for that date; It has already passed!");
                            return;
                        }
                        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds - currentTime);
                        p.getDialogueManager().finish();
                        try {
                            final AllocatedArea area = MapBuilder.findEmptyChunk(64, 64);
                            final TournamentLobby lobby = new TournamentLobby(area, preset);
                            lobby.constructRegion();
                            lobby.createTournament((int) seconds, date);
                            Tournament.tournaments.add(lobby.getTournament());
                            World.sendMessage(MessageType.GLOBAL_BROADCAST, "A " + preset + " tournament has been started! Go talk to the Tournament Guard in Edgeville to participate.");
                        } catch (OutOfSpaceException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.MODERATOR, "ip", "Get the IP address of a user.", (p, args) -> p.sendInputString("Whose IP address to obtain?", value -> {
            final Optional<Player> player = World.getPlayer(value);
            if (!player.isPresent()) {
                p.sendMessage("Player is not online.");
                return;
            }
            final Player target = player.get();
            p.sendMessage("IP address for " + target.getName() + " is: " + target.getIP());
        }));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "superiorrate", (p, args) -> {
            p.getTemporaryAttributes().put("superior rate", Math.max(0, Integer.parseInt(args[0]) - 1));
            p.sendMessage("Superiors will now appear at a rate of 1/" + (p.getNumericTemporaryAttribute("superior rate").intValue() + 1) + ".");
        });
        new Command(Privilege.ADMINISTRATOR, "broadcast", "Initiates sending a server-wide broadcast. No arguments required.", (p, args) -> {
            p.sendInputString("Enter text to broadcast: ", string -> {
                p.getDialogueManager().start(new Dialogue(p) {
                    @Override
                    public void buildDialogue() {
                        plain("Broadcast message: <br>" + string);
                        options("Broadcast it?", new DialogueOption("Yes.", () -> World.sendMessage(GLOBAL_BROADCAST, string)), new DialogueOption("No."));
                    }
                });
            });
        });
        new Command(Privilege.ADMINISTRATOR, "clanunban", "Unban a user from your clan.", (p, args) -> {
            final String name = StringUtilities.compile(args, 0, args.length, '_');
            final Optional<ClanChannel> clan = ClanManager.getChannel(p.getUsername());
            if (clan.isPresent()) {
                final Object2LongOpenHashMap<String> bannedMembers = clan.get().getBannedMembers();
                if (bannedMembers.removeLong(StringFormatUtil.formatUsername(name)) != 0) {
                    p.sendMessage("User successfully unbanned from clan.");
                } else {
                    p.sendMessage("Could not find user " + name + ".");
                }
            } else {
                p.sendMessage("You do not own a clan.");
            }
        });
        new Command(Privilege.ADMINISTRATOR, "status", "Get status of a user.", (player, strings) -> {
            final String targetName = StringUtilities.compile(strings, 0, strings.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(targetName);
            if (!targetPlayer.isPresent()) {
                player.sendMessage("The user " + targetName + " is not online.");
                return;
            }
            final Player t = targetPlayer.get();
            player.sendMessage(Colour.RS_RED.wrap("Status on " + t.getName() + ":"));
            player.sendMessage("Logout timer: " + t.getLogoutCount());
            player.sendMessage("Channel active: " + t.getSession().getChannel().isActive());
            player.sendMessage("Channel open: " + t.getSession().getChannel().isOpen());
            player.sendMessage("Last packet received: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - t.getLastReceivedPacket()) + " seconds ago");
            player.sendMessage("-------------------");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "resetge", (p, args) -> p.getGrandExchange().resetExistingOffers());
        new Command(Privilege.SPAWN_ADMINISTRATOR, "multigfx", (p, args) -> {
            int id = Integer.parseInt(args[0]);
            final int px = p.getX();
            final int py = p.getY();
            for (int x = px - 10; x < px + 10; x++) {
                for (int y = py - 10; y < py + 10; y++) {
                    final Projectile proj = new Projectile(id++, 50, 50, 0, 0, 5000, 0, 5);
                    World.sendProjectile(new Location(x, y, p.getPlane()), new Location(x + 5, y, p.getPlane()), proj);
                }
            }
            p.sendMessage("Last: " + (id - 1));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "barrows", (p, args) -> {
            if (args.length < 2) {
                p.sendMessage("Arguments are <Number of kills> <Reward potential>");
                return;
            }
            final Barrows barrows = p.getBarrows();
            final int number = Math.min(100, Integer.parseInt(args[0]));
            final int rp = Math.max(0, Integer.parseInt(args[1]) - 668);
            for (int i = 0; i < number; i++) {
                barrows.setMaximumReward(rp);
                GameInterface.BARROWS_REWARDS.open(p);
            }
            p.sendMessage("Rolled " + number + " Barrows rewards at a reward potential of " + (rp + 668) + ".");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "defencemultiplier", (p, args) -> {
            final double dbl = Double.parseDouble(args[0]);
            Constants.defenceMultiplier = Math.max(0.5, Math.min(2, dbl));
            for (final Player player : World.getPlayers()) {
                player.sendMessage("PvP Defence multiplier has been set to " + Constants.defenceMultiplier, GLOBAL_BROADCAST);
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "birthdayeventreload", (p, args) -> {
            BirthdayEventRewardList.reload();
            p.sendMessage("Birthday event reward list reloaded.");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "addbirthdayreward", (p, args) -> {
            final String username = StringUtilities.compile(args, 0, args.length, '_');
            BirthdayEventRewardList.addUsername(username);
            p.sendMessage(username + " added to birthday event reward list.");
        });
        new Command(Privilege.ADMINISTRATOR, "partyroom", "Opens the party room modification menu.", (p, args) -> {
            PartyRoomVariables.openEditMode(p);
        });
        new Command(Privilege.MODERATOR, "pc", "Opens the pest control modification menu.", (p, args) -> {
            p.getDialogueManager().start(new OptionsMenuD(p, "Select the setting to change", "Minimum players requirement: " + Colour.RS_GREEN.wrap(String.valueOf(PestControlUtilities.MINIMUM_PLAYERS_LIMIT)), "Maximum players requirement: " + Colour.RS_GREEN.wrap(String.valueOf(PestControlUtilities.MAXIMUM_PLAYERS_LIMIT)), "Time until deporting: " + Colour.RS_GREEN.wrap(String.valueOf(PestControlUtilities.TIME_UNTIL_GAME_START))) {
                @Override
                public void handleClick(int slotId) {
                    if (slotId == 0) {
                        player.sendInputInt("Enter minimum players requirement", PestControlUtilities::setMinimum);
                    } else if (slotId == 1) {
                        player.sendInputInt("Enter maximum players requirement", PestControlUtilities::setMaximum);
                    } else if (slotId == 2) {
                        player.sendInputInt("Enter delay between deportation in ticks", PestControlUtilities::setTime);
                    }
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "multispawn", (p, args) -> {
            final int id = Integer.parseInt(args[0]);
            final int radius = args.length == 1 ? 10 : Integer.parseInt(args[1]);
            final NPCDefinitions defs = Objects.requireNonNull(NPCDefinitions.get(id));
            final int size = defs.getSize();
            final int px = p.getX();
            final int py = p.getY();
            int count = 0;
            for (int x = px - radius; x <= px + radius; x += size) {
                for (int y = py - radius; y <= py + radius; y += size) {
                    final Location tile = new Location(x, y, p.getPlane());
                    if (p.isProjectileClipped(tile, true)) {
                        continue;
                    }
                    if (++count > 250) {
                        break;
                    }
                    final NPC npc = World.spawnNPC(id, tile);
                    npc.setSpawned(true);
                }
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "smoke", (p, args) -> {
            final int radius = args.length == 0 ? p.getViewDistance() : Integer.parseInt(args[0]);
            final ArrayList<NPC> list = new ArrayList<>();
            final boolean unrestricted = args.length == 2;
            CharacterLoop.forEach(p.getLocation(), Math.min(p.getViewDistance(), radius), NPC.class, n -> {
                if (n.isAttackable() && n.isAttackable(p) && (unrestricted || !p.isProjectileClipped(n, false))) {
                    list.add(n);
                }
            });
            final Projectile projectile = new Projectile(310, 34, 50, 0, 0, 20, 0, 5);
            for (final NPC npc : list) {
                World.scheduleProjectile(p, npc, projectile).schedule(() -> npc.applyHit(new Hit(p, npc.getHitpoints(), HitType.REGULAR)));
            }
        });
        new Command(Privilege.MODERATOR, "yellmute", "Yell mute a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.YELL_MUTE));
        new Command(Privilege.MODERATOR, "ipyellmute", "Ip yell mute a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.IP_YELL_MUTE));
        new Command(Privilege.MODERATOR, "macyellmute", "Mac yell mute a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.MAC_YELL_MUTE));
        new Command(Privilege.MODERATOR, "mute", "Mute a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.MUTE));
        new Command(Privilege.MODERATOR, "ban", "Ban a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.BAN));
        new Command(Privilege.MODERATOR, "ipmute", "Ip mute a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.IP_MUTE));
        new Command(Privilege.MODERATOR, "ipban", "Ip ban a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.IP_BAN));
        new Command(Privilege.MODERATOR, "macmute", "Mac mute a player", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.MAC_MUTE));
        new Command(Privilege.MODERATOR, "macban", "Mac ban a player.", (p, args) -> PunishmentManager.requestPunishment(p, PunishmentType.MAC_BAN));
        new Command(Privilege.MODERATOR, "revoke", "Open the punishment revocation menu.", (p, args) -> PunishmentManager.revokePunishments(p));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "resetfarming", (p, args) -> {
            p.getFarming().reset();
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "resettog", (p, args) -> p.sendInputName("Whose Tears of Guthix restriction to remove?", name -> World.getPlayer(name).ifPresent(targetPlayer -> targetPlayer.getAttributes().remove(JunaEnterDialogue.LAST_ATTEMPT_DATE_ATTR))));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "cycle", (p, args) -> {
            Constants.CYCLE_DEBUG = !Constants.CYCLE_DEBUG;
            p.sendMessage("Cycle debug: " + Constants.CYCLE_DEBUG);
        });
//        new Command(Privilege.SPAWN_ADMINISTRATOR, "savepreset", (p, args) -> {
//            if (args.length == 0) {
//                p.sendMessage("Must enter name as the argument.");
//                return;
//            }
//            val name = Utils.formatString(StringUtilities.compile(args, 0, args.length, ' '));
//            if (name.length() == 0) {
//                return;
//            }
//            p.getPresetManager().savePreset(name, false);
//        });
//
//        new Command(Privilege.SPAWN_ADMINISTRATOR, "presets", (p, args) -> p.getPresetManager().open());
        new Command(Privilege.MODERATOR, "heatmaps", "Toggles heatmap mode. Usage: ::heatmaps [0 = off, 1 = on]", (p, args) -> {
            final Integer value = Integer.valueOf(args[0]);
            final int distance = 16383;//Integer.valueOf(args[1]);
            final boolean bool = value == 1;
            p.setHeatmap(bool);
            p.setHeatmapRenderDistance(distance);
            p.send(new Heatmap(bool));
        });
        new Command(Privilege.ADMINISTRATOR, "campos", (p, args) -> {
            int x = p.getX();
            int y = p.getY();
            int plane = 1000;
            int speed = 127;
            int acceleration = 127;
            if (args.length > 0) {
                x = Integer.parseInt(args[0]);
            }
            if (args.length > 1) {
                y = Integer.parseInt(args[1]);
            }
            if (args.length > 2) {
                plane = Integer.parseInt(args[2]);
            }
            if (args.length > 3) {
                speed = Integer.parseInt(args[3]);
            }
            if (args.length > 4) {
                acceleration = Integer.parseInt(args[4]);
            }
            new CameraPositionAction(p, new Location(x, y), plane, speed, acceleration).run();
        });
        new Command(Privilege.ADMINISTRATOR, "camlook", (p, args) -> {
            int x = p.getX();
            int y = p.getY();
            int plane = 1000;
            int speed = 127;
            int acceleration = 127;
            if (args.length > 0) {
                x = Integer.parseInt(args[0]);
            }
            if (args.length > 1) {
                y = Integer.parseInt(args[1]);
            }
            if (args.length > 2) {
                plane = Integer.parseInt(args[2]);
            }
            if (args.length > 3) {
                speed = Integer.parseInt(args[3]);
            }
            if (args.length > 4) {
                acceleration = Integer.parseInt(args[4]);
            }
            new CameraLookAction(p, new Location(x, y), plane, speed, acceleration).run();
        });
        new Command(Privilege.ADMINISTRATOR, "camreset", (p, args) -> {
            p.getPacketDispatcher().resetCamera();
        });
        new Command(Privilege.ADMINISTRATOR, "testproj", (p, args) -> {
            final Projectile proj = new Projectile(Integer.parseInt(args[0]), 50, 50, 0, 0, 50, 0, 5);
            World.sendProjectile(p.getLocation(), new Location(p.getX() + 10, p.getY(), p.getPlane()), proj);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "task", "Choose a slayer task.", (p, args) -> {
            List<SlayerTask> tasks = new ArrayList<>();
            tasks.addAll(Arrays.asList(ObjectArrays.concat(RegularTask.VALUES, BossTask.VALUES, SlayerTask.class)));
            tasks.sort(Comparator.comparing(Object::toString));
            final ArrayList<String> names = new ArrayList<>();
            for (final SlayerTask task : tasks) {
                names.add(task.getTaskName());
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select the task to receive", names.toArray(new String[0])) {
                @Override
                public void handleClick(final int slotId) {
                    if (slotId >= tasks.size()) {
                        return;
                    }
                    final SlayerTask task = tasks.get(slotId);
                    //noinspection Convert2Lambda
                    player.sendInputInt("Enter kill count requirement:", new CountDialogue() {
                        @Override
                        public void run(int amount) {
                            final Assignment assignment = new Assignment(player, player.getSlayer(), task, task.getEnumName(), amount, amount, player.getSlayer().getMaster());
                            p.getSlayer().setAssignment(assignment);
                            p.getDialogueManager().start(new Dialogue(p, p.getSlayer().getMaster().getNpcId()) {
                                @Override
                                public void buildDialogue() {
                                    npc("Your new task is to kill " + assignment.getAmount() + " " + assignment.getTask().toString() + ".");
                                }
                            });
                        }
                    });
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.ADMINISTRATOR, "combatdebug", (p, args) -> p.getTemporaryAttributes().put("combat debug", Boolean.valueOf(args[0])));
        new Command(Privilege.ADMINISTRATOR, "home", "Teleport home. Works anywhere.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.ZENYTE_PORTAL_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(3087, 3490, 0) ;
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
                    return 3;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return 100;
                }

                @Override
                public boolean isCombatRestricted() {
                    return false;
                }
            };
            teleport.teleport(p);
        });
        new Command(Privilege.ADMINISTRATOR, "perks", (p, args) -> {
            final StringBuilder builder = new StringBuilder();
            builder.append("Unlocked perks:<br>");
            for (final Map.Entry<PerkWrapper, Perk> entry : p.getPerkManager().getPerks().entrySet()) {
                builder.append("- <col=00080>");
                builder.append(entry.getValue().getName());
                builder.append("</col><br>");
            }
            p.sendMessage(builder.toString());
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "spawning", (p, args) -> {
            try {
                final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                final DynamicArea dynamicArea = new DynamicArea(area, 0, 0) {
                    @Override
                    public void enter(Player player) {
                    }

                    @Override
                    public void leave(Player player, boolean logout) {
                    }

                    @Override
                    public String name() {
                        return "Spawning area";
                    }

                    @Override
                    public Location onLoginLocation() {
                        return new Location(3222, 3219, 0);
                    }

                    @Override
                    public void constructed() {
                        p.setLocation(new Location((area.getChunkX() + 4) << 3, (area.getChunkY() + 4) << 3, 0));
                    }

                    @Override
                    public void constructRegion() {
                        if (constructed) {
                            return;
                        }
                        GlobalAreaManager.add(this);
                        try {
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; y++) {
                                    MapBuilder.copySquare(area, 1, 396, 441, 0, x + area.getChunkX(), y + area.getChunkY(), 0, 0);
                                }
                            }
                        } catch (OutOfBoundaryException e) {
                            log.error("", e);
                        }
                        constructed = true;
                        constructed();
                    }
                };
                dynamicArea.constructRegion();
            } catch (OutOfSpaceException e) {
                log.error("", e);
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "toggleoptions", (p, args) -> {
            p.setUpdatingNPCOptions(!p.isUpdatingNPCOptions());
            p.setUpdateNPCOptions(true);
        });
        new Command(Privilege.ADMINISTRATOR, "scene", (p, args) -> {
            p.setViewDistance(Integer.parseInt(args[0]));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, new String[]{"unlimitedrunes", "unlrunes", "runes"}, "Grants you unlimited runes.", (p, args) -> {
            p.getVarManager().sendBit(4145, 1);
            p.sendMessage(Colour.RS_GREEN.wrap("Fountain of Rune effect activated - no runes are required, and you get no base experience for casting spells."));
        });
        new Command(Privilege.ADMINISTRATOR, "open", (p, args) -> {
            final String name = StringUtilities.compile(args, 0, args.length, ' ');
            for (final GameInterface inter : GameInterface.VALUES) {
                if (inter.toString().replaceAll("_", " ").toLowerCase().startsWith(name)) {
                    inter.open(p);
                    return;
                }
            }
        });
        new Command(Privilege.ADMINISTRATOR, new String[]{"drops", "drop", "dropviewer"}, "Opens the drop viewer.", (p, args) -> {
            GameInterface.DROP_VIEWER.open(p);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "wave", (p, args) -> {
            if (!p.inArea("Fight caves")) {
                p.sendMessage("You must be in fight caves to do this.");
                return;
            }
            final FightCaves caves = (FightCaves) p.getArea();
            caves.skip(Integer.parseInt(args[0]));
        });
        new Command(Privilege.ADMINISTRATOR, "scrapdrops", (p, args) -> {
        });
        new Command(Privilege.ADMINISTRATOR, "chunkhash", (p, args) -> {
            final int x = p.getX();
            final int y = p.getY();
            final int hash = x >> 3 << 16 | y >> 3;
            p.sendMessage("Chunk hash: " + hash);
            System.err.println(hash);
        });
        new Command(Privilege.ADMINISTRATOR, "value", (p, args) -> {
            final Integer id = Integer.valueOf(args[0]);
            final ItemDefinitions definitions = ItemDefinitions.get(id);
            p.sendMessage("Value of " + definitions.getName() + " is " + definitions.getPrice());
        });
        new Command(Privilege.ADMINISTRATOR, "play", (p, args) -> {
            p.getPacketDispatcher().sendMusic(Integer.parseInt(args[0]));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, new String[]{"update", "shutdown"}, (p, args) -> {
            if (!Constants.isOwner(p)) {
                p.sendMessage("You are not authorized to use this command!");
                return;
            }
            p.sendInputInt("How many ticks until server shutdown?", value -> p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options("Shut the server down in " + TimeUnit.TICKS.toSeconds(value) + " seconds?", new DialogueOption("Shut it down.", () -> World.setShutdown(value)), new DialogueOption("Keep it running."));
                }
            }));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "xp", "Sets your experience modifier to suggested value(s)", (p, args) -> {
            try {
                final int combat = Integer.parseInt(args[0]);
                final int skilling = Integer.parseInt(args[1]);
                if (combat < 1 || skilling < 1) {
                    p.sendMessage("Minimum experience rate value permitted is 1!");
                    return;
                }
                if (combat > 1000 || skilling > 1000) {
                    p.sendMessage("Maximum experience rate value permitted is 1000!");
                    return;
                }
                p.setExperienceMultiplier(combat, skilling);
                p.sendMessage("Experience rate set to x" + combat + " & x" + skilling + ".");
                GameInterface.GAME_NOTICEBOARD.open(p);
            } catch (final Exception e) {
                e.printStackTrace();
                p.sendMessage("Format is ::xp combat_rate_value skilling_rate_value");
            }
        });
        new Command(Privilege.ADMINISTRATOR, "attr", (p, args) -> {
            final String attr = StringUtilities.compile(args, 0, args.length, ' ');
            p.sendMessage("Value for attr: " + attr + ", " + p.getAttributes().get(attr));
        });
        new Command(Privilege.ADMINISTRATOR, "setattr", (p, args) -> p.getTemporaryAttributes().put(args[0], args[1]));
        /*new Command(Privilege.SPAWN_ADMINISTRATOR, "doublexp", (p, args) -> {
            Constants.BOOSTED_XP = !Constants.BOOSTED_XP;
            p.sendMessage("Boosted xp: " + Constants.BOOSTED_XP);
        });

        new Command(Privilege.SPAWN_ADMINISTRATOR, "setboostedxp", (p, args) -> {
            p.sendInputInt("Set the xp boost % (eg. 50 for 1.5x xp) to?", amount -> {
                Constants.BOOSTED_XP_MODIFIER = amount;
                val boost = (1F + Constants.BOOSTED_XP_MODIFIER / 100D);
                p.sendMessage("XP boost set to " + boost + "x (" + Constants.BOOSTED_XP_MODIFIER + "%).");
            });
        });*/
        new Command(Privilege.SPAWN_ADMINISTRATOR, "ironman", "Sets your ironman mode", (p, args) -> {
            final String rights = args[0];
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                GameMode mode;
                if (rights.startsWith("reg") || rights.startsWith("ironman")) {
                    mode = GameMode.STANDARD_IRON_MAN;
                } else if (rights.startsWith("ult")) {
                    mode = GameMode.ULTIMATE_IRON_MAN;
                } else if (rights.startsWith("hard")) {
                    mode = GameMode.HARDCORE_IRON_MAN;
                } else {
                    mode = GameMode.REGULAR;
                }
                a.setGameMode(mode);
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "member", "Sets your member rank", (p, args) -> {
            final Integer value = Integer.valueOf(args[0]);
            final MemberRank rank = MemberRank.get(value);
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                if (rank != null) {
                    a.setMemberRank(rank);
                }
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "shufflepids", "Shuffles all of the players' PIDs", (p, args) -> {
            World.shufflePids();
            p.sendMessage("Your new PID is: " + p.getPid());
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "questpoints", "Sets your quest points to the defined value.", (p, args) -> {
            p.setQuestPoints(Math.max(0, Integer.parseInt(args[0])));
            p.refreshQuestPoints();
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "slayerpoints", "Sets your slayer points to the defined value.", (p, args) -> {
            p.getSlayer().setSlayerPoints(Integer.parseInt(args[0]), true);
        });
        new Command(Privilege.ADMINISTRATOR, "area", (p, args) -> {
            final Area area = p.getArea();
            if (area == null) {
                p.sendMessage("Currently not in any defined area.");
                return;
            }
            p.sendMessage("Current area: " + area.name());
        });
        new Command(Privilege.ADMINISTRATOR, "areas", (p, args) -> {
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
                final Area area = target.getArea();
                if (area == null) {
                    p.sendMessage("Currently not in any defined areas.");
                    return;
                }
                final List<Area> areas = new ArrayList<>();
                Area extension = area;
                while (extension.getSuperArea() != null && extension.getSuperArea().inside(p.getLocation())) {
                    extension = extension.getSuperArea();
                }
                areas.add(extension);
                Area a;
                while (!extension.getExtendAreas().isEmpty()) {
                    a = extension;
                    for (int i = extension.getExtendAreas().size() - 1; i >= 0; i--) {
                        final Area nextPick = extension.getExtendAreas().get(i);
                        if (nextPick.inside(p.getLocation())) {
                            extension = nextPick;
                            areas.add(extension);
                            break;
                        }
                    }
                    if (extension == a) {
                        break;
                    }
                }
                p.sendMessage("Current areas: " + areas);
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "setlevelother", "Set one of your own levels. Usage: ::setlevelother [Optional 'temp'] [Skillname or id] [Level]", (p, args) -> {
            p.sendInputName("Whose levels to change?", n -> {
                final Optional<Player> target = World.getPlayer(n);
                if (!target.isPresent()) {
                    p.sendMessage("User not online.");
                    return;
                }
                try {
                    final Player t = target.get();
                    final boolean temporary = args[0].equals("temp");
                    final boolean isNumber = NumberUtils.isCreatable(args[temporary ? 1 : 0]);
                    final EnumDefinitions e = EnumDefinitions.get(680);
                    if (isNumber) {
                        final Integer number = Integer.valueOf(args[temporary ? 1 : 0]);
                        if (number < 0 || number >= e.getSize()) {
                            p.sendMessage("Invalid skill id of " + number + ", valid values are 0-" + e.getSize() + ".");
                            return;
                        }
                        if (temporary) {
                            final int level = Math.min(255, Math.max(0, Integer.parseInt(args[2])));
                            t.getSkills().setLevel(number, level);
                            t.log(LogLevel.INFO, Skills.getSkillName(number) + " has been temporarily boosted to level " + level + " by " + p.getName() + ".");
                            t.sendMessage(Skills.getSkillName(number) + " has been temporarily boosted to level " + level + ".");
                            p.sendMessage(Skills.getSkillName(number) + " has been temporarily boosted to level " + level + " for " + t.getName() + ".");
                            t.getAppearance().resetRenderAnimation();
                        } else {
                            final int level = Math.min(99, Math.max(1, Integer.parseInt(args[1])));
                            t.getSkills().setSkill(number, level, Skills.getXPForLevel(level));
                            t.log(LogLevel.INFO, Skills.getSkillName(number) + " has been set to level " + level + " by " + p.getName() + ".");
                            t.sendMessage(Skills.getSkillName(number) + " has been set to level " + level + ".");
                            p.sendMessage(Skills.getSkillName(number) + " has been set to level " + level + " for " + t.getName() + ".");
                            t.getAppearance().resetRenderAnimation();
                        }
                    } else {
                        final String name = args[temporary ? 1 : 0].toLowerCase();
                        for (int i = e.getSize() - 1; i >= 0; i--) {
                            final String skillName = e.getStringValue(i);
                            if (skillName.toLowerCase().startsWith(name)) {
                                if (temporary) {
                                    final int level = Math.min(255, Math.max(0, Integer.parseInt(args[2])));
                                    t.getSkills().setLevel(i, level);
                                    t.log(LogLevel.INFO, skillName + " has been temporarily boosted to level " + level + " by " + p.getName() + ".");
                                    t.sendMessage(skillName + " has been temporarily boosted to level " + level + ".");
                                    p.sendMessage(skillName + " has been temporarily boosted to level " + level + " for " + t.getName() + ".");
                                    t.getAppearance().resetRenderAnimation();
                                } else {
                                    final int level = Math.min(99, Math.max(1, Integer.parseInt(args[1])));
                                    t.getSkills().setSkill(i, level, Skills.getXPForLevel(level));
                                    t.log(LogLevel.INFO, skillName + " has been set to level " + level + " by " + p.getName() + ".");
                                    t.sendMessage(skillName + " has been set to level " + level + ".");
                                    p.sendMessage(skillName + " has been set to level " + level + " for " + t.getName() + ".");
                                    t.getAppearance().resetRenderAnimation();
                                }
                                return;
                            }
                        }
                    }
                } catch (final Exception e) {
                    p.sendMessage("Invalid syntax. Use command as: ;;setlevelother [Optional 'temp'] [Skillname or id] [Level]");
                }
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "setlevel", "Set one of your own levels. Usage: ::setlevel [Optional 'temp'] [Skillname or id] [Level]", (p, args) -> {
            try {
                final boolean temporary = args[0].equals("temp");
                final boolean isNumber = NumberUtils.isCreatable(args[temporary ? 1 : 0]);
                final EnumDefinitions e = EnumDefinitions.get(680);
                if (isNumber) {
                    final Integer number = Integer.valueOf(args[temporary ? 1 : 0]);
                    if (number < 0 || number >= e.getSize()) {
                        p.sendMessage("Invalid skill id of " + number + ", valid values are 0-" + e.getSize() + ".");
                        return;
                    }
                    if (temporary) {
                        final int level = Math.min(255, Math.max(0, Integer.parseInt(args[2])));
                        p.getSkills().setLevel(number, level);
                        p.sendMessage(Skills.getSkillName(number) + " has been temporarily boosted to level " + level + ".");
                        p.getAppearance().resetRenderAnimation();
                    } else {
                        final int level = Math.min(99, Math.max(1, Integer.parseInt(args[1])));
                        p.getSkills().setSkill(number, level, Skills.getXPForLevel(level));
                        p.sendMessage(Skills.getSkillName(number) + " has been set to level " + level + ".");
                        p.getAppearance().resetRenderAnimation();
                    }
                } else {
                    final String name = args[temporary ? 1 : 0].toLowerCase();
                    for (int i = e.getSize() - 1; i >= 0; i--) {
                        final String skillName = e.getStringValue(i);
                        if (skillName.toLowerCase().startsWith(name)) {
                            if (temporary) {
                                final int level = Math.min(255, Math.max(0, Integer.parseInt(args[2])));
                                p.getSkills().setLevel(i, level);
                                p.sendMessage(skillName + " has been temporarily boosted to level " + level + ".");
                                p.getAppearance().resetRenderAnimation();
                            } else {
                                final int level = Math.min(99, Math.max(1, Integer.parseInt(args[1])));
                                p.getSkills().setSkill(i, level, Skills.getXPForLevel(level));
                                p.sendMessage(skillName + " has been set to level " + level + ".");
                                p.getAppearance().resetRenderAnimation();
                            }
                            return;
                        }
                    }
                }
            } catch (final Exception e) {
                p.sendMessage("Invalid syntax. Use command as: ;;setlevel [Optional 'temp'] [Skillname or id] [Level]");
            }
        });
        new Command(Privilege.ADMINISTRATOR, "nametag", (p, args) -> {
            final int index = Integer.parseInt(args[0]);
            final String tag = StringUtilities.compile(args, 1, args.length, ' ');
            p.setNametag(index, tag);
        });
        new Command(Privilege.SUPPORT, "kick", "Disconnects a user.", (p, args) -> {
            final Optional<Player> t = World.getPlayer(StringUtilities.compile(args, 0, args.length, ' '));
            if (!t.isPresent()) {
                p.sendMessage("Player was not found.");
                return;
            }
            final Player target = t.get();
            target.log(LogLevel.INFO, "Forcefully kicked by " + p.getName() + ".");
            target.logout(true);
            p.sendMessage("Successfully kicked <col=C22731>" + target.getUsername() + "</col>!");
        });
        new Command(Privilege.ADMINISTRATOR, "unlock", (p, args) -> {
            final Player target = World.getPlayerByUsername(String.valueOf(args[0]));
            if (target == null) {
                p.sendMessage("Player was not found.");
                return;
            }
            target.unlock();
            p.sendMessage("Target unlocked.");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "killme", (p, args) -> {
            p.applyHit(new Hit(p.getHitpoints(), HitType.REGULAR));
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "poison", (p, args) -> {
            p.getToxins().applyToxin(ToxinType.POISON, Integer.parseInt(args[0]));
        });
        new Command(Privilege.ADMINISTRATOR, "printmasks", (p, args) -> {
            final int val = Integer.parseInt(args[0]);
            System.err.println("Masks for value " + val + ": " + AccessMask.getBuilder(val, false));
        });
        new Command(Privilege.ADMINISTRATOR, "duration", (p, args) -> {
            final int anim = Integer.parseInt(args[0]);
            final AnimationDefinitions defs = AnimationDefinitions.get(anim);
            p.sendMessage("Duration: " + defs.getDuration());
        });
        new Command(Privilege.ADMINISTRATOR, "objvar", (p, args) -> {
            int varbit = ObjectDefinitions.get(Integer.parseInt(args[0])).getVarbit();
            if (varbit == -1) {
                varbit = ObjectDefinitions.get(Integer.parseInt(args[0])).getVarp();
                if (varbit == -1) {
                    p.sendMessage("No varps or varbits found for that object.");
                    return;
                }
                p.sendMessage("Varp for " + args[0] + " is " + varbit);
                p.getVarManager().sendVar(varbit, 1);
            } else {
                p.sendMessage("Varbit for " + args[0] + " is " + varbit);
                p.getVarManager().sendBit(varbit, 1);
            }
        });
        new Command(Privilege.ADMINISTRATOR, "extract", (p, args) -> {
            new AnimationExtractor().extract();
        });
        new Command(Privilege.ADMINISTRATOR, "tolerance", (p, args) -> {
            final int value = Integer.parseInt(args[0]);
            p.setMaximumTolerance(value == 1);
            p.sendMessage("Maximum tolerance set to: " + value);
        });
        new Command(Privilege.PLAYER, "commands", (p, args) -> {
            final ArrayList<String> entries = new ArrayList<>();
            COMMANDS.values().stream().filter(distinctByKey(c -> c.name)).sorted().forEach(c -> {
                if (!p.getPrivilege().eligibleTo(c.privilege)) {
                    return;
                }
                if (c.description != null) {
                    final String[] lines = Book.splitIntoLine(c.description, 55);
                    entries.add(c.privilege.getCrown() + "<col=ffff00> ::" + c.name);
                    entries.addAll(Arrays.asList(lines));
                }
            });
            Diary.sendJournal(p, "Commands list", entries);
        });
        new Command(Privilege.ADMINISTRATOR, "testvarp", (p, args) -> {
            for (int i = 0; i < Utils.getIndiceSize(Indice.VARBIT_DEFINITIONS); i++) {
                final VarbitDefinitions def = VarbitDefinitions.get(i);
                if (def.getBaseVar() == Integer.parseInt(args[0])) {
                    System.out.println("Varbit: " + i + ", from bitshift:" + def.getStartBit() + ", till bitshift: " + def.getEndBit());
                }
            }
        });
        new Command(Privilege.ADMINISTRATOR, "ping", "Sends a pulse to the client, after which the client will respond to the server with your current FPS, GC count & the time it took to respond.", (p, args) -> {
            p.send(new PingStatisticsRequest());
        });
        new Command(Privilege.MODERATOR, "hide", "Hides or unhides your character.", (p, args) -> {
            p.getAppearance().setInvisible(!p.isHidden());
            p.setHidden(!p.isHidden());
            p.setMaximumTolerance(p.isHidden());
            if (p.isHidden()) {
                p.sendMessage(Colour.RS_GREEN.wrap("You are now hidden from other players and monsters will not be aggressive towards you."));
            } else {
                p.sendMessage(Colour.RS_RED.wrap("You are no longer hidden from other players and monsters are now aggressive towards you again."));
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "gamemode", "Change ironman mode of a player.", (p, args) -> {
            p.sendInputString("Whose Game Mode to change?", name -> {
                final Optional<Player> player = World.getPlayer(name);
                if (!player.isPresent()) {
                    p.sendMessage("That player is not online.");
                    return;
                }
                final Player targetPlayer = player.get();
                p.sendInputString("What Game Mode to set them to?", mode -> {
                    GameMode gameMode = null;
                    if (mode.startsWith("standard")) {
                        gameMode = GameMode.STANDARD_IRON_MAN;
                    } else if (mode.startsWith("ult")) {
                        gameMode = GameMode.ULTIMATE_IRON_MAN;
                    } else if (mode.startsWith("hard")) {
                        gameMode = GameMode.HARDCORE_IRON_MAN;
                    } else {
                        gameMode = GameMode.REGULAR;
                    }
                    if (gameMode == null) {
                        p.sendMessage("Game Mode by the name of " + mode + " not found.");
                        return;
                    }
                    final GameMode m = gameMode;
                    p.getDialogueManager().start(new Dialogue(p) {
                        @Override
                        public void buildDialogue() {
                            plain("Set the Game Mode of player " + Colour.RS_GREEN.wrap(targetPlayer.getName()) + " to " + Colour.RS_GREEN.wrap(m.toString().toLowerCase()) + "?");
                            options("Change the Game Mode?", new DialogueOption("Yes.", () -> {
                                targetPlayer.setGameMode(m);
                                targetPlayer.sendMessage("Your game mode has been changed to " + targetPlayer.getGameMode().toString().toLowerCase() + ".");
                                p.sendMessage(targetPlayer.getName() + "'s game mode have been changed to " + m.toString().toLowerCase() + ".");
                            }), new DialogueOption("No."));
                        }
                    });
                });
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "rights", "Sets your character rights to the specified rights. Valid arguments: player/nor, mod, admin", (p, args) -> {
            p.sendInputString("Whose rank to change?", name -> {
                final Optional<Player> player = World.getPlayer(name);
                if (!player.isPresent()) {
                    p.sendMessage("That player is not online.");
                    return;
                }
                final Player targetPlayer = player.get();
                p.sendInputString("What rank to set them to?", rights -> {
                    Privilege privilege = null;
                    if (rights.startsWith("player") || rights.startsWith("nor") || rights.startsWith("reg")) {
                        privilege = Privilege.PLAYER;
                    } else if (rights.startsWith("mod")) {
                        privilege = Privilege.MODERATOR;
                    } else if (rights.startsWith("spawn admin")) {
                        privilege = Privilege.SPAWN_ADMINISTRATOR;
                    } else if (rights.startsWith("admin")) {
                        privilege = Privilege.ADMINISTRATOR;
                    } else if (rights.startsWith("global")) {
                        privilege = Privilege.GLOBAL_MODERATOR;
                    } else if (rights.startsWith("forum")) {
                        privilege = Privilege.FORUM_MODERATOR;
                    } else if (rights.startsWith("sup")) {
                        privilege = Privilege.SUPPORT;
                    } else if (rights.startsWith("youtube")) {
                        privilege = Privilege.YOUTUBER;
                    } else if (rights.startsWith("hidden")) {
                        privilege = Privilege.HIDDEN_ADMINISTATOR;
                    }
                    if (privilege == null) {
                        p.sendMessage("Privilege by the name of " + rights + " not found.");
                        return;
                    }
                    final Privilege priv = privilege;
                    p.getDialogueManager().start(new Dialogue(p) {
                        @Override
                        public void buildDialogue() {
                            plain("Set the rights of player " + Colour.RS_GREEN.wrap(targetPlayer.getName()) + " to " + Colour.RS_GREEN.wrap(priv.toString().toLowerCase()) + "?");
                            options("Change the rights?", new DialogueOption("Yes.", () -> {
                                targetPlayer.setPrivilege(priv);
                                targetPlayer.sendMessage("Your privileges have been changed to " + targetPlayer.getPrivilege().toString().toLowerCase() + ".");
                                p.sendMessage(targetPlayer.getName() + "'s privileges have been changed to " + priv.toString().toLowerCase() + ".");
                            }), new DialogueOption("No."));
                        }
                    });
                });
            });
        });
        new Command(Privilege.PLAYER, "wiki", "Opens the OSRSWikia page requested.", (p, args) -> {
            final String page = "https://oldschool.runescape.wiki/w/";
            final String arguments = StringUtilities.compile(args, 0, args.length, '_');
            p.getPacketDispatcher().sendURL(page + arguments);
        });
        new Command(Privilege.ADMINISTRATOR, "printenum", (p, args) -> {
            final int id = Integer.parseInt(args[0]);
            final EnumDefinitions map = EnumDefinitions.get(id);
            if (map.getValues() == null) {
                return;
            }
            final boolean itemn = args.length > 1 && args[1].equals("item");
            map.getValues().forEach((k, v) -> {
                if (itemn) {
                    System.out.println(k + ": " + ItemDefinitions.get((int) v).getName() + "(" + v + ")");
                } else {
                    System.out.println(k + ": " + v);
                }
            });
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "copy", "Copies the requested player's inventory and equipment.", (p, args) -> {
            final StringBuilder bldr = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                bldr.append(args[i] + ((i == args.length - 1) ? "" : " "));
            }
            final String name = Utils.formatString(bldr.toString());
            final Player player = World.getPlayerByDisplayname(name);
            if (player == null) {
                p.sendMessage("Could not find player.");
                return;
            }
            p.getInventory().setInventory(player.getInventory());
            p.getEquipment().setEquipment(player.getEquipment());
            p.getInventory().refreshAll();
            p.getEquipment().refreshAll();
            p.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
            p.sendMessage("Inventory & Equipment copied from " + player.getPlayerInformation().getDisplayname() + ".");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "copyinv", "Copies the requested player's inventory.", (p, args) -> {
            final StringBuilder bldr = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                bldr.append(args[i] + ((i == args.length - 1) ? "" : " "));
            }
            final String name = Utils.formatString(bldr.toString());
            final Player player = World.getPlayerByDisplayname(name);
            if (player == null) {
                p.sendMessage("Could not find player.");
                return;
            }
            p.getInventory().setInventory(player.getInventory());
            p.getInventory().refreshAll();
            p.sendMessage("Inventory copied from " + player.getPlayerInformation().getDisplayname() + ".");
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "copyequipment", "Copies the requested player's equipment.", (p, args) -> {
            final StringBuilder bldr = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                bldr.append(args[i] + ((i == args.length - 1) ? "" : " "));
            }
            final String name = Utils.formatString(bldr.toString());
            final Player player = World.getPlayerByDisplayname(name);
            if (player == null) {
                p.sendMessage("Could not find player.");
                return;
            }
            p.getEquipment().setEquipment(player.getEquipment());
            p.getEquipment().refreshAll();
            p.sendMessage("Equipment copied from " + player.getPlayerInformation().getDisplayname() + ".");
        });
        new Command(Privilege.ADMINISTRATOR, "region", (p, args) -> {
            final int regionId = Integer.parseInt(args[0]);
            final int x = (regionId >> 8) << 6;
            final int y = (regionId & 255) << 6;
            p.setLocation(new Location(x, y, p.getPlane()));
        });
        new Command(Privilege.ADMINISTRATOR, "rdrops", (p, args) -> NPCDrops.init());
        new Command(Privilege.ADMINISTRATOR, "rinfo", (p, args) -> RaidFloorOverviewD.open(p));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "maxbank", "Sets your bank to a preset.", (p, args) -> BankPreset.setBank(p));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "god", "Sets all your bonuses to 15000.", (p, args) -> {
            for (int i = 0; i < 12; i++) {
                p.getBonuses().setBonus(i, 15000);
            }
        });
        new Command(Privilege.ADMINISTRATOR, "raids", "Teleports you to raids recruiting board.", (p, args) -> p.setLocation(new Location(1246, 3562, 0)));
        new Command(Privilege.ADMINISTRATOR, "enter", (p, args) -> {
            p.getConstruction().enterHouse(p.getConstruction().isBuildingMode());
        });
        new Command(Privilege.ADMINISTRATOR, "leave", (p, args) -> {
            p.getConstruction().leaveHouse();
        });
        new Command(Privilege.ADMINISTRATOR, "spellbook", "Switches your spellbook to the requested book. Argument: 0-3/name of the spellbook.", (p, args) -> {
            final String arg = args[0].toLowerCase();
            if (arg.startsWith("r") || arg.startsWith("norm")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.NORMAL, true);
            } else if (arg.startsWith("an")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.ANCIENT, true);
            } else if (arg.startsWith("l")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.LUNAR, true);
            } else if (arg.startsWith("ar")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.ARCEUUS, true);
            } else {
                p.getCombatDefinitions().setSpellbook(Spellbook.getSpellbook(Integer.parseInt(args[0])), true);
            }
        });
        new Command(Privilege.ADMINISTRATOR, "gfx", "Performs the requested graphics. Argument: id", (p, args) -> p.setGraphics(new Graphics(Integer.parseInt(args[0]))));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "spec", "Sets your special energy to 100 or requested value. Arguments: <Optional>amount", (p, args) -> {
            int amount = 100;
            if (args.length > 0) {
                amount = Integer.parseInt(args[0]);
            }
            p.getCombatDefinitions().setSpecialEnergy(amount);
        });
        new Command(Privilege.ADMINISTRATOR, "sound", (p, args) -> p.getPacketDispatcher().sendSoundEffect(new SoundEffect(Integer.parseInt(args[0]))));
        new Command(Privilege.ADMINISTRATOR, new String[]{"heal", "hitpoints", "hp"}, "Sets your health to your max or requested value. Argument: <Optional>amount", (p, args) -> {
            int amount = p.getSkills().getLevelForXp(Skills.HITPOINTS);
            if (args.length > 0) {
                amount = Integer.parseInt(args[0]);
            }
            p.setHitpoints(amount);
            if (p.getPrayerManager().getPrayerPoints() < p.getSkills().getLevelForXp(Skills.PRAYER)) {
                p.getPrayerManager().setPrayerPoints(p.getSkills().getLevelForXp(Skills.PRAYER));
            }
            if (p.getCombatDefinitions().getSpecialEnergy() < 100) {
                p.getCombatDefinitions().setSpecialEnergy(100);
            }
        });
        new Command(Privilege.ADMINISTRATOR, new String[]{"pray", "prayer"}, "Sets your prayer to your max or requested value. Argument: <Optional>amount", (p, args) -> {
            int amount = p.getSkills().getLevelForXp(Skills.PRAYER);
            if (args.length > 0) {
                amount = Integer.parseInt(args[0]);
            }
            p.getPrayerManager().setPrayerPoints(amount);
        });
        new Command(Privilege.ADMINISTRATOR, new String[]{"run", "runenergy"}, "Sets your run energy to your max or requested value. Argument: <Optional>amount", (p, args) -> {
            int amount = 100;
            if (args.length > 0) {
                amount = Integer.parseInt(args[0]);
            }
            p.getVariables().forceRunEnergy(amount);
        });
        new Command(Privilege.ADMINISTRATOR, "replenish", (p, args) -> {
            p.setHitpoints(1000000);
            p.getPrayerManager().setPrayerPoints(1000000);
            p.getCombatDefinitions().setSpecialEnergy(1000000);
            p.getVariables().forceRunEnergy(1000000);
        });
        new Command(Privilege.ADMINISTRATOR, "object", "Spawns an object underneath you. Arguments: id, <Optional>type, <Optional>rotation", (p, args) -> {
            if (!Constants.isOwner(p) && !p.inArea("Spawning area")) {
                p.sendMessage("You can only spawn objects within the spawning area. ::spawning to enter.");
                return;
            }
            final int objectId = Integer.parseInt(args[0]);
            int type = 10;
            int rotation = 0;
            if (args.length > 1) {
                type = Integer.parseInt(args[1]);
            }
            if (args.length > 2) {
                rotation = Integer.parseInt(args[2]);
            }
            final ObjectDefinitions defs = ObjectDefinitions.get(objectId);
            /*if (defs != null) {
                if (defs.getTypes() == null) {
                    if (type != 10) {
                        type = 10;
                        p.sendMessage("Object " + objectId + " spawned with type " + type + ", as input type was invalid.");
                    }
                } else {
                    if (!ArrayUtils.contains(defs.getTypes(), type)) {
                        type = defs.getTypes()[0];
                        p.sendMessage("Object " + objectId + " spawned with type " + type + ", as input type was invalid.");
                    }
                }
            }*/
            if (objectId < 0) {
                World.removeObject(World.getObjectWithType(p.getLocation(), type));
            } else {
                World.spawnObject(new WorldObject(objectId, type, rotation, p.getLocation()));
            }
        });
        new Command(Privilege.ADMINISTRATOR, "npc", "Spawns a NPC underneath you. Argument: id", (p, args) -> {
            if (!Constants.isOwner(p) && !p.inArea("Spawning area")) {
                p.sendMessage("You can only spawn NPCs within the spawning area. ::spawning to enter.");
                return;
            }
            World.spawnNPC(Integer.parseInt(args[0]), new Location(p.getLocation())).setSpawned(true);
        });
        new Command(Privilege.ADMINISTRATOR, "tonpc", "Transmogrifies you to a NPC. Argument: id", (p, args) -> {
            final Integer id = Integer.valueOf(args[0]);
            if (id >= 0 && NPCDefinitions.get(id) == null) {
                p.sendMessage("Invalid transformation.");
                return;
            }
            p.setAnimation(Animation.STOP);
            p.getAppearance().setNpcId(Math.max(-1, id));
            p.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        });

        new Command(Privilege.MODERATOR, "joinnex", "Spawns nex Event", (p, args) -> {
            if (nex_started == false ) {
                p.sendMessage("The nex event has not started.");
               // p.sendMessage("The nightmare Rewards are random and can be.");
                //p.sendMessage("10$ Bond, Mystery Box , Herbs, Cash 5$ bond and more yet rare it possible!.");
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.ZENYTE_PORTAL_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(2904, 5204, 0);
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
                    return 0;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return p.getPrivilege().eligibleTo(Privilege.ADMINISTRATOR) ? 100 : 20;
                }

                @Override
                public boolean isCombatRestricted() {
                    return false;
                }
            };
            teleport.teleport(p);


        });



        new Command(Privilege.PLAYER, "joinnm", "Spawns Nightmare Event", (p, args) -> {
            if (event_started == false ) {
                p.sendMessage("The nightmare event has not started.");
                p.sendMessage("The nightmare Rewards are random and can be.");
                p.sendMessage("10$ Bond, Mystery Box , Herbs, Cash 5$ bond and more yet rare it possible!.");
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.ZENYTE_PORTAL_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(3872, 9961, 3);
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
                    return 0;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return p.getPrivilege().eligibleTo(Privilege.ADMINISTRATOR) ? 100 : 20;
                }

                @Override
                public boolean isCombatRestricted() {
                    return false;
                }
            };
            teleport.teleport(p);


        });
        new Command(Privilege.MODERATOR, "startnm", "Spawns Nightmare Event", (p, args) -> {
            if (event_started == true ) {
                p.sendMessage("The nightmare event has already started.");
                return;
            }
            int NM_NPC = 378;
            Location START = new Location(3870, 9949, 3);
            event_started = true;
            World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=FF0000>Nightmare Event has started, type ::joinNM to begin!</col>");
            World.spawnNPC(NM_NPC, START, Direction.SOUTH, 0);


        });


        new Command(Privilege.MODERATOR, "startnex", "Spawns nex Event", (p, args) -> {
            if (nex_started == true ) {
                p.sendMessage("The nex event has already started.");
                return;
            }
            int NM_NPC = 11278;
            Location START = new Location(2925, 5203, 0);
            nex_started = true;
            World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=FF0000>nex Event has started, type ::joinNex to begin!</col>");
            World.spawnNPC(NM_NPC, START, Direction.SOUTH, 0);


        });

        new Command(Privilege.SPAWN_ADMINISTRATOR, "master", "Sets all your levels to 99.", (p, args) -> {
            if (p.getNumericAttribute("first_99_skill").intValue() == -1) {
                p.addAttribute("first_99_skill", 0);
            }
            for (int i = 0; i < 23; i++) {
                p.getSkills().setSkill(i, 99, 13034431);
            }
            p.getAppearance().resetRenderAnimation();
        });
        new Command(Privilege.PLAYER, "99con", "Sets all your levels to 99.", (p, args) -> {
            if (p.getNumericAttribute("first_99_skill").intValue() == -1) {
                p.addAttribute("first_99_skill", 0);
            }
            for (int i = 22; i < 23; i++) {
                p.getSkills().setSkill(i, 99, 13034431);
            }
            p.getAppearance().resetRenderAnimation();
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "unmaster", "Sets all your levels to 1.", (p, args) -> {
            p.getSkills().resetAll();
            p.getSkills().refresh();
            p.getAppearance().resetRenderAnimation();
        });
        new Command(Privilege.ADMINISTRATOR, "teleloc", "Teleports you to one of the available teleportations.", (p, args) -> {
            final String query = StringUtilities.compile(args, 0, args.length, ' ').toLowerCase();
            final PortalTeleport[] teleports = PortalTeleport.values;
            for (final PortalTeleport teleport : teleports) {
                final String name = teleport.getSmallDescription().toLowerCase();
                if (name.startsWith(query)) {
                    teleport.teleport(p);
                    return;
                }
            }
        });
        new Command(Privilege.ADMINISTRATOR, "tele", "Teleports you to the requested coordinates. Arguments: x y <Optional>z", (p, args) -> {
            final String arg1 = args[0];
            final String arg2 = args[1];
            int x = 0;
            int y = 0;
            if (arg1.equalsIgnoreCase("region")) {
                final int regionId = Integer.parseInt(arg2);
                x = (regionId >> 8) << 6;
                y = (regionId & 255) << 6;
                p.setLocation(new Location(x, y, p.getPlane()));
                return;
            }
            if (arg1.startsWith("rx")) {
                x = Integer.parseInt(arg1.substring(2)) << 6;
            } else if (arg1.startsWith("cx")) {
                x = Integer.parseInt(arg1.substring(2)) << 3;
            } else {
                x = Integer.parseInt(arg1);
            }
            if (arg2.startsWith("ry")) {
                y = Integer.parseInt(arg2.substring(2)) << 6;
            } else if (arg2.startsWith("cy")) {
                y = Integer.parseInt(arg2.substring(2)) << 3;
            } else {
                y = Integer.parseInt(arg2);
            }
            final int plane = args.length > 2 ? Integer.parseInt(args[2]) : p.getPlane();
            p.setLocation(new Location(x, y, plane));
        });
        new Command(Privilege.ADMINISTRATOR, "telespecific", (p, args) -> {
            final String arg1 = args[0];
            final String arg2 = args[1];
            int x;
            int y;
            x = (Integer.parseInt(args[0]) << 6) + (Integer.parseInt(args[1]) << 3);
            y = (Integer.parseInt(args[2]) << 6) + (Integer.parseInt(args[3]) << 3);
            p.setLocation(new Location(x, y, p.getPlane()));
        });
        new Command(Privilege.ADMINISTRATOR, "intertext", (p, args) -> {
            String text = "";
            for (int i = 1; i < args.length; i++) {
                text += args[i] + ((i == args.length - 1) ? "" : " ");
            }
            p.getPacketDispatcher().sendComponentText(182, Integer.parseInt(args[0]), text);
        });
        new Command(Privilege.ADMINISTRATOR, "objtypes", "Sends a game message about the valid types of the requested object. Argument: id", (p, args) -> {
            if (args.length < 1) {
                p.sendMessage("Invalid syntax: Use as ::objtypes objectId");
                return;
            }
            final int value = Integer.parseInt(args[0]);
            final ObjectDefinitions defs = ObjectDefinitions.get(value);
            if (defs.getTypes() == null) {
                p.sendMessage("Object types for " + defs.getName() + "(" + value + "): [10]");
            } else {
                p.sendMessage("Object types for " + defs.getName() + "(" + value + "): " + ArrayUtils.toString(defs.getTypes()));
            }
        });
        new Command(Privilege.ADMINISTRATOR, "itemn", "Displays a list of items that meet the requested name criteria. Argument: name", (p, args) -> {
            final ObjectArrayList<ItemDefinitions> listOfItems = new ObjectArrayList<>(50);
            final ObjectArrayList<String> listOfNames = new ObjectArrayList<>(50);
            final String name = StringUtilities.compile(args, 0, args.length, ' ');
            int characterCount = 0;
            for (final ItemDefinitions defs : ItemDefinitions.definitions) {
                if (defs == null) {
                    continue;
                }
                if (defs.getName().toLowerCase().contains(name)) {
                    listOfItems.add(defs);
                    final String string = defs.getId() + " - " + defs.getName() + (defs.getNotedTemplate() > 0 ? "(noted)" : "");
                    listOfNames.add(string);
                    characterCount += string.length();
                    //Cap it out at 39kb for the payload, gives enough room for the header and rest of the packet.
                    if (characterCount >= 39000) {
                        break;
                    }
                }
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Query: " + name + " (Click to spawn one)", listOfNames.toArray(new String[0])) {
                @Override
                public void handleClick(int slotId) {
                    p.getInventory().addItem(new Item(listOfItems.get(slotId).getId()));
                    p.getDialogueManager().start(this);
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.ADMINISTRATOR, "raidlist", (p, args) -> {
            final ObjectArrayList<Raid> list = new ObjectArrayList<>(Raid.existingRaidsMap.values());
            final ObjectArrayList<String> nameList = new ObjectArrayList<>();
            for (final Raid raid : list) {
                if (nameList.size() >= 128) {
                    break;
                }
                nameList.add(raid.getParty().getChannel().getOwner() + "'s raid");
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select existing raid", nameList.toArray(new String[0])) {
                public void handleClick(final int slotId) {
                    final Raid raid = list.get(slotId);
                    p.setLocation(raid.getRespawnTile());
                }

                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.ADMINISTRATOR, "objectn", "Displays a list of objects that meet the requested name criteria. Argument: name", (p, args) -> {
            String name = "";
            for (int i = 0; i < args.length; i++) {
                name += args[i] + (i == args.length - 1 ? "" : " ");
            }
            final ArrayList<String> entries = new ArrayList<>();
            for (int i = 0; i < ObjectDefinitions.definitions.length; i++) {
                final ObjectDefinitions defs = ObjectDefinitions.get(i);
                if (defs == null) {
                    continue;
                }
                if (defs.getName().toLowerCase().contains(name)) {
                    entries.add(defs.getId() + " - " + defs.getName() + ", types: " + (defs.getTypes() == null ? "[10]" : ArrayUtils.toString(defs.getTypes())));
                }
            }
            Diary.sendJournal(p, "Query: " + name, entries);
        });
        new Command(Privilege.ADMINISTRATOR, "npcn", "Displays a list of npcs that meet the requested name criteria. Argument: name", (p, args) -> {
            String name = "";
            for (int i = 0; i < args.length; i++) {
                name += args[i] + (i == args.length - 1 ? "" : " ");
            }
            final ArrayList<String> entries = new ArrayList<>();
            for (int i = 0; i < NPCDefinitions.definitions.length; i++) {
                final NPCDefinitions defs = NPCDefinitions.get(i);
                if (defs == null) {
                    continue;
                }
                if (defs.getName().toLowerCase().contains(name)) {
                    entries.add(defs.getId() + " - " + defs.getName() + " (lvl-" + defs.getCombatLevel() + ")");
                }
            }
            Diary.sendJournal(p, "Query: " + name, entries);
        });
        new Command(Privilege.SUPPORT, "players", "Displays a list of players online and their coordinates/area.", (p, args) -> {
            final ArrayList<String> entries = new ArrayList<>();
            final ArrayList<Player> players = new ArrayList<>(World.getPlayers().size());
            players.addAll(World.getPlayers());
            players.sort((a, b) -> a.getPlayerInformation().getDisplayname().compareToIgnoreCase(b.getPlayerInformation().getDisplayname()));
            for (final Player player : players) {
                if (player == null) {
                    continue;
                }
                final StringBuilder sb = new StringBuilder(player.getPrivilege().getCrown()).append(player.getPlayerInformation().getDisplayname());
                final Area area = player.getArea();
                sb.append(" - ").append(area != null ? area.name() : "Unknown");
                if (p.getPrivilege().eligibleTo(Privilege.GLOBAL_MODERATOR)) {
                    sb.append(" - (").append(player.getX()).append(", ").append(player.getY()).append(", ").append(player.getPlane()).append(")");
                }
                if (player.isOnMobile()) {
                    sb.append(" - Mobile");
                }
                entries.add(sb.toString().trim());
            }
            Diary.sendJournal(p, "Players online: " + World.getPlayers().size(), entries);
        });
        new Command(Privilege.SUPPORT, "rp", "Spawns a rotten potato.", (p, args) -> {
            if (!p.getInventory().checkSpace()) {
                return;
            }
            if (p.carryingItem(ItemId.ROTTEN_POTATO)) {
                p.sendMessage("You can only have one rotten potato.");
                return;
            }
            p.getInventory().addItem(ItemId.ROTTEN_POTATO, 1);
        });
        new Command(Privilege.MODERATOR, "mobileplayers", "Displays a list of mobile players online and their coordinates.", (p, args) -> {
            final ArrayList<String> entries = new ArrayList<>();
            int count = 0;
            for (final Player player : World.getPlayers()) {
                if (player == null || !player.isOnMobile()) {
                    continue;
                }
                count++;
                entries.add(player.getPlayerInformation().getDisplayname() + " (" + player.getX() + ", " + player.getY() + ", " + player.getPlane() + ")");
            }
            Diary.sendJournal(p, "Players online on mobile: " + count, entries);
        });
        new Command(Privilege.ADMINISTRATOR, "overlay", "Sends an overlay of the requested id. Argument: id", (p, args) -> {
            final int interfaceId = Integer.parseInt(args[0]);
            if (interfaceId == -1) {
                p.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
            } else {
                p.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, interfaceId);
            }
        });
        new Command(Privilege.MODERATOR, "teletome", "Teleports the requested player to you. Usage: ::teletome player name", (p, args) -> {
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(t -> {
                final Optional<Raid> raid = t.getRaid();
                if (raid.isPresent() && !t.getPrivilege().eligibleTo(Privilege.ADMINISTRATOR)) {
                    p.sendMessage("You cannot teleport non-administrators into a raid.");
                    return;
                }
                if (p.getArea() instanceof Inferno && !t.getPrivilege().eligibleTo(Privilege.SPAWN_ADMINISTRATOR)) {
                    p.sendMessage("You cannot teleport a player into the Inferno.");
                    return;
                }
                t.log(LogLevel.INFO, "Force teleported by " + p.getName() + " to " + p.getLocation() + ".");
                t.setLocation(p.getLocation());
            });
        });
        new Command(Privilege.MODERATOR, "teleto", "Teleport to a player. Usage: ::teleto player name", (p, args) -> {
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(t -> {
                final Optional<Raid> raid = t.getRaid();
                if (raid.isPresent() && !p.getPrivilege().eligibleTo(Privilege.ADMINISTRATOR)) {
                    p.sendMessage("You cannot teleport to a player in a raid as a non-administrator.");
                    return;
                }
                if (t.getArea() instanceof Inferno && !p.getPrivilege().eligibleTo(Privilege.SPAWN_ADMINISTRATOR)) {
                    p.sendMessage("You cannot teleport to a player in the Inferno.");
                    return;
                }
                t.log(LogLevel.INFO, p.getName() + " force teleported to you at " + t.getLocation() + ".");
                p.setLocation(t.getLocation());
            });
        });
        new Command(Privilege.ADMINISTRATOR, "varbits", (p, args) -> {
            final int low = Integer.parseInt(args[0]);
            final int high = Integer.parseInt(args[1]);
            final int value = Integer.parseInt(args[2]);
            for (int index = low; index < high; index++) {
                p.getVarManager().sendBit(index, value);
            }
            p.sendMessage("set values from " + low + " to " + high + " with value: " + value);
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, new String[]{"b", "bank"}, "Opens the bank.", (p, args) -> GameInterface.BANK.open(p));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "var", "Sends a varp of the requested id and value. Arguments: id value", (p, args) -> p.getVarManager().sendVar(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        new Command(Privilege.SPAWN_ADMINISTRATOR, "prayers", (p, args) -> {
            p.getSettings().setSetting(Setting.RIGOUR, 1);
            p.getSettings().setSetting(Setting.AUGURY, 1);
            p.getSettings().setSetting(Setting.PRESERVE, 1);
        });
        new Command(Privilege.ADMINISTRATOR, "cs2", (p, args) -> {
            p.getPacketDispatcher().sendClientScript(Integer.parseInt(args[0]));
        });
        new Command(Privilege.ADMINISTRATOR, "journal", (p, args) -> {
            p.getInterfaceHandler().setJournal(InterfaceHandler.Journal.values()[Integer.parseInt(args[0])]);
        });
        new Command(Privilege.ADMINISTRATOR, "varbit", "Sends a varbit of the requested id and value. Arguments: id value", (p, args) -> p.getVarManager().sendBit(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        new Command(Privilege.ADMINISTRATOR, "getobjvarbit", (p, args) -> p.sendMessage("Varbit for " + args[0] + " is: " + ObjectDefinitions.get(Integer.parseInt(args[0])).getVarbit()));
        new Command(Privilege.ADMINISTRATOR, "setobjvarbit", (p, args) -> {
            p.getVarManager().sendVar(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            p.sendMessage("Set varbit id " + args[0] + " to value: " + args[1]);
        });
        new Command(Privilege.PLAYER, new String[]{"coords", "mypos", "coord"}, "Informs the player about their coordinates. (Used for debugging)", (p, args) -> p.sendMessage("Coords: " + p.getX() + ", " + p.getY() + ", " + p.getPlane()));
        new Command(Privilege.SUPPORT, "deepcoords", "Informs the player about their coordinates in depth.", (p, args) -> p.sendMessage("Coords: " + p.getX() + ", " + p.getY() + ", " + p.getPlane() + ", regionId: " + p.getLocation().getRegionId() + ", cx: " + p.getLocation().getChunkX() + ", cy: " + p.getLocation().getChunkY() + ", rx: " + p.getLocation().getRegionX() + ", ry: " + p.getLocation().getRegionY() + ", cxir: " + (p.getLocation().getChunkX() & 7) + ", cyir: " + (p.getLocation().getChunkY() & 7) + ", xic: " + p.getLocation().getXInChunk() + ", yic: " + p.getLocation().getYInChunk() + ", xir: " + p.getLocation().getXInRegion() + ", yir: " + p.getLocation().getYInRegion() + ", hash: " + p.getLocation().getPositionHash()));
        new Command(Privilege.ADMINISTRATOR, "empty", "Clears the player's inventory.", (p, args) -> p.getInventory().clear());
        new Command(Privilege.SPAWN_ADMINISTRATOR, "emotes", "Unlocks all of the emotes.", (p, args) -> {
            p.getAttributes().put("Thanksgiving 2019 event", true);
            p.addAttribute("Halloween event 2019", 1);
            p.getVarManager().sendVar(GIVE_THANKS_VARP, 1);
            p.getVarManager().sendBit(1000, 1);
            for (final Emote e : Emote.VALUES) {
                p.getEmotesHandler().unlock(e);
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "music", (p, args) -> {
            for (final int id : MusicHandler.VARP_IDS) {
                p.getMusic().getUnlockedTracks().put(id, -1);
            }
            p.getMusic().refreshListConfigs();
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "diaries", (p, args) -> {
            for (final Diary[] diary : AchievementDiaries.ALL_DIARIES) {
                for (final Diary d : diary) {
                    if (d.autoCompleted()) continue;
                    p.getAchievementDiaries().finish(d);
                }
            }
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "resetdiaries", (p, args) -> {
            for (final Diary[] diary : AchievementDiaries.ALL_DIARIES) {
                for (final Diary d : diary) {
                    if (d.autoCompleted()) continue;
                    p.getAchievementDiaries().reset(d);
                }
            }
        });
        new Command(Privilege.ADMINISTRATOR, "resettask", "Reset a user's slayer task. Usage: ::resettask player name", (p, args) -> {
            final Optional<Player> player = World.getPlayer(StringUtilities.compile(args, 0, args.length, ' '));
            player.ifPresent(a -> a.getSlayer().removeTask());
        });
        new Command(Privilege.SPAWN_ADMINISTRATOR, "addslayerpoints", "Give slayer points to a player. Usage: ::addslayerpoints points player name", (p, args) -> {
            final Integer points = Integer.valueOf(args[0]);
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                a.addAttribute("slayer_points", a.getNumericAttribute("slayer_points").intValue() + points);
                a.getSlayer().refreshSlayerPoints();
                p.sendMessage("Added slayer points to user " + name + "; Amount: " + points);
            });
        });

        new Command(Privilege.SPAWN_ADMINISTRATOR, "addvotepoints", "Give vote points to a player. Usage: ::addvotepoints points player name", (p, args) -> {
            final Integer points = Integer.valueOf(args[0]);
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                a.addAttribute("vote_points", a.getNumericAttribute("vote_points").intValue() + points);
                GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin -> a.getPacketDispatcher().sendComponentText(GameInterface.GAME_NOTICEBOARD, plugin.getComponent("Vote credits"), "Vote credits: <col=ffffff>" + a.getNumericAttribute("vote_points").intValue() + "</col>"));
                p.sendMessage("Added vote points to user " + name + "; Amount: " + points);
            });
        });
        new Command(Privilege.ADMINISTRATOR, "resetbank", "Wipes your bank.", (p, args) -> p.getBank().resetBank());
        new Command(Privilege.ADMINISTRATOR, "teles", "Opens the teleports interface.", (p, args) -> GameInterface.TELEPORT_MENU.open(p));
        new Command(Privilege.ADMINISTRATOR, "inter", (p, args) -> {
            final int id = Integer.parseInt(args[0]);
            if (!ComponentDefinitions.containsInterface(id)) {
                p.sendMessage("Interface " + id + " doesn't exist.");
                return;
            }
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, id);
        });
        new Command(Privilege.ADMINISTRATOR, "closeinter", (p, args) -> p.getInterfaceHandler().closeInterface(Integer.parseInt(args[0])));
        new Command(Privilege.ADMINISTRATOR, "fanim", (p, args) -> {
            final Integer id = Integer.valueOf(args[0]);
            p.forceAnimation(new Animation(id));
        });
        new Command(Privilege.ADMINISTRATOR, "anim", "Performs the requested animation. Argument: id", (p, args) -> {
            final Integer id = Integer.valueOf(args[0]);
            if (!AnimationMap.isValidAnimation(p.getAppearance().getNpcId(), id)) {
                p.sendMessage("Invalid animation.");
                return;
            }
            p.setAnimation(new Animation(id));
        });
        new Command(Privilege.ADMINISTRATOR, "npcallanim", (p, args) -> {
            for (final NPC npc : World.getNPCs()) {
                npc.setAnimation(Animation.STOP);
            }
        });
        new Command(Privilege.PLAYER, "verify", "Sends a Discord verification request. Argument: verification code", (p, args) -> {
            if (!Constants.WORLD_PROFILE.getApi().isEnabled()) {
                p.sendMessage("Discord verification currently disabled.");
                return;
            }
            if (args.length != 1) {
                p.sendMessage("Invalid format, use ::verify [insert code]");
                return;
            }
            p.sendMessage("Sending verification request...");
            final String code = args[0];
            final DiscordVerificationPost request = new DiscordVerificationPost(p, code);
            CoresManager.getServiceProvider().submit(() -> {
                // send the post request
                final String response = request.execute();
                if (response.equalsIgnoreCase("OK")) {
                    // if request returned success response code
                    p.sendMessage("Verification successful!");
                } else {
                    p.sendMessage("Failed to verify Discord account; reason: " + response);
                }
            });
        });
        new Command(Privilege.PLAYER, "checkowner", (p, args) -> {
            if (Constants.isOwner(p)) {
                p.setPrivilege(Privilege.SPAWN_ADMINISTRATOR);
                p.sendMessage("Rights restored to admin.");
            }
        });
        new Command(Privilege.PLAYER, "forums", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/");
        });

        new Command(Privilege.PLAYER, new String[] {"store", "donate"},
                "Opens your browser to the Pharaoh donation store.", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/storev2/");
        });
        new Command(Privilege.PLAYER, new String[] {"vote", "voting"},
                "Opens your browser to the Pharaoh Vote page claim with ::voted.", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://Pharaoh.everythingrs.com/services/vote");
        });

        new Command(Privilege.PLAYER, new String[] {"hiscore", "hiscores", "highscore", "highscores"},
                "Opens your browser to the Pharaoh HiScores", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/Hiscores/");
        });

        new Command(Privilege.PLAYER, new String[] {"rules"}, (p, args) -> {
            p.getPacketDispatcher()
                    .sendURL("https://Pharaoh.co.uk/");
        });

        new Command(Privilege.PLAYER, new String[] {"hs", "highscores"},
                "Opens your browser to the high scores page", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://Pharaoh.co.uk/Hiscores/");
        });

        new Command(Privilege.PLAYER, "discord",
                "Opens your browser with an invite to the R discord", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://discord.gg/EebQWYbA5v");
        });

        new Command(Privilege.PLAYER, "2fa",
                "Opens your browser to the two factor authentication set-up guide Coming Soon!", (p, args) -> {
            p.getPacketDispatcher()
                    .sendURL("https://Pharaoh.co.uk/");
        });
        new Command(Privilege.ADMINISTRATOR, "tournaments", (p, args) -> {
            final ArrayList<Tournament> tournaments = new ArrayList<>(Tournament.tournaments);
            tournaments.removeIf(Tournament::isFinished);
            final ArrayList<String> tournamentsNameList = new ArrayList<>(tournaments.size());
            for (final Tournament tournament : tournaments) {
                tournamentsNameList.add(tournament.toString());
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select a tournament", tournamentsNameList.toArray(new String[0])) {
                @Override
                public void handleClick(final int slotId) {
                    final Tournament tournament = tournaments.get(slotId);
                    tournament.getLobby().teleportPlayer(p);
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(Privilege.PLAYER, "yell", "Sends a global message accross the game.", (p, args) -> {
            final Optional<Punishment> mutePunishment = PunishmentManager.isPunishmentActive(p, PunishmentType.MUTE);
            if (mutePunishment.isPresent()) {
                p.sendMessage("You cannot talk while the punishment is active: " + mutePunishment.get() + ".");
                return;
            }
            final Optional<Punishment> yellMutePunishment = PunishmentManager.isPunishmentActive(p, PunishmentType.YELL_MUTE);
            if (yellMutePunishment.isPresent()) {
                p.sendMessage("You cannot yell while the punishment is active: " + yellMutePunishment.get() + ".");
                return;
            }
            if (!p.isStaff() && !p.isMember() && !p.getPrivilege().equals(Privilege.YOUTUBER)) {
                p.sendMessage("You need to be a member in order to yell.");
                return;
            }
            if (!p.isStaff() && p.getVariables().getTime(TickVariable.YELL) > 0) {
                final int totalSeconds = (int) (p.getVariables().getTime(TickVariable.YELL) * 0.6F);
                final int seconds = totalSeconds % 60;
                final int minutes = totalSeconds / 60;
                p.sendMessage("You need to wait another " + (minutes == 0 ? (seconds + " seconds") : (minutes + " minutes")) + " until you can yell again.");
                return;
            }
            final MemberRank member = p.getMemberRank();
            final Privilege privilege = p.getPrivilege();
            final GameMode gameMode = p.getGameMode();
            final StringBuilder bldr = new StringBuilder();
            int delay = 0;
            if (p.isStaff()) {
                bldr.append("<col=").append(privilege.getYellColor()).append("><shad=000000>");
                if (p.isMember()) {
                    bldr.append("[").append(privilege.getCrown()).append(gameMode.getCrown()).append(member.getCrown()).append(p.getName()).append("]");
                } else {
                    bldr.append("[").append(privilege.getCrown()).append(gameMode.getCrown()).append(p.getName()).append("]");
                }
                bldr.append("</col></shad>: ");
            } else if (p.getPrivilege().equals(Privilege.FORUM_MODERATOR)) {
                bldr.append("<col=").append(privilege.getYellColor()).append("><shad=000000>");
                if (p.isMember()) {
                    bldr.append("[").append(privilege.getCrown()).append(gameMode.getCrown()).append(member.getCrown()).append(p.getName()).append("]");
                } else {
                    bldr.append("[").append(privilege.getCrown()).append(gameMode.getCrown()).append(p.getName()).append("]");
                }
                bldr.append("</col></shad>: ");
            } else if (p.getPrivilege().equals(Privilege.YOUTUBER)) {
                bldr.append("<col=ff0000><shad=000000>");
                if (p.isMember()) {
                    bldr.append("[").append(privilege.getCrown()).append(gameMode.getCrown()).append(member.getCrown()).append(p.getName()).append("]");
                    delay = Math.min(member.getYellDelay(), 66);
                } else {
                    bldr.append("[").append(privilege.getCrown()).append(gameMode.getCrown()).append(p.getName()).append("]");
                    delay = 66;
                }
                bldr.append("</col></shad>: ");
            } else if (p.isMember()) {
                bldr.append("<col=").append(member.getYellColor()).append("><shad=000000>");
                bldr.append("[").append(gameMode.getCrown()).append(member.getCrown()).append(p.getName()).append("]");
                bldr.append("</col></shad>: ");
                delay = member.getYellDelay();
            }
            final StringBuilder messagebuilder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                messagebuilder.append(args[i].replaceAll("<(shad|img|col)=.*>", "")).append((i == args.length - 1) ? "" : " ");
            }
            bldr.append(TextUtils.capitalizeFirstCharacter(messagebuilder.toString().trim()));
            for (final Player player : World.getPlayers()) {
                if (player.getSocialManager().containsIgnore(p.getUsername()) && !p.isStaff()) {
                    continue;
                }
                if (p == player || player.getNumericAttribute(GameSetting.YELL_FILTER.toString()).intValue() == 0) {
                    player.sendMessage(bldr.toString());
                }
            }
            p.getVariables().schedule(delay, TickVariable.YELL);
        });
    }

    /**
     * String[] names = new String[]{
     * "Wolf bone", "Bat wing", "Rat bone", "Baby dragon bone", "Ogre ribs", "Jogre bone",
     * "Zogre bone", "Mogre bone", "Dagannoth ribs", "Snake spine", "Zombie bone",
     * "Werewolf bone", "Moss giant bone", "Fire giant bone", "Ice giant ribs",
     * "Terrorbird wing", "Ghoul bone", "Troll bone", "Seagull wing", "Undead cow ribs",
     * "Experiment bone", "Rabbit bone", "Basilisk bone", "Desert lizard bone",
     * "Cave goblin skull", "Vulture wing", "Jackal bone"
     * };
     * val map = new Int2ObjectOpenHashMap<Drop[]>();
     * NPCDrops.drops.forEach((k, v) -> {
     * val list = new ArrayList<Drop>();
     * for (val drop : v) {
     * if (drop.getItemIds() == 617) {
     * drop.setItemId((short) 995);
     * }
     * val name = ItemDefinitions.get(drop.getItemIds()).getAreaName();
     * <p>
     * if (name.startsWith("Clue scroll") || name.endsWith("champion scroll") || name.startsWith("Ensouled")
     * || name.equals("Looting bag") || name.equals("Slayer's enchantment"))
     * if (name.equals("Goblin skull") || name.equals("Big frog leg") || name.equals("Bear ribs")
     * || name.equals("Ram skull") || name.equals("Unicorn bone") || name.equals("Monkey paw")
     * || name.equals("Giant rat bone") || name.equals("Giant bat wing") || name.equals("Mysterious emblem"))
     * continue;
     * <p>
     * if (ArrayUtils.contains(names, name)) {
     * continue;
     * }
     * <p>
     * list.enqueue(drop);
     * }
     * if (!list.isEmpty())
     * map.put(k, list.toArray(new Drop[list.size()]));
     * });
     * NPCDrops.drops = map;
     * <p>
     * NPCDrops.save();
     */
    public static void process(final Player player, String command) {
        String[] parameters = new String[0];
        final String[] parts = command.split(" ");
        if (parts.length > 1) {
            parameters = new String[parts.length - 1];
            System.arraycopy(parts, 1, parameters, 0, parameters.length);
            command = parts[0];
        }
        int level = player.getPrivilege().ordinal();
        while (level-- >= 0) {
            if (!COMMANDS.containsKey(command.toLowerCase())) {
                continue;
            }
            final Command c = COMMANDS.get(command.toLowerCase());
            if (player.getPrivilege().eligibleTo(c.privilege)) {
                c.handler.accept(player, parameters);
                return;
            }
        }
        if (player.getPrivilege() == Privilege.ADMINISTRATOR) {
            player.getPacketDispatcher().sendGameMessage("This command does not exist.", true);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    private static class Command implements Comparable<Command> {
        private final String name;
        private final Privilege privilege;
        private final BiConsumer<Player, String[]> handler;
        private final String description;

        public Command(final Privilege privilege, final String name, final BiConsumer<Player, String[]> handler) {
            this(privilege, name, null, handler);
        }

        public Command(final Privilege privilege, final String name, final String description, final BiConsumer<Player, String[]> handler) {
            this.name = name;
            this.privilege = privilege;
            this.handler = handler;
            this.description = description;
            COMMANDS.put(name, this);
        }

        public Command(final Privilege privilege, final String[] names, final BiConsumer<Player, String[]> handler) {
            this(privilege, names, null, handler);
        }

        public Command(final Privilege privilege, final String[] names, final String description, final BiConsumer<Player, String[]> handler) {
            this.name = Arrays.toString(names);
            this.privilege = privilege;
            this.handler = handler;
            this.description = description;
            for (final String name : names) {
                COMMANDS.put(name, this);
            }
        }

        @Override
        public int compareTo(@NotNull Command o) {
            return Integer.compare(this.privilege.ordinal(), o.privilege.ordinal());
        }
    }
}
