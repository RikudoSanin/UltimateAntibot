package me.kr1s_d.ultimateantibot.spigot.service;

import me.kr1s_d.ultimateantibot.spigot.task.QueueClearTask;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;

import java.util.ArrayList;
import java.util.List;

public class QueueService {

    private final UltimateAntibotSpigot plugin;
    private final int limit;
    private final List<String> ipList;

    public QueueService(UltimateAntibotSpigot plugin) {
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
