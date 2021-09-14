package me.kr1s_d.ultimateantibot.spigot.Checks;


import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TimerCheck {
    private final UltimateAntibotSpigot plugin;
    private final List<String> pendingChecks;
    private final List<String> completedChecksWaiting;
    private final ConfigManager configManager;

    public TimerCheck(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.pendingChecks = new ArrayList<>();
        this.completedChecksWaiting = new ArrayList<>();
        this.configManager = plugin.getConfigManager();
    }

    public void startCountDown(String ip, int waitTime){
        if(!pendingChecks.contains(ip)) {
            pendingChecks.add(ip);
            new BukkitRunnable() {
                @Override
                public void run() {
                    pendingChecks.remove(ip);
                    completedChecksWaiting.add(ip);
                    betweenCountDown(ip);
                }
            }.runTaskLater(plugin, waitTime * 20L);
        }
    }

    public boolean isWaitingResponse(String ip){
        return completedChecksWaiting.contains(ip);
    }

    public boolean isPending(String ip){
        return pendingChecks.contains(ip);
    }

    public void betweenCountDown(String ip){
        new BukkitRunnable() {
            @Override
            public void run() {
                completedChecksWaiting.remove(ip);
            }
        }.runTaskLater(plugin, Math.round((float)configManager.getTimer_between() * 20L / 1000));
    }
}
