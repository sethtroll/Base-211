package com.zenyte.game.world.entity.player.action.combat;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 11. nov 2017 : 19:56.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface SpecialAttackScript {

    int WEAPON_SPEED = -1;
    Projectile DESCENT_OF_DRAGONS_FIRST_PROJ = new Projectile(1099, 30, 20, 40, 20, 18, 0, 5);
    Projectile DESCENT_OF_DRAGONS_SECOND_PROJ = new Projectile(1099, 30, 20, 40, 30, 3, 0, 5);
    Projectile DESCENT_OF_DARKNESS_FIRST_PROJ = new Projectile(1101, 30, 20, 40, 20, 18, 0, 5);
    Projectile DESCENT_OF_DARKNESS_SECOND_PROJ = new Projectile(1101, 30, 20, 40, 30, 3, 0, 5);
    Projectile SNAPSHOT_FIRST_PROJ = new Projectile(249, 42, 30, 21, 15, 3, 0, 5);
    Projectile SNAPSHOT_SECOND_PROJ = new Projectile(249, 42, 30, 66, 15, 0, 0, 5);
    Projectile ARMADYL_EYE_PROJ = new Projectile(301, 42, 30, 40, 15, 10, 64, 5);
    Projectile ARMADYL_EY_PROJ = new Projectile(1995, 36, 36, 50, 5, 10, 64, 5);
    Projectile SNIPE_PROJ = new Projectile(698, 42, 30, 40, 15, 10, 64, 5);
    Projectile ANNIHILATE_PROJ = new Projectile(698, 42, 30, 40, 15, 10, 64, 5);
    Projectile SOULSHOT_PROJ = new Projectile(473, 42, 30, 40, 5, 3, 0, 5);
    Projectile CHAINHIT_PROJ = new Projectile(258, 40, 36, 41, 0, 5, 11, 5);
    Projectile HAMSTRING_PROJ = new Projectile(1625, 42, 30, 40, 15, 3, 64, 5);
    Projectile PHANTOM_STRIKE_PROJ = new Projectile(1622, 42, 30, 40, 15, 3, 64, 5);
    Projectile CHAINHIT_CHAIN_PROJ = new Projectile(258, 40, 36, 61, 0, 8, 11, 5);
    Projectile MOMENTUM_THROW_PROJ = new Projectile(1318, 34, 30, 40, 15, 3, 64, 5);
    Projectile TOXIC_SIPHON_PROJ = new Projectile(1043, 35, 36, 32, 21, 0, 105, 7);
    Projectile DUALITY_REGULAR_PROJ = new Projectile(699, 40, 36, 25, 21, 0, 11, 5);
    Projectile DUALITY_POISONOUS_PROJ = new Projectile(1629, 40, 36, 25, 21, 0, 11, 5);
    Projectile PULSATE_PROJ = new Projectile(1547, 23, 15, 51, 16, 25, 64, 10 );
    Animation ORNAMENT_JUDGEMENT_ANIM = new Animation(7645);
    Animation ANCIENT_JUDGEMENT_ANIM = new Animation(9171);


    Animation ANCIENT_ANIM = new Animation(9171);
    Animation JUDGEMENT_ANIM = new Animation(7644);
    Animation ORNAMENT_HEALING_BLADE_ANIM = new Animation(7641);
    Animation HEALING_BLADE_ANIM = new Animation(7640);
    Animation ORNAMENT_WARSTRIKE_ANIM = new Animation(7643);
    Animation WARSTRIKE_ANIM = new Animation(7642);
    Animation ORNAMENT_ICE_CLEAVE_ANIM = new Animation(7639);
    Animation ICE_CLEAVE_ANIM = new Animation(7638);
    Animation ROCK_KNOCKER_INFERNAL_ANIM = new Animation(334);
    Animation ROCK_KNOCKER_ANIM = new Animation(7138);
    Animation FISHSTABBER_DRAGON_ANIM = new Animation(7393);
    Animation FISHSTABBER_INFERNAL_ANIM = new Animation(7394);
    Animation POWER_OF_DEATH_ANIM = new Animation(7083);
    Animation TOXIC_POWER_OF_DEATH_ANIM = new Animation(1720);
    Animation SOL_POWER_OF_DEATH_ANIM = new Animation(7967);
    Animation DUALITY_REGULAR_ANIM = new Animation(8291);
    Animation DUALITY_POISONOUS_ANIM = new Animation(8292);
    Graphics PENANCE_GFX = new Graphics(1284);
    Graphics SHOVE_GFX = new Graphics(254, 40, 96);
    Graphics WHIP_GFX = new Graphics(341, 0, 96);
    Graphics SWEEP_DRAGON_SOUTH_GFX = new Graphics(506, 0, 96);
    Graphics SWEEP_DRAGON_NORTH_GFX = new Graphics(478, 0, 96);
    Graphics SWEEP_DRAGON_EAST_GFX = new Graphics(1172, 0, 96);
    Graphics SWEEP_DRAGON_WEST_GFX = new Graphics(1231, 0, 96);
    Graphics SWEEP_SCYTHE_SOUTH_GFX = new Graphics(478, 20, 92);
    Graphics SWEEP_SCYTHE_NORTH_GFX = new Graphics(506, 20, 92);
    Graphics SWEEP_SCYTHE_EAST_GFX = new Graphics(1172, 20, 92);
    Graphics SWEEP_SCYTHE_WEST_GFX = new Graphics(1231, 20, 92);

    Graphics SWEEP_CRYSTAL_SOUTH_GFX = new Graphics(1233);
    Graphics SWEEP_CRYSTAL_NORTH_GFX = new Graphics(1232);
    Graphics SWEEP_CRYSTAL_EAST_GFX = new Graphics(1234);
    Graphics SWEEP_CRYSTAL_WEST_GFX = new Graphics(1235);
    Graphics ICE_CLEAVE_GFX = new Graphics(369);
    Graphics SARADOMINS_LIGHTNING_GFX = new Graphics(1196, 30, 0);
    Graphics BLESSED_SARADOMINS_LIGHTNING_GFX = new Graphics(1221, 30, 0);
    Graphics SOULSHOT_GFX = new Graphics(474);
    Graphics POWER_OF_DEATH_GFX = new Graphics(1229, 0, 300);
    Graphics SOL_POWER_OF_DEATH_GFX = new Graphics(1517, 0, 300);
    Graphics ANNIHILATE_GFX = new Graphics(1466, 0, 92);
    Graphics TOXIC_POWER_OF_DEATH_GFX = new Graphics(1228, 0, 300);
    Graphics PULSATE_GFX = new Graphics(1548, 56, 60);
    ForceTalk RAMPAGE_FORCETALK = new ForceTalk("Raarrrrrgggggghhhhhhh!");
    ForceTalk ROCK_KNOCKER_FORCETALK = new ForceTalk("Smashing!");
    ForceTalk LUMBER_UP_FORCETALK = new ForceTalk("Chop chop!");
    ForceTalk FISHSTABBER_FORCETALK = new ForceTalk("Here fishy fishies!");
    ForceTalk SANCTUARY_FORCETALK = new ForceTalk("For Camelot!");
    SoundEffect POWERSHOT_SOUND = new SoundEffect(2536);
    SoundEffect SNAPSHOT_SOUND = new SoundEffect(2545);
    SoundEffect THROWNAXE_SOUND = new SoundEffect(2528);
    SoundEffect SNIPE_SOUND = new SoundEffect(1080);
    SoundEffect SOULSHOT_SOUND = new SoundEffect(2546);
    SoundEffect DESCENT_OF_DRAGONS_SOUND = new SoundEffect(3737, 10, -1);
    SoundEffect DARK_BOW_DRAGON_LOCAL_FIRST_SOUND = new SoundEffect(3731);
    SoundEffect DARK_BOW_DRAGON_LOCAL_SECOND_SOUND = new SoundEffect(3733);
    SoundEffect CONCENTRATED_SHOT_SOUND = new SoundEffect(2536);
    SoundEffect POWER_OF_DEATH_SOUND = new SoundEffect(1595, 5, 0);
    SoundEffect QUICKSMASH_SOUND = new SoundEffect(2715);
    SoundEffect HAMMER_BLOW_SOUND = new SoundEffect(2520);
    SoundEffect SEVER_SOUND = new SoundEffect(2540);
    SoundEffect BINDING_TENTACLE_SOUND = new SoundEffect(2713);
    SoundEffect SHATTER_SOUND = new SoundEffect(2541);
    SoundEffect WEAKEN_SOUND = new SoundEffect(225);
    SoundEffect SARADOMINS_LIGHTNING_SWORD_SOUND = new SoundEffect(3869, 5, 0);
    SoundEffect SARADOMINS_LIGHTNING_SOUND = new SoundEffect(3887, 10, 0);
    SoundEffect THE_JUDGEMENT_SOUND = new SoundEffect(3869, 5, 0);
    SoundEffect WARSTRIKE_SOUND = new SoundEffect(3869, 5, 0);
    SoundEffect HEALING_BLADE_SOUND = new SoundEffect(3869, 5, 0);
    SoundEffect ICE_CLEAVE_SOUND = new SoundEffect(3869, 5, 0);
    SoundEffect PENANCE_SWORD_SOUND = new SoundEffect(2715, 1, 10);
    SoundEffect PENANCE_SPECIAL_SOUND = new SoundEffect(1930, 1, 30);
    SoundEffect ABYSSAL_PUNCTURE_SOUND = new SoundEffect(2537);
    SoundEffect PUNCTURE_SOUND = new SoundEffect(2537);
    SoundEffect DUALITY_SOUND = new SoundEffect(2528, 0, 0);
    SoundEffect SWEEP_SOUND = new SoundEffect(2533);
    SoundEffect CLEAVE_SOUND = new SoundEffect(2529);
    SoundEffect BACKSTAB_SOUND = new SoundEffect(1084);
    SoundEffect POWERSTAB_SOUND = new SoundEffect(2530);
    SoundEffect SMASH_SOUND = new SoundEffect(2520);
    SoundEffect SLICE_AND_DICE_SOUND = new SoundEffect(2537, 5, 10);
    SoundEffect WILD_STAB_SOUND = new SoundEffect(3552);
    SoundEffect SUNDER_SOUND = new SoundEffect(3481, 5, 0);
    SoundEffect TOXIC_SIPHON_DART_SOUND = new SoundEffect(2696);
    SoundEffect TOXIC_SIPHON_FART_SOUND = new SoundEffect(800, 0, 32);
    SoundEffect SPEAR_WALL_SOUND = new SoundEffect(2529);
    SoundEffect FEINT_SOUND = new SoundEffect(2529);
    SoundEffect HAMSTRING_SOUND = new SoundEffect(2706);
    SoundEffect LUMBER_UP_SOUND = new SoundEffect(2531, 1, 0);
    SoundEffect ROCK_KNOCKER_SOUND = new SoundEffect(2655, 1, 0);
    SoundEffect SANCTUARY_SOUND = new SoundEffect(2539);
    SoundEffect RAMPAGE_SOUND = new SoundEffect(2538);
    SoundEffect ANNIHILATE_START_SOUND = new SoundEffect(1080);
    SoundEffect ANNIHILATE_END_SOUND = new SoundEffect(163, 10, 51);
    SoundEffect ARMADYL_EYE_SOUND = new SoundEffect(3892, 1, 15);
    SoundEffect SHIELD_BASH_SOUND = new SoundEffect(3454, 5, 0);
    SoundEffect IMPALE_SOUND = new SoundEffect(2534);
    SoundEffect SHOVE_SOUND = new SoundEffect(2544);
    SoundEffect FAVOUR_OF_THE_WAR_GOD_SOUND = new SoundEffect(3592);
    SoundEffect PULSATE_SOUND = new SoundEffect(1460, 10, 0);

    void attack(final Player player, final PlayerCombat combat, final Entity target);

}
