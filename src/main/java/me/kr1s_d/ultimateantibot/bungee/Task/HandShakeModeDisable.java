package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class HandShakeModeDisable {

    private final AntibotManager antibotManager;

    public HandShakeModeDisable(UltimateAntibotWaterfall plugin){
        this.antibotManager = plugin.getAntibotManager();
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            antibotManager.setHandShakeModeStatus(false);
            antibotManager.setModeType(ModeType.OFFLINE);
        }, plugin.getConfigYml().getLong("handshakemode.keep"), TimeUnit.SECONDS);
    }
}
