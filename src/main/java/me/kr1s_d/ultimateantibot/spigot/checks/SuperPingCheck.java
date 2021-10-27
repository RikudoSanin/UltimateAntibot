package me.kr1s_d.ultimateantibot.spigot.checks;


import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SuperPingCheck {

    private final AntibotManager antibotManager;
    private final Map<String, Integer> data;
    private final ConfigManager configManager;

    public SuperPingCheck(UltimateAntibotSpigot plugin){
        this.data = new HashMap<>();
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();

        new BukkitRunnable() {
            @Override
            public void run() {
                data.clear();
            }
        }.runTaskTimer(plugin, 20, 20L * configManager.getSuperJoin_time());
    }

    public void check(String ip){
        if(data.containsKey(ip)){
            data.put(ip, data.get(ip) + 1);
        }else{
            data.put(ip, 0);
        }
        if(data.get(ip) > configManager.getSuperJoin_amount() * 3){
            antibotManager.blacklist(ip);
        }
    }
}
