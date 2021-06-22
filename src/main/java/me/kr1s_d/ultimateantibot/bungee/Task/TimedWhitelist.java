package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class TimedWhitelist {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;


    public TimedWhitelist(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

        public void check(String ip){
            ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    antibotManager.removeWhitelist(ip);
                    plugin.getCounter().getStuffs().remove(ip);
                }
            }, 500, TimeUnit.MILLISECONDS);
        }
}
