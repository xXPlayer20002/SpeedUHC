package me.devcode.uhc.utils;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import me.devcode.uhc.SpeedUHC;

public class ChunkLoader extends BukkitRunnable
{

    /*
    Inet Class
     */
    public static Integer a = Integer.valueOf(0);
    private Double chunks;
    private Integer chunks2;//d
    private Double d;//e
    private Double math;
    private Integer x;//g
    private Integer z;//h
    private Integer i;
    private Boolean b;
    public World world;

    public ChunkLoader(Integer paramInteger)
    {
        System.out.println("Starting Chunk Load");
        this.chunks = Double.valueOf(0.0D);
        this.chunks2 = Integer.valueOf(0);
        this.math = Double.valueOf(Math.pow(paramInteger.intValue(), 2.0D) / 64.0D);
        this.d = Double.valueOf(0.0D);
        this.x = Integer.valueOf(-paramInteger.intValue());
        this.z = Integer.valueOf(-paramInteger.intValue());
        this.world = ((World) Bukkit.getWorlds().get(0));
        if (this.world == null) {
            throw new IllegalStateException("World not found.");
        }
        this.i = paramInteger;
        this.b = Boolean.valueOf(false);
    }

    public void run()
    {
        Bukkit.getWorld("world").getSpawnLocation().getChunk().load(true);
        int mb = 1024 * 1024;

        if(Runtime.getRuntime().freeMemory()/mb <= 30) {
            System.out.println("Waiting for Ram... " + Runtime.getRuntime().maxMemory() + ":");
            return;
        }
        for (int i = 0; (i < 10) && (!this.b.booleanValue()); i++)
        {
            Location localLocation = new Location(this.world, this.x.intValue(), 0.0D, this.z.intValue());
            if (!localLocation.getChunk().isLoaded()) {
                localLocation.getWorld().loadChunk(localLocation.getChunk().getX(), localLocation.getChunk().getZ(), true);
            }
            this.x = Integer.valueOf(this.x.intValue() + 16);
            this.d = Double.valueOf(this.d.doubleValue() + 1.0D);
            if (this.x.intValue() > this.i.intValue())
            {
                this.x = Integer.valueOf(-this.i.intValue());
                this.z = Integer.valueOf(this.z.intValue() + 16);
                if (this.z.intValue() > this.i.intValue())
                {
                    this.d = this.math;
                    this.b = Boolean.valueOf(true);
                }
            }
        }
        this.chunks = Double.valueOf(this.d.doubleValue() / this.math.doubleValue() * 100.0D);
        a = Integer.valueOf(this.chunks.intValue());
        if (this.chunks2.intValue() < this.chunks.intValue())
        {
            if (this.chunks.intValue() <= 100)
            {
                System.out.println(this.chunks.intValue() + "%");

            }
            this.chunks2 = Integer.valueOf(this.chunks.intValue());
        }
        if (this.b.booleanValue())
        {

            System.out.println("Loaded.");
            System.out.println("Ram free: " +Runtime.getRuntime().freeMemory()/mb);
            Bukkit.getScheduler().runTaskLater(SpeedUHC.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println("Joinable");
                    System.out.println("Ram free: " +Runtime.getRuntime().freeMemory()/mb);
                    SpeedUHC.getInstance().worldUtils.setJoinAble(true);
                }
            }, 20*10);

            cancel();
        }
    }
}
