package me.devcode.uhc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import lombok.Getter;
import lombok.SneakyThrows;
import me.devcode.uhc.SpeedUHC;

@Getter
public class ServerUtils {

    private List<Team> allTeams = new ArrayList<>();
    private List<Team> teamsLeft = new ArrayList<>();
    private int maxSize = 0;
    private File teamFile = new File("plugins/SpeedUHC", "teams.yml");
    private FileConfiguration teamsConfiguration;
    private Random random = new Random();

    public void registerTeams() {
        IntStream.range(0, SpeedUHC.getInstance().getTeams()).forEach(i ->{
           String teamStrings = teamsConfiguration.getStringList("Teams.Default").get(i);

           String[] array = teamStrings.split(";");
           Team team = new Team(array[0], array[1].replace("&", "§"), Integer.valueOf(array[2]), Byte.valueOf(array[3]));
           int tab = i+1;
           team.setTab("00" +tab+array[0]);
           //soon
        });
    }

    public Location getRandomLocation() {
        World world = Bukkit.getWorld(SpeedUHC.getInstance().getWorldName());
        int rangeMax = 350;
        int rangeMin = -350;

        int x = random.nextInt(rangeMax-rangeMin+1)+rangeMin;
        int z = random.nextInt(rangeMax-rangeMin+1)+rangeMin;
        int y = world.getHighestBlockYAt(x, z)+50;

        if(y > 110) {
            y = 110;
        }
        Location loc = new Location(world, x, y, z);
        if(loc.getBlock().getType().isSolid()) {
            return getRandomLocation();
        }
        return loc;
    }

    @SneakyThrows
    public void setConfig() {
        teamsConfiguration = YamlConfiguration.loadConfiguration(teamFile);
    }

    public void addTeamAll(Team team) {
        allTeams.add(team);
    }

    public void addTeamLeft(Team team) {
        teamsLeft.add(team);
    }

}
