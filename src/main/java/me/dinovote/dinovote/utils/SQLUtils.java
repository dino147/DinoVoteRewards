package me.dinovote.dinovote.utils;

import me.dinovote.dinovote.DinoVoteRewards;
import me.dinovote.dinovote.Jucator;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {

    private DinoVoteRewards plugin;

    private String table;

    public SQLUtils (DinoVoteRewards plugin){
        this.plugin = plugin;
        table = plugin.getConfig().getString("MySQL.table");
    }

    public void createTable(){
        PreparedStatement ps;

        try{
            ps = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (USER VARCHAR(100), VOTES INT(100), PRIMARY KEY(USER))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createColumn(String column){
        PreparedStatement ps;

        try{
            //IF COL_LENGTH('Person.Address', 'AddressID') IS NOT NULL
            //"ALTER TABLE " + table + " ADD " + server + " VARCHAR(100)"
            ps = plugin.sql.getConnection().prepareStatement("SELECT COUNT(*) as col_count FROM information_schema.COLUMNS WHERE  TABLE_SCHEMA = DATABASE() AND  TABLE_NAME = '" +
                    table + "' AND  COLUMN_NAME = '" + column + "'");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt("col_count") == 0){
                    PreparedStatement ps2;
                    ps2 = plugin.sql.getConnection().prepareStatement("ALTER TABLE " + table + " ADD " + column + " VARCHAR(100)");
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createPlayer(String player){
        try{
            if(!exists(player)){
                PreparedStatement ps2 = plugin.sql.getConnection().prepareStatement("INSERT IGNORE INTO " + table +
                        " (USER) VALUES (?)");
                ps2.setString(1,player);
                ps2.executeUpdate();

                return;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean exists(String player){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE USER=?");
            ps.setString(1,player);

            ResultSet results = ps.executeQuery();

            if(results.next()){
                return true;
            }
            return false;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void addVotes(String player, int votes){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + table + " SET VOTES=? WHERE USER=? ");
            ps.setString(1, String.valueOf(getVotes(player) + votes));//doar votes daca vreau sa fac setVotes
            ps.setString(2,player);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int getVotes(String player){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT VOTES FROM " + table + " WHERE USER=?");
            ps.setString(1,player);

            ResultSet result = ps.executeQuery();
            int votes = 0;
            if(result.next()){
                votes = result.getInt("VOTES");
            }
            return votes;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setVotes(String player, int votes){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + table + " SET VOTES=? WHERE USER=? ");
            ps.setString(1, String.valueOf(votes));//doar votes daca vreau sa fac setVotes
            ps.setString(2,player);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addOfflineVotes(String player, int votes){
        try{
            String sectiune = plugin.getConfig().getString("MySQL.ServerName");
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + table + " SET " + sectiune + "=? WHERE USER=? ");
            ps.setString(1, String.valueOf(getVotes(player) + votes));//doar votes daca vreau sa fac setVotes
            ps.setString(2,player);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int getOfflineVotes(String player){
        try{
            String sectiune = plugin.getConfig().getString("MySQL.ServerName");
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT " + sectiune + " FROM " + table + " WHERE USER=?");
            ps.setString(1,player);

            ResultSet result = ps.executeQuery();
            int votes = 0;
            if(result.next()){
                votes = result.getInt(sectiune);
            }
            return votes;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setOfflineVotes(String player, int votes){
        try{
            String sectiune = plugin.getConfig().getString("MySQL.ServerName");
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + table + " SET " + sectiune + "=? WHERE USER=? ");
            ps.setString(1, String.valueOf(votes));//doar votes daca vreau sa fac setVotes
            ps.setString(2,player);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void setLastVoted(String player, long lastvoted){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + table + " SET lastvoted=? WHERE USER=? ");
            ps.setString(1, String.valueOf(lastvoted));//doar votes daca vreau sa fac setVotes
            ps.setString(2,player);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void loadVotes(){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + table);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                //rs.getString("USER"),
                plugin.jucatori.put(rs.getString("USER"), new Jucator());
                int votes = rs.getInt("VOTES");
                int offvotes = rs.getInt(plugin.getConfig().getString("MySQL.ServerName"));
                long lastvoted = rs.getLong("lastvoted");
                plugin.jucatori.get(rs.getString("USER")).setData(rs.getString("USER"), votes, offvotes, lastvoted);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    //STERGERI
    public void emptyTable(){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("TRUNCATE " + table);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //PENTRU REMOVE DIN TABLE
    public void remove(Player player){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("DELETE FROM " + table + " WHERE USER=?");
            ps.setString(1, player.getName());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
