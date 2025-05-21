package com.zenyte.game.world.entity.player.dailychallenge.challenge;

import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dailychallenge.ChallengeCategory;
import com.zenyte.game.world.entity.player.dailychallenge.ChallengeDifficulty;
import com.zenyte.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import com.zenyte.game.world.entity.player.dailychallenge.reward.impl.ExperienceReward;

import static com.zenyte.game.world.entity.player.dailychallenge.ChallengeDifficulty.*;

/**
 * @author Tommeh | 03/05/2019 | 22:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum SkillingChallenge implements DailyChallenge {
    /**
     * DO NOT CHANGE THE NAMES OF THE ENUM ENTRIES AS IT MIGHT FUCK UP DESERIALIZATION FOR EXISTING CHARACTERS
     */
    CRAFT_OPAL_BRACELETS("Craft Opal Bracelets", EASY, 20, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 2000)),
    CRAFT_GOLD_BRACELETS("Craft Gold bracelets", EASY, 25, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 2100)),
    CRAFT_TOPAZ_AMULETS("Craft Topaz Amulets", EASY, 15, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 2200)),
    CRAFT_SAPPHIRE_NECKLACES("Craft Sapphire Necklaces", EASY, 20, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 2450)),
    CRAFT_EMERALD_RINGS("Craft Emerald Rings", EASY, 20, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 2750)),
    CRAFT_UNPOWERED_ORBS("Craft Unpowered Orbs", MEDIUM, 150, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 2900)),
    CRAFT_DIAMOND_RINGS("Craft Diamond Rings", HARD, 10, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 3550)),
    CRAFT_DRAGONSTONE_AMULETS("Craft Dragonstone Amulets", HARD, 5, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 3600)),
    CRAFT_BLACK_DRAGONHIDE_BODIES("Craft Black Dragonhide Bodies", ELITE, 15, Skills.CRAFTING, new ExperienceReward(Skills.CRAFTING, 4500)),
    CLEAN_MARRENTILLS("Clean Grimy Marrentills", MEDIUM, 35, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 2300)),
    CLEAN_RANARRS("Clean Grimy Ranarrs", MEDIUM, 35, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 2600)),
    MAKE_PRAYER_POTIONS("Make Prayer Potions", MEDIUM, 20, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 2900)),
    MAKE_SUPER_RESTORES("Make Super Restore Potions", HARD, 35, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 3500)),
    CLEAN_CADANTINES("Clean Grimy Cadantines", MEDIUM, 35, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 3600)),
    MAKE_WEAPON_POISON_PLUS_PLUS_POTIONS("Make Weapon Poison++ Potions", HARD, 5, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 3800)),
    MAKE_SARADOMIN_BREWS("Make Saradomin Brews", ELITE, 25, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 4500)),
    MAKE_SUPER_COMBAT_POTIONS("Make Super Combat Potions", ELITE, 25, Skills.HERBLORE, new ExperienceReward(Skills.HERBLORE, 5000)),
    MINE_CLAY("Mine Clay", EASY, 50, Skills.MINING, new ExperienceReward(Skills.MINING, 2000)),
    MINE_IRON_ORES("Mine Iron ores", EASY, 50, Skills.MINING, new ExperienceReward(Skills.MINING, 2200)),
    MINE_ESSENCE("Mine Rune/Pure Essence", EASY, 100, Skills.MINING, new ExperienceReward(Skills.MINING, 2500)),
    MINE_SILVER_ORES("Mine Silver Ore", MEDIUM, 75, Skills.MINING, new ExperienceReward(Skills.MINING, 3200)),
    MINE_SANDSTONE("Mine Sandstone Rocks", MEDIUM, 100, Skills.MINING, new ExperienceReward(Skills.MINING, 3000)),
    MINE_GOLD_ORES("Mine Gold Ore", MEDIUM, 75, Skills.MINING, new ExperienceReward(Skills.MINING, 3200)),
    MINE_MITHRIL_ORES("Mine Mithril ores", HARD, 50, Skills.MINING, new ExperienceReward(Skills.MINING, 3900)),
    MINE_RUNITE_ORES("Mine Runite Ores", ELITE, 25, Skills.MINING, new ExperienceReward(Skills.MINING, 4500)),
    MINE_AMETHYST("Mine Amethyst", ELITE, 50, Skills.MINING, new ExperienceReward(Skills.MINING, 5000)),
    CRAFT_WATER_RUNES("Craft Water Runes", EASY, 200, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 1900)),
    CRAFT_FIRE_RUNES("Craft Fire Runes", EASY, 200, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 1900)),
    CRAFT_COSMIC_RUNES("Craft Cosmic Runes", EASY, 150, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 2000)),
    CRAFT_LAW_RUNES("Craft Law Runes", MEDIUM, 150, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 2400)),
    CRAFT_NATURE_RUNES("Craft Nature Runes", MEDIUM, 150, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 2400)),
    CRAFT_DEATH_RUNES("Craft Death Runes", HARD, 100, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 3500)),
    CRAFT_WRATH_RUNES("Craft Wrath Runes", ELITE, 100, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 4400)),
    CRAFT_SOUL_RUNES("Craft Soul Runes", ELITE, 175, Skills.RUNECRAFTING, new ExperienceReward(Skills.RUNECRAFTING, 4600)),
    HARVEST_MARRENTILLS("Harvest Marrentills", EASY, 50, Skills.FARMING, new ExperienceReward(Skills.FARMING, 2550)),
    HARVEST_RANARRS("Harvest Ranarrs", MEDIUM, 50, Skills.FARMING, new ExperienceReward(Skills.FARMING, 2750)),
    HARVEST_SNAPDRAGONS("Harvest Snapdragons", HARD, 50, Skills.FARMING, new ExperienceReward(Skills.FARMING, 3500)),
    HARVEST_SNAPE_GRASS("Harvest Snape Grass", HARD, 60, Skills.FARMING, new ExperienceReward(Skills.FARMING, 3800)),
    CHECK_HEALTH_PAPAYA_TREES("Check-health of papaya trees", HARD, 3, Skills.FARMING, new ExperienceReward(Skills.FARMING, 4200)),
    CHECK_HEALTH_MAGIC_TREES("Check-health of magic trees", ELITE, 3, Skills.FARMING, new ExperienceReward(Skills.FARMING, 5700)),
    COOK_WINES("Make Jugs of Wine", EASY, 100, Skills.COOKING, new ExperienceReward(Skills.COOKING, 2500)),
    COOK_KARAMBWAN("Cook Karambwans", EASY, 65, Skills.COOKING, new ExperienceReward(Skills.COOKING, 2600)),
    COOK_TROUT("Cook Trouts", EASY, 35, Skills.COOKING, new ExperienceReward(Skills.COOKING, 1250)),
    COOK_SWORDFISH("Cook Swordfish", MEDIUM, 50, Skills.COOKING, new ExperienceReward(Skills.COOKING, 2000)),
    COOK_MONKFISH("Cook Monkfish", MEDIUM, 50, Skills.COOKING, new ExperienceReward(Skills.COOKING, 3400)),
    COOK_SHARKS("Cook Sharks", HARD, 50, Skills.COOKING, new ExperienceReward(Skills.COOKING, 4000)),
    COOK_WILD_PIES("Bake Wild Pies", ELITE, 20, Skills.COOKING, new ExperienceReward(Skills.COOKING, 4500)),
    COOK_SUMMER_PIES("Bake Summer Pies", ELITE, 30, Skills.COOKING, new ExperienceReward(Skills.COOKING, 4500)),
    PICKPOCKET_HAM_MEMBERS("Pickpocket H.A.M Members", EASY, 20, Skills.THIEVING, new ExperienceReward(Skills.THIEVING, 2400)),
    PICKPOCKET_MASTER_FARMERS("Pickpocket Master Farmers", MEDIUM, 40, Skills.THIEVING, new ExperienceReward(Skills.THIEVING, 3600)),
    PICKPOCKET_GEM_STALL_TZHAARS("Steal 20 times from the gem stall in Mor Ul Rek", HARD, 20, Skills.THIEVING, new ExperienceReward(Skills.THIEVING, 3900)),
    PICKPOCKET_ELVES("Pickpocket Elves", ELITE, 50, Skills.THIEVING, new ExperienceReward(Skills.THIEVING, 4500)),
    COMPLETE_LAPS_VARROCK_COURSE("Complete laps of the Varrock Rooftop course", MEDIUM, 20, Skills.AGILITY, new ExperienceReward(Skills.AGILITY, 2800)),
    COMPLETE_LAPS_WILDERNESS_AGILITY_COURSE("Complete laps of the Wilderness course", MEDIUM, 20, Skills.AGILITY, new ExperienceReward(Skills.AGILITY, 3500)),
    COMPLETE_LAPS_SEERS_COURSE("Complete laps of the Seers Rooftop course", HARD, 20, Skills.AGILITY, new ExperienceReward(Skills.AGILITY, 4100)),
    COMPLETE_WILDERNESS_ASSIGNMENTS("Complete Wilderness Slayer Assignments", MEDIUM, 5, Skills.SLAYER, new ExperienceReward(Skills.SLAYER, 5000)),
    COMPLETE_NIEVE_ASSIGNMENTS("Complete Assignments from Nieve", HARD, 5, Skills.SLAYER, new ExperienceReward(Skills.SLAYER, 8000)),
    COMPLETE_DURADEL_ASSIGNMENTS("Complete Assignments from Duradel", HARD, 5, Skills.SLAYER, new ExperienceReward(Skills.SLAYER, 7800)),
    COMPLETE_MAZCHNA_ASSIGNMENTS("Complete Assignments from Mazchna", EASY, 5, Skills.SLAYER, new ExperienceReward(Skills.SLAYER, 4000)),
    SMELT_IRON_BARS("Smelt Iron Bars", EASY, 150, Skills.SMITHING, new ExperienceReward(Skills.SMITHING, 3000)),
    SMELT_GOLD_BARS("Smelt Gold Bars", MEDIUM, 200, Skills.SMITHING, new ExperienceReward(Skills.SMITHING, 4000)),
    SMELT_MITHRIL_BARS("Smelt Mithril Bars", MEDIUM, 100, Skills.SMITHING, new ExperienceReward(Skills.SMITHING, 6800)),
    SMELT_ADAMANT_BARS("Smelt Adamant Bars", HARD, 100, Skills.SMITHING, new ExperienceReward(Skills.SMITHING, 5750)),
    SMELT_RUNITE_BARS("Smelt Runite Bars", ELITE, 75, Skills.SMITHING, new ExperienceReward(Skills.SMITHING, 6500)),
    SMITH_RUNE_PLATEBODIES("Smith Rune Platebodies", ELITE, 15, Skills.SMITHING, new ExperienceReward(Skills.SMITHING, 7000)),
    FLETCH_IRON_DARTS("Fletch Iron Darts", EASY, 250, Skills.FLETCHING, new ExperienceReward(Skills.FLETCHING, 2500)),
    FLETCH_BROAD_BOLTS("Fletch Broad Bolts", MEDIUM, 200, Skills.FLETCHING, new ExperienceReward(Skills.FLETCHING, 3000)),
    FLETCH_RUBY_BOLTS("Fletch Ruby Bolts", HARD, 200, Skills.FLETCHING, new ExperienceReward(Skills.FLETCHING, 4500)),
    FLETCH_MAGIC_SHORTBOWS("Fletch Magic Shortbows (u)", HARD, 10, Skills.FLETCHING, new ExperienceReward(Skills.FLETCHING, 4600)),
    CATCH_GREATER_SIREN("Catch Greater Siren", ELITE, 50, Skills.HUNTER, new ExperienceReward(Skills.HUNTER, 7500)),
    FLETCH_DRAGON_BOLTS("Fletch Dragon Bolts", ELITE, 100, Skills.FLETCHING, new ExperienceReward(Skills.FLETCHING, 5000)),
    CATCH_BLACK_CHINCHOMPAS("Catch Black Chinchompas", ELITE, 50, Skills.HUNTER, new ExperienceReward(Skills.HUNTER, 5000)),
    CATCH_RED_CHINCHOMPAS("Catch Red Chinchompas", HARD, 75, Skills.HUNTER, new ExperienceReward(Skills.HUNTER, 4500)),
    CATCH_GREY_CHINCHOMPAS("Catch Grey Chinchompas", HARD, 100, Skills.HUNTER, new ExperienceReward(Skills.HUNTER, 4600)),
    CHOP_WILLOW_LOGS("Chop Willow Logs", EASY, 100, Skills.WOODCUTTING, new ExperienceReward(Skills.WOODCUTTING, 3250)),
    CHOP_MAHOGANY_LOGS("Chop Mahogany Logs", MEDIUM, 100, Skills.WOODCUTTING, new ExperienceReward(Skills.WOODCUTTING, 4000)),
    CHOP_MAGIC_LOGS("Chop Magic Logs", HARD, 50, Skills.WOODCUTTING, new ExperienceReward(Skills.WOODCUTTING, 4500)),
    CHOP_REDWOOD_LOGS("Chop Redwood Logs", ELITE, 50, Skills.WOODCUTTING, new ExperienceReward(Skills.WOODCUTTING, 5000)),
    CATCH_KARAMBWANJI("Catch Karambwanji", EASY, 200, Skills.FISHING, new ExperienceReward(Skills.FISHING, 2600)),
    CATCH_TROUT("Catch Trout", EASY, 200, Skills.FISHING, new ExperienceReward(Skills.FISHING, 2600)),
    CATCH_SWORDFISH("Catch Swordfish", EASY, 200, Skills.FISHING, new ExperienceReward(Skills.FISHING, 2600)),
    CATCH_MONKFISH("Catch Monkfish", MEDIUM, 150, Skills.FISHING, new ExperienceReward(Skills.FISHING, 3100)),
    CATCH_SHARKS("Catch Sharks", HARD, 100, Skills.FISHING, new ExperienceReward(Skills.FISHING, 4200)),
    CATCH_ANGLERFISH("Catch Anglerfish", ELITE, 75, Skills.FISHING, new ExperienceReward(Skills.FISHING, 4750)),
    OFFER_SUPERIOR_DRAGON_BONES_CHAOS_ALTAR("Offer Superior Dragon Bones to the Wilderness Altar (lvl-38)", ELITE, 20, Skills.PRAYER, new ExperienceReward(Skills.PRAYER, 5300)),
    BURY_BIG_BONES("Bury Big Bones", EASY, 50, Skills.PRAYER, new ExperienceReward(Skills.PRAYER, 3200)),
    OFFER_DRAGON_BONES("Offer Dragon Bones", HARD, 50, Skills.PRAYER, new ExperienceReward(Skills.PRAYER, 4800));
    public static final SkillingChallenge[] all = values();
    private final String name;
    private final ChallengeDifficulty difficulty;
    private final ChallengeReward[] rewards;
    private final int length;
    private final int skill;

    SkillingChallenge(final String name, final ChallengeDifficulty difficulty, final int length, final int skill, final ChallengeReward... rewards) {
        this.name = name;
        this.difficulty = difficulty;
        this.length = length;
        this.skill = skill;
        this.rewards = rewards;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ChallengeCategory getCategory() {
        return ChallengeCategory.SKILLING;
    }

    @Override
    public ChallengeDifficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public ChallengeReward[] getRewards() {
        return rewards;
    }

    @Override
    public int getLength() {
        return length;
    }

    public int getSkill() {
        return this.skill;
    }
}
