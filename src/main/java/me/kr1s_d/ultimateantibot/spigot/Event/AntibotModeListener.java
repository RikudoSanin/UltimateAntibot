package me.kr1s_d.ultimateantibot.spigot.Event;


import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.Event.custom.ModeEnableEvent;
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
         * Slow-mode Cecks
         */
        if(e.getAntibotManager().isOnline() || e.getAntibotManager().isSafeAntiBotModeOnline()){
            if(config.getBoolean("checks.slowmode.disconnect")) {
                e.disconnectBots();
            }
        }
    }
}
