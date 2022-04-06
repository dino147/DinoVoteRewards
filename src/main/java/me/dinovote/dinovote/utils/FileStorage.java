package me.dinovote.dinovote.utils;

import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileStorage {
    private DinoVoteRewards plugin;
    private static File file1;
    private static File file2;
    private static FileConfiguration votes;
    private static FileConfiguration server;

    public FileStorage(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    //Gaseste sau genereaza fisierul
    public static void setup(){
        file1 = new File(Bukkit.getServer().getPluginManager().getPlugin("DinoVoteRewards").getDataFolder(),"votes.yml");
        if(!file1.exists()){
            try{
                file1.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        votes = YamlConfiguration.loadConfiguration(file1);

        file2 = new File(Bukkit.getServer().getPluginManager().getPlugin("DinoVoteRewards").getDataFolder(),"server.yml");
        if(!file2.exists()){
            try{
                file2.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        server = YamlConfiguration.loadConfiguration(file2);
    }

    public static FileConfiguration getFile1(){
        return votes;
    }

    public static FileConfiguration getFile2(){
        return server;
    }

    public static void saveFiles(){
        try{
            votes.save(file1);
        }
        catch(IOException e){
            System.out.println("[DinoVoteRewards] votes.yml couldn't be saved !");
            e.printStackTrace();
        }

        try{
            server.save(file2);
        }
        catch(IOException e){
            System.out.println("[DinoVoteRewards] server.yml couldn't be saved !");
            e.printStackTrace();
        }
    }
}
