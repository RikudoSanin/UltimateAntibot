package me.kr1s_d.ultimateantibot.spigot.Task;


import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueClearTask {
    private final UltimateAntibotSpigot plugin;
    private final String ip;
    private final AntibotManager antibotManager;
    private final Counter counter;

    public QueueClearTask(UltimateAntibotSpigot plugin, String ip){
        this.plugin = plugin;
        this.ip = ip;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
    }

    public void clear(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (antibotManager.getQueue().contains(ip)) {
                    if (antibotManager.getBlacklist().contains(ip) || antibotManager.getWhitelist().contains(ip)) {
                        antibotManager.removeQueue(ip);
                    }
                    antibotManager.removeQueue(ip);
                    if (antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline()) {
                        counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.queue"));
                    }

                }
            }
        }.runTaskLater(plugin, plugin.getConfigYml().getLong("taskmanager.queue") * 20L * 60L);
    }
}
