package me.kr1s_d.ultimateantibot.bungee.Thread;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.Task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.concurrent.TimeUnit;

public class UltimateThreadCore {
    private final UltimateAntibotWaterfall plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;
    private int count;
    private final AntibotInfo antibotInfo;

    public UltimateThreadCore(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.count = 0;
        this.antibotInfo = plugin.getAntibotInfo();
    }

    public void enable(){
        Utils.debug(Utils.prefix() + "&aLoading Core...");
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            if(antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline() || antibotManager.isPingModeOnline()) {
                Utils.debug(Utils.prefix() + plugin.getMessageYml().getString("console.on_attack")
                        .replace("$1", String.valueOf(counter.getBotSecond()))
                        .replace("$2", String.valueOf(counter.getPingSecond()))
                        .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                        .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                        .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                        .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                );
            }
            antibotInfo.setBotSecond(counter.getBotSecond());
            antibotInfo.setPingSecond(counter.getPingSecond());
            antibotInfo.setCheckSecond(counter.getCheckPerSecond());
            counter.setBotSecond(0L);
            counter.setPingSecond(0L);
            counter.setJoinPerSecond(0L);
            counter.setCheck(0L);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void hearthBeatMaximal() {
        Utils.debug(Utils.prefix() + "&aLoading BeatMaximal..");
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
                                if (antibotManager.getWhitelist().contains(Utils.getIP(p))) {
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
        Utils.debug(Utils.prefix() + "&aBeatMaximal Loaded!");
    }

    public void hearthBeatExaminal(){
        Utils.debug(Utils.prefix() + "&aLoading BeatExaminal...");
        plugin.getUpdater().check();
        plugin.getUpdater().checkNotification();
        Utils.debug(Utils.prefix() + "&aBeatExaminal loaded...");
    }

    public void heartBeatMinimal(){
        //
    }
}
