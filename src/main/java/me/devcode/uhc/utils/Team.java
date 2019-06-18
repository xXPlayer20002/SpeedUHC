package me.devcode.uhc.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.devcode.uhc.SpeedUHC;

@Getter
@Setter
public class Team {

    private String name, prefix, tab;
    private Integer size;
    private Location loc, teamLoc, teamLoc2;
    private List<Player> players;
    private byte colorData;

    public Team(String pName, String pPrefix, Integer pSize, byte pColorData) {
        this.name = pName;
        this.prefix = pPrefix;
        this.size = pSize;
        this.colorData = pColorData;
    }

    public boolean isInTeam(Player player) {
        return players.contains(player);
    }

    public void addPlayer(Player player) {
        if(isInTeam(player)) {
            //Wird später replaced durch Message Config
            player.sendMessage("Du bist bereits in diesem Team");
            return;
        }
        SpeedUHC.getInstance().getPlayerUtils().setTeam(player, this);
        players.add(player);
        player.sendMessage("Du bist jetzt im Team" + getPrefix() + getName());
        player.setDisplayName(prefix + player.getName());
    }

    public void removePlayer(Player player) {
        if(!isInTeam(player))
            return;
        players.remove(player);
        SpeedUHC.getInstance().getPlayerUtils().removeTeamByPlayer(player);
    }

    public ItemStack getIcon() {
        ItemStack stack = new ItemStack(Material.STAINED_CLAY, 1, colorData);
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.setDisplayName(prefix+name);
        List<String> teamPlayers = new ArrayList<>();
        getPlayers().forEach( player ->
                teamPlayers.add(prefix + player.getName()));
        itemMeta.setLore(teamPlayers);
        stack.setItemMeta(itemMeta);
        return stack;
    }

}
