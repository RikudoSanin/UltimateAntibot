package me.kr1s_d.ultimateantibot.Thread;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import me.kr1s_d.ultimateantibot.Utils.Metrics;
import me.kr1s_d.ultimateantibot.Utils.utils;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class UltimateThreadCore {
    private final UltimateAntibotWaterfall plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;

    public UltimateThreadCore(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
    }

    public void enable(){
        utils.debug(utils.prefix() + "&aLoading Core...");
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                counter.setBotSecond(0L);
                counter.setPingSecond(0L);
                counter.setJoinPerSecond(0L);
            }
        }, 0, 1, TimeUnit.SECONDS);
        utils.debug(utils.colora(utils.prefix() + "&aCore loaded..."));
    }

    public void hearthBeatMaximal(){
        utils.debug(utils.prefix() + "&aLoading BeatMaximal..");
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
             counter.getAnalyzer().clear();
            }
        },  0, plugin.getConfigYml().getLong("taskmanager.analyzer"), TimeUnit.SECONDS);
        utils.debug(utils.prefix() + "&aBeatMaximal Loaded!");
    }

    public void hearthBeatExaminal(){
        utils.debug(utils.prefix() + "&aLoading BeatExaminal...");
        //
        utils.debug(utils.prefix() + "&aBeatExaminal loaded...");
    }
    public void heartBeatMinimal(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getMetrics().addCustomChart(new Metrics.SingleLineChart("blacklisted_ip", new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return antibotManager.getBlacklist().size();
                    }
                }));

                plugin.getMetrics().addCustomChart(new Metrics.SingleLineChart("whitelist_ip", new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return antibotManager.getWhitelist().size();
                    }
                }));

                plugin.getMetrics().addCustomChart(new Metrics.SingleLineChart("totalbot_blocked", new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return Integer.parseInt(String.valueOf(counter.getTotalBot()));
                    }
                }));
            }
        },  0, 5, TimeUnit.MINUTES);
    }
}
