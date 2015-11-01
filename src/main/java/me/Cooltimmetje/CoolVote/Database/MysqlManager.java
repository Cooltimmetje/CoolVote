package me.Cooltimmetje.CoolVote.Database;

import com.zaxxer.hikari.HikariDataSource;
import me.Cooltimmetje.CoolVote.Main;
import me.Cooltimmetje.CoolVote.Utilities.ChatUtils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class has been created on 10/29/2015 at 17:20 by Cooltimmetje.
 */
public class MysqlManager {

    private static HikariDataSource hikari = null;

    public static void setupHikari(){
        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", Main.getPlugin().getConfig().getString("serverName"));
        hikari.addDataSourceProperty("port", Main.getPlugin().getConfig().getString("port"));
        hikari.addDataSourceProperty("databaseName", Main.getPlugin().getConfig().getString("databaseName"));
        hikari.addDataSourceProperty("user", Main.getPlugin().getConfig().getString("user"));
        hikari.addDataSourceProperty("password", Main.getPlugin().getConfig().getString("password"));
    }

    public static void registerVote(Player p, String vote){
        ChatUtils.sendMsgTag(p, "Vote", "Verifying...");
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String uuid = p.getUniqueId().toString();
        String load = "SELECT * FROM votes WHERE uuid = '" + uuid + "';";

        try {
            ChatUtils.sendMsgTag(p, "Vote", "Connecting to database...");
            c = hikari.getConnection();
            ps = c.prepareStatement(load);
            rs = ps.executeQuery();

            ChatUtils.sendMsgTag(p, "Vote", "Checking if you have already voted...");
            if(rs.next()){
                ChatUtils.sendMsgTag(p, "Vote", ChatUtils.error + "You can only vote once!");
            } else {
                saveVote(p, vote);
            }

        } catch (SQLException e){
            e.printStackTrace();
            ChatUtils.sendMsgTag(p, "Vote", ChatUtils.error + "We're sorry, something went wrong, please try again.");
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveVote(Player p, String vote) {
        ChatUtils.sendMsgTag(p, "Vote", "Saving your vote...");
        Connection c = null;
        PreparedStatement ps = null;
        String uuid = p.getUniqueId().toString();
        String create = "INSERT INTO votes VALUES(?,?)";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(create);

            ps.setString(1, uuid);
            ps.setString(2, vote);

            ps.execute();
            ChatUtils.sendMsgTag(p, "Vote", "&2&lSUCCESS! &aYour vote has been saved! Thank you for voting!");
        } catch (SQLException e) {
            e.printStackTrace();
            ChatUtils.sendMsgTag(p, "Vote", ChatUtils.error + "We're sorry, something went wrong, please try again.");
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
