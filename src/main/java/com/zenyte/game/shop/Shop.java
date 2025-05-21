package com.zenyte.game.shop;

import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.AccomplishmentCape;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.item.containers.GemBag;
import com.zenyte.game.item.degradableitems.DegradableItem;
import com.zenyte.game.util.StringUtilities;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.item.CoalBag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.ints.IntHash;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * @author Kris | 23/11/2018 14:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Shop {
    static final int SHOPS_DUPLICATOR_COUNT = 5;
    static final int DEFAULT_RESTOCK_TIMER = 15;
    private static final Logger log = LoggerFactory.getLogger(Shop.class);
    private static final boolean USE_INSTANCED_SHOPS = false;
    private static final Map<String, Shop> shops = new ConcurrentHashMap<>();
    private static final Map<String, Shop> ironmanShops = new ConcurrentHashMap<>();
    private static final List<Map<String, Shop>> allShopMaps = new ArrayList<>(Arrays.asList(shops, ironmanShops));
    private static final Map<String, List<BiConsumer<Player, Item>>> consumerMap = new HashMap<>();
    private static final Map<String, Consumer<Player>> consumerOpenMap = new HashMap<>();
    private static final Map<String, List<BiPredicate<Player, Item>>> predicateMap = new HashMap<>();

    static {
        appendConsumer("Culinaromancer's Chest", (player, item) -> {
            if (item.getId() == 7462) {
                player.getAchievementDiaries().update(LumbridgeDiary.PURCHASE_BARROWS_GLOVES);
            }
        });
        appendConsumer("Candle Shop", (player, item) -> {
            if (item.getId() == 36) {
                player.getAchievementDiaries().update(KandarinDiary.BUY_CANDLE);
            }
        });
        appendConsumer("Grace's Graceful Clothing", (player, item) -> {
            if (item.getName().contains("Graceful")) {
                player.getCollectionLog().add(item);
            }
        });
        appendConsumer("Melee Weaponry Shop", (player, item) -> {
            if (item.getId() == 2402) {
                player.sendMessage("You can upgrade the Silverlight into a Darklight after killing 100 demons.");
            }
        });
        appendConsumer("Accomplishment Cape Shop", (player, item) -> {
            final AccomplishmentCape cape = AccomplishmentCape.get(item.getId());
            if (cape == null) {
                return;
            }
            player.getInventory().addItem(cape.getHood(), item.getAmount());
        });
        appendPredicate("Prospector Percy's Nugget Shop", (player, item) -> {
            if (item.getId() == CoalBag.ITEM.getId() || item.getId() == GemBag.GEM_BAG.getId()) {
                if (item.getAmount() > 1) {
                    player.sendMessage("You can only buy one of that item.");
                    return false;
                }
                if (player.containsItem(item.getId())) {
                    player.sendMessage("You already have this item and cannot get another one.");
                    return false;
                }
                return true;
            }
            return true;
        });
        //Making sure players can only have 1 seed box/rune pouch at a time
        appendPredicate("Vote Shop", (player, item) -> {
            //TODO improve
            if (item.getId() == ItemId.SMALL_POUCH) {
                if (item.getAmount() > 1 || player.containsItem(item.getId())) {
                    player.sendMessage("You can only buy one of that item.");
                    return false;
                }
            }
            if (item.getId() == ItemId.MEDIUM_POUCH) {
                if (item.getAmount() > 1 || player.containsItem(item.getId())) {
                    player.sendMessage("You can only buy one of that item.");
                    return false;
                }
                if (!player.containsItem(ItemId.SMALL_POUCH)) {
                    player.sendMessage("You need to own a small pouch before you can buy a medium pouch.");
                    return false;
                }
            }
            if (item.getId() == ItemId.LARGE_POUCH) {
                if (item.getAmount() > 1 || player.containsItem(item.getId())) {
                    player.sendMessage("You can only buy one of that item.");
                    return false;
                }
                if (!player.containsItem(ItemId.SMALL_POUCH) || !player.containsItem(ItemId.MEDIUM_POUCH)) {
                    player.sendMessage("You need to own a small and a medium pouch before you can buy a large pouch.");
                    return false;
                }
            }
            if (item.getId() == ItemId.GIANT_POUCH) {
                if (item.getAmount() > 1 || player.containsItem(item.getId())) {
                    player.sendMessage("You can only buy one of that item.");
                    return false;
                }
                if (!player.containsItem(ItemId.SMALL_POUCH) || !player.containsItem(ItemId.MEDIUM_POUCH) || !player.containsItem(ItemId.LARGE_POUCH)) {
                    player.sendMessage("You need to own a small, a medium and a large pouch before you can buy a giant pouch.");
                    return false;
                }
            }
            if (item.getId() == CoalBag.ITEM.getId() || item.getId() == GemBag.GEM_BAG.getId() || item.getId() == 12791 || item.getId() == 13639) {
                if (item.getAmount() > 1) {
                    player.sendMessage("You can only buy one of that item.");
                    return false;
                }
                if (player.containsItem(item.getId())) {
                    player.sendMessage("You already have this item and cannot get another one.");
                    return false;
                }
                return true;
            }
            return true;
        });
        //Making sure the player has 99 in a specific skill before buying a skillcape
        appendPredicate("Accomplishment Cape Shop", (player, item) -> {
            final AccomplishmentCape cape = AccomplishmentCape.get(item.getId());
            if (cape == null) {
                return false;
            }
            if (cape.equals(AccomplishmentCape.DIARY)) {
                if (!player.getAchievementDiaries().isAllCompleted()) {
                    player.sendMessage("You need to complete all the Achievement diaries to buy this cape");
                    return false;
                }
            } else {
                final int skill = cape.getSkill();
                if (player.getSkills().getLevelForXp(skill) < 99) {
                    player.sendMessage("You need level 99 in " + cape + " to buy this skillcape.");
                    return false;
                }
            }
            if (!player.getInventory().checkSpace(2)) {
                player.sendMessage("You need to have at least 2 inventory spaces for the cape and hood.");
                return false;
            }
            return true;
        });
        //On open consumers
        bind("Aleck's Hunter Emporium", p -> p.getAchievementDiaries().update(ArdougneDiary.VIEW_ALECKS_HUNTER_EMPORIUM));
        bind("Sarah's Farming Shop", p -> p.getAchievementDiaries().update(FaladorDiary.BROWSE_SARAHS_FARM_SHOP));
        bind("Keldagrim Stonemason", p -> p.getAchievementDiaries().update(FremennikDiary.BROWSE_THE_STONEMASONS_SHOP));
        bind("Thessalia's Fine Clothes", p -> p.getAchievementDiaries().update(VarrockDiary.BROWSE_THESSELIA_STORE));
    }

    private final String name;
    private final ShopCurrency currency;
    private final ShopPolicy policy;
    private final List<StockItem> stock;
    private final ShopContainer container;
    private final List<Player> players;
    private final boolean ironman;
    private final float sellModifier;

    /**
     * Constructs a new game shop based on the scrap json shop.
     *
     * @param scrap the json serialized shop.
     */
    public Shop(final JsonShop scrap, final boolean ironman) {
        final String shopName = scrap.getShopName();
        this.name = StringUtilities.escape(shopName);
        this.currency = scrap.getCurrency();
        this.policy = scrap.getSellPolicy();
        this.sellModifier = scrap.getSellMultiplier() == 0 ? 0.4F : scrap.getSellMultiplier();
        this.container = new ShopContainer(ContainerPolicy.ALWAYS_STACK, ContainerType.SHOP);
        this.stock = new ArrayList<>();
        for (final JsonShop.Item item : scrap.getItems()) {
            if (ItemDefinitions.isInvalid(item.id)) continue;
            if (ironman && item.ironmanRestricted) {
                continue;
            }
            stock.add(new StockItem(item));
        }
        this.players = new LinkedList<>();
        if (ironman ? ironmanShops.containsKey(shopName) : shops.containsKey(shopName)) {
            throw new ExceptionInInitializerError("Shop by the name of " + shopName + " is already mapped.");
        }
        this.ironman = ironman;
        fillContainer();
    }

    public Shop(final String name, final ShopCurrency currency, final ShopPolicy policy, final List<StockItem> stock, final ShopContainer container, final List<Player> players, final boolean ironman, final float sellModifier) {
        this.name = name;
        this.currency = currency;
        this.policy = policy;
        this.stock = stock;
        this.container = container;
        this.players = players;
        this.ironman = ironman;
        this.sellModifier = sellModifier;
    }

    private static void bind(final String name, final Consumer<Player> consumer) {
        consumerOpenMap.put(name, consumer);
    }

    public static void load() {
        try {
            final File[] files = new File("data/shops/").listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {
                    final BufferedReader reader = new BufferedReader(new FileReader(file));
                    final JsonShop shop = World.getGson().fromJson(reader, JsonShop.class);
                    if (shop == null) continue;
                    final String name = shop.getShopName();
                    //General stores should not get duplicated.
                    if (shop.getSellPolicy() == ShopPolicy.CAN_SELL) {
                        final Shop normalShop = new Shop(shop, false);
                        final Shop ironmanShop = new Shop(shop, true);
                        if (USE_INSTANCED_SHOPS) {
                            for (int i = 0; i < SHOPS_DUPLICATOR_COUNT; i++) {
                                shops.put(name + "|" + i, normalShop);
                                ironmanShops.put(name + "|" + i, ironmanShop);
                            }
                        } else {
                            shops.put(name, normalShop);
                            ironmanShops.put(name, ironmanShop);
                        }
                        continue;
                    }
                    if (USE_INSTANCED_SHOPS) {
                        for (int i = 0; i < SHOPS_DUPLICATOR_COUNT; i++) {
                            shops.put(name + "|" + i, new Shop(shop, false));
                            ironmanShops.put(name + "|" + i, new Shop(shop, true));
                        }
                    } else {
                        shops.put(name, new Shop(shop, false));
                        ironmanShops.put(name, new Shop(shop, true));
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * Gets the shop by the {@param name}.
     *
     * @param name the name of the shop.
     * @return the shop if it exist.
     * @throws RuntimeException if the shop by the requested name doesn't exist.
     */
    public static Shop get(@NotNull final String name, final boolean ironman, @NotNull final Player player) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        final int identifier = player.getNumericTemporaryAttributeOrDefault("shop_unique_identifier", player.getPlayerInformation().getUserIdentifier()).intValue();
        final int shopIndex = identifier % SHOPS_DUPLICATOR_COUNT;
        final String key = USE_INSTANCED_SHOPS ? name + "|" + shopIndex : name;
        final Shop shop = ironman ? ironmanShops.get(key) : shops.get(key);
        if (shop == null) throw new RuntimeException("Shop with key \"" + key + "\" does not exist.");
        return shop;
    }

    /**
     * Appends a biconsumer into the consumer map. Used for adding consumers for purchasing from the shop.
     *
     * @param shopName the name of the shop.
     * @param consumer the biconsumer of player & item.
     */
    private static void appendConsumer(final String shopName, final BiConsumer<Player, Item> consumer) {
        consumerMap.computeIfAbsent(shopName, name -> new ArrayList<>()).add(consumer);
    }

    private static void appendPredicate(final String shopName, final BiPredicate<Player, Item> predicate) {
        predicateMap.computeIfAbsent(shopName, name -> new ArrayList<>()).add(predicate);
    }

    public static void process() {
        allShopMaps.forEach(shopMap -> {
            try {
                shopMap.forEach((name, shop) -> {
                    final Shop.ShopContainer container = shop.getContainer();
                    final Shop.ShopContainer.StockMap<Shop.StockItem> stock = container.stockItems;
                    stock.int2ObjectEntrySet().fastForEach(entry -> {
                        final Shop.StockItem item = entry.getValue();
                        final int key = entry.getIntKey();
                        if (--item.restockTimer <= 0) {
                            item.restockTimer = item.defaultRestockTimer;
                            final com.zenyte.game.item.Item it = container.get(key);
                            if (it == null) {
                                container.stockItems.remove(key);
                                return;
                            }
                            final boolean isStockItem = shop.stock.contains(item);
                            final int amount = it.getAmount();
                            if (amount == item.defaultAmount && isStockItem) return;
                            if (!isStockItem) {
                                it.setAmount(amount - 1);
                                if (amount == 1) {
                                    container.set(key, null);
                                    container.stockItems.remove(key);
                                }
                                container.refresh(key);
                                return;
                            }
                            it.setAmount(amount + (amount < item.defaultAmount ? 1 : -1));
                            container.refresh(key);
                        }
                    });
                    if (!container.getModifiedSlots().isEmpty()) {
                        shop.refresh();
                    }
                });
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    /**
     * Refreshes the stock for all players currently browsing this shop.
     */
    private void refresh() {
        for (final Player player : players) {
            container.refresh(player);
        }
    }

    /**
     * Fills the shop's container with the stock.
     */
    private void fillContainer() {
        for (int i = 0; i < stock.size(); i++) {
            final Shop.StockItem item = stock.get(i);
            container.set(i, item);
            item.defaultStockItem = true;
            if (item.defaultRestockTimer == 0) {
                item.defaultRestockTimer = item.restockTimer = DEFAULT_RESTOCK_TIMER;
            }
            container.stockItems.put(i, item);
        }
    }

    /**
     * Opens this shop for the player; appends the shop as a temporary attribute for the interfaces.
     *
     * @param player the player opening the shop.
     */
    public void open(@NotNull final Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        player.getTemporaryAttributes().put("Shop", this);
        GameInterface.SHOP.open(player);
        final Consumer<Player> consumer = consumerOpenMap.get(name);
        if (consumer != null) {
            consumer.accept(player);
        }
    }

    /**
     * Gets the current buy price of the item. If value returned is 0, the item is considered to be free,
     * if it is <= -1, the item cannot be purchased.
     *
     * @param id the id of the item.
     * @return the current buy price of the item.
     */
    int getBuyPrice(final Player player, final int id) {
        float modifier = 1.0F;
        if (player != null && this.name.startsWith("Culinaromancer's Chest")) {
            if (DiaryReward.EXPLORERS_RING4.eligibleFor(player)) {
                modifier = 0.8F;
            }
        }
        if (player != null && this.currency == ShopCurrency.TOKKUL) {
            if (DiaryReward.KARAMJA_GLOVES1.eligibleFor(player)) {
                modifier = 13.0F / 15.0F;
            }
        }
        final Shop.StockItem item = container.stockItems.get(container.getSlotOf(id));
        if (item == null) {
            return (int) (ItemDefinitions.getOrThrow(id).getPrice() * modifier);
        }
        return (int) Math.ceil(item.buyPrice * modifier);
    }

    /**
     * Gets the current sell price of the item. If the value returned is -1, the item cannot be sold to this shop.
     *
     * @param player
     * @param id     the id of the item.
     * @return the sell price of the item.
     */
    int getSellPrice(final Player player, final int id) {
        if (this.policy == ShopPolicy.NO_SELLING) return -1;
        final int unnotedId = ItemDefinitions.getOrThrow(id).getUnnotedOrDefault();
        final int notedId = ItemDefinitions.getOrThrow(id).getNotedOrDefault();
        int slot = container.getSlotOf(id);
        if (slot == -1) {
            final ItemDefinitions definition = ItemDefinitions.getOrThrow(id);
            final int oppositeId = definition.isNoted() ? definition.getUnnotedOrDefault() : definition.getNotedOrDefault();
            if (oppositeId != -1) {
                slot = container.getSlotOf(oppositeId);
            }
        }
        final Shop.StockItem item = container.stockItems.get(slot);
        if (policy == ShopPolicy.STOCK_ONLY) {
            if (item == null || !item.defaultStockItem) return -1;
        }
        double modifier = 1;
        /*if (player != null && this.currency == ShopCurrency.TOKKUL) {
            if (DiaryReward.eligibleFor(player, DiaryReward.KARAMJA_GLOVES1)) {
                modifier = 7F/3F;
            }
        }*/
        if (item == null) {
            return (int) Math.ceil((ItemDefinitions.getOrThrow(id).getPrice() * sellModifier) * modifier);
        }
        return (int) Math.ceil(item.sellPrice * modifier);
    }

    /**
     * Attempts to purchase the item from this shop. Verifies all conditions before-hand, refreshes the shop for all
     * viewing players afterwards.
     *
     * @param player the player purchasing the item.
     * @param option the option clicked.
     * @param slotId the slot clicked.
     */
    void purchase(@NotNull final Player player, @NotNull final ShopInterface.ItemOption option, final int slotId) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (option == null) {
            throw new NullPointerException("option is marked non-null but is null");
        }
        if (option.is(ShopInterface.ItemOption.VALUE) || option.is(ShopInterface.ItemOption.EXAMINE)) {
            throw new IllegalArgumentException("Invalid purchase option: " + option);
        }
        final com.zenyte.game.item.Item item = container.get(slotId);
        if (item == null) {
            throw new RuntimeException("Purchased item is null, slot: " + slotId + ", stock: " + container.getItems());
        }
        int amount = Math.min(item.getAmount(), option.amount);
        final int baseAmount = amount;
        if (amount <= 0) {
            player.sendMessage("There's currently no stock of this item.");
            return;
        }
        final int singleCost = getBuyPrice(player, item.getId());
        if (singleCost < 0) {
            player.sendMessage("This item is currently unavailable.");
            return;
        }
        final Shop.StockItem stockItem = container.stockItems.get(slotId);
        if (ironman) {
            if (!stockItem.defaultStockItem) {
                player.sendMessage("You cannot purchase items sold by other players.");
                return;
            }
            if (item.getAmount() > stockItem.defaultAmount) {
                player.sendMessage("Iron Men may not take advantage of the pricing when a shop is over-stocked.");
                return;
            }
        }
        final long totalCost = (long) amount * singleCost;
        final Container inventory = player.getInventory().getContainer();
        final int held = currency.getAmount(player);
        if (totalCost > held) {
            amount = held / singleCost;
        }
        final List<BiPredicate<Player, com.zenyte.game.item.Item>> predicates = predicateMap.get(name);
        if (predicates != null) {
            for (final BiPredicate<Player, com.zenyte.game.item.Item> predicate : predicates) {
                if (!predicate.test(player, new Item(item.getId(), amount))) {
                    return;
                }
            }
        }
        int freeSlots = inventory.getFreeSlotsSize();
        final int affordableAmount = amount;
        final int inInventory = inventory.getAmountOf(item.getId());
        if (amount + inInventory < 0) {
            amount = Integer.MAX_VALUE - inInventory;
        }
        if (item.isStackable()) {
            if (currency.isPhysical()) {
                if ((freeSlots == 0 && inInventory == 0 && currency.getAmount(player) != (amount * singleCost))) {
                    amount = 0;
                }
            } else {
                if (freeSlots == 0 && inInventory == 0) {
                    amount = 0;
                }
            }
        } else {
            if (freeSlots < amount && currency.isPhysical() && currency.getAmount(player) == (amount * singleCost)) {
                freeSlots++;
            }
            amount = Math.min(freeSlots, amount);
        }
        final Optional<String> message = affordableAmount != baseAmount ? Optional.of("You don't have enough " + currency + ".") : amount != affordableAmount ? Optional.of("Not enough space in your inventory.") : Optional.empty();
        message.ifPresent(mes -> player.sendMessage(mes));
        if (amount <= 0) {
            return;
        }
        final int cost = amount * singleCost;
        if (cost > 0) {
            currency.remove(player, cost);
        }
        final com.zenyte.game.item.Item remainderItem = new Item(item.getId(), item.getAmount() - amount);
        container.set(slotId, remainderItem);
        if (remainderItem.getAmount() <= 0) {
            if (!stockItem.defaultStockItem) {
                container.set(slotId, null);
            }
        }
        final com.zenyte.game.item.Item succeeded = new Item(item.getId(), amount, DegradableItem.getFullCharges(item.getId()));
        inventory.add(succeeded);
        inventory.refresh(player);
        refresh();
        final List<BiConsumer<Player, com.zenyte.game.item.Item>> consumers = consumerMap.get(name);
        if (consumers == null) {
            return;
        }
        for (final BiConsumer<Player, com.zenyte.game.item.Item> consumer : consumers) {
            consumer.accept(player, succeeded);
        }
    }

    /**
     * Attempts to sell the selected item into this shop. Verifies the option, calculates the amount & refreshes
     * the interface for all players afterwards.
     *
     * @param player the player selling the item.
     * @param option the option clicked.
     * @param slotId the slot clicked.
     */
    void sell(@NotNull final Player player, @NotNull final ShopInventoryInterface.ItemOption option, final int slotId) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (option == null) {
            throw new NullPointerException("option is marked non-null but is null");
        }
        if (option.is(ShopInventoryInterface.ItemOption.VALUE) || option.is(ShopInventoryInterface.ItemOption.EXAMINE)) {
            throw new IllegalArgumentException("Invalid sell option: " + option);
        }
        final Container inventory = player.getInventory().getContainer();
        final com.zenyte.game.item.Item item = inventory.get(slotId);
        if (item == null) {
            throw new RuntimeException("Sold item is null, slot: " + slotId + ", stock: " + inventory.getItems());
        }
        final int id = item.getId();
        if ((id == 995 || !item.isTradable()) && !name.toLowerCase().contains("grace's graceful clothing")) {
            player.sendMessage("You can't sell this item.");
            return;
        }
        final int singleCost = getSellPrice(player, item.getId());
        if (singleCost < 0) {
            player.sendMessage("You can't sell this item to this shop.");
            return;
        }
        final int itemAmount = inventory.getAmountOf(id);
        final int unnotedId = name.equals("Food Shop") ? item.getId() : item.getDefinitions().getUnnotedOrDefault(); //TODO improve this
        if (ironman) {
            for (final Int2ObjectMap.Entry<Shop.StockItem> entry : container.stockItems.int2ObjectEntrySet()) {
                final Shop.StockItem it = entry.getValue();
                if (it == null) continue;
                if (it.getId() == id) {
                    if (it.defaultStockItem && container.get(entry.getIntKey()).getAmount() < it.defaultAmount) {
                        player.sendMessage("Iron Men may not take advantage of the pricing when a shop is under-stocked.");
                        return;
                    }
                    break;
                }
            }
        }
        int amount = Math.min(Math.min(itemAmount, option.amount), container.getMaximumTransferrableAmount(new Item(unnotedId, itemAmount)));
        if (amount <= 0) {
            player.sendMessage("The shop is full!");
            return;
        }
        final int freeSlots = inventory.getFreeSlotsSize();
        amount = currency.isPhysical() && !currency.isStackable() ? Math.min(freeSlots, amount) : Math.min(Integer.MAX_VALUE - currency.getAmount(player), amount);
        if (amount <= 0 || (currency.isPhysical() && currency.getAmount(player) <= 0 && freeSlots == 0 && amount < item.getAmount())) {
            player.sendMessage("Not enough free space in your inventory.");
            return;
        }
        inventory.remove(new Item(item.getId(), amount));
        container.add(new Item(unnotedId, amount));
        final int currencyReturn = amount * singleCost;
        if (currencyReturn > 0) {
            currency.add(player, currencyReturn);
        }
        inventory.refresh(player);
        refresh();
    }

    public String getName() {
        return this.name;
    }

    public ShopCurrency getCurrency() {
        return this.currency;
    }

    public ShopPolicy getPolicy() {
        return this.policy;
    }

    public List<StockItem> getStock() {
        return this.stock;
    }

    public ShopContainer getContainer() {
        return this.container;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public boolean isIronman() {
        return this.ironman;
    }

    public float getSellModifier() {
        return this.sellModifier;
    }

    @SuppressWarnings("unused")
    private class StockItem extends Item {
        //Default variables of the stock item.
        private final transient int defaultAmount;
        private transient int defaultRestockTimer;
        private transient int defaultBuyPrice;
        private transient int defaultSellPrice;
        private transient boolean defaultStockItem;
        //Current variables of the stock item.
        private transient int buyPrice;
        private transient int sellPrice;
        private transient int restockTimer;

        StockItem(final int id, final int amount, final int buyPrice, final int sellPrice, final int restockTimer) {
            super(id, amount);
            this.defaultAmount = amount;
            this.defaultRestockTimer = this.restockTimer = restockTimer;
            this.defaultBuyPrice = this.buyPrice = buyPrice;
            this.defaultSellPrice = this.sellPrice = sellPrice;
        }

        private StockItem(final JsonShop.Item item) {
            super(item.id, item.amount);
            this.defaultAmount = item.amount;
            this.defaultRestockTimer = this.restockTimer = item.restockTimer;
            this.defaultBuyPrice = this.buyPrice = item.buyPrice;
            this.defaultSellPrice = this.sellPrice = item.sellPrice;
        }

        private StockItem(final StockItem item) {
            super(item.getId(), item.getAmount());
            this.defaultAmount = item.getAmount();
            this.defaultRestockTimer = this.restockTimer = item.restockTimer;
            this.defaultBuyPrice = this.buyPrice = item.buyPrice;
            this.defaultSellPrice = this.sellPrice = item.sellPrice;
        }

        private void setDefaults(final StockItem item) {
            this.defaultRestockTimer = this.restockTimer = item.restockTimer;
            this.defaultBuyPrice = this.buyPrice = item.buyPrice;
            this.defaultSellPrice = this.sellPrice = item.sellPrice;
        }
    }

    class ShopContainer extends Container {
        private final transient StockMap<StockItem> stockItems = new StockMap<>();

        private ShopContainer(final ContainerPolicy policy, final ContainerType type) {
            super(policy, type, Optional.empty());
        }

        @Override
        public void set(final int slot, final Item item) {
            if (item == null) {
                stockItems.remove(slot);
            } else if (stockItems.get(slot) == null) {
                stockItems.put(slot, new StockItem(item.getId(), item.getAmount(), getBuyPrice(null, item.getId()), getSellPrice(null, item.getId()), DEFAULT_RESTOCK_TIMER));
            }
            super.set(slot, item);
        }


        class StockMap<V> extends Int2ObjectOpenCustomHashMap<V> {
            StockMap() {
                super(40, 0.75F, new IntHash.Strategy() {
                    @Override
                    public int hashCode(int e) {
                        return e;
                    }

                    @Override
                    public boolean equals(int a, int b) {
                        return a == b;
                    }
                });
            }

            public V[] valuesArray() {
                return value;
            }

            public int[] keysArray() {
                return key;
            }

            public int getN() {
                return n;
            }
        }
    }
}
