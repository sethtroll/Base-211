package com.zenyte.database.impl;

import com.zenyte.Constants;
import com.zenyte.game.BonusXpManager;
import com.zenyte.game.RuneDate;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;

import java.sql.*;


public class Store implements Runnable {

    public static final String HOST = "localhost"; // website ip address
    public static final String USER = "Pharaoh1";
    public static final String PASS = "NEWrspsPass123!!";
    public static final String DATABASE = "Pharaoh";

    public int donationGoal = 100; //$100 a goal for today
    private Player player;
    private Connection conn;
    private Statement stmt;

    /**
     * The constructor
     * @param player
     */
    public Store(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getUsername().replace("_", " ");
            ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND status='Completed' AND claimed=0");
            while (rs.next()) {
                int item_number = rs.getInt("item_number");
                double paid = rs.getDouble("amount");
                int quantity = rs.getInt("quantity");
                int personalGoal = 0;

                switch (item_number) {// add products according to their ID in the ACP
                case 10: //5
                    player.sendMessage("Thank you for supporting Pharaoh!");
                    player.sendMessage("You have purchased a $5 bond");
                    player.getInventory().addOrDrop(30051, quantity);
                    donationGoal+= 5 * quantity;
                    personalGoal+= 5 * quantity;
                    break;

                    case 19: //10
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a $10 bond");
                        player.getInventory().addOrDrop(13190, quantity);
                        int value = 10 * quantity;
                        World.sendMessage(MessageType.GLOBAL_BROADCAST, "["+player.getName()+"] has just donated $"+value+"!");
                        donationGoal+= 10* quantity;
                        personalGoal+= 10 * quantity;
                        break;
                    case 20: //50
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a $50 bond");
                        player.getInventory().addOrDrop(30017, quantity);
                        int value2 = 50 * quantity;
                        World.sendMessage(MessageType.GLOBAL_BROADCAST, "["+player.getName()+"] has just donated $"+value2+"!");
                        donationGoal+= 50 * quantity;
                        personalGoal+= 50 * quantity;
                        break;
                    case 21: //100
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a $100 bond");
                        player.getInventory().addOrDrop(30018, quantity);
                        int value3 = 100 * quantity;
                        World.sendMessage(MessageType.GLOBAL_BROADCAST, "["+player.getName()+"] has just donated $"+value3+"!");
                        donationGoal+= 100 * quantity;
                        personalGoal+= 100 * quantity;
                        break;
                    case 22: //farmin ring
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a Farming Ring");
                        player.getInventory().addOrDrop(40001, quantity);
                        int value4 = 20 * quantity;
                        World.sendMessage(MessageType.GLOBAL_BROADCAST, "["+player.getName()+"] has just donated $"+value4+"!");
                        donationGoal+= 20 * quantity;
                        personalGoal+= 20 * quantity;
                        break;
                    case 23: //teleportal ring
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a Teleportal Ring");
                        player.getInventory().addOrDrop(32135, quantity);
                        int value5 = 25 * quantity;
                        World.sendMessage(MessageType.GLOBAL_BROADCAST, "["+player.getName()+"] has just donated $"+value5+"!");
                        donationGoal+= 25 * quantity;
                        break;
                    case 24: //Mystery box
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a Mystery box");
                        player.getInventory().addOrDrop(60001, quantity);
                        int value6 = 20 * quantity;
                        World.sendMessage(MessageType.GLOBAL_BROADCAST, "["+player.getName()+"] has just donated $"+value6+"!");
                        donationGoal+= 20 * quantity;
                        personalGoal+= 20 * quantity;
                        break;
                    case 25: //5 box
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a $5 Lucky Box");
                        player.getInventory().addOrDrop(60050, quantity);
                        donationGoal+= 5 * quantity;
                        personalGoal+= 5 * quantity;
                        break;
                    case 26: //pvm
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased a Pvm Loot Key");
                        player.getInventory().addOrDrop(85, quantity);
                        donationGoal+= 3 * quantity;
                        personalGoal+= 3 * quantity;
                        break;
                    case 27: //boss
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased Boss Loot Key");
                        player.getInventory().addOrDrop(7678, quantity);
                        donationGoal+= 5 * quantity;
                        personalGoal+= 5 * quantity;
                        break;
                    case 28: //Event scroll
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased 5x Event Scroll");
                        player.getInventory().addOrDrop(50700, quantity);
                        donationGoal+= 15 * quantity;
                        personalGoal+= 15 * quantity;
                        break;
                    case 29: //weapon box
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have purchased Weapon Box");
                        player.getInventory().addOrDrop(50800, quantity);
                        donationGoal+= 5 * quantity;
                        personalGoal+= 5 * quantity;
                        break;
                    case 30: //1$ Tickets
                        player.sendMessage("Thank you for supporting Pharaoh!");
                        player.sendMessage("You have 1$ Ticket or Tickets");
                        player.getInventory().addOrDrop(620, quantity);
                        donationGoal+= 1 * quantity;
                        personalGoal+= 1 * quantity;
                        break;
                    default:
                        player.sendMessage("Sorry your payment has not been found.");
                        player.sendMessage("Please contact an administratior if your payment has gone wrong.");

                        break;
            }
                int chance = 0;
                final int random = Utils.random(chance);
                if (personalGoal >= 100) {
                    if (random <= 80) {
                        player.getInventory().addItem(6199, 1);
                    } else  if (random > 80 && random <= 95) {
                        player.getInventory().addItem(6199, 2);
                    } else {
                        player.getInventory().addItem(6199, 3);

                    }
                    player.sendMessage("<col=00FF00><shad=000000>Thank you for donating a very generous amount of money.</col></shad>");
                    player.sendMessage("<col=00FF00><shad=000000>Due to your support you have gained a chance to earn 1/3 mystery boxes.</col></shad>");
                    player.sendMessage("3 = 5%,  2 = 15%  3 = 80%");
                    personalGoal = 0;
                }
                if (donationGoal >= 100) {
                    World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=00FF00><shad=000000>the server has reached $100 today</col></shad>");
                    World.sendMessage(MessageType.GLOBAL_BROADCAST, "<col=00FF00><shad=000000>Due to reaching a server goal double experience has been added.</col></shad>");
                    WorldBroadcasts.broadcast(player, BroadcastType.WELL_EVENT,"BXP", 24*1);
                    BonusXpManager.set((long) 1*60*60*1000*24 + (Constants.BOOSTED_XP ? BonusXpManager.expirationDate : RuneDate.currentTimeMillis()));
                    donationGoal = 0;
                }
                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param host the host ip address or url
     * @param database the name of the database
     * @param user the user attached to the database
     * @param pass the users password
     * @return true if connected
     */
    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

    /**
     * Disconnects from the MySQL server and destroy the connection
     * and statement instances
     */
    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
