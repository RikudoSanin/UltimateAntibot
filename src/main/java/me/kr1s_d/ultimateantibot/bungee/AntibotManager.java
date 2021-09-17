package me.kr1s_d.ultimateantibot.bungee;


import me.kr1s_d.ultimateantibot.bungee.event.custom.ModeEnableEvent;
import me.kr1s_d.ultimateantibot.bungee.task.AntibotModeDisable;
import me.kr1s_d.ultimateantibot.bungee.task.HandShakeModeDisable;
import me.kr1s_d.ultimateantibot.bungee.task.PingModeDisabler;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.*;

public class AntibotManager {

    private final UltimateAntibotWaterfall plugin;
    private boolean antibotModeStatus;
    private boolean pingMode;
    private boolean handShakeMode;
    private final List<String> queue;
    private final List<String> whitelist;
    private final List<String> blacklist;
    private ModeType modeType;
    private final ConfigManager configManager;

    public AntibotManager(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotModeStatus = false;
        this.queue = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.blacklist = new ArrayList<>();
        this.pingMode = false;
        this.handShakeMode = false;
        this.modeType = ModeType.OFFLINE;
        this.configManager = plugin.getConfigManager();
    }

    public boolean isHandShakeModeOnline() {
        return handShakeMode;
    }

    public void setHandShakeModeStatus(boolean handShakeMode) {
        this.handShakeMode = handShakeMode;
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
            queue.remove(ip);
            whitelist.remove(ip);
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

    public void enableAntibotMode() {
        setAntibotModeStatus(true);
        setPingMode(false);
        setModeType(ModeType.ANTIBOTMODE);
        new AntibotModeDisable(plugin).disable();
        ProxyServer.getInstance().getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.ANTIBOTMODE));
    }

    public void enablePingMode(){
        setPingMode(true);
        setModeType(ModeType.PING);
        new PingModeDisabler(plugin).clear();
        ProxyServer.getInstance().getPluginManager().callEvent(new ModeEnableEvent(plugin, ModeType.PING));
    }

    public void enableHandShakeMode(){
        if(configManager.isHandShakeMode_enabled()) {
            setHandShakeModeStatus(true);
            setModeType(ModeType.HANDSHAKE);
            new HandShakeModeDisable(plugin);
        }
    }

    public boolean isSomeModeOnline(){
        return pingMode || antibotModeStatus || handShakeMode;
    }

}
