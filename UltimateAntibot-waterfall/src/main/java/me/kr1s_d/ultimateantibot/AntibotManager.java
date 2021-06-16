package me.kr1s_d.ultimateantibot;

import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.ArrayList;
import java.util.List;

public class AntibotManager {

    private boolean antibotModeStatus;
    private boolean slowantibotmode;
    private final List<String> queue;
    private final List<String> whitelist;
    private final List<String> blacklist;

    public AntibotManager(UltimateAntibotWaterfall plugin){
        this.antibotModeStatus = false;
        this.slowantibotmode = false;
        this.queue = new ArrayList<>();
        this.whitelist = new ArrayList<>();
        this.blacklist = new ArrayList<>();
    }

    public void setAntibotModeStatus(boolean antibotModeStatus) {
        this.antibotModeStatus = antibotModeStatus;
    }

    public void setSlowantibotmodeStatus(boolean slowantibotmode) {
        this.slowantibotmode = slowantibotmode;
    }

    public boolean isSlowantibotmodeOnline() {
        return slowantibotmode;
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
}