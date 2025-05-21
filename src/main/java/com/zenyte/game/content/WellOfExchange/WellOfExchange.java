package com.zenyte.game.content.WellOfExchange;

import com.zenyte.Constants;
import com.zenyte.game.BonusXpManager;
import com.zenyte.game.RuneDate;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class WellOfExchange implements ItemOnObjectAction {
    int GLOBAL_POINTS = 0;
    int CURRENCY_ITEM = 7478;
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        int points = 0;
        switch (item.getId()) {
            case ItemId.DRAGON_BOOTS:
            case ItemId.ABYSSAL_WHIP:
                points = 50;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;

            case ItemId.ARMADYL_GODSWORD:
                points = 800;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.SARADOMIN_GODSWORD:
                points = 600;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ZAMORAK_GODSWORD:
            case ItemId.BANDOS_GODSWORD:
                points = 400;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.FIRE_CAPE:
                points = 100;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.AMULET_OF_AVARICE:
                points = 100;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.INFERNAL_CAPE:
                points = 1200;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.BANDOS_CHESTPLATE:
            case ItemId.BANDOS_TASSETS:
                points = 900;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ARMADYL_HELMET:
                points = 500;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;

            case ItemId.ARMADYL_CHESTPLATE:
            case ItemId.ARMADYL_CHAINSKIRT:
                points = 800;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;

            case ItemId.BLUE_PARTYHAT:
            case ItemId.RED_PARTYHAT:
            case ItemId.YELLOW_PARTYHAT:
            case ItemId.WHITE_PARTYHAT:
            case ItemId.PURPLE_PARTYHAT:
                points = 2300;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.BLACK_PARTYHAT:
                points = 2700;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.RAINBOW_PARTYHAT:
                points = 2800;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.TWISTED_BOW:
                points = 5400;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.DARK_BOW:
                points = 45;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ABYSSAL_BLUDGEON:
                points = 300;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.SPECTRAL_SPIRIT_SHIELD:
                points = 825;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ARCANE_SPIRIT_SHIELD:
                points = 980;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ELYSIAN_SPIRIT_SHIELD:
                points = 1300;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.BANDOS_BOOTS:
                points = 35;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.MAGES_BOOK:
                points = 75;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.AMULET_OF_FURY:
                points = 50;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.OCCULT_NECKLACE:
                points = 110;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.NECKLACE_OF_ANGUISH:
                points = 130;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.DRAGON_AXE:
                points = 30;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ZAMORAKIAN_SPEAR:
                points = 25;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.DRAGONFIRE_SHIELD:
                points = 80;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.DRAGONFIRE_SHIELD_11284:
                points = 80;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.DINHS_BULWARK:
                points = 1100;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.DRAGON_WARHAMMER:
                points = 690;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.TORAGS_ARMOUR_SET:
            case ItemId.AHRIMS_ARMOUR_SET:
            case ItemId.DHAROKS_ARMOUR_SET:
            case ItemId.VERACS_ARMOUR_SET:
            case ItemId.GUTHANS_ARMOUR_SET:
            case ItemId.KARILS_ARMOUR_SET:
                points = 200;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.JUSTICIAR_FACEGUARD:
                points = 1650;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.JUSTICIAR_CHESTGUARD:
                points = 1650;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.JUSTICIAR_LEGGUARDS:
                points = 1650;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.TORVA_FULL_HELM:
                points = 1900;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.TORVA_PLATEBODY:
            case ItemId.TORVA_PLATELEGS:
                points = 2250;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.IMBUED_HEART:
                points = 325;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.MASORI_BODY:
            case ItemId.MASORI_CHAPS:
            case ItemId.MASORI_MASK:
                points = 2600;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ANCESTRAL_HAT:
                points = 1200;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.ANCESTRAL_ROBE_TOP:
            case ItemId.ANCESTRAL_ROBE_BOTTOM:
                points = 1300;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.SCYTHE_OF_VITUR:
                points = 5000;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.CHRISTMAS_CRACKER:
                points = 2300;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.INFINITY_HAT:
            case ItemId.INFINITY_GLOVES:
            case ItemId.INFINITY_BOOTS:
                points = 38;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.INFINITY_BOTTOMS:
            case ItemId.INFINITY_TOP:
                points = 43;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.INQUISITORS_GREAT_HELM:
            case ItemId.INQUISITORS_HAUBERK:
            case ItemId.INQUISITORS_MACE:
            case ItemId.INQUISITORS_PLATESKIRT:
                points = 1000;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;
            case ItemId.JAR_OF_CHEMICALS:
            case ItemId.JAR_OF_DARKNESS:
            case ItemId.JAR_OF_DECAY:
            case ItemId.JAR_OF_DIRT:
            case ItemId.JAR_OF_DREAMS:
            case ItemId.JAR_OF_EYES:
            case ItemId.JAR_OF_MIASMA:
            case ItemId.JAR_OF_SAND:
            case ItemId.JAR_OF_SMOKE:
            case ItemId.JAR_OF_SOULS:
            case ItemId.JAR_OF_SPIRITS:
            case ItemId.JAR_OF_STONE:
            case ItemId.JAR_OF_SWAMP:
                points = 200;
                player.getInventory().addItem(CURRENCY_ITEM, points);
                player.getInventory().deleteItem(item.getId(), 1);
                player.sendMessage("You have used your "+item.getName()+" for "+points+" exchange points!");
                break;

        }
        GLOBAL_POINTS+= points;
        if (GLOBAL_POINTS == 2000) {
            World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=00FF00><shad=000000>WellOfExchange has recieved another total of 2000 points today!</col></shad>");
            WorldBroadcasts.broadcast(player, BroadcastType.WELL_EVENT,"BXP", 2*1);
            BonusXpManager.set((long) 1*60*60*1000*24 + (Constants.BOOSTED_XP ? BonusXpManager.expirationDate : RuneDate.currentTimeMillis()));
            GLOBAL_POINTS = 0;

        }

    }

    @Override
    public Object[] getItems() {
        return new Object[]{
                ItemId.ABYSSAL_WHIP,
                ItemId.DRAGON_BOOTS,
                ItemId.FIRE_CAPE,
                ItemId.INFERNAL_CAPE,
                ItemId.BANDOS_GODSWORD,
                ItemId.ARMADYL_GODSWORD,
                ItemId.SARADOMIN_GODSWORD,
                ItemId.ZAMORAK_GODSWORD,
                ItemId.BANDOS_CHESTPLATE,
                ItemId.BANDOS_TASSETS,
                ItemId.ARMADYL_CHAINSKIRT,
                ItemId.ARMADYL_CHESTPLATE,
                ItemId.ARMADYL_HELMET,
                ItemId.BLUE_PARTYHAT,
                ItemId.GREEN_PARTYHAT,
                ItemId.RED_PARTYHAT,
                ItemId.PURPLE_PARTYHAT,
                ItemId.WHITE_PARTYHAT,
                ItemId.YELLOW_PARTYHAT,
                ItemId.BLACK_PARTYHAT,
                ItemId.RAINBOW_PARTYHAT,
                ItemId.TWISTED_BOW,
                ItemId.SCYTHE_OF_VITUR,
                ItemId.DARK_BOW,
                ItemId.ABYSSAL_BLUDGEON,
                ItemId.SPECTRAL_SPIRIT_SHIELD,
                ItemId.ARCANE_SPIRIT_SHIELD,
                ItemId.ELYSIAN_SPIRIT_SHIELD,
                ItemId.BANDOS_BOOTS,
                ItemId.MAGES_BOOK,
                ItemId.AMULET_OF_FURY,
                ItemId.OCCULT_NECKLACE,
                ItemId.NECKLACE_OF_ANGUISH,
                ItemId.AMULET_OF_TORTURE,
                ItemId.DRAGON_AXE,
                ItemId.DRAGON_PICKAXE,
                ItemId.ZAMORAKIAN_SPEAR,
                ItemId.DRAGONFIRE_SHIELD,
                ItemId.NEITIZNOT_FACEGUARD,
                ItemId.JUSTICIAR_FACEGUARD,
                ItemId.DINHS_BULWARK,
                ItemId.DRAGON_WARHAMMER,
                ItemId.JUSTICIAR_CHESTGUARD,
                ItemId.JUSTICIAR_LEGGUARDS,
                ItemId.INQUISITORS_GREAT_HELM,
                ItemId.INQUISITORS_HAUBERK,
                ItemId.INQUISITORS_MACE,
                ItemId.INQUISITORS_PLATESKIRT,
                ItemId.TORVA_FULL_HELM,
                ItemId.TORVA_PLATEBODY,
                ItemId.TORVA_PLATELEGS,
                ItemId.ANCESTRAL_HAT,
                ItemId.ANCESTRAL_ROBE_BOTTOM,
                ItemId.ANCESTRAL_ROBE_TOP,
                ItemId.MASORI_MASK,
                ItemId.MASORI_CHAPS,
                ItemId.MASORI_BODY,
                ItemId.DRAGON_WARHAMMER,
                ItemId.DHAROKS_ARMOUR_SET,
                ItemId.VERACS_ARMOUR_SET,
                ItemId.KARILS_ARMOUR_SET,
                ItemId.AHRIMS_ARMOUR_SET,
                ItemId.TORAGS_ARMOUR_SET,
                ItemId.GUTHANS_ARMOUR_SET,
                ItemId.IMBUED_HEART,
                ItemId.INFINITY_BOTTOMS,
                ItemId.INFINITY_HAT,
                ItemId.INFINITY_TOP,
                ItemId.INFINITY_GLOVES,
                ItemId.INFINITY_BOOTS,
                ItemId.AMULET_OF_AVARICE,
                ItemId.JAR_OF_CHEMICALS,
                ItemId.JAR_OF_DARKNESS,
                ItemId.JAR_OF_DECAY,
                ItemId.JAR_OF_DIRT,
                ItemId.JAR_OF_DREAMS,
                ItemId.JAR_OF_EYES,
                ItemId.JAR_OF_MIASMA,
                ItemId.JAR_OF_SAND,
                ItemId.JAR_OF_SMOKE,
                ItemId.JAR_OF_SOULS,
                ItemId.JAR_OF_SPIRITS,
                ItemId.JAR_OF_STONE,
                ItemId.INQUISITORS_GREAT_HELM,
                ItemId.INQUISITORS_HAUBERK,
                ItemId.INQUISITORS_MACE,
                ItemId.INQUISITORS_PLATESKIRT,
                ItemId.JAR_OF_SWAMP,
                ItemId.CHRISTMAS_CRACKER



        };
    }


    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.FIRE_OF_DEHUMIDIFICATION};
    }
}
