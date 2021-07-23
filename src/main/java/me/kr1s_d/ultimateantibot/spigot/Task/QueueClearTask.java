package me.kr1s_d.ultimateantibot.spigot.Task;


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
    private final Counter counter;

    public QueueClearTask(UltimateAntibotSpigot plugin, List<String> ipList){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.ipListToClear = new ArrayList<>(ipList);
    }

    public void clear(){
        new BukkitRunnable() {
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
        }.runTaskLater(plugin, plugin.getConfigYml().getLong("taskmanager.queue") * 20L * 60L);
    }
}
