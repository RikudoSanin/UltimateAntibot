package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SuperJoinCheck {
    private final Map<String, Integer> superJoin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public SuperJoinCheck(UltimateAntibotWaterfall plugin){
        this.superJoin = new HashMap<>();
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> superJoin.clear(), 0, configManager.getSuperJoin_time(), TimeUnit.SECONDS);
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
