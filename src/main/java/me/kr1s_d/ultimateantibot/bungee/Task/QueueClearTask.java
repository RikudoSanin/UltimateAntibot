package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class QueueClearTask {
    private final UltimateAntibotWaterfall plugin;
    private final String ip;
    private final AntibotManager antibotManager;
    private final Counter counter;

    public QueueClearTask(UltimateAntibotWaterfall plugin, String ip){
        this.plugin = plugin;
        this.ip = ip;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
    }

    public void clear(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(antibotManager.getQueue().contains(ip)){
                    if(antibotManager.getBlacklist().contains(ip) || antibotManager.getWhitelist().contains(ip)){
                        antibotManager.removeQueue(ip);
                    }
                    antibotManager.removeQueue(ip);
                    if(antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline()){
                        counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.queue"));
                    }

                }
            }
        }, plugin.getConfigYml().getLong("taskmanager.queue"), TimeUnit.MINUTES);
    }
}
