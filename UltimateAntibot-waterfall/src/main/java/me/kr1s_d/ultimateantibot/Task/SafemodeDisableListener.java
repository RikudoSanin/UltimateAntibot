package me.kr1s_d.ultimateantibot.Task;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class SafemodeDisableListener {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private ScheduledTask schedule;

    public SafemodeDisableListener(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void start(){
       schedule =  ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                antibotManager.setSafeAntiBotMode(false);
            }
        }, plugin.getConfigYml().getLong("safemode.keep"), TimeUnit.SECONDS);
    }
}
