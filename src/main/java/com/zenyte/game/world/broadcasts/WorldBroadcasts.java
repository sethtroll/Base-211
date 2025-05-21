package com.zenyte.game.world.broadcasts;

import com.zenyte.api.client.query.adventurerslog.AdventurersLogIcon;
import com.zenyte.api.client.webhook.GlobalBroadcastWebhook;
import com.zenyte.discord.DiscordThread;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.minigame.inferno.model.InfernoCompletions;
import com.zenyte.game.content.treasuretrails.rewards.BroadcastedTreasure;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.enums.RareDrop;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.*;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tommeh | 4-2-2019 | 22:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WorldBroadcasts {
    public static final List<String> DISABLED_SKILLS = Arrays.asList("attack", "strength", "defence", "hitpoints");
    public static final String[] HELPFUL_TIPS = {

            "Did you know: Items lost on death will remain invisible on the ground for 30 minutes(boosted to 60 for UIM), followed by 5 minutes of visibility to everyone.",
            "Did you know: You can filter this message in the game settings.",
            "Did you know: You will lose any untradable past 20 wilderness! Be Aware.",
            "Did you know: You can Teleport to the agility pyramid at Portal Each lap pays 75k !",
            "Did you know: You can Buy the nex teleport at home and Nex Fight Scrolls from Slayer Masters!",
            "Did you know: Pharaoh Points are given to those actively playing Pharaoh. These points can be spent at the achievement hall for cosmetic items.",
            "Did you know: You can imbue items such as slayer helmets and dagannoth rings using Imbue Tokens purchasable from the Pharaoh store North off Home Area.",
            "Did you know: The box of restoration can be used once every ten minutes (or less) to restore your stats including hitpoints and prayer, run energy, and cure poison.",
            "Did you know: You can use regular teleports, fairy rings, spirit trees, gnome gliders and the Pharaoh portal to travel around the world.",
            "Did you know: You can complete daily challenges to obtain extra experience.",
            "Did you know: Pharaoh has full RuneLite integration? You can click the wrench icon on the client to customize the RuneLite settings to your liking.",
            // "Did you know: You can enable two factor authentication (2FA) to prevent unauthorized logins to your accounts. Enable it in the Game noticeboard tab!",
            "Did you know: You can enable or disable level-up dialogues and broadcasts in the Game Settings menu in the Game noticeboard tab.",
            "Did you know: You can view the drop rate of any monster or item using the Drop Viewer in your Game noticeboard tab.",
            "Did you know: You can easily access our Website, Discord and Store from your Game noticeboard tab.",
            "Did you know: Your home, Varrock, Camelot, and Watchtower teleport can be right clicked so you can teleport to additional locations.",
            "Did you know: Runecrafting yields double the normal amount of runes per essence in addition to multiple runes at certain Runecrafting levels.",
            "Did you know: You can change your displayed experience drops by right clicking the “XP” orb and choosing “XP multiplier.”",
            "Did you know: You can fully customize your F-keys in the Options tab.",
            // "Did you know: You can rewatch the Pharaoh tutorial by talking to the Pharaoh guide in the achievement hall.",
            "Did you know: You can change your spellbook using the altar In the house north of the bank at home.",
            "Did you know: The Magic shop sells Pharaoh home teleport tablets that allow you to instantly teleport to the home area.",
            "Did you know: You can vote for Pharaoh to receive vote points once voted use ::voted.",
            "Did you know: You can track your character's progress and achievements by visiting the Adventurer’s Log.",
            "Did you know: There's a chance Krystilia will upgrade your emblem when completing a Wilderness slayer assignment.",
            "Did you know: You can buy RFD gloves from the chest in the Lumbridge Castle Basement.",
            "Did you know: You can view current buy and sell offers in the Grand Exchange with the \"Offers Viewer\" on the G.E Interface.",
            "Did you know: All farming timers are half that of Oldschool Runescape.",
            "Did you know: We provide a Wiki command and quick link under the world map that directs you to the Oldschool Runescape Wiki which is rather accurate.",
            "Did you know: We have a help chat called \"Pharaoh\" for any new and seasoned players alike. Join via the clan chat interface.",
            "Did you know: Teleport scrolls, which unlock convenient portal boss teleports, can be purchased from other players or from our store.",
            // "Did you know: You can play Pharaoh on your favorite Android mobile device by downloading the APK via our website.",
            "Did you know: You can purchase a dramen staff from the Magic shop at home to navigate via the fairy rings teleport system.",
            //"Did you know: You can find all of our community made guides with the ::guides command.",
            "Did you know: Donators start the Fight Caves minigame at wave 31.",
            "Did you know: You can start slayer in Burthorpe with Turael. All Slayer Masters and NPCs are in the correct locations.",
            "Did you know: You can purchase fully charged custom starter weapons from the Pharaoh guide for 200K each.",
            "Did you know: Donation, Vote, and Pharaoh Point Shop is South of Home.",
            "Did you know: Zahur in Edgeville can add herbs to vials of water and crush secondary ingredients for you.",
            "Did you know: You can apply to be part of the quality assurance team on our Discord and be the first to test upcoming content."

    };

    private static Map<String, String> imageMap = new HashMap<>();

    static {
        imageMap.put("0", "<:img0:1208789377757741126>");
        imageMap.put("1", "<:img1:1208789108676108318>");
        imageMap.put("2", "<:img2:1208789058352840734>");
        imageMap.put("3", "<:img3:1208788873862320158>");
        imageMap.put("10", "<:img10:1204768425961066537>");
        imageMap.put("44", "<:img44:1208789433130819584>");
        imageMap.put("45", "<:img45:1208789488898146315>");
        imageMap.put("46", "<:img46:1208791431980916787>");
        imageMap.put("47", "<:img47:1208791461483782224>");
        imageMap.put("48", "<:img48:1208791491485376563>");
        imageMap.put("49", "<:img49:1208791522166710324>");
        imageMap.put("50", "<:img50:1208791559063998504>");

    }

    public static void sendMessage(final String message, final BroadcastType type, final boolean aboveChatbox) {
        final MessageType messageType = aboveChatbox ? MessageType.GLOBAL_BROADCAST : MessageType.UNFILTERABLE;
        for (final Player player : World.getPlayers()) {
            if (type.getSetting().isPresent() && player.getNumericAttribute(type.getSetting().get().toString()).intValue() == 0) {
                continue;
            }
            player.sendMessage(message, messageType);
        }
    }


    public static String formatMessageForDiscord(String message) {
        Pattern pattern = Pattern.compile("<img=([0-9]+)>");
        Matcher matcher = pattern.matcher(message);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String num = matcher.group(1);
            String replacement = imageMap.getOrDefault(num, matcher.group(0));

            replacement = Matcher.quoteReplacement(replacement);

            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }




    public static void broadcast(final Player player, final BroadcastType type, final Object... args) {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder secondaryBuilder = new StringBuilder();
        final int icon = type.getIcon();
        final String color = type.getColor();
        Boolean broadcast = true;
        builder.append("<img=").append(icon).append("><col=").append(color).append(">").append("<shad=000000>");
        switch (type) {
            case MYSTERY_BOX_RARE_ITEM:
            case COSMETIC_BOX_RARE_ITEM: {
                final int id = Integer.parseInt(args[0].toString());
                final Item it = new Item(id);
                builder.append("News: ");
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getPrivilege().getCrown());
                secondaryBuilder.append(player.getMemberRank().getCrown());
                secondaryBuilder.append(" has found ");
                secondaryBuilder.append(Utils.getAOrAn(it.getName()));
                secondaryBuilder.append(" ");
                secondaryBuilder.append(it.getName()).append(" in a ").append(type == BroadcastType.MYSTERY_BOX_RARE_ITEM ? "mystery" : "cosmetic").append(" box!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(it.getId() + ".png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(it.getId() + ".png", secondaryBuilder.toString(), "Unboxed! Rare item found!").execute();
                DiscordThread.sendDropMessage(formatMessageForDiscord(secondaryBuilder.toString()));
            }
            break;

            case COMBAT_ACHIEVEMENTS: {
                final String tierName = args[0].toString();
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has completed the ");
                secondaryBuilder.append(tierName);
                secondaryBuilder.append(" Combat Achievements!");
                builder.append(secondaryBuilder.toString());
                player.sendAdventurersEntry(AdventurersLogIcon.MINIGAME.getLink(), secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(AdventurersLogIcon.MINIGAME.getLink(), secondaryBuilder.toString(), "Combat Achievement Completed!").execute();
            }
            break;
            case TRADE_IN: {
                final int value = Integer.parseInt(args[0].toString());
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has donated ");
                secondaryBuilder.append(value/1000000);
                secondaryBuilder.append("M worth of items or gp to the well of goodwill!");
                builder.append(secondaryBuilder.toString());
                new GlobalBroadcastWebhook(ItemId.COINS_6964 + ".png", secondaryBuilder.toString(), "Well Of Goodwill Donation Made!").execute();
            }
            break;
            case WELL_EVENT: {
                final String eventName = args[0].toString();
                final int time = Integer.parseInt(args[1].toString());
                builder.append("News: ");
                secondaryBuilder.append("The total gp in the the well of goodwill exceeded 200M, ");
                if (!eventName.equals("DEPO")) {
                    secondaryBuilder.append(time);
                }
                switch (eventName) {
                    case "COX":
                        secondaryBuilder.append(" hours of COX Boost have been added.");
                        break;
                    case "PURPLES":
                        secondaryBuilder.append(" hours of higher COX Purple Rates have been added.");
                        break;
                    case "DEPO":
                        secondaryBuilder.append("the special deposit boxes around Pharaoh have been unlocked for another ");
                        secondaryBuilder.append(time);
                        secondaryBuilder.append(" hours.");
                        break;
                    case "BXP":
                        secondaryBuilder.append(" hours of BXP have been added.");
                        break;
                    case "SLAYER":
                        secondaryBuilder.append(" hours of Bonus Slayer Points have been added.");
                        break;
                    case "TOB":
                        secondaryBuilder.append(" hours of TOB Boost have been added.");
                        break;
                }
                builder.append(secondaryBuilder.toString());
                new GlobalBroadcastWebhook(AdventurersLogIcon.MINIGAME.getLink(), secondaryBuilder.toString(), "Event live!").execute();
            }
            break;



            case TREASURE_TRAILS:
                if (!(args[0] instanceof Integer) || !(args[1] instanceof String)) {
                    return;
                }
                final int id = Integer.parseInt(args[0].toString());
                if (!BroadcastedTreasure.isBroadcasted(id)) {
                    return;
                }
                final String name = ItemDefinitions.getOrThrow(id).getName();
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received ");
                if (name.contains("gloves") || name.contains("vambraces") || name.contains("gauntlets") || name.contains("boots") || name.contains("manacles") || name.contains("sandals") || name.contains("legs")) {
                    secondaryBuilder.append("a pair of ").append(name);
                } else {
                    secondaryBuilder.append(Utils.getAOrAn(name));
                    secondaryBuilder.append(" ");
                    secondaryBuilder.append(name);
                }
                final String tier = args[1].toString();
                secondaryBuilder.append(" from ").append(Utils.getAOrAn(tier)).append(" ").append(tier).append(" clue scroll on casket ").append(player.getNumericAttribute("completed " + tier + " treasure trails"));
                secondaryBuilder.append("!");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getPrivilege().getCrown());
                secondaryBuilder.append(player.getMemberRank().getCrown());
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(id + ".png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(id + ".png", secondaryBuilder.toString(), "New loot! Treasure found!").execute();
                DiscordThread.sendDropMessage(formatMessageForDiscord(secondaryBuilder.toString()));
                break;








            case RARE_DROP:
                if (!(args[0] instanceof Item item) || !(args[1] instanceof String)) {
                    return;
                }
                if (!RareDrop.contains(item)) {
                    return;
                }
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getPrivilege().getCrown());
                secondaryBuilder.append(player.getMemberRank().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received ");
                secondaryBuilder.append(Utils.getAOrAn(item.getName()));
                secondaryBuilder.append(" ");
                secondaryBuilder.append(item.getName()).append(" drop ");
                final String fromString = args[1].toString();
                if (fromString.endsWith("Chambers of Xeric")) {
                    secondaryBuilder.append("from ").append(fromString).append(" on chest ").append(player.getNumericAttribute(fromString.startsWith("Challenge Mode") ? "challengechambersofxeric" : "chambersofxeric").intValue());
                } else {
                    secondaryBuilder.append("from ").append(Utils.getAOrAn(fromString)).append(" ").append(fromString);
                    if (NotificationSettings.isKillcountTracked(fromString)) {
                        secondaryBuilder.append(" on killcount ").append(player.getNotificationSettings().getKillcount(fromString) + 1);
                    }
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(item.getId() + ".png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(item.getId() + ".png", secondaryBuilder.toString(), "New loot! Rare drop received!").execute();
                DiscordThread.sendDropMessage(formatMessageForDiscord(secondaryBuilder.toString()));
                break;
            case LVL_99:
                if (!(args[0] instanceof Integer)) {
                    return;
                }
                int skill = (Integer) args[0];
                String skillName = Skills.getSkillName(skill);
                if (player.getCombatXPRate() == 50 && DISABLED_SKILLS.contains(skillName.toLowerCase()))
                    broadcast = false;
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has achieved level 99 ");
                secondaryBuilder.append(skillName);
                secondaryBuilder.append(" on x").append(player.getCombatXPRate());
                if (player.getCombatXPRate() != player.getSkillingXPRate()) {
                    secondaryBuilder.append('/').append(player.getSkillingXPRate());
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(skillName.toLowerCase() + ".png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(skillName.toLowerCase() + ".png", secondaryBuilder.toString(), "Gains! Level 99 reached!").execute();
                break;
            case MAXED:
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has achieved level 99 in all skills");
                secondaryBuilder.append(" on x").append(player.getCombatXPRate());
                if (player.getCombatXPRate() != player.getSkillingXPRate()) {
                    secondaryBuilder.append('/').append(player.getSkillingXPRate());
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(AdventurersLogIcon.OVERALL_SKILLING, secondaryBuilder.toString());
                new GlobalBroadcastWebhook(AdventurersLogIcon.OVERALL_SKILLING.getLink(), secondaryBuilder.toString(), "Is it over? New maxed player enters the battlefield!").execute();
                break;
            case XP_200M:
                if (!(args[0] instanceof Integer)) {
                    return;
                }
                skill = (Integer) args[0];
                skillName = Skills.getSkillName(skill);
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has achieved 200 million XP in ");
                secondaryBuilder.append(skillName);
                secondaryBuilder.append(" on x").append(player.getCombatXPRate());
                if (player.getCombatXPRate() != player.getSkillingXPRate()) {
                    secondaryBuilder.append('/').append(player.getSkillingXPRate());
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(skillName.toLowerCase() + ".png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(skillName.toLowerCase() + ".png", secondaryBuilder.toString(), "Maxed! 200m Achieved!").execute();
                break;
            case PET:
                if (!(args[0] instanceof Pet pet)) {
                    return;
                }
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received the ");
                secondaryBuilder.append(NPCDefinitions.get(pet.petId()).getName());
                secondaryBuilder.append(" pet!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(pet.itemId() + ".png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(pet.itemId() + ".png", secondaryBuilder.toString(), "Woof! A new friend has been found!").execute();
                break;
            case GAMBLE_FIRECAPE:
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received the TzRek-Jad pet by gambling their fire cape!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry("13225.png", secondaryBuilder.toString(), false);
                new GlobalBroadcastWebhook(BossPet.TZREK_JAD.getItemId(), secondaryBuilder.toString(), "Lucky! A new friend appears!").execute();
                break;
            case HCIM_DEATH:
                if (player.getSkills().getTotalLevel() < 500) {
                    return;
                }
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has died as a Hardcore ");
                secondaryBuilder.append(player.getAppearance().getGender());
                secondaryBuilder.append(" with a skill total of ");
                secondaryBuilder.append(player.getSkills().getTotalLevel());
                if (args[0] instanceof Entity source) {
                    if (source instanceof Player) {
                        if (source.equals(player)) {
                            secondaryBuilder.append(", by self-inflicted damage!");
                        } else {
                            secondaryBuilder.append(", losing against ");
                            secondaryBuilder.append(((Player) source).getName());
                            secondaryBuilder.append("!");
                        }
                    } else if (source instanceof NPC) {
                        secondaryBuilder.append(", fighting against: ");
                        secondaryBuilder.append(((NPC) source).getDefinitions().getName());
                    }
                } else {
                    secondaryBuilder.append(", by unknown damage!");
                }
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(AdventurersLogIcon.HCIM_DEATH, secondaryBuilder.toString());
                new GlobalBroadcastWebhook(AdventurersLogIcon.HCIM_DEATH.getLink(), secondaryBuilder.toString(), "Unlucky! Another HCIM down!").execute();
                break;
            case HELPFUL_TIP:
                if (!(args[0] instanceof String tip)) {
                    return;
                }
                builder.append(tip);
                break;
            case INFERNO_COMPLETION:
                final GameMode mode = player.getGameMode();
                final int completions = InfernoCompletions.getCompletions(mode);
                final String completion = completions == 1 ? "first" : completions == 2 ? "second" : completions == 3 ? "third" : "";
                builder.append("News: ");
                secondaryBuilder.append(player.getGameMode().getCrown());
                secondaryBuilder.append(player.getName());
                if (!InfernoCompletions.isBroadcasted(player) && !completion.isEmpty()) {
                    secondaryBuilder.append(" is the ").append(completion).append(" ").append(mode.getCrown()).append("</col>").append(GameMode.getTitle(player)).append("<col=").append(type.getColor()).append("> to complete the Inferno").append(", with a combat level of ").append(player.getCombatLevel()).append("!");
                    InfernoCompletions.setBroadcasted(player);
                } else {
                    secondaryBuilder.append(" has completed the Inferno").append(", with a combat level of ").append(player.getCombatLevel()).append("!");
                }
                builder.append(secondaryBuilder);
                new GlobalBroadcastWebhook(ItemId.INFERNAL_CAPE, secondaryBuilder.toString().replaceAll("<col=[a-zA-Z0-9]{6}>", "").replaceAll("</col>", ""), "The JalYt did it!").execute();
                DiscordThread.sendDropMessage(formatMessageForDiscord(secondaryBuilder.toString()));
                break;
        }
        if (broadcast) {
            sendMessage(builder.toString(), type, false);
        }
    }
}
