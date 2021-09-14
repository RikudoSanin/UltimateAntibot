package me.kr1s_d.ultimateantibot.spigot.Checks;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NameChangerCheck {
    private final UltimateAntibotSpigot plugin;
    private final Map<String, Set<String>> databaseName;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public NameChangerCheck(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.databaseName = new HashMap<>();
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
        loadTask();
    }

    public void detect(String ip, String name){
        Set<String> names = databaseName.getOrDefault(ip, new HashSet<>());
        if(names.size() >= configManager.getAccount_limit()){
            antibotManager.addBlackList(ip);
        }else {
            names.add(name);
            databaseName.put(ip, names);
        }
    }

    public void loadTask(){
        new BukkitRunnable() {
            @Override
            public void run() {
                databaseName.clear();
            }
        }.runTaskTimer(plugin, 0, configManager.getTaskManager_account() * 20L);
    }
}
