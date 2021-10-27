package me.kr1s_d.ultimateantibot.bungee.event;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.checks.SuperPingCheck;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {

    private final Counter counter;
    private final AntibotManager antibotManager;
    private final SuperPingCheck superPingCheck;
    private final ConfigManager configManager;

    public PingListener(UltimateAntibotWaterfall plugin){
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.superPingCheck = new SuperPingCheck(plugin);
        this.configManager = plugin.getConfigManager();
    }


    @EventHandler(priority = -128)
    public void onPing(ProxyPingEvent e) {
        String ip = e.getConnection().getAddress().getAddress().toString();
        counter.addPingSecond(1);
        counter.addTotalPing(1);
        if (!antibotManager.getBlacklist().contains(ip)) {
            counter.addChecks(1);
        }
        /**
         * Check
         */
        if(antibotManager.isPingModeOnline()) {
            superPingCheck.check(ip);
            if(!configManager.isPingMode_sendInfo()){
                ServerPing ping = e.getResponse();
                ping.setFavicon("");
                e.setResponse(ping);
            }
        }
        /**
         * pinmode enable
         */
        if (counter.getPingSecond() > configManager.getPingMode_trigger() && !antibotManager.isOnline()) {
            if (!antibotManager.isPingModeOnline()) {
                antibotManager.enablePingMode();
            }
        }
        /**
         * whitelist protection
         */
        if (antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)) {
            antibotManager.removeQueue(ip);
        }
    }

}
