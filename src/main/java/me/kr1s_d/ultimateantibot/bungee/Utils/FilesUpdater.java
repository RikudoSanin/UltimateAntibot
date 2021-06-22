package me.kr1s_d.ultimateantibot.bungee.Utils;

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
        utils.debug(utils.prefix() + "&C-----ALERT----");
        utils.debug(utils.prefix() + "&CYour %s.yml is outdated!".replace("%s", type));
        utils.debug(utils.prefix()+ "&cRegen it and Restart");
        utils.debug(utils.prefix() + "&C-----ALERT----");
    }

    public void check(){
        if(config.getDouble("version") != 2.2){
            ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    a("config");
                }
            }, 0, 1, TimeUnit.MINUTES);
        }
        if(messages.getDouble("version") != 2.2){
            ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    a("messages");
                }
            }, 0, 1, TimeUnit.MINUTES);
        }
    }
}
