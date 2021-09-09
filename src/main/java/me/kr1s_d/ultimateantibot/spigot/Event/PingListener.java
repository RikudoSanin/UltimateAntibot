package me.kr1s_d.ultimateantibot.spigot.Event;

import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Task.PingModeDisabler;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {
    private final UltimateAntibotSpigot plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;

    public PingListener(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
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
            if(plugin.getConfigYml().getBoolean("pingmode.send_info")) {
                e.setMaxPlayers(0);
                e.setServerIcon(null);
                return;
            }
        }
        counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.ping"));
        /**
         * pinmode enable
         */
        if(counter.getPingSecond() > plugin.getConfigYml().getLong("pingmode.trigger") && !antibotManager.isOnline() && !antibotManager.isSafeAntiBotModeOnline()){
            if(!antibotManager.isPingModeOnline()){
                antibotManager.enablePingMode();
            }
        }
        if(antibotManager.isPingModeOnline()) {
            counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.ping_mode"));
            if (counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")) {
                antibotManager.addBlackList(ip);
                antibotManager.removeWhitelist(ip);
            }
        }

        /**
         * whitelist protection
         */
        if(antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)){
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
