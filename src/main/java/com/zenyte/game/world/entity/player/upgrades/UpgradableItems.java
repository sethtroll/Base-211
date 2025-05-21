package com.zenyte.game.world.entity.player.upgrades;

import com.zenyte.game.item.Item;
enum UpgradableItems {


        // Weapon
        W0(UpgradeCategory.WEAPON, 12788, 100, new Item[]{new Item(861,1), new Item(995, 5000000)}),
        W1(UpgradeCategory.WEAPON, 28907, 100, new Item[]{ new Item(20997,1), new Item(7478, 5000)}),
        W2(UpgradeCategory.WEAPON, 27690, 100, new Item[]{ new Item(27687,1), new Item(27681, 1), new Item(27684, 1)}),
        W3(UpgradeCategory.WEAPON, 25739, 100, new Item[]{ new Item(995,10000000), new Item(25744, 1), new Item(22325, 1)}),
        W4(UpgradeCategory.WEAPON, 4212, 100, new Item[]{ new Item(4207,1), new Item(995, 1000000)}),
        W5(UpgradeCategory.WEAPON, 25865, 100, new Item[]{ new Item(30785,1), new Item(30787, 1), new Item(995, 10000000)}),
        W6(UpgradeCategory.WEAPON, 23995, 100, new Item[]{ new Item(30790,1), new Item(30787, 1), new Item(995, 10000000)}),




        // Armour
        A1(UpgradeCategory.ARMOUR, 4224, 100, new Item[]{new Item(4207,1), new Item(995, 100000)}),
        A2(UpgradeCategory.ARMOUR, 23979, 100, new Item[]{new Item(30804,1), new Item(30796,1), new Item(995, 10000000)}),
        A3(UpgradeCategory.ARMOUR, 23975, 100, new Item[]{new Item(30804,1), new Item(30794,1), new Item(995, 10000000)}),
        A4(UpgradeCategory.ARMOUR, 23971, 100, new Item[]{new Item(30804,1), new Item(30792,1), new Item(995, 10000000)}),
        A5(UpgradeCategory.ARMOUR, 24664, 100, new Item[]{new Item(24670,1), new Item(21018,1)}),
        A6(UpgradeCategory.ARMOUR, 24666, 100, new Item[]{new Item(24670,1), new Item(21021,1)}),
        A7(UpgradeCategory.ARMOUR, 24668, 100, new Item[]{new Item(24670,1), new Item(21024,1)}),
        A8(UpgradeCategory.ARMOUR, 21018, 100, new Item[]{new Item(995,1000000), new Item(24664,1)}),
        A9(UpgradeCategory.ARMOUR, 21021, 100, new Item[]{new Item(995,1000000), new Item(24666,1)}),
        A10(UpgradeCategory.ARMOUR, 21024, 100, new Item[]{new Item(995,1000000), new Item(24668,1)}),



        // Jewellery
        J1(UpgradeCategory.JEWELLERY, 19710, 100, new Item[]{new Item(19550), new Item(995, 15000000),}),

        J2(UpgradeCategory.JEWELLERY, 23444, 75, new Item[]{new Item(19544, 2), new Item(995, 25000000)}),

        J3(UpgradeCategory.JEWELLERY, 22249, 75, new Item[]{new Item(19547, 2), new Item(995, 5000000)}),

        J4(UpgradeCategory.JEWELLERY, 12785, 50, new Item[]{new Item(2572), new Item(995, 5000000)}),

        J5(UpgradeCategory.JEWELLERY, 12017, 100, new Item[]{new Item(4081), new Item(995, 5000000)}),

        J6(UpgradeCategory.JEWELLERY, 12018, 100, new Item[]{new Item(12017), new Item(995, 10000000)}),

        J7(UpgradeCategory.JEWELLERY, 13202, 100, new Item[]{new Item(12601), new Item(995, 5000000)}),

        J8(UpgradeCategory.JEWELLERY, 12692, 100, new Item[]{new Item(12605), new Item(995, 5000000)}),

        J9(UpgradeCategory.JEWELLERY, 12691, 100, new Item[]{new Item(12603), new Item(995, 5000000)}),

        J10(UpgradeCategory.JEWELLERY, 11773, 100, new Item[]{new Item(6737), new Item(995, 5000000)}),

        J11(UpgradeCategory.JEWELLERY, 11771, 100, new Item[]{new Item(6733), new Item(995, 5000000)}),

        J12(UpgradeCategory.JEWELLERY, 11770, 100, new Item[]{new Item(6731), new Item(995, 5000000)}),

        J13(UpgradeCategory.JEWELLERY, 11772, 100, new Item[]{new Item(6735), new Item(995, 5000000)}),


// Pets



        // OTHER
        M1(UpgradeCategory.MISC, 620, 50, new Item[]{new Item(21535,10), new Item(995,1000000)}),

        M2(UpgradeCategory.MISC, 11865, 100, new Item[]{new Item(11864), new Item(995, 10000000)}),

        M3(UpgradeCategory.MISC, 25185, 75, new Item[]{new Item(25191), new Item(25183), new Item(25908)}),

        M4(UpgradeCategory.MISC, 25191, 100, new Item[]{new Item(11865), new Item(20724), new Item(995, 10000000)}),

        M5(UpgradeCategory.MISC, 25183, 100, new Item[]{new Item(11865), new Item(20724), new Item(995, 10000000)}),

        M6(UpgradeCategory.MISC, 25908, 100, new Item[]{new Item(11865), new Item(20724), new Item(995, 10000000)}),

        M7(UpgradeCategory.MISC, 21793, 100, new Item[]{new Item(2413), new Item(995, 5000000)}),

        M8(UpgradeCategory.MISC, 21795, 100, new Item[]{new Item(2414), new Item(995, 5000000)}),

        M9(UpgradeCategory.MISC, 21791, 100, new Item[]{new Item(2412), new Item(995, 5000000)}),


        ;

        public transient final UpgradeCategory category;
        public final int id;
        public final int chance;
        public final Item[] required;

        UpgradableItems(UpgradeCategory category, int id, int chance, Item[] required) {
            this.category = category;
            this.id = id;
            this.chance = chance;
            this.required = required;
        }
    }