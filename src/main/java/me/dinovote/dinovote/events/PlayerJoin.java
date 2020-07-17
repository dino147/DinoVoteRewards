package me.dinovote.dinovote.events;

import me.dinovote.dinovote.DinoVoteRewards;
import me.dinovote.dinovote.Jucator;
import me.dinovote.dinovote.utils.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoin implements Listener {

    //Plugin plugin = DinoVoteRewards.getPlugin(DinoVoteRewards.class);

    private final DinoVoteRewards plugin;

    public PlayerJoin(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        boolean jmsgbool = plugin.getConfig().getBoolean("Settings.PlayerJoinMessage");
        List<String> joinmsg = plugin.getConfig().getStringList("Messages.JoinMessage");

        if(jmsgbool) {
            plugin.sendMessage(joinmsg, player);
        }
        if(plugin.jucatori.get(player.getName()) == null){
            plugin.jucatori.put(player.getName(), new Jucator());
        }
        if(plugin.jucatori.get(player.getName()) != null && plugin.jucatori.get(player.getName()).getOfflineVotes() != 0){
            int offvotes = plugin.jucatori.get(player.getName()).getOfflineVotes();

            boolean ok = false; //default jucatorul n-are permisiuni de vot dublu
            if(plugin.getConfig().getBoolean("Settings.PermVote")) {
                List<String> permcmds;
                for (String key : plugin.getConfig().getConfigurationSection("PermVote").getKeys(false)) {
                    if (player.hasPermission("dinovote." + key)) {
                        ok = true;
                        permcmds = plugin.getConfig().getStringList("PermVote." + key);
                        for (int j = 0; j < offvotes; j++) {
                            //Acum trimitem comenzile setate in config pe care le-am salvat in lista comenzi
                            plugin.sendCmds(permcmds, player);
                        }
                    }
                }
            }
            if(!ok) {
                List<String> comenzi = plugin.getConfig().getStringList("Commands");
                for (int i = 0; i < offvotes; i++) {
                    plugin.sendCmds(comenzi,player);
                }
            }



            boolean cumbool = plugin.getConfig().getBoolean("Settings.Cumulative");
            //sectiunea pt vot cumulative
            if(cumbool) {
                List<String> cumulative;
                List<String> msgcum;
                int voturion = plugin.jucatori.get(player.getName()).getVotes();
                int voturioff = plugin.jucatori.get(player.getName()).getOfflineVotes();
                int sumvotes = voturion + voturioff;
                int i = 1, ex = 0;
                for (String key : plugin.getConfig().getConfigurationSection("Cumulative").getKeys(false)) {
                    i = Integer.parseInt(key);
                    ex = sumvotes/i - voturion/i;
                    for (int k = 0; k < ex; k++) {
                        cumulative = plugin.getConfig().getStringList("Cumulative." + key + ".commands");
                        msgcum = plugin.getConfig().getStringList("Cumulative." + key + ".message");
                        plugin.sendCmds(cumulative,player);
                        plugin.sendMessage(msgcum,player);
                    }
                }
            }


           // plugin.jucatori.get(player.getName()).setVotes(plugin.jucatori.get(player.getName()).getVotes() + plugin.jucatori.get(player.getName()).getOfflineVotes());
            plugin.jucatori.get(player.getName()).setOfflinevotes(0);

            if(plugin.getConfig().getBoolean("MySQL.enable")) {
                //plugin.data.setVotes(player.getName(),plugin.jucatori.get(player.getName()).getVotes());
                plugin.data.setOfflineVotes(player.getName(),plugin.jucatori.get(player.getName()).getOfflineVotes());
            }
            else{
                if (!FileStorage.getFile().isConfigurationSection(player.getName())) {
                    FileStorage.getFile().createSection(player.getName());
                }

                FileStorage.getFile().set(player.getName() + ".votes", plugin.jucatori.get(player.getName()).getVotes());
                FileStorage.getFile().set(player.getName() + ".offlinevotes", plugin.jucatori.get(player.getName()).getOfflineVotes());
                FileStorage.saveFile();
            }



        }
    }
}
