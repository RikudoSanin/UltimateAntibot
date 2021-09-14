package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class SafemodeDisableListener {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public SafemodeDisableListener(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
    }

    public void start(){
        ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                antibotManager.setSafeAntiBotMode(false);
                antibotManager.setModeType(ModeType.OFFLINE);
            }
        }, configManager.getSafeMode_keep(), TimeUnit.SECONDS);
    }
}
