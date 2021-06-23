package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
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
                antibotManager.setModeType(ModeType.OFFLINE);
            }
        }, plugin.getConfigYml().getLong("safemode.keep"), TimeUnit.SECONDS);
    }
}