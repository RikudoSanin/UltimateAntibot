package me.kr1s_d.ultimateantibot.Event;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.Database.Config;
import me.kr1s_d.ultimateantibot.Task.AntibotModeDisable;
import me.kr1s_d.ultimateantibot.Task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.Task.QueueClearTask;
import me.kr1s_d.ultimateantibot.Task.TempJoin;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import me.kr1s_d.ultimateantibot.Utils.utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class PreloginEventListener implements Listener {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final Configuration messages;

    public PreloginEventListener (UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.messages = plugin.getMessageYml();
    }

    @EventHandler
    public void onPreloginEvent(PreLoginEvent e){
        String ip =  e.getConnection().getAddress().getAddress().toString();
        counter.addJoinSecond(1);
        if(!antibotManager.isOnline()) {
            if(!counter.isFirstJoin(ip)) {
                if(!antibotManager.getWhitelist().contains(ip)) {
                    counter.addFirstJoin(ip);
                    e.setCancelReason(new TextComponent((BaseComponent) utils.coloralista(messages.getStringList("first_join"))));
                    e.setCancelled(true);
                }
            }
        }
        if(!antibotManager.getQueue().contains(ip)){
            if(!antibotManager.getWhitelist().contains(ip)) {
                if(!antibotManager.getBlacklist().contains(ip)){
                    antibotManager.addQueue(ip);
                    new QueueClearTask(plugin, ip).clear();
                }
            }
        }
        if(antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)){
            antibotManager.removeQueue(ip);
        }
        if(counter.getJoinPerSecond() > plugin.getConfigYml().getLong("antibotmode.trigger")){
            if(!antibotManager.isOnline()) {
                antibotManager.setAntibotModeStatus(true);
                new AntibotModeDisable(plugin).disable();
            }
        }
        if(antibotManager.isOnline()){
            counter.addTotalBot(1);
            counter.addBotSecond(1);
            counter.analyzeIP(ip);
            if(counter.getJoinPerSecond() > 1000){
                counter.analyzeHard(ip, 2);
            }
            if(counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.join_max_every_analyzer")){
                antibotManager.addBlackList(ip);
                antibotManager.removeWhitelist(ip);
            }
            if(antibotManager.getWhitelist().contains(ip)){
                e.setCancelled(false);
            }else{
                e.setCancelReason(new TextComponent((BaseComponent) utils.coloralista(messages.getStringList("antibotmode"))));
                e.setCancelled(true);
            }
        }
        if(antibotManager.getBlacklist().contains(ip)){
            e.setCancelReason(new TextComponent((BaseComponent) utils.coloralista(messages.getStringList("blacklisted"))));
            e.setCancelled(true);
        }
        if(antibotManager.isOnline()){
            disconnectBots();
        }
    }

    @EventHandler
    public void onLoginEvent(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        if(!antibotManager.getWhitelist().contains(p.getAddress().getAddress().toString())) {
            new AutoWhitelistTask(plugin, p).start();
        }
        if(!antibotManager.getWhitelist().contains(p.getAddress().getAddress().toString())){
            counter.addJoined(p);
            new TempJoin(plugin, p).clear();
        }
    }

    public void disconnectBots(){
        for(ProxiedPlayer p : counter.getJoined()){
            p.disconnect(new TextComponent((BaseComponent) utils.coloralista(messages.getStringList("safe_mode"))));
        }
        counter.getFirstjoin().clear();
    }

    public void disconnect(ProxiedPlayer p){
        p.disconnect((BaseComponent) new TextComponent(utils.colora("&cYou are a bot")));
    }

}
