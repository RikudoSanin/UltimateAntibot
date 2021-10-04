package me.kr1s_d.ultimateantibot.spigot.event;


import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntibotModeListener implements Listener {
    private final Config config;

    public AntibotModeListener(UltimateAntibotSpigot plugin){
        this.config = plugin.getConfigYml();
    }

    @EventHandler
    public void onAntibotModeEnable(ModeEnableEvent e){
        /**
         * Slow-mode Checks
         */
        if(e.getEnabledMode().equals(ModeType.ANTIBOTMODE)){
            if(config.getBoolean("checks.slowmode.disconnect")) {
                e.disconnectBots();
            }
        }
    }
}
