package me.kr1s_d.ultimateantibot.spigot.task;


import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

public class TimedWhitelist {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;


    public TimedWhitelist(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void check(String ip){
        new BukkitRunnable() {
            @Override
            public void run() {
                antibotManager.removeWhitelist(ip);
                plugin.getCounter().getStuffs().remove(ip);
            }
        }.runTaskLater(plugin, 10L);
    }
}
