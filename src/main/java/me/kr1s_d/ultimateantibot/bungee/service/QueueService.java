package me.kr1s_d.ultimateantibot.bungee.service;

import me.kr1s_d.ultimateantibot.bungee.task.QueueClearTask;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;

import java.util.ArrayList;
import java.util.List;

public class QueueService {

    private final UltimateAntibotWaterfall plugin;
    private final int limit;
    private final List<String> ipList;

    public QueueService(UltimateAntibotWaterfall plugin) {
        this.plugin = plugin;
        this.limit = 100;
        this.ipList = new ArrayList<>();
    }

    public void addToQueueService(String ip){
        if(!ipList.contains(ip)) {
            ipList.add(ip);
        }
        check();
    }

    public void check(){
        if(ipList.size() >= limit){
            new QueueClearTask(plugin, ipList);
            ipList.clear();
        }
    }
}
