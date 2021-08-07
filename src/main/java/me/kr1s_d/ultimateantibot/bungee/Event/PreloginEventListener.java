package me.kr1s_d.ultimateantibot.bungee.Event;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.Checks.SlowJoinCheck;
import me.kr1s_d.ultimateantibot.bungee.service.QueueService;
import me.kr1s_d.ultimateantibot.bungee.user.UserInfo;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.Checks.DisconnectCheck;
import me.kr1s_d.ultimateantibot.bungee.Checks.TimerCheck;
import me.kr1s_d.ultimateantibot.bungee.Checks.TimerAnalyzer;
import me.kr1s_d.ultimateantibot.bungee.Task.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PreloginEventListener implements Listener {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final Configuration messages;
    private final TimerCheck timerCheck;
    private final TimerAnalyzer timerAnalyzer;
    private final Configuration config;
    private final QueueService queueService;
    private final SlowJoinCheck slowJoinCheck;
    private final UserInfo userInfo;

    public PreloginEventListener (UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.messages = plugin.getMessageYml();
        this.timerCheck = new TimerCheck(plugin);
        this.timerAnalyzer = new TimerAnalyzer(plugin);
        this.config = plugin.getConfigYml();
        this.queueService = plugin.getQueueService();
        this.slowJoinCheck = plugin.getSlowJoinCheck();
        this.userInfo = plugin.getUserInfo();
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

        if(!antibotManager.getBlacklist().contains(ip)) {
            counter.addChecks(1);
            if(antibotManager.isSafeAntiBotModeOnline()){
                counter.analyzeHard(ip, plugin.getConfigYml().getInt("blacklist.check"));
            }
        }

        if(antibotManager.isOnline() || antibotManager.isSafeAntiBotModeOnline()){
            counter.addTotalBot(1);
            counter.addBotSecond(1);
        }

        /**
         * whitelist bypass
         */
        if(antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)){
            antibotManager.removeQueue(ip);
        }

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
         * Queue
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
         * antibotmode enable
         */
        if(counter.getJoinPerSecond() > plugin.getConfigYml().getLong("antibotmode.trigger") && percentualeBlacklistata < plugin.getConfigYml().getLong("safemode.percent")){
            if(!antibotManager.isOnline()) {
                antibotManager.enableAntibotMode();
            }
        }

        /**
         * Enable safemode
         */
        if(counter.getCheckPerSecond() < plugin.getConfigYml().getLong("safemode.modifier") && counter.getJoinPerSecond() > plugin.getConfigYml().getLong("antibotmode.trigger")){
            if(percentualeBlacklistata >= plugin.getConfigYml().getLong("safemode.percent")) {
                if(!antibotManager.isSafeAntiBotModeOnline()) {
                    antibotManager.enableSafeMode();
                }
            }
        }

        /**
         * safemode checks
         */

        if(antibotManager.isSafeAntiBotModeOnline()){
            counter.analyzeIP(ip);
            if(counter.getAnalyzeStatus(ip) > plugin.getConfigYml().getLong("blacklist.max")){
                antibotManager.addBlackList(ip);
                antibotManager.removeWhitelist(ip);
            }
            int checktime = ThreadLocalRandom.current().nextInt(plugin.getConfigYml().getInt("checks.timer.min"), plugin.getConfigYml().getInt("checks.timer.max"));
            if(!timerCheck.isPending(ip)) {
                timerCheck.startCountDown(ip, checktime);
            }
            if(!timerCheck.isWaitingResponse(ip)){
                counter.analyzeHard(ip, plugin.getConfigYml().getInt("checks.timer.increaser"));
                timerAnalyzer.reset(ip);
            }else{
                timerAnalyzer.analyzeHard(ip, 1);
                if(timerAnalyzer.getAnalyzeStatus(ip) >= plugin.getConfigYml().getInt("checks.timer.repeat")){
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

        /**
         * antibotmode checks
         */

        if(antibotManager.isOnline()){
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
                e.setCancelReason(new TextComponent(convertToString(Utils.coloralista(Utils.coloraListaConReplaceDueVolte(messages.getStringList("antibotmode"), "$1", String.valueOf(plugin.getConfigYml().getInt("safemode.percent")), "$2", String.valueOf(percentualeBlacklistata))))));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoginEvent(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        String ip = Utils.getIP(p);
        slowJoinCheck.maxAccountCheck(ip);
        if(!antibotManager.getWhitelist().contains(ip)) {
            new AutoWhitelistTask(plugin, p).start();
            new DisconnectCheck(plugin).checkDisconnect(p);
            counter.addJoined(p);
            new TempJoin(plugin, p).clear();
        }
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
