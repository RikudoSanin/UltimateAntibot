package me.kr1s_d.ultimateantibot.spigot;


import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.Event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.spigot.Task.AntibotModeDisable;
import me.kr1s_d.ultimateantibot.spigot.Task.PingModeDisabler;
import me.kr1s_d.ultimateantibot.spigot.Task.SafemodeDisableListener;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AntibotManager {
    private final UltimateAntibotSpigot plugin;
    private boolean antibotModeStatus;
    private boolean safeAntiBotMode;
    private boolean pingMode;
    private final List<String> queue;
    private final List<String> whitelist;
    private final List<String> blacklist;
    private ModeType modeType;

    public AntibotManager(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotModeStatus = false;
        this.safeAntiBotMode = false;
        this.queue = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.blacklist = new ArrayList<>();
        this.pingMode = false;
        this.modeType = ModeType.OFFLINE;
    }

    public void setPingMode(boolean pingMode) {
        this.pingMode = pingMode;
    }

    public boolean isPingModeOnline() {
        return pingMode;
    }

    public void setModeType(ModeType type){
        this.modeType = type;
    }

    public ModeType getModeType() {
        return modeType;
    }

    public void setAntibotModeStatus(boolean antibotModeStatus) {
        this.antibotModeStatus = antibotModeStatus;
    }

    public void setSafeAntiBotMode(boolean safeAntiBotMode) {
        this.safeAntiBotMode = safeAntiBotMode;
    }

    public boolean isSafeAntiBotModeOnline() {
        return safeAntiBotMode;
    }

    public boolean isOnline(){
        return antibotModeStatus;
    }

    public void addQueue(String ip){
        if(!queue.contains(ip)){
            queue.add(ip);
        }
    }

    public void removeQueue(String ip){
        queue.remove(ip);
    }

    public void addWhitelist(String ip){
        if(!whitelist.contains(ip)){
            whitelist.add(ip);
        }
    }

    public void removeWhitelist(String ip){
        whitelist.remove(ip);
    }

    public void addBlackList(String ip){
        if(!blacklist.contains(ip)) {
            blacklist.add(ip);
        }
    }

    public void removeBlackList(String ip){
        blacklist.remove(ip);
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public List<String> getQueue() {
        return queue;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void enableAntibotMode() {
        setSafeAntiBotMode(false);
        setAntibotModeStatus(true);
        setPingMode(false);
        setModeType(ModeType.ANTIBOTMODE);
        new AntibotModeDisable(plugin).disable();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.ANTIBOTMODE));
            }
        }.runTaskLater(plugin, 2L);
    }

    public void enableSafeMode(){
        setAntibotModeStatus(false);
        setSafeAntiBotMode(true);
        setPingMode(false);
        setModeType(ModeType.SAFEMODE);
        new SafemodeDisableListener(plugin).start();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.SAFEMODE));
            }
        }.runTaskLater(plugin, 2L);
    }

    public void enablePingMode(){
        setPingMode(true);
        setModeType(ModeType.PING);
        new PingModeDisabler(plugin).clear();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.PING));
            }
        }.runTaskLater(plugin, 2L);
    }

    public boolean isSomeModeOnline(){
        return safeAntiBotMode || pingMode || antibotModeStatus;
    }
}
