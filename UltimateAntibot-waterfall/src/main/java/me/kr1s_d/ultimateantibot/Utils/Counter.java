package me.kr1s_d.ultimateantibot.Utils;


import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {
    private long botSecond;
    private long pingSecond;
    private long totalPing;
    private long totalBot;
    private long joinPerSecond;
    private Map<String, Integer> analyzer;
    private final List<ProxiedPlayer> joined;
    private final List<String> firstjoin;


    public Counter(){
        this.botSecond = 0L;
        this.pingSecond = 0L;
        this.totalPing = 0L;
        this.totalBot = 0L;
        this.joinPerSecond = 0L;
        this.analyzer = new HashMap<>();
        this.joined = new ArrayList<>();
        this.firstjoin = new ArrayList<>();
    }

    public long getBotSecond() {
        return botSecond;
    }

    public long getPingSecond() {
        return pingSecond;
    }

    public long getTotalBot() {
        return totalBot;
    }

    public long getTotalPing() {
        return totalPing;
    }

    public long getJoinPerSecond() {
        return joinPerSecond;
    }

    public void addJoinSecond(long ad){
        this.joinPerSecond = joinPerSecond + ad;
    }

    public void addBotSecond(long added){
        this.botSecond = botSecond + added;
    }

    public void addPingSecond(long ping){
        this.pingSecond = pingSecond + ping;
    }

    public void  addTotalBot(long bot){
        this.totalBot = totalBot + bot;
    }

    public void addTotalPing(long ping){
        this.totalPing = totalPing + ping;
    }

    public void setBotSecond(long botSecond) {
        this.botSecond = botSecond;
    }

    public void setPingSecond(long pingSecond) {
        this.pingSecond = pingSecond;
    }

    public void setTotalBot(long totalBot) {
        this.totalBot = totalBot;
    }

    public void setTotalPing(long totalPing) {
        this.totalPing = totalPing;
    }

    public void setJoinPerSecond(long joinPerSecond) {
        this.joinPerSecond = joinPerSecond;
    }

    public Map<String, Integer> getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Map<String, Integer> analyzer) {
        this.analyzer = analyzer;
    }

    public void analyzeIP(String ip){
        if(analyzer.containsKey(ip)){
            int count = analyzer.get(ip);
            analyzer.remove(ip);
            analyzer.put(ip, count + 1);
        }else{
            analyzer.put(ip, 1);
        }
    }

    public void analyzeHard(String ip, int increaser){
        if(analyzer.containsKey(ip)){
            int count = analyzer.get(ip);
            analyzer.remove(ip);
            analyzer.put(ip, count + increaser);
        }else{
            analyzer.put(ip, 1);
        }
    }

    public int getAnalyzeStatus(String ip){
        if (analyzer.containsKey(ip)){
           return analyzer.get(ip);
        }
        return 0;
    }

    public boolean isAnalyzed(String ip){
        return analyzer.containsKey(ip);
    }

    public List<ProxiedPlayer> getJoined() {
        return joined;
    }

    public void addJoined(ProxiedPlayer p){
        if(!joined.contains(p)){
            joined.add(p);
        }
    }

    public void removeJoined(ProxiedPlayer p){
        joined.remove(p);
    }

    public List<String> getFirstjoin() {
        return firstjoin;
    }

    public boolean isFirstJoin(String ip){
        if(firstjoin.contains(ip)){
            return true;
        }
        return false;
    }

    public void addFirstJoin(String ip){
        if(!firstjoin.contains(ip)){
            firstjoin.add(ip);
        }
    }
}