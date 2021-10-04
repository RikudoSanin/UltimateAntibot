package me.kr1s_d.ultimateantibot.spigot;


import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.spigot.task.AntibotModeDisable;
import me.kr1s_d.ultimateantibot.spigot.task.PingModeDisabler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AntibotManager {
    private final UltimateAntibotSpigot plugin;
    private boolean antibotModeStatus;
    private boolean pingMode;
    private final List<String> queue;
    private final List<String> whitelist;
    private final List<String> blacklist;
    private ModeType modeType;

    public AntibotManager(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotModeStatus = false;
        this.queue = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.blacklist = new ArrayList<>();
        this.pingMode = false;
        this.modeType = ModeType.OFFLINE;
    }

    @Deprecated
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


    @Deprecated
    public void setAntibotModeStatus(boolean antibotModeStatus) {
        this.antibotModeStatus = antibotModeStatus;
    }

    @Deprecated
    public boolean isOnline(){
        return antibotModeStatus;
    }

    public boolean isAntiBotModeOnline(){
        return antibotModeStatus;
    }

    @Deprecated
    public void addWhitelist(String ip){
        if(!whitelist.contains(ip)){
            whitelist.add(ip);
        }
    }

    @Deprecated
    public void addBlackList(String ip){
        if(!blacklist.contains(ip)) {
            blacklist.add(ip);
        }
    }

    @Deprecated
    public List<String> getBlacklist() {
        return blacklist;
    }

    @Deprecated
    public List<String> getQueue() {
        return queue;
    }

    @Deprecated
    public List<String> getWhitelist() {
        return whitelist;
    }

    public void enableAntibotMode() {
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
        return pingMode || antibotModeStatus;
    }

    public boolean isWhitelisted(String ip){
        return whitelist.contains(ip);
    }

    public void whitelist(String ip){
        if (!isWhitelisted(ip)) {
            whitelist.add(ip);
        }
    }

    public void removeWhitelist(String ip){
        whitelist.remove(ip);
    }

    public int getWhitelistSize(String ip){
        return whitelist.size();
    }

    public List<String> getWhitelistInstance(){
        return whitelist;
    }

    public boolean isBlacklisted(String ip){
        return blacklist.contains(ip);
    }

    public void blacklist(String ip){
        if(!isBlacklisted(ip)){
            blacklist.add(ip);
        }
    }

    public void removeBlackList(String ip){
        blacklist.remove(ip);
    }

    public int getBlackListSize(){
        return blacklist.size();
    }

    public List<String> getBlacklistInstance(){
        return blacklist;
    }

    public boolean isQueued(String ip){
        return queue.contains(ip);
    }

    public void addQueue(String ip){
        if(!isQueued(ip)){
            queue.add(ip);
        }
    }

    public void removeQueue(String ip){
        queue.remove(ip);
    }

    public int getQueueSize(){
        return queue.size();
    }

    public List<String> getQueueInstance(){
        return queue;
    }
}
