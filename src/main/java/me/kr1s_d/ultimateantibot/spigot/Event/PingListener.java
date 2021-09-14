package me.kr1s_d.ultimateantibot.spigot.Event;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {
    private final Counter counter;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public PingListener(UltimateAntibotSpigot plugin){
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onPing(ServerListPingEvent e){
        String ip = e.getAddress().getHostAddress();
        counter.addPingSecond(1);
        counter.addTotalPing(1);
        if(!antibotManager.getBlacklist().contains(ip)){
            counter.addChecks(1);
        }
        /**
         * blacklist punish
         */
        if(antibotManager.getBlacklist().contains(ip)){
            if(!configManager.isPingMode_sendInfo()) {
                e.setServerIcon(null);
                return;
            }
        }
        /**
         * pinmode enable
         */
        if(counter.getPingSecond() > configManager.getPingMode_trigger() && !antibotManager.isOnline() && !antibotManager.isSafeAntiBotModeOnline()){
            if(!antibotManager.isPingModeOnline()){
                antibotManager.enablePingMode();
            }
        }

        /**
         * whitelist protection
         */
        if(antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)){
            antibotManager.removeQueue(ip);
        }
    }
}
