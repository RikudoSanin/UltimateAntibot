package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class PingModeDisabler {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public PingModeDisabler(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
    }

    public void clear(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            antibotManager.setPingMode(false);
            antibotManager.setModeType(ModeType.OFFLINE);
        }, configManager.getPingMode_keep(), TimeUnit.SECONDS);
    }
}
