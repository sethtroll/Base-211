package com.zenyte.database.impl;

import com.zenyte.game.world.entity.player.Player;

import java.sql.*;


public class Vote implements Runnable {

    public static final String HOST = "141.136.33.165"; // website ip address
    public static final String USER = "u334180530_vote";
    public static final String PASS = "Newrsps123";
    public static final String DATABASE = "u334180530_vote";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public Vote(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getName().replace(" ", "_");
            ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date IS NOT NULL");

            while (rs.next()) {
                String timestamp = rs.getTimestamp("callback_date").toString();
                String ipAddress = rs.getString("ip_address");
                int siteId = rs.getInt("site_id");


                player.getInventory().addItem(8855, 1);
                player.sendMessage("Thank you for voting for Pharaoh!");
                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

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
