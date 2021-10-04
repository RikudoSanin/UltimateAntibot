package me.kr1s_d.ultimateantibot.spigot.checks;


import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SuperJoinCheck {
    private final Map<String, Integer> superJoin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public SuperJoinCheck(UltimateAntibotSpigot plugin){
        this.superJoin = new HashMap<>();
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                superJoin.clear();
            }
        }.runTaskTimer(plugin, 0, configManager.getSuperJoin_time() * 20L);
    }

    public void increaseConnection(String ip){
        if(superJoin.containsKey(ip)){
            int times = superJoin.get(ip);
            if(times > configManager.getSuperJoin_amount()){
                antibotManager.blacklist(ip);
                superJoin.remove(ip);
            }else{
                superJoin.put(ip, times + 1);
            }
        }else{
            superJoin.put(ip, 0);
        }
    }
}
