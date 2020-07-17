package me.dinovote.dinovote.commands;

import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VotesCommand implements CommandExecutor {

    private DinoVoteRewards plugin;

    public VotesCommand(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                int votes = plugin.jucatori.get(player.getName()).getVotes();
                List<String> msg = plugin.getConfig().getStringList("Messages.VotesCommand");
                plugin.sendMessage(msg, player);
            } else if (player.hasPermission("dinovote.votes.other")) {
                //Player target = Bukkit.getPlayerExact(args[0]);
                String target = args[0];
                int votes;
                if(plugin.jucatori.get(target) == null){
                    votes = 0;
                }
                else{
                    votes = plugin.jucatori.get(target).getVotes();
                }
                List<String> msg = plugin.getConfig().getStringList("Messages.VotesCommandOther");
                for (int i = 0; i < msg.size(); i++) {
                    String msgcolor = (ChatColor.translateAlternateColorCodes('&', msg.get(i).replace("%player%", target).replace("%votes%", Integer.toString(votes))));
                    player.sendMessage(msgcolor);
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "You don't have permission to execute this command");
            }
        }
        else{
            sender.sendMessage("You need to be a player to execute this command");
        }
        return true;
    }
}
