package me.dinovote.dinovote.utils;

import me.dinovote.dinovote.DinoVoteRewards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private DinoVoteRewards plugin;
    private String host,database,username,password;
    private int port;
    public MySQL(DinoVoteRewards plugin){
        this.plugin = plugin;
        host = plugin.getConfig().getString("MySQL.host");
        database = plugin.getConfig().getString("MySQL.database");
        username = plugin.getConfig().getString("MySQL.username");
        password = plugin.getConfig().getString("MySQL.password");
        port = plugin.getConfig().getInt("MySQL.port");
    }


    private Connection connection;

    public boolean isConnected(){
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            //connection = DriverManager.getConnection(link,user,parola)
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
        }
    }

    public void disconnect() {
        if(isConnected()){
            try{
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }
}
