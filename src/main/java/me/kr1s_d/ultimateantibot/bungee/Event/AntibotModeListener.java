package me.kr1s_d.ultimateantibot.bungee.Event;

import me.kr1s_d.ultimateantibot.bungee.Event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class AntibotModeListener implements Listener {
    private final Configuration config;

    public AntibotModeListener(UltimateAntibotWaterfall plugin){
        this.config = plugin.getConfigYml();
    }

    @EventHandler
    public void onAntibotModeEnable(ModeEnableEvent e){
        /**
         * Slow-mode Cecks
         */
        if(e.getAntibotManager().isOnline()){
            if(config.getBoolean("checks.slowmode.disconnect")) {
                e.disconnectBots();
            }
        }
    }
}
