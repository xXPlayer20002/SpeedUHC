package me.devcode.uhc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.Getter;
import lombok.SneakyThrows;
import me.devcode.uhc.utils.PlayerUtils;
import me.devcode.uhc.utils.ServerUtils;
import me.devcode.uhc.utils.WorldUtils;

@Getter
public class SpeedUHC extends JavaPlugin {

    @Getter
    private static SpeedUHC instance;

    private int teams;

    private String worldName;

    public ServerUtils serverUtils;
    public PlayerUtils playerUtils;
    public WorldUtils worldUtils;

    @Override
    public void onLoad() {
        this.getDataFolder().mkdir();
        instance = this;
        serverUtils = new ServerUtils();

        if(!serverUtils.getTeamFile().exists())
            loadFile("teams.yml");

        serverUtils.setConfig();
        playerUtils = new PlayerUtils();
        worldUtils = new WorldUtils();

        if(!worldUtils.getFile().exists())
            loadFile("world.yml");
        worldUtils.setConfig();
        worldName = worldUtils.getCfg().getString("World.Name");
        deleteWorld(new File(worldName));
        worldUtils.changeBiome();


        //Ändern
        teams = 5;
    }

    @Override
    public void onEnable() {
        new BukkitRunnable() {

            @Override
            public void run() {
                worldUtils.createWorld();
            }
        }.runTaskLater(this, 1);

    }

    @SneakyThrows
    public void loadFile(String file) {
        File t = new File(this.getDataFolder(), file);
        System.out.println("Writing new file: " + t.getAbsolutePath());

        t.createNewFile();
        FileWriter out = new FileWriter(t);
        InputStream is = getClass().getResourceAsStream("/" + file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            out.write(line + "\n");
        }
        System.out.println(line);
        out.flush();
        is.close();
        isr.close();
        br.close();
        out.close();
    }

    private boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

}
