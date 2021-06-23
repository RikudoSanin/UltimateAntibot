package me.kr1s_d.ultimateantibot.bungee.Checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimerCheck {
    private final UltimateAntibotWaterfall plugin;
    private final List<String> pendingChecks;
    private final List<String> completedChecksWaiting;

    public TimerCheck(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.pendingChecks = new ArrayList<>();
        this.completedChecksWaiting = new ArrayList<>();
    }

    public void startCountDown(String ip, int waitTime){
        if(!pendingChecks.contains(ip)){
            pendingChecks.add(ip);
            ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    pendingChecks.remove(ip);
                    completedChecksWaiting.add(ip);
                    betweenCountDown(ip);
                }
            }, waitTime, TimeUnit.SECONDS);
        }
    }

    public boolean isWaitingResponse(String ip){
        return completedChecksWaiting.contains(ip);
    }

    public boolean isPending(String ip){
        return pendingChecks.contains(ip);
    }

    public void betweenCountDown(String ip){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                completedChecksWaiting.remove(ip);
            }
        }, plugin.getConfigYml().getLong("checks.timer.between"), TimeUnit.MILLISECONDS);
    }
}