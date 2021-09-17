package me.kr1s_d.ultimateantibot.bungee.event;

import me.kr1s_d.ultimateantibot.bungee.event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AntibotModeListener implements Listener {
    private final ConfigManager configManager;

    public AntibotModeListener(UltimateAntibotWaterfall plugin){
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onAntibotModeEnable(ModeEnableEvent e){
        /**
         * Slow-mode Cecks
         */
        if(e.getAntibotManager().isOnline()){
            if(configManager.isSlowMode_disconnect()) {
                e.disconnectBots();
            }
        }
    }
}
