package me.devcode.uhc.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import lombok.Getter;
import me.devcode.uhc.SpeedUHC;

@Getter
public class PlayerUtils {

    private List<Player> players = new ArrayList<>();
    private List<Player> players2 = new ArrayList<>();
    private List<Player> specs = new ArrayList<>();
    private Map<Player, Team> getTeamByPlayer = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addPlayer2(Player player) {
        players2.add(player);
    }

    public void removePlayer2(Player player) {
        players2.remove(player);
    }

    public void addSpec(Player player) {
        specs.add(player);
    }

    public void removeSpec(Player player) {
        specs.remove(player);
    }

    public Team getTeamByPlayer(Player player) {
        return getTeamByPlayer.getOrDefault(player, null);
    }

    public void setTeam(Player player, Team team) {
        if(getTeamByPlayer(player) != null) {
            removeTeamByPlayer(player);
        }
        getTeamByPlayer.put(player, team);
    }

    public void removeTeamByPlayer(Player player) {
        if(getTeamByPlayer(player)== null)
            return;
        getTeamByPlayer.remove(player);
    }

    public void addRandomTeam(Player player) {
        if(getTeamByPlayer(player) !=null)
            return;
        IntStream.range(0, SpeedUHC.getInstance().getServerUtils().getMaxSize()).forEach(teamSize ->{
            IntStream.range(0, SpeedUHC.getInstance().getServerUtils().getAllTeams().size()).forEach(i ->{
                if(getTeamByPlayer(player) != null)
                    return;
                Team team = SpeedUHC.getInstance().getServerUtils().getAllTeams().get(i);
                if(team.getPlayers().size() == teamSize) {
                    if(!team.getPlayers().contains(player)) {
                        setTeam(player, team);
                        team.addPlayer(player);
                    }
                }
            });
        });
    }

}
