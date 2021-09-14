package me.kr1s_d.ultimateantibot.bungee.Event;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.Checks.*;
import me.kr1s_d.ultimateantibot.bungee.Task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.bungee.Task.TempJoin;
import me.kr1s_d.ultimateantibot.bungee.Task.TimedWhitelist;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.bungee.service.QueueService;
import me.kr1s_d.ultimateantibot.bungee.user.UserInfo;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PreloginEventListener implements Listener {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final Configuration messages;
    private final TimerCheck timerCheck;
    private final TimerAnalyzer timerAnalyzer;
    private final QueueService queueService;
    private final SlowJoinCheck slowJoinCheck;
    private final UserInfo userInfo;
    private final NameChangerCheck nameChangerDetection;
    private final AntibotInfo antibotInfo;
    private final ConfigManager configManager;

    public PreloginEventListener (UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.messages = plugin.getMessageYml();
        this.timerCheck = new TimerCheck(plugin);
        this.timerAnalyzer = new TimerAnalyzer(plugin);
        this.queueService = plugin.getQueueService();
        this.slowJoinCheck = plugin.getSlowJoinCheck();
        this.userInfo = plugin.getUserInfo();
        this.nameChangerDetection = new NameChangerCheck(plugin);
        this.antibotInfo = plugin.getAntibotInfo();
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = -128)
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
        if(!antibotManager.getBlacklist().contains(ip) && !antibotManager.getWhitelist().contains(ip)) {
            counter.addChecks(1);
        }
        if(antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline()){
            counter.addTotalBot(1);
            counter.addBotSecond(1);
        }
        /**
         * queue clear
         */
        if(antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)){
            antibotManager.removeQueue(ip);
        }
        /**
         * Check if is Whitelisted Or Blacklisted
         */
        if(antibotManager.getBlacklist().contains(ip)){
            e.setCancelReason(new TextComponent(convertToString(Utils.coloralista(Utils.coloraListaConReplaceUnaVolta(messages.getStringList("blacklisted"), "$1", blacklisttime())))));
            e.setCancelled(true);
            return;
        }
        if(antibotManager.getWhitelist().contains(ip)){
            e.setCancelled(false);
            return;
        }

        /**
         * First Join
         */
         if(userInfo.checkFirstJoin(e, ip)){
             return;
         }

        /**
         * if queue whitelist & blacklist doesn't contains ip
         * add to queue & to queue service
         */
        if(!antibotManager.getQueue().contains(ip)){
            if(!antibotManager.getWhitelist().contains(ip)) {
                if(!antibotManager.getBlacklist().contains(ip)) {
                    antibotManager.addQueue(ip);
                    queueService.addToQueueService(ip);
                }
            }
        }

        /**
         * NameChangerDetection check
         */
        nameChangerDetection.detect(ip, e.getConnection().getName());

        /**
         * antibotmode enable
         */
        if(counter.getJoinPerSecond() > configManager.getAntiBotMode_trigger() && percentualeBlacklistata < configManager.getSafeMode_percent()){
            if(!antibotManager.isOnline()) {
                antibotManager.enableAntibotMode();
            }
        }

        /**
         * Enable safemode
         */
        if(antibotInfo.getCheckSecond() < configManager.getSafeMode_modifier() && antibotInfo.getJoinSecond() > configManager.getAntiBotMode_trigger()){
            if(percentualeBlacklistata >= configManager.getSafeMode_percent()) {
                if(!antibotManager.isSafeAntiBotModeOnline()) {
                    antibotManager.enableSafeMode();
                }
            }
        }

        /**
         * antibotmode checks
         */

        if(antibotManager.isOnline()){
            if(antibotManager.getWhitelist().contains(ip)){
                e.setCancelled(false);
            }else {
                e.setCancelReason(new TextComponent(convertToString(Utils.coloralista(Utils.coloraListaConReplaceDueVolte(messages.getStringList("antibotmode"), "$1", String.valueOf(plugin.getConfigYml().getInt("safemode.percent")), "$2", String.valueOf(percentualeBlacklistata))))));
                e.setCancelled(true);
            }
        }

        /**
         * safemode checks
         */

        if(antibotManager.isSafeAntiBotModeOnline()){
            int checktime = ThreadLocalRandom.current().nextInt(configManager.getTimer_min(), configManager.getTimer_max());
            if(!timerCheck.isPending(ip)) {
                timerCheck.startCountDown(ip, checktime);
            }
            if(!timerCheck.isWaitingResponse(ip)){
                timerAnalyzer.reset(ip);
            }else{
                timerAnalyzer.analyzeHard(ip, 1);
                if(timerAnalyzer.getAnalyzeStatus(ip) >= configManager.getTimer_repeat()){
                    antibotManager.addWhitelist(ip);
                    new TimedWhitelist(plugin).check(ip);
                }
            }
            if(antibotManager.getWhitelist().contains(ip)){
                e.setCancelled(false);
            }else{
                e.setCancelReason(new TextComponent(convertToString(Utils.coloralista(Utils.coloraListaConReplaceDueVolte(messages.getStringList("safe_mode"), "$1", String.valueOf(checktime), "$2", String.valueOf(String.valueOf(Math.round(plugin.getConfigYml().getInt("checks.timer.repeat") - timerAnalyzer.getAnalyzeStatus(ip)))))))));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoginEvent(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        String ip = Utils.getIP(p);
        slowJoinCheck.maxAccountCheck(ip, p);
        if(!antibotManager.getWhitelist().contains(ip)) {
            new AutoWhitelistTask(plugin, p).start();
            new DisconnectCheck(plugin).checkDisconnect(p);
            counter.addJoined(p);
            new TempJoin(plugin, p).clear();
        }
    }

    @EventHandler
    public void onUnlogin(PlayerDisconnectEvent e){
        ProxiedPlayer p = e.getPlayer();
        String ip = Utils.getIP(p);
        slowJoinCheck.removeFromOnline(ip, p);
    }

    private String convertToString(List<String> stringList) {
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
