package me.dinovote.dinovote.utils;

import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RemindMessage extends BukkitRunnable {
    private final DinoVoteRewards plugin;

    public RemindMessage(DinoVoteRewards plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<String> remindmsg = plugin.getConfig().getStringList("Messages.RemindMessage");
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            String name = p.getName().toLowerCase();
            Long lastvoted = plugin.jucatori.get(p.getName()).getLastVoted();
            if (lastvoted <= System.currentTimeMillis() - 86400000L) {
                if (!p.hasPermission("dinovote.noremind")) {
                    plugin.sendMessage(remindmsg, p);
                }
            }
            //System.out.println("S-a executat task-ul !");
        }
    }
}
