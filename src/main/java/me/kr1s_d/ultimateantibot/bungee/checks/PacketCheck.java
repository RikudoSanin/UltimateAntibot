package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PacketCheck {

    private final UltimateAntibotWaterfall plugin;
    private final Set<String> joined;
    private final Set<String> packetReceived;
    private final Set<String> suspected;
    private final ConfigManager configManager;
    private final AntibotManager antibotManager;

    public PacketCheck(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.joined = new HashSet<>();
        this.packetReceived = new HashSet<>();
        this.suspected = new HashSet<>();
        this.configManager = plugin.getConfigManager();
        this.antibotManager = plugin.getAntibotManager();
        loadTask();
    }

    public void onUnLogin(String ip){
        joined.remove(ip);
        packetReceived.remove(ip);
        suspected.remove(ip);
    }

    public void registerJoin(String ip) {
        if (configManager.isSlowJoin_packet_enabled() && !antibotManager.isWhitelisted(ip)) {
            joined.add(ip);
            removeTask(ip);
            checkForAttack();
        }
    }


    public void registerPacket(String ip){
        if(configManager.isSlowJoin_packet_enabled() && joined.contains(ip) & !antibotManager.isWhitelisted(ip)) {
            packetReceived.add(ip);
        }
    }

    public void removeTask(String ip){
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            joined.remove(ip);
            packetReceived.remove(ip);
        }, configManager.getSlowJoin_packet_time(), TimeUnit.SECONDS);
    }

    public void checkForAttack(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            joined.forEach(user -> {
                if(!packetReceived.contains(user)){
                    suspected.add(user);
                }
            });
            if(suspected.size() >= configManager.getSlowJoin_packet_trigger()){
                Utils.disconnectAll(new ArrayList<>(suspected), MessageManager.getSafeModeMsg());
                for(String ip : suspected){
                    if(configManager.isSlowJoin_packet_blacklist()) {
                        antibotManager.blacklist(ip);
                    }
                }
                if(configManager.isSlowJoin_packet_antibotmode()) {
                    antibotManager.enableSlowAntibotMode();
                }
                suspected.clear();
            }
        }, 1, TimeUnit.SECONDS);
    }

    private void loadTask(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(!antibotManager.isAntiBotModeOnline()){
                    return;
                }
                joined.clear();
                packetReceived.clear();
                suspected.clear();
            }
        }, 0, configManager.getTaskManager_packet(), TimeUnit.SECONDS);
    }
}
