package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SuperPingCheck {

    private final AntibotManager antibotManager;
    private final Map<String, Integer> data;
    private final ConfigManager configManager;

    public SuperPingCheck(UltimateAntibotWaterfall plugin){
        this.data = new HashMap<>();
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();

        ProxyServer.getInstance().getScheduler().schedule(plugin, data::clear, configManager.getSuperJoin_time(), TimeUnit.SECONDS);
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
