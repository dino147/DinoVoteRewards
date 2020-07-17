package me.dinovote.dinovote.commands;

import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VoteCommand implements CommandExecutor {

    private final DinoVoteRewards plugin;

    public VoteCommand(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;//facem un cast de la sender catre obiecul player

            //variabilele din config
            //List<String> votemessage = getConfig().getStringList("broadcastmessage");
            // int nr = getConfig().getInt("Numar");
            // boolean ok = getConfig().getBoolean("Adevarat");
            // String Lista = getConfig().getStringList("votemessage").get(2); - elementul de pe linia 2
            List<String> msgmare = plugin.getConfig().getStringList("Messages.votemessage");
            int votes = plugin.jucatori.get(player.getName()).getVotes();

            //  player.sendMessage(ChatColor.GREEN + "Noobs " + nr + " " + ok + " " + Lista);

            //acum trimitem mesajul multiplu cu un for, lista List<String>
            for(int i = 0; i < msgmare.size(); i++){
                String msgcolor = (ChatColor.translateAlternateColorCodes('&', msgmare.get(i).replace("%votes%", Integer.toString(votes))));
                player.sendMessage(msgcolor);
            }
            //     player.setHealth(0);
        }
        else{
            System.out.println("You need to be a player to send this message");
        }
        return true;
    }
}
