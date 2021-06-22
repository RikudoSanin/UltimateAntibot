package me.kr1s_d.ultimateantibot.bungee.Event;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.Task.PingModeDisabler;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {

    private final UltimateAntibotWaterfall plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;

    public PingListener(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
    }

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        String ip = e.getConnection().getAddress().getAddress().toString();
        counter.addPingSecond(1);
        counter.addTotalPing(1);
        if (!antibotManager.getBlacklist().contains(ip)) {
            counter.addChecks(1);
        }
        /**
         * punish blacklist
         */

        if (antibotManager.getBlacklist().contains(ip)) {
            if (plugin.getConfigYml().getBoolean("pingmode.send_info")) {
                e.getResponse().setPlayers(null);
                e.getResponse().setVersion(null);
                return;
            }
        }
        counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.ping"));
        /**
         * pinmode enable
         */
        if (counter.getPingSecond() > plugin.getConfigYml().getLong("pingmode.trigger") && !antibotManager.isOnline() && !antibotManager.isSafeAntiBotModeOnline()) {
            if (!antibotManager.isPingModeOnline()) {
                antibotManager.setPingMode(true);
                antibotManager.setModeType(ModeType.PING);
                new PingModeDisabler(plugin).clear();
            }
        }
        if (antibotManager.isPingModeOnline()) {
            counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.ping_mode"));
            if (counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")) {
                antibotManager.addBlackList(ip);
                antibotManager.removeWhitelist(ip);
            }
        }

        /**
         * whitelist protection
         */
        if (antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)) {
            antibotManager.removeQueue(ip);
        }

        /**
         * blacklist + punish
         */
        if (counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")) {
            antibotManager.addBlackList(ip);
            antibotManager.removeWhitelist(ip);
        }
    }

}
