package me.dinovote.dinovote.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dinovote.dinovote.DinoVoteRewards;
import org.bukkit.entity.Player;

public class PAPIExpansion extends PlaceholderExpansion {

    private final DinoVoteRewards plugin;
    public PAPIExpansion(DinoVoteRewards plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "dinovoterewards";
    }

    @Override
    public String getAuthor() {
        return "dino14";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %dinovoterewards_votes%
        if(identifier.equals("votes")){
            /*int idx = -1;
            for(int i = 0;i < plugin.jucatori.size();i++){
                if(plugin.jucatori.get(i).getNume().equals(player.getName())){
                    idx = i;
                    break;
                }
            }*/
            if(plugin.jucatori.get(player.getName()) != null) {
                return Integer.toString(plugin.jucatori.get(player.getName()).getVotes());
                //return Integer.toString(plugin.jucatori.get(idx).getVotes());
            }
            return "0";
        }

        // %dinovoterewards_vp_votes%
        if(identifier.equals("vp_votes")){
            if(plugin.vpVotes != 0) {
                return Integer.toString(plugin.vpVotes);
            }
            return "0";
        }

        // %dinovoterewards_vp_votes_required%
        if(identifier.equals("vp_votes_required")){
            return Integer.toString(plugin.vpVotesRequired);
        }

        //daca este alt placeholder, returnam null
        return null;
    }
}
