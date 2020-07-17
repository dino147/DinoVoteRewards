package me.dinovote.dinovote.utils;

import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileStorage {
    private DinoVoteRewards plugin;
    private static File file;
    private static FileConfiguration votes;

    public FileStorage(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    //Gaseste sau genereaza fisierul
    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("DinoVoteRewards").getDataFolder(),"votes.yml");
        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        votes = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getFile(){
        return votes;
    }

    public static void saveFile(){
        try{
            votes.save(file);
        }
        catch(IOException e){
            System.out.println("Nu s-a putut salva votes.yml ");
            e.printStackTrace();
        }
    }
}
