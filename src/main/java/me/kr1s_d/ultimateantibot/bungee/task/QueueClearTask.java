package me.kr1s_d.ultimateantibot.bungee.task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QueueClearTask {
    private final AntibotManager antibotManager;
    private final List<String> ipListToClear;

    public QueueClearTask(UltimateAntibotWaterfall plugin, List<String> ipList){
        this.antibotManager = plugin.getAntibotManager();
        this.ipListToClear = new ArrayList<>(ipList);
        ConfigManager configManager = plugin.getConfigManager();
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            for (String ip : ipListToClear) {
                if (antibotManager.getQueue().contains(ip)) {
                    antibotManager.removeQueue(ip);
                }
            }
        }, configManager.getTaskManager_queue(), TimeUnit.MINUTES);

    }

}
