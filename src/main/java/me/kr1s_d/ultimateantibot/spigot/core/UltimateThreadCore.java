package me.kr1s_d.ultimateantibot.spigot.core;

import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.Task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import me.kr1s_d.ultimateantibot.spigot.data.AntibotInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UltimateThreadCore {
    private final UltimateAntibotSpigot plugin;
    private final Counter counter;
    private final AntibotManager antibotManager;
    private int count;
    private final Config config;
    private final AntibotInfo antibotInfo;

    public UltimateThreadCore(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.count = 0;
        this.config = plugin.getConfigYml();
        this.antibotInfo = plugin.getAntibotInfo();
    }

    public void enable(){
        Utils.debug(Utils.prefix() + "&aLoading Core...");
        new BukkitRunnable() {
            @Override
            public void run() {
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
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    public void hearthBeatMaximal() {
        Utils.debug(Utils.prefix() + "&aLoading BeatMaximal..");
        new BukkitRunnable() {
            @Override
            public void run() {
                counter.getAnalyzer().clear();
                counter.getPingAnalyZer().clear();
            }
        }.runTaskTimer(plugin, 0,config.getLong("taskmanager.analyzer") * 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!antibotManager.isOnline()) {
                    if (!antibotManager.isSafeAntiBotModeOnline()) {
                        count = count + 1;
                        if (count > 3 && !antibotManager.isOnline() || !antibotManager.isSafeAntiBotModeOnline()) {
                            antibotManager.getBlacklist().clear();
                            antibotManager.getQueue().clear();
                            for (Player p : Bukkit.getOnlinePlayers()) {
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
            }
        }.runTaskTimer(plugin, 0, config.getLong("taskmanager.clearcache") * 20L * 60L);
        Utils.debug(Utils.prefix() + "&aBeatMaximal Loaded!");
    }

    public void hearthBeatExaminal(){
        Utils.debug(Utils.prefix() + "&aLoading BeatExaminal...");
                plugin.getUpdater().check();
                plugin.getUpdater().checkNotification();
        Utils.debug(Utils.prefix() + "&aBeatExaminal loaded...");
    }

    public void heartBeatMinimal(){

    }
}
