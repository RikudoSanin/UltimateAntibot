package me.kr1s_d.ultimateantibot.bungee.core;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
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
            if(antibotManager.isOnline() || antibotManager.isPingModeOnline()) {
                Utils.debug(Utils.prefix() + MessageManager.getConsole_on_attack()
                        .replace("$1", String.valueOf(counter.getBotSecond()))
                        .replace("$2", String.valueOf(counter.getPingSecond()))
                        .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                        .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                        .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                        .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                );
            }
            if(antibotManager.isHandShakeModeOnline() && !antibotManager.isOnline() && !antibotManager.isPingModeOnline()){
                Utils.debug(Utils.prefix() + MessageManager.getConsole_onHandShake()
                        .replace("$1", String.valueOf(counter.getHandshakeSecond()))
                        .replace("$2", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                        .replace("$3", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                        .replace("$4", String.valueOf(counter.getCheckPerSecond()))
                        .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                );
            }
            antibotInfo.setHandShakeSecond(counter.getHandshakeSecond());
            antibotInfo.setBotSecond(counter.getBotSecond());
            antibotInfo.setPingSecond(counter.getPingSecond());
            antibotInfo.setCheckSecond(counter.getCheckPerSecond());
            antibotInfo.setJoinSecond(counter.getJoinPerSecond());
            counter.setHandshakeSecond(0);
            counter.setBotSecond(0L);
            counter.setPingSecond(0L);
            counter.setJoinPerSecond(0L);
            counter.setCheck(0L);
            counter.setJoinPerSecond(0L);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void hearthBeatMaximal() {
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            if(!antibotManager.isSomeModeOnline()) {
                count = count + 1;
                if (count > 3 && !antibotManager.isOnline()) {
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

        },  0, plugin.getConfigManager().getTaskManager_clearCache(), TimeUnit.MINUTES);
    }

    public void hearthBeatExaminal(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(antibotManager.isAntiBotModeOnline()){
                    return;
                }
                if(plugin.getUpdater().isAvailable()){
                    return;
                }
                plugin.getUpdater().check();
                plugin.getUpdater().checkNotification();
            }
        }, 0, 30, TimeUnit.MINUTES);
    }

    public void heartBeatMinimal(){
        //
    }
}
