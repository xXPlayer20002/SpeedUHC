package me.devcode.uhc.utils;

import net.minecraft.server.v1_8_R3.BiomeBase;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;
import me.devcode.uhc.SpeedUHC;

@Getter
@Setter
public class WorldUtils {

    private World world;

    private boolean joinAble = false;

    public File file = new File("plugins/SpeedUHC", "world.yml");
    public FileConfiguration cfg = null;

    public void changeBiome() {
        Field biomesField = null;
        try{
            biomesField = BiomeBase.class.getDeclaredField("biomes");
            biomesField.setAccessible(true);
            if(biomesField.get(null) instanceof  BiomeBase[]) {
                BiomeBase[] biomes = BiomeBase.getBiomes();
                for(BiomeBase biomeBase : biomes) {
                    if(biomeBase != null) {
                        if(biomeBase.ah != null) {
                            String biome = cfg.getString("World.Biomes." + biomeBase.ah.toUpperCase());
                            if(biome != null) {
                                biomes[biomeBase.id] = getBiomes(biome);
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private BiomeBase getBiomes(String biome) {
        for(BiomeBase biomeBase : BiomeBase.getBiomes()) {
            if(biomeBase.ah.toUpperCase().contains(biome)) {
                return biomeBase;
            }
        }
        return BiomeBase.PLAINS;
    }

    public void createWorld() {
        if(world == null) {
            WorldCreator worldCreator = new WorldCreator(SpeedUHC.getInstance().getWorldName());
            worldCreator.environment(World.Environment.NORMAL);
            world = worldCreator.createWorld();
            world.setTime(1000);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setSpawnLocation(0,100,0);
            world.setDifficulty(Difficulty.HARD);
            System.out.println("Chunks loading (this takes a lot of time)");
            new ChunkLoader(Integer.valueOf(360+(Bukkit.getViewDistance()+1)*16)).runTaskTimer(SpeedUHC.getInstance(), 5, 3);
        }
    }




    public void setConfig() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }
}
