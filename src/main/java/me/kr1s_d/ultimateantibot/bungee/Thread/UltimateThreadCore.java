package me.kr1s_d.ultimateantibot.bungee.Thread;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.Task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.Utils.utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.concurrent.TimeUnit;

public class UltimateThreadCore {
    private final UltimateAntibotWaterfall plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;
    private int count;

    public UltimateThreadCore(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.count = 0;
    }

    public void enable(){
        utils.debug(utils.prefix() + "&aLoading Core...");
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            if(antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline() || antibotManager.isPingModeOnline()) {
                utils.debug(utils.prefix() + plugin.getMessageYml().getString("console.on_attack")
                        .replace("$1", String.valueOf(counter.getBotSecond()))
                        .replace("$2", String.valueOf(counter.getPingSecond()))
                        .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                        .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                        .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                        .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                );
            }
            counter.setBotSecond(0L);
            counter.setPingSecond(0L);
            counter.setJoinPerSecond(0L);
            counter.setCheck(0L);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void hearthBeatMaximal() {
        utils.debug(utils.prefix() + "&aLoading BeatMaximal..");
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            counter.getAnalyzer().clear();
            counter.getPingAnalyZer().clear();
        }, 0, plugin.getConfigYml().getLong("taskmanager.analyzer"), TimeUnit.SECONDS);


        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            if(!antibotManager.isOnline()) {
                if (!antibotManager.isSafeAntiBotModeOnline()) {
                        count = count + 1;
                        if (count > 3 && !antibotManager.isOnline() || !antibotManager.isSafeAntiBotModeOnline()) {
                            antibotManager.getBlacklist().clear();
                            antibotManager.getQueue().clear();
                            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                                if (antibotManager.getWhitelist().contains(p.getAddress().getAddress().toString())) {
                                    return;
                                }
                                new AutoWhitelistTask(plugin, p).start();
                            }
                        } else {
                            count = 0;
                        }
                }
            }

        },  0, plugin.getConfigYml().getLong("taskmanager.clearcache"), TimeUnit.MINUTES);
        utils.debug(utils.prefix() + "&aBeatMaximal Loaded!");
    }

    public void hearthBeatExaminal(){
        utils.debug(utils.prefix() + "&aLoading BeatExaminal...");
        plugin.getUpdater().check();
        plugin.getUpdater().checkNotification();
        utils.debug(utils.prefix() + "&aBeatExaminal loaded...");
    }
    public void heartBeatMinimal(){
        //
    }
}
