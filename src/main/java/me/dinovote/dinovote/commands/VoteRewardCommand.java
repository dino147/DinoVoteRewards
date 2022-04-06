package me.dinovote.dinovote.commands;

import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VoteRewardCommand implements CommandExecutor {

    private final DinoVoteRewards plugin;

    public VoteRewardCommand(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            List<String> rwdmsg = plugin.getConfig().getStringList("Messages.voterewardmsg");
            int votes;
            if(plugin.jucatori.get(player.getName()) == null){
                votes = 0;
            }
            else{
                votes = plugin.jucatori.get(player.getName()).getVotes();
            }
            if(player.hasPermission("dinovote.votereward")) {
                for (int i = 0; i < rwdmsg.size(); i++) {
                    String msgcolor = (ChatColor.translateAlternateColorCodes('&', rwdmsg.get(i).replace("%votes%", Integer.toString(votes))));
                    //String msgcolor = (ChatColor.translateAlternateColorCodes('&', rwdmsg.get(i)));
                    player.sendMessage(msgcolor);
                }
            }
            else{
                player.sendMessage(ChatColor.RED + "You don't have permission to send this command !");
            }
        }
        else{
            System.out.println("You need to be a player  to send this command");
        }
        return true;
    }
}
