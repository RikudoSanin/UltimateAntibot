package me.kr1s_d.ultimateantibot.Event;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.ModeType;
import me.kr1s_d.ultimateantibot.Task.*;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import me.kr1s_d.ultimateantibot.Utils.utils;
import me.kr1s_d.ultimateantibot.checks.SlowDetect;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

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
        int blacklistAmount = antibotManager.getBlacklist().size();
        int queueAmount = antibotManager.getQueue().size();
        int totali = blacklistAmount + queueAmount;
        int percentualeBlacklistata = 0;
        if(blacklistAmount != 0 && totali != 0) {
            percentualeBlacklistata = Math.round((float) blacklistAmount / totali * 100);
        }
        counter.addJoinSecond(1);
        if(!antibotManager.getBlacklist().contains(ip)) {
            counter.addChecks(1);
            if(antibotManager.isSafeAntiBotModeOnline()){
                counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.check"));
            }
        }
        /**
         * First Join
         */
        if(!antibotManager.isOnline()) {
            if(!counter.isFirstJoin(ip)) {
                if(!antibotManager.getWhitelist().contains(ip)) {
                    counter.addFirstJoin(ip);
                    e.setCancelReason(new TextComponent(convertToString(utils.coloralista(messages.getStringList("first_join")))));
                    e.setCancelled(true);
                }
            }
        }
        /**
         * Queue
         */
        if(!antibotManager.getQueue().contains(ip)){
            if(!antibotManager.getWhitelist().contains(ip)) {
                if(!antibotManager.getBlacklist().contains(ip)) {
                    antibotManager.addQueue(ip);
                    new QueueClearTask(plugin, ip).clear();
                }
            }
        }

        /**
         * whitelist bypass
         */
        if(antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)){
            antibotManager.removeQueue(ip);
        }

        /**
         * antibotmode enable
         */
        if(counter.getJoinPerSecond() > plugin.getConfigYml().getLong("antibotmode.trigger") && percentualeBlacklistata < plugin.getConfigYml().getLong("safemode.percent")){
            if(!antibotManager.isOnline()) {
                antibotManager.setSafeAntiBotMode(false);
                antibotManager.setAntibotModeStatus(true);
                antibotManager.setPingMode(false);
                antibotManager.setModeType(ModeType.ANTIBOTMODE);
                new AntibotModeDisable(plugin).disable();
            }
        }

        /**
         * Enable safemode
         */
        if(counter.getCheckPerSecond() < plugin.getConfigYml().getLong("safemode.modifier") && counter.getJoinPerSecond() > plugin.getConfigYml().getLong("antibotmode.trigger")){
            if(percentualeBlacklistata >= plugin.getConfigYml().getLong("safemode.percent")) {
                if(!antibotManager.isSafeAntiBotModeOnline()) {
                    antibotManager.setAntibotModeStatus(false);
                    antibotManager.setSafeAntiBotMode(true);
                    antibotManager.setPingMode(false);
                    antibotManager.setModeType(ModeType.SAFEMODE);
                    new SafemodeDisableListener(plugin).start();
                }
            }
        }

        /**
         * safemode checks
         */

        if(antibotManager.isSafeAntiBotModeOnline()){
            counter.addTotalBot(1);
            counter.addBotSecond(1);
            counter.analyzeIP(ip);
            if(counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")){
                antibotManager.addBlackList(ip);
                antibotManager.removeWhitelist(ip);
            }
            int mancanti = (int) (plugin.getConfigYml().getLong("safemode.refresh") - counter.getSafeModeAnalyzeStatus(ip));
            if(counter.getSafeModeAnalyzeStatus(ip) == plugin.getConfigYml().getLong("safemode.refresh")){
                e.setCancelled(false);
                counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.bypass"));
                return;
            }else{
                if(counter.getSafeModeAnalyzeStatus(ip) != plugin.getConfigYml().getLong("safemode.refresh")){
                    counter.resetSafemodeAnalyzeStatus(ip);
                }
            }
            if(antibotManager.getWhitelist().contains(ip)){
                e.setCancelled(false);
            }else{
                e.setCancelReason(new TextComponent(convertToString(utils.coloralista(utils.coloraListaConReplaceUnaVolta(messages.getStringList("safe_mode"), "$1", String.valueOf(mancanti))))));
                e.setCancelled(true);
            }
        }

        /**
         * antibotmode checks
         */

        if(antibotManager.isOnline()){
            counter.addTotalBot(1);
            counter.addBotSecond(1);
            if(percentualeBlacklistata < 50){
                counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.usain"));
            }
            counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.antibot_mode"));
            if(counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")){
                antibotManager.addBlackList(ip);
                antibotManager.removeWhitelist(ip);
            }
            if(antibotManager.getWhitelist().contains(ip)){
                e.setCancelled(false);
            }else {
                e.setCancelReason(new TextComponent(convertToString(utils.coloralista(utils.coloraListaConReplaceDueVolte(messages.getStringList("antibotmode"), "$1", String.valueOf(plugin.getConfigYml().getInt("safemode.percent")), "$2", String.valueOf(percentualeBlacklistata))))));
                e.setCancelled(true);
            }
        }

        /**
         * blacklist & slow-attack check
         */
        if(antibotManager.getBlacklist().contains(ip)){
            e.setCancelReason(new TextComponent(convertToString(utils.coloraListaConReplaceUnaVolta(messages.getStringList("blacklisted"), "$1", blacklisttime()))));
            e.setCancelled(true);
        }

        /**
         * Slow-mode Cecks
         */
        if(antibotManager.isOnline()){
            disconnectBots();
        }
    }

    @EventHandler
    public void onLoginEvent(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        if(!antibotManager.getWhitelist().contains(p.getAddress().getAddress().toString())) {
            new AutoWhitelistTask(plugin, p).start();
            new SlowDetect(plugin).check(p);
            counter.addJoined(p);
            new TempJoin(plugin, p).clear();
        }
    }

    public void disconnectBots(){
        for(ProxiedPlayer p : counter.getJoined()){
            p.disconnect(new TextComponent(convertToString(utils.coloralista(utils.coloraListaConReplaceUnaVolta(messages.getStringList("safe_mode"), "$1", plugin.getConfigYml().getString("safemode.refresh"))))));
        }
        counter.getFirstjoin().clear();
    }

    public String convertToString(List<String> stringList) {
        return String.join(System.lineSeparator(), stringList );
    }

    private String blacklisttime(){
        int timemax = plugin.getConfigYml().getInt("taskmanager.clearcache") * 3;
        if(antibotManager.isSafeAntiBotModeOnline()){
            return plugin.getMessageYml().getString("stuff.less") + " " + timemax + "m";
        }
        if(antibotManager.isOnline()){
            return plugin.getMessageYml().getString("stuff.plus") + " " + timemax + "m";
        }
        return timemax + "m";
    }

}
