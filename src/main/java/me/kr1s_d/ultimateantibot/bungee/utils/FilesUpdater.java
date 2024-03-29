package me.kr1s_d.ultimateantibot.bungee.utils;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.TimeUnit;

public class FilesUpdater {
    private final UltimateAntibotWaterfall plugin;
    private final Configuration config;
    private final Configuration messages;

    public FilesUpdater(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigYml();
        this.messages = plugin.getMessageYml();
    }
    public void a(String type){
        Utils.debug(Utils.prefix() + "&C-----ALERT----");
        Utils.debug(Utils.prefix() + "&CYour %s.yml is outdated!".replace("%s", type));
        Utils.debug(Utils.prefix()+ "&cRegen it and Restart");
        Utils.debug(Utils.prefix() + "&C-----ALERT----");
    }

    public void check(){
        if(config.getDouble("version") != 3.1){
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> a("config"), 0, 1, TimeUnit.MINUTES);
        }
        if(messages.getDouble("version") != 3.1){
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> a("messages"), 0, 1, TimeUnit.MINUTES);
        }
    }
}
