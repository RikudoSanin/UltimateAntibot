package me.kr1s_d.ultimateantibot.Task;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class AntibotModeDisable {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;

    public AntibotModeDisable(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void disable(){
        ScheduledTask scheduledTask = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                antibotManager.setAntibotModeStatus(false);
            }
        }, plugin.getConfigYml().getLong("antibotmode.keep"), TimeUnit.SECONDS);
    }
}
