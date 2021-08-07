package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class AutoWhitelistTask {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final ProxiedPlayer player;

    public AutoWhitelistTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.player = player;
    }

    public void start(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                String ip = Utils.getIP(player);
                if(!player.isConnected()){
                    if(!antibotManager.isOnline()){
                        plugin.getCounter().analyzeIP(ip);
                    }
                }
                if(player.isConnected()){
                    antibotManager.addWhitelist(ip);
                }
                antibotManager.getQueue().remove(ip);
            }
        }, plugin.getConfigYml().getLong("playtime_for_whitelist"), TimeUnit.MINUTES);
    }
}
