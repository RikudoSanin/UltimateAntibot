package me.kr1s_d.ultimateantibot.Event;

import me.kr1s_d.ultimateantibot.AntibotManager;
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
        if(antibotManager.getBlacklist().contains(ip)){
            e.setResponse(null);
        }
        counter.addPingSecond(1);
        counter.addTotalPing(1);
        if(antibotManager.isSafeAntiBotModeOnline()){
            counter.safeModeAnalyze(ip);
        }
    }

}
