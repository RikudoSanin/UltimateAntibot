package me.kr1s_d.ultimateantibot.Event;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.ModeType;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingListener implements Listener {

    private UltimateAntibotWaterfall plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;

    public PingListener(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
    }

    @EventHandler
    public void onPing(ProxyPingEvent e){
        String ip = e.getConnection().getAddress().getAddress().toString();
        counter.addPingSecond(1);
        counter.addTotalPing(1);
        counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.ping"));
        /**
         * pinmode enable
         */
        if(counter.getPingSecond() > plugin.getConfigYml().getLong("pingmode.trigger")){
            if(antibotManager.isPingModeOnline()){
                antibotManager.setPingMode(true);
                antibotManager.setModeType(ModeType.PING);
            }
        }
        /**
         * some safemode checks
         */
        if(antibotManager.isSafeAntiBotModeOnline()){
            counter.safeModeAnalyze(ip);
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
        if(counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")){
            antibotManager.addBlackList(ip);
            antibotManager.removeWhitelist(ip);
        }
        if(antibotManager.getBlacklist().contains(ip)){
            e.setResponse(null);
        }
    }

}
