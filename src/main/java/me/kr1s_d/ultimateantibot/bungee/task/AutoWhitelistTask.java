package me.kr1s_d.ultimateantibot.bungee.task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class AutoWhitelistTask {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final ProxiedPlayer player;
    private final ConfigManager configManager;

    public AutoWhitelistTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.player = player;
        this.configManager = plugin.getConfigManager();
    }

    public void start(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                String ip = Utils.getIP(player);
                if(player.isConnected()){
                    antibotManager.addWhitelist(ip);
                }
                antibotManager.getQueue().remove(ip);
            }
        }, configManager.getPlaytime_whitelist(), TimeUnit.MINUTES);
    }
}
