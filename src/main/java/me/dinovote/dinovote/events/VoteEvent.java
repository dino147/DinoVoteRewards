package me.dinovote.dinovote.events;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.dinovote.dinovote.DinoVoteRewards;
import me.dinovote.dinovote.Jucator;
import me.dinovote.dinovote.utils.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class VoteEvent implements Listener {

    //Plugin plugin = DinoVoteRewards.getPlugin(DinoVoteRewards.class);
    private final DinoVoteRewards plugin;

    public VoteEvent(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onVotifierEvent(VotifierEvent event){
        Vote vote = event.getVote();
        String adr = vote.getAddress();
        Player player = Bukkit.getPlayerExact(vote.getUsername());
        String service = vote.getServiceName();

        List<String> bcmesaj = plugin.getConfig().getStringList("Messages.broadcastmessage");
        List<String> vpbcmesaj = plugin.getConfig().getStringList("Messages.votepartybroadcast");
        List<String> comenzi = plugin.getConfig().getStringList("Commands");
        boolean bcbool = plugin.getConfig().getBoolean("Settings.BroadCastVotes");
        boolean vpbool = plugin.getConfig().getBoolean("Settings.VoteParty");
        boolean mysqlbool = plugin.getConfig().getBoolean("MySQL.enable");
        int vpreqVotes = plugin.getConfig().getInt("VoteParty.votesRequired");
        plugin.vpVotes = plugin.vpVotes + 1;

        if(plugin.jucatori.get(vote.getUsername()) == null){
            plugin.jucatori.put(vote.getUsername(), new Jucator());
        }
        plugin.jucatori.get(vote.getUsername()).addVote();

        if(Bukkit.getPlayerExact(vote.getUsername()) != null) {
            //plugin.jucatori.get(idx).addVote();
            boolean ok = false; //default jucatorul n-are permisiuni de vot dublu
            List<String> playermsg = plugin.getConfig().getStringList("Messages.playermessage");
            plugin.sendMessage(playermsg, player);
            if(plugin.getConfig().getBoolean("Settings.PermVote")) {
                List<String> permcmds;
                for (String key : plugin.getConfig().getConfigurationSection("PermVote").getKeys(false)) {
                    if (player.hasPermission("dinovote." + key)) {
                        ok = true;
                        permcmds = plugin.getConfig().getStringList("PermVote." + key);
                        //Acum trimitem comenzile setate in config pe care le-am salvat in lista comenzi
                        plugin.sendCmds(permcmds,player);
                    }
                }
            }
            if(!ok){
                //Acum trimitem comenzile setate in config pe care le-am salvat in lista comenzi
                plugin.sendCmds(comenzi,player);
            }
        }
        else{
            //plugin.jucatori.get(idx).addOfflineVote();
            plugin.jucatori.get(vote.getUsername()).addOfflineVote();
            //plugin.data.addOfflineVotes(vote.getUsername(),1);
            }



        boolean cumbool = plugin.getConfig().getBoolean("Settings.Cumulative");
        //sectiunea pt vot cumulative
        if(cumbool) {
            List<String> cumulative;
            List<String> msgcum;
            if (Bukkit.getPlayerExact(vote.getUsername()) != null) {
                for (String key : plugin.getConfig().getConfigurationSection("Cumulative").getKeys(false)) {
                    int i = Integer.parseInt(key);
                    if (plugin.jucatori.get(vote.getUsername()).getVotes() % i == 0) {
                        cumulative = plugin.getConfig().getStringList("Cumulative." + key + ".commands");
                        msgcum = plugin.getConfig().getStringList("Cumulative." + key + ".message");
                        plugin.sendCmds(cumulative,player);
                        plugin.sendMessage(msgcum,player);
                    }
                }
            }
        }

        //sectiunea de voteparty
        if(vpbool) {
            //int vpreqVotes = plugin.getConfig().getInt("VoteParty.votesRequired");
             //!
            if(plugin.vpVotes == vpreqVotes) {
                plugin.vpVotes = plugin.vpVotes % vpreqVotes;
                List<String> vpCmds = plugin.getConfig().getStringList("VoteParty.commands");
                List<String> vpMsg = plugin.getConfig().getStringList("VoteParty.message");
                List<Player> onlinePlayersList = new ArrayList<>(Bukkit.getOnlinePlayers());
                for(Player p : onlinePlayersList) {
                    plugin.sendCmds(vpCmds, p);
                    plugin.sendMessage(vpMsg, p);
                }
                for (String line : vpbcmesaj) {
                    String msgcolor = (ChatColor.translateAlternateColorCodes('&', line.replace("%player%", vote.getUsername())));
                    Bukkit.getServer().broadcastMessage(msgcolor);
                }
            }
        }


        //acum trimitem mesajul multiplu cu un for, lista List<String>
        if(bcbool) {
            for (String line : bcmesaj) {
                String msgcolor = (ChatColor.translateAlternateColorCodes('&', line.replace("%player%", vote.getUsername())));
                Bukkit.getServer().broadcastMessage(msgcolor);
            }
        }

        if(mysqlbool) {
            plugin.data.createPlayer(vote.getUsername());
            int mysqlvotes = plugin.data.getVotes(vote.getUsername());
            if(mysqlvotes <= plugin.jucatori.get(vote.getUsername()).getVotes()){
                plugin.data.setVotes(vote.getUsername(), plugin.jucatori.get(vote.getUsername()).getVotes());

                //comenzi globale
                if(plugin.getConfig().getBoolean("Settings.GlobalCmds")){
                    if (Bukkit.getPlayerExact(vote.getUsername()) != null) {
                        List<String> global;
                        global = plugin.getConfig().getStringList("GlobalCmds");
                        plugin.sendCmds(global, player);
                    }
                }
            }
            else{
                plugin.jucatori.get(vote.getUsername()).setVotes(mysqlvotes);
            }

            plugin.data.setSetting("VotePartyVotes", (plugin.vpVotes % vpreqVotes));

            //System.out.println(vote.getUsername() + " are un numar de " + plugin.jucatori.get(vote.getUsername()).getOfflineVotes() + " voturi offline");
            plugin.data.setOfflineVotes(vote.getUsername(), plugin.jucatori.get(vote.getUsername()).getOfflineVotes());
            if(plugin.getConfig().getBoolean("Settings.RemindMessage")){
                plugin.data.setLastVoted(vote.getUsername(), plugin.jucatori.get(vote.getUsername()).getLastVoted());
            }
            //plugin.data.addVotes(player,1);
        }
        else {
            if (!FileStorage.getFile1().isConfigurationSection(vote.getUsername())) {
                FileStorage.getFile1().createSection(vote.getUsername());
            }
            FileStorage.getFile1().set(vote.getUsername() + ".votes", plugin.jucatori.get(vote.getUsername()).getVotes());
            FileStorage.getFile1().set(vote.getUsername() + ".offlinevotes", plugin.jucatori.get(vote.getUsername()).getOfflineVotes());
            FileStorage.getFile1().set(vote.getUsername() + ".lastvoted", plugin.jucatori.get(vote.getUsername()).getLastVoted());

            if (!FileStorage.getFile2().isConfigurationSection("VoteParty")) {
                FileStorage.getFile2().createSection("VoteParty");
            }
            FileStorage.getFile2().set("VoteParty.votes", plugin.vpVotes);

            FileStorage.saveFiles();
        }


    }


}
