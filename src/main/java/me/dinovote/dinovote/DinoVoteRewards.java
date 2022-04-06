package me.dinovote.dinovote;

import me.dinovote.dinovote.commands.VoteCommand;
import me.dinovote.dinovote.commands.VoteRewardCommand;
import me.dinovote.dinovote.commands.VotesCommand;
import me.dinovote.dinovote.events.PlayerJoin;
import me.dinovote.dinovote.events.VoteEvent;
import me.dinovote.dinovote.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class DinoVoteRewards extends JavaPlugin implements Listener {

    //public ArrayList<Jucator> jucatori = new ArrayList<Jucator>();
    public HashMap<String, Jucator>  jucatori = new HashMap<String, Jucator>();
    public int vpVotes;
    public int vpVotesRequired;

    public MySQL sql;
    public SQLUtils data;

    @Override
    public void onEnable() {
        getLogger().info("DinoVoteRewards " + getDescription().getVersion() + " has been enabled");
        getServer().getPluginManager().registerEvents(this,this); //pentru inregistrarea eventurilor din main
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new VoteEvent(this), this);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PAPIExpansion(this).register();
        }
        Objects.requireNonNull(getCommand("vote")).setExecutor(new VoteCommand(this));
        Objects.requireNonNull(getCommand("votereward")).setExecutor(new VoteRewardCommand(this));
        Objects.requireNonNull(getCommand("votes")).setExecutor(new VotesCommand(this));

        //generare config.yml
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        vpVotesRequired = getConfig().getInt("VoteParty.votesRequired");


        //setari pentru baza de date mysql
        if(getConfig().getBoolean("MySQL.enable")) {
            this.sql = new MySQL(this);
            this.data = new SQLUtils(this);

            try {
                sql.connect();
            } catch (ClassNotFoundException | SQLException e) {
                //e.printStackTrace();
                getLogger().info("Database not connected");
            } //throwables.printStackTrace();


            if (sql.isConnected()) {
                getLogger().info("Database is connected");
                data.createTable();
                data.createTableSettings();
                String server = getConfig().getString("MySQL.ServerName");
                data.createColumn(server);
                if(getConfig().getBoolean("Settings.RemindMessage")){
                    data.createColumn("lastvoted");
                }
                data.createSetting("VotePartyVotes");
                data.loadVotes();
                //noVotes = data.getSetting("TotalVotes");

            }
        }
        else {
            //generare votes.yml
            FileStorage.setup();
            FileStorage.getFile1().options().copyDefaults(true);
            FileStorage.getFile2().options().copyDefaults(true);
            FileStorage.saveFiles();
            this.getPlayers();
            this.getVotes();
        }

        if (getConfig().getBoolean("Settings.RemindMessage")) {
            RemindMessage remind = new RemindMessage(this);

            int remindtime = getConfig().getInt("Settings.RemindTime"); //in secunde
            remind.runTaskTimer(this,20L, remindtime * 20L);

           /* BukkitRunnable.runTaskTimer(this, 20L, 15*20L) {
                @Override
                public void run() {
                    List<String> remindmsg = getConfig().getStringList("Messages.RemindMessage");
                    for (Player p : getServer().getOnlinePlayers()) {
                        String name = p.getName().toLowerCase();
                        Long lastvoted = jucatori.get(p.getName()).getLastVoted();
                        if (lastvoted <= System.currentTimeMillis() - 86400000L) {
                            if (!p.hasPermission("dinovote.noremind")) {
                                sendMessage(remindmsg, p);
                            }
                        }
                        System.out.println("S-a executat task-ul !");
                    }
                }
            }, 20L, 15 * 20L);*/
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("DinoVote " + getDescription().getVersion() +  " has been disabled");
        if(getConfig().getBoolean("MySQL.enable")) {
            sql.disconnect();
        }
        else {
            FileStorage.saveFiles();
        }

    }


    public void getPlayers(){
        for (String key : FileStorage.getFile1().getKeys(false)) {
            //deep false inseamna ca nu vrem sectiunile inferioare din config (adica in cazul asta x,y,z)
            if (FileStorage.getFile1().isConfigurationSection(key)) {
               // jucatori.add(new Jucator());
                jucatori.put(key, new Jucator());
                //String nume = key;
                int votes = 0;
                int offlinevotes = 0;
                long lastvoted = 0;
                for(String str : FileStorage.getFile1().getConfigurationSection(key).getKeys(false)){
                    if(str.contains("votes")){
                        votes = FileStorage.getFile1().getInt(key + ".votes" );
                        //System.out.println("Jucatorul are " + votes);
                    }
                    if(str.contains("offlinevotes")){
                        offlinevotes = FileStorage.getFile1().getInt(key + ".offlinevotes" );
                        //System.out.println("Jucatorul are " + votes + "voturi offline");
                    }
                    if(str.contains("lastvoted")){
                        lastvoted = FileStorage.getFile1().getLong(key + ".lastvoted");
                    }
                }
                //jucatori.get(jucatori.size() - 1).setData(key,votes,offlinevotes);
                jucatori.get(key).setData(key,votes,offlinevotes,lastvoted);
            }
        }
    }

    public void getVotes() {
        if(FileStorage.getFile2().isConfigurationSection("VoteParty")) {
            this.vpVotes = FileStorage.getFile2().getInt("VoteParty.votes");
        } else {
            FileStorage.getFile2().set("VoteParty.votes", this.vpVotes);
            FileStorage.saveFiles();
        }
    }

    public void sendCmds(List<String> cmds, Player player){
        for (int i = 0; i < cmds.size(); i++) {
            String cmdcolor = (ChatColor.translateAlternateColorCodes('&', cmds.get(i).replace("%player%", player.getName())));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmdcolor);
        }
    }

    public void sendMessage(List<String> msg, Player player){
        int votes;
        if(jucatori.get(player.getName()) == null){
            votes = 0;
        }
        else{
            votes = jucatori.get(player.getName()).getVotes();
        }
        for (int i = 0; i < msg.size(); i++) {
            String msgcolor = (ChatColor.translateAlternateColorCodes('&', msg.get(i).replace("%player%", player.getName()).replace("%votes%", Integer.toString(votes))));
            player.sendMessage(msgcolor);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("dinovote")){
            if(args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "Plugin developed by dino14");
            }
            else if(args[0].equalsIgnoreCase("reload")){
                if (sender.hasPermission("dinovote.reload")) {
                    reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Dinovote has been reloaded.");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to execute this command");
                }
            }
            else if(args[0].equalsIgnoreCase("version")){
                if(sender.hasPermission("dinovote.version")) {
                    sender.sendMessage(ChatColor.GREEN + "You are running DinoVote version" + ChatColor.YELLOW + " " + getDescription().getVersion());
                }
            }
        }


        return true;
    }


}
