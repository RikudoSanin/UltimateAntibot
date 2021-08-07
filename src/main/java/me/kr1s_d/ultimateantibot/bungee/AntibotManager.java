package me.kr1s_d.ultimateantibot.bungee;


import me.kr1s_d.ultimateantibot.bungee.Event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.bungee.Task.AntibotModeDisable;
import me.kr1s_d.ultimateantibot.bungee.Task.PingModeDisabler;
import me.kr1s_d.ultimateantibot.bungee.Task.SafemodeDisableListener;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import net.md_5.bungee.api.ProxyServer;

import java.util.*;

public class AntibotManager {

    private final UltimateAntibotWaterfall plugin;
    private boolean antibotModeStatus;
    private boolean safeAntiBotMode;
    private boolean pingMode;
    private final List<String> queue;
    private final List<String> whitelist;
    private final List<String> blacklist;
    private ModeType modeType;

    public AntibotManager(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotModeStatus = false;
        this.safeAntiBotMode = false;
        this.queue = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.blacklist = new ArrayList<>();
        this.pingMode = false;
        this.modeType = ModeType.OFFLINE;
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

    public void setPingMode(boolean pingMode) {
        this.pingMode = pingMode;
    }

    public void setAntibotModeStatus(boolean antibotModeStatus) {
        this.antibotModeStatus = antibotModeStatus;
    }

    public void setSafeAntiBotMode(boolean safeAntiBotMode) {
        this.safeAntiBotMode = safeAntiBotMode;
    }

    public void enableAntibotMode() {
        setSafeAntiBotMode(false);
        setAntibotModeStatus(true);
        setPingMode(false);
        setModeType(ModeType.ANTIBOTMODE);
        new AntibotModeDisable(plugin).disable();
        ProxyServer.getInstance().getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.ANTIBOTMODE));
    }

    public void enableSafeMode(){
        setAntibotModeStatus(false);
        setSafeAntiBotMode(true);
        setPingMode(false);
        setModeType(ModeType.SAFEMODE);
        new SafemodeDisableListener(plugin).start();
        ProxyServer.getInstance().getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.SAFEMODE));
    }

    public void enablePingMode(){
        setPingMode(true);
        setModeType(ModeType.PING);
        new PingModeDisabler(plugin).clear();
        ProxyServer.getInstance().getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.PING));
    }
}
