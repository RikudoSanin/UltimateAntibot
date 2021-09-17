package me.kr1s_d.ultimateantibot.bungee.task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class AntibotModeDisable {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private ScheduledTask scheduledTask;
    private final ConfigManager configManager;
    private final AntibotInfo antibotInfo;

    public AntibotModeDisable(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
        this.antibotInfo = plugin.getAntibotInfo();
    }

    public void disable(){
         scheduledTask = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(antibotInfo.getJoinSecond() > configManager.getAntiBotMode_trigger()){
                    disable();
                    antibotManager.setModeType(ModeType.ANTIBOTMODE);
                    return;
                }
                antibotManager.setAntibotModeStatus(false);
                plugin.getAntibotManager().setModeType(ModeType.OFFLINE);
            }
        }, configManager.getAntiBotMode_keep(), TimeUnit.SECONDS);
    }

    public int getId(){
        return scheduledTask.getId();
    }
}
