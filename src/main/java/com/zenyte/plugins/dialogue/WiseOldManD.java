package com.zenyte.plugins.dialogue;

import com.everythingrs.donate.Donation;
import com.zenyte.Constants;
import com.zenyte.api.model.StorePurchase;
import com.zenyte.game.constants.GameInterface;
import com.zenyte.game.content.drops.DropTableBuilder;
import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.*;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

public class WiseOldManD extends Dialogue {

    public static final String BOOSTER_VOTES = "booster votes";
    public static final String ALREADY_VOTED_TODAY = "voted today";
    public static final String BOOSTER_END = "booster end";

    public WiseOldManD(final Player player, final NPC npc) {
        super(player, npc);
    }

    public static void checkVotesWith2FA(final Player player) {
        if (Constants.WORLD_PROFILE.isPrivate() || Constants.WORLD_PROFILE.isBeta()) {
            player.sendMessage("You cannot do that on this world.");
            return;
        }

        if (!player.getAuthenticator().isEnabled()) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("Firstly id like to thankyou For voting." +
                            " Would you like to claim the votes now?");
                    options(TITLE, new DialogueOption("Claim votes", () -> claimVotes(player)), new DialogueOption("No thanks."));
                }
            });
            return;
        }
        claimVotes(player);
    }

    private static int getVotePointBonus(final Player player) {
        if (player.getMemberRank().eligibleTo(MemberRank.DRAGON_MEMBER)) {
            return 4;
        }
        if (player.getMemberRank().eligibleTo(MemberRank.STEEL_MEMBER)) {
            return 3;
        }
        return 2;
    }

    private static void claimVotes(final Player player) {
        final String playerName = player.getUsername().replace('_', ' ');
        final String id = "1";
        final String amount = "all";

        com.everythingrs.vote.Vote.service.execute(() -> {
            try {
                com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("bvhAnkghmrx9J7qIWrKY4LFqP6pcM11qZeJgEP9Vtgas9zvMDYxOong0D7v0rq4tlhBhp2fY",
                        playerName, id, amount);
                if (reward[0].message != null) {
                    player.sendMessage(reward[0].message);
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
                    player.getInventory().addItem(reward[0].reward_id, reward[0].give_amount);
                }
                player.sendMessage(
                        "Thank you for voting!");
            } catch (Exception e) {
                player.sendMessage("Api Services are currently offline. Please check back shortly");
                e.printStackTrace();
            }
        });
    }

    private static void applyVotes(@NotNull final Player player, final int amount) {
        var votePoints = amount;
        int bonusPoints = getVotePointBonus(player);

        if (bonusPoints > 0) {
            for (int i = 0; i < amount; i++) {
                if (player.getAttributes().remove("extra vote point") != null) {
                    votePoints += bonusPoints;
                } else {
                    player.getAttributes().put("extra vote point", true);
                }
            }
        }
        boolean twofactor = player.getAuthenticator().isEnabled();
        int totalAmount = votePoints;
        player.log(LogLevel.INFO, "User claimed " + amount + "(+" + (votePoints - amount) + " bonus) vote points and had 2FA " + (twofactor ? "enabled" :
                "disabled") + ".");
        player.unlock();
        player.getInterfaceHandler().closeInterfaces();
        if (totalAmount > 0) {
            int gpReward = twofactor ? 300_000 : 200_000;
            player.getInventory().addOrDrop(new Item(995, gpReward * totalAmount));
            player.sendMessage("You received " + Utils.format(gpReward * totalAmount) + " gold pieces for voting!");
            rollClues(player, totalAmount);
            player.addAttribute("vote_points", player.getNumericAttribute("vote_points").intValue() + totalAmount);
            GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentText(GameInterface.GAME_NOTICEBOARD,
                    plugin.getComponent("Vote credits"), "Vote credits: <col=ffffff>" + player.getNumericAttribute("vote_points").intValue() + "</col>"));

            if(player.getNumericAttribute(ALREADY_VOTED_TODAY).intValue() == 0)
            {
                player.addAttribute(ALREADY_VOTED_TODAY, 1);
                int votes = player.getNumericAttribute(BOOSTER_VOTES).intValue();
                if(votes == 6)
                {
                    player.sendMessage(Colour.GREEN.wrap("You have earned 7 more days of booster privileges for 7 days of voting!"));
                    player.addAttribute(BOOSTER_END, player.getNumericAttribute(BOOSTER_END).longValue() > 0 ? player.getNumericAttribute(BOOSTER_END).longValue() + 604800000L : System.currentTimeMillis() + 604800000L);
                    player.addAttribute(BOOSTER_VOTES, 0);

                } else
                {
                    player.addAttribute(BOOSTER_VOTES, player.getNumericAttribute(BOOSTER_VOTES).intValue() + 1);
                    player.sendMessage("You have voted for " + (votes + 1) + "/7 days for booster perks.");
                }
            }
        } else
        {
            player.sendMessage("You have no votes to claim. You have voted for " + player.getNumericAttribute(BOOSTER_VOTES).intValue() + "/7 days for booster perks.");
        }

        StringBuilder sb = new StringBuilder();

        if (totalAmount == 0) {
            sb.append("You have no pending votes.");
        } else {
            sb.append(amount).append(" votes have been successfully claimed.");

            if (totalAmount > amount) {
                int extraVotes = totalAmount - amount;
                sb.append(" You receive ");
                sb.append(extraVotes);
                sb.append(" extra vote");
                if (extraVotes > 1) {
                    sb.append("s");
                }
                sb.append(".");
            }

        }

        player.getDialogueManager().start(new PlainChat(player, sb.toString()));
    }

    private static final DropTable voteNon2faTable = new DropTableBuilder()
            .append(ClueItem.MEDIUM.getScrollBox(), 70).append(ClueItem.HARD.getScrollBox(), 20)
            .append(ClueItem.ELITE.getScrollBox(), 10).build();

    private static final DropTable vote2faTable = new DropTableBuilder()
            .append(ClueItem.MEDIUM.getScrollBox(), 68).append(ClueItem.HARD.getScrollBox(), 20)
            .append(ClueItem.ELITE.getScrollBox(), 10).append(ClueItem.MASTER.getScrollBox(), 2).build();

    public static final void rollClues(@NotNull final Player player, final int amount) {
        if (amount <= 0) {
            return;
        }
        int existingRolls = player.getNumericAttribute("claimed vote points").intValue();
        player.addAttribute("claimed vote points", existingRolls + amount);
        boolean twoFactorAuthenticator = player.getAuthenticator().isEnabled();
        Inventory inventory = player.getInventory();
        //Offset it by one so that when the player claims their very first vote, they don't immediately receive a clue.
        int length = 1 + existingRolls + amount;
        int interval = 3;
        Int2IntAVLTreeMap map = new Int2IntAVLTreeMap();
        for (int i = 1 + existingRolls; i < length; i++) {
            if (i % interval != 0) {
                continue;
            }
            Item scrollBox = generateRandomClue(twoFactorAuthenticator);
            inventory.addOrDrop(scrollBox);
            map.put(scrollBox.getId(), map.get(scrollBox.getId()) + scrollBox.getAmount());
        }
        if (map.isEmpty()) {
            return;
        }
        player.sendMessage(Colour.RED.wrap("You've received the following scroll boxes for voting: "));
        for (Int2IntMap.Entry entry : map.int2IntEntrySet()) {
            if (entry.getIntKey() == ClueItem.MASTER.getScrollBox()) {
                player.sendMessage(Colour.RS_PURPLE.wrap("2FA Special: ") + Colour.RED.wrap( entry.getIntValue() + " x " + ItemDefinitions.getOrThrow(entry.getIntKey()).getName()));
                continue;
            }
            player.sendMessage(Colour.RED.wrap(entry.getIntValue() + " x " + ItemDefinitions.getOrThrow(entry.getIntKey()).getName()));
        }
    }

    private static final Item generateRandomClue(final boolean twofactorAuthenticator) {
        DropTable table = twofactorAuthenticator ? vote2faTable : voteNon2faTable;
        return table.rollItem();
    }
    private static void claimDonations(final Player player) {
        new Thread(() -> {
            try {
                Donation[] donations = Donation.donations("bvhAnkghmrx9J7qIWrKY4LFqP6pcM11qZeJgEP9Vtgas9zvMDYxOong0D7v0rq4tlhBhp2fY",
                        player.getUsername());
                if (donations.length == 0) {
                    player.sendMessage("You currently don't have any items waiting. You must donate first!");
                    return;
                }
                if (donations[0].message != null) {
                    player.sendMessage(donations[0].message);
                    return;
                }
                for (Donation donate : donations) {
                    player.getInventory().addItem(donate.product_id, donate.product_amount);
                }
                player.sendMessage("Thank you for donating!");
            } catch (Exception e) {
                player.sendMessage("Api Services are currently offline. Please check back shortly");
                e.printStackTrace();
            }
        }).start();
    }

    private static void applyDonations(@NotNull final Player player, @NotNull final StorePurchase[] request) {
        player.refreshTotalDonated();
        player.unlock();
        player.getInterfaceHandler().closeInterfaces();
        StringBuilder builder = new StringBuilder();
        for (@NotNull StorePurchase item : request) {
            builder.append("Id: ").append(item.getId()).append(", name: ").append(item.getItemName()).append(", amount: ").append(item.getAmount())
                    .append(", quantity purchased: ").append(item.getItemQuantity()).append(", price: ").append(item.getPrice())
                    .append(", discount: ").append(item.getDiscount()).append("\n");
            if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN)) {
                player.getInventory().addItem(new Item(item.getId(), item.getItemQuantity() * item.getAmount())).onFailure(r -> {
                    player.sendMessage("<col=ff0000>Some of the donation rewards were dropped on the ground due to lack of space.");
                    World.spawnFloorItem(r, player);
                });
            } else {
                player.getInventory().addItem(new Item(item.getId(), item.getItemQuantity() * item.getAmount())).onFailure(remaining -> {
                    ContainerResult result = player.getBank().add(remaining);
                    result.onFailure(lastRemaining -> {
                        player.sendMessage("<col=ff0000>Some of the donation rewards were dropped on the ground due to lack of space.");
                        World.spawnFloorItem(lastRemaining, player);
                    });
                    if (result.getSucceededAmount() > 0) {
                        player.sendMessage("<col=ff0000>Some of the donation rewards were sent to your bank due to lack of space.");
                    }
                });
            }
            if (item.getId() == ItemId.DARKLIGHT || item.getId() == ItemId.DARKLIGHT_8281) {
                player.addAttribute("demon_kills", 100);
            }
        }
        if (builder.length() >= 2) {
            builder.delete(builder.length() - 2, builder.length());
        }
        player.log(LogLevel.INFO, "Successfully retrieved donations: \n" + builder.toString());
        for (Player p2 : World.getPlayers()) {
            if (p2.getPrivilege().eligibleTo(Privilege.MODERATOR) && !player.getUsername().equals(p2.getUsername())) {
                p2.sendMessage("[Staff] " + player.getUsername() + " retrieved " + builder.toString() + " from donating.");
            }
        }
        player.getDialogueManager().start(new PlainChat(player, request.length == 0 ? "You have no pending items." : "All donations successfully claimed."));
    }



    private static void givezenyteCrystal(@NotNull final Player player) {
        int price = 2147000000;
        if (!player.getInventory().checkSpace()) {
            player.sendMessage("You need at least 1 free space in your inventory.");
        }else {
            if (player.getInventory().containsItem(ItemId.COINS_995, price) || player.getBank().getContainer().contains(ItemId.COINS_995, price)) {
                if (player.getInventory().containsItem(ItemId.COINS_995, price)) {
                    player.getInventory().deleteItem(ItemId.COINS_995, price);
                } else {
                    int slot = player.getBank().getContainer().getSlotOf(ItemId.COINS_995);
                    player.getBank().remove(slot, price, true);
                }
                player.getInventory().addItem(ItemId.CRYSTAL_OF_IORWERTH, 1);
            }else {
                player.sendMessage("You need at least 500k gp in either your inventory or bank.");
            }
        }
    }

    private static void giveIthellCrystal(@NotNull final Player player) {
        int price = 2147000000;
        if (!player.getInventory().checkSpace()) {
            player.sendMessage("You need at least 1 free space in your inventory.");
        }else {
            if (player.getInventory().containsItem(ItemId.COINS_995, price) || player.getBank().getContainer().contains(ItemId.COINS_995, price)) {
                if (player.getInventory().containsItem(ItemId.COINS_995, price)) {
                    player.getInventory().deleteItem(ItemId.COINS_995, price);
                } else {
                    int slot = player.getBank().getContainer().getSlotOf(ItemId.COINS_995);
                    player.getBank().remove(slot, price, true);
                }
                player.getInventory().addItem(ItemId.CRYSTAL_OF_ITHELL, 1);
            }else {
                player.sendMessage("You need at least 500k gp in either your inventory or bank.");
            }
        }
    }

    private static void giveMeilyrCrystal(@NotNull final Player player) {
        int price = 2147000000;
        if (!player.getInventory().checkSpace()) {
            player.sendMessage("You need at least 1 free space in your inventory.");
        }else {
            if (player.getInventory().containsItem(ItemId.COINS_995, price) || player.getBank().getContainer().contains(ItemId.COINS_995, price)) {
                if (player.getInventory().containsItem(ItemId.COINS_995, price)) {
                    player.getInventory().deleteItem(ItemId.COINS_995, price);
                } else {
                    int slot = player.getBank().getContainer().getSlotOf(ItemId.COINS_995);
                    player.getBank().remove(slot, price, true);
                }
                player.getInventory().addItem(ItemId.CRYSTAL_OF_MEILYR, 1);
            }else {
                player.sendMessage("You need at least 500k gp in either your inventory or bank.");
            }
        }
    }

    @Override
    public void buildDialogue() {
        if (Constants.WORLD_PROFILE.isPrivate() || Constants.WORLD_PROFILE.isBeta()) {
            npc("Sorry, I cannot help you on this world.");
            //return;
        }

        npc("What can I help you with, " + player.getName() + "?");
        options("Choose an option", "Check donations", "Check vote rewards", "Ask for a Crystal of Pharaoh", "Ask for a Crystal of Ithell", "Ask for a Crystal of Meilyr")
                .onOptionOne(() -> claimDonations(player))
                .onOptionTwo(() -> checkVotesWith2FA(player))
                .onOptionThree(() -> setKey(50))
                .onOptionFour(() -> setKey(200))
                .onOptionFive(() -> setKey(350));
        npc(50,"You want a Crystal of Pharaoh? Let me check your rank first.").executeAction(() -> {
            if (!player.getMemberRank().eligibleTo(MemberRank.DRAGON_MEMBER)) {
                setKey(100);
            } else {
                setKey(150);
            }
        });
        npc(100, "You do not have the Pharaoh rank, come back once you do.").executeAction(() -> {
            player.getInterfaceHandler().closeInterfaces();
        });
        npc(150,"This Crystal of Pharaoh will cost you 500k gold pieces. Are you sure?");
        options("Buy the Crystal of Pharaoh for 500k gp?", "Yes.", "No.")
                .onOptionOne(() -> givezenyteCrystal(player));
        npc(200,"You want a Crystal of Ithell? " +
                "Let me check your unlocked music tracks first.").executeAction(() -> {
            if (player.getMusic().unlockedMusicCount() < 556) {
                setKey(250);
            } else {
                setKey(300);
            }
        });
        npc(250, "You do not have all music tracks unlocked, come back once you do.").executeAction(() -> {
            player.getInterfaceHandler().closeInterfaces();
        });
        npc(300,"This Crystal of Ithell will cost you 500k gold pieces. Are you sure?");
        options("Buy the Crystal of Ithell for 500k gp?", "Yes.", "No.")
                .onOptionOne(() -> giveIthellCrystal(player));
        npc(350,"You want a Crystal of Meilyr? This Crystal has the power to revert the colored versions of the Blade of Saeldor and Bow of Faerdhinen.").executeAction(() -> {
            setKey(400);
        });
        npc(400,"This Crystal of Meilyr will cost you 500k gold pieces. Are you sure?");
        options("Buy the Crystal of Meilyr for 500k gp?", "Yes.", "No.")
                .onOptionOne(() -> giveMeilyrCrystal(player));
    }


}
