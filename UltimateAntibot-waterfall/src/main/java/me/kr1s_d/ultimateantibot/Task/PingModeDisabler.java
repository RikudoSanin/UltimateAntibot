package me.kr1s_d.ultimateantibot.Task;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.ModeType;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class PingModeDisabler {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;

    public PingModeDisabler(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void clear(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                antibotManager.setPingMode(false);
                antibotManager.setModeType(ModeType.OFFLINE);
            }
        }, plugin.getConfigYml().getLong("pingmode.keep"), TimeUnit.SECONDS);
    }
}
