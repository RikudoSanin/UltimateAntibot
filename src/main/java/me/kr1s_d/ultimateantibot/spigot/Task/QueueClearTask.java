package me.kr1s_d.ultimateantibot.spigot.Task;


import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class QueueClearTask {
    private final UltimateAntibotSpigot plugin;
    private final List<String> ipListToClear;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public QueueClearTask(UltimateAntibotSpigot plugin, List<String> ipList){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.ipListToClear = new ArrayList<>(ipList);
        this.configManager = plugin.getConfigManager();
    }

    public void clear(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String ip : ipListToClear) {
                    if (antibotManager.getQueue().contains(ip)) {
                        antibotManager.removeQueue(ip);
                    }
                }
            }
        }.runTaskLater(plugin, configManager.getTaskManager_queue() * 20L * 60L);
    }
}
