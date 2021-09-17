package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NameChangerCheck {
    private final UltimateAntibotWaterfall plugin;
    private final Map<String, Set<String>> databaseName;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public NameChangerCheck(UltimateAntibotWaterfall plugin){
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
        }else{
            names.add(name);
            databaseName.put(ip, names);
        }
    }

    public void loadTask(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                databaseName.clear();
            }
        },0, configManager.getTaskManager_account() * 20L, TimeUnit.SECONDS);
    }
}
