package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class AntibotModeDisable {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private ScheduledTask scheduledTask;

    public AntibotModeDisable(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void disable(){
         scheduledTask = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                antibotManager.setAntibotModeStatus(false);
                plugin.getAntibotManager().setModeType(ModeType.OFFLINE);
            }
        }, plugin.getConfigYml().getLong("antibotmode.keep"), TimeUnit.SECONDS);
    }

    public int getId(){
        return scheduledTask.getId();
    }
}
