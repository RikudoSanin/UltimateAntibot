package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QueueClearTask {
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final List<String> ipListToClear;

    public QueueClearTask(UltimateAntibotWaterfall plugin, List<String> ipList){
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.ipListToClear = new ArrayList<>(ipList);
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                for (String ip : ipListToClear) {
                    if (antibotManager.getQueue().contains(ip)) {
                        antibotManager.removeQueue(ip);
                        if (antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline()) {
                            counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.queue"));
                        }
                    }
                }
            }
        }, plugin.getConfigYml().getLong("taskmanager.queue"), TimeUnit.MINUTES);

    }

}
