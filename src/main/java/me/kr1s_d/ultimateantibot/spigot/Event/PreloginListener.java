package me.kr1s_d.ultimateantibot.spigot.Event;

import me.kr1s_d.ultimateantibot.spigot.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.Checks.*;
import me.kr1s_d.ultimateantibot.spigot.service.QueueService;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.Task.*;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import me.kr1s_d.ultimateantibot.spigot.user.UserInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PreloginListener implements Listener {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final Config messages;
    private final TimerCheck timerCheck;
    private final TimerAnalyzer timerAnalyzer;
    private final QueueService queueService;
    private final SlowJoinCheck slowJoinCheck;
    private final UserInfo userInfo;
    private NameChangerCheck nameChangerCheck;
    private final AntibotInfo antibotInfo;
    private final ConfigManager configManager;

    public PreloginListener (UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.messages = plugin.getMessageYml();
        this.timerCheck = new TimerCheck(plugin);
        this.timerAnalyzer = new TimerAnalyzer(plugin);
        this.queueService = plugin.getQueueService();
        this.slowJoinCheck = plugin.getSlowJoinCheck();
        this.userInfo = plugin.getUserInfo();
        this.nameChangerCheck =  new NameChangerCheck(plugin);
        this.antibotInfo = plugin.getAntibotInfo();
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreloginEvent(AsyncPlayerPreLoginEvent e){
        String ip =  e.getAddress().getHostAddress();
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
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, convertToString(Utils.coloralista(Utils.coloraListaConReplaceUnaVolta(messages.getStringList("blacklisted"), "$1", blacklisttime()))));
            antibotManager.removeWhitelist(ip);
            return;
        }

        if(antibotManager.getWhitelist().contains(ip)){
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
         * NameChangerCheck
         */

        nameChangerCheck.detect(ip, e.getName());

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
        if(antibotInfo.getCheckSecond() < configManager.getSafeMode_modifier() && counter.getJoinPerSecond() > configManager.getAntiBotMode_trigger()){
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
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
            }else {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, convertToString(Utils.coloralista(Utils.coloraListaConReplaceDueVolte(messages.getStringList("antibotmode"), "$1", String.valueOf(plugin.getConfigYml().getInt("safemode.percent")), "$2", String.valueOf(percentualeBlacklistata)))));
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
                if(timerAnalyzer.getAnalyzeStatus(ip) >=configManager.getTimer_repeat()){
                    antibotManager.addWhitelist(ip);
                    new TimedWhitelist(plugin).check(ip);
                }
            }
            if(antibotManager.getWhitelist().contains(ip)){
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
            }else{
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, convertToString(Utils.coloralista(Utils.coloraListaConReplaceDueVolte(messages.getStringList("safe_mode"), "$1", String.valueOf(checktime), "$2", String.valueOf(String.valueOf(Math.round(plugin.getConfigYml().getInt("checks.timer.repeat") - timerAnalyzer.getAnalyzeStatus(ip))))))));
            }
        }
    }

    @EventHandler
    public void onLoginEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String  ip = Utils.getIP(p);
        slowJoinCheck.maxAccountCheck(ip, e.getPlayer());
        if(!antibotManager.getWhitelist().contains(ip)) {
            new AutoWhitelistTask(plugin, p).start();
            new DisconnectCheck(plugin).check(p);
            counter.addJoined(p);
            new TempJoin(plugin, p).clear();
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        String ip = Utils.getIP(p);
        slowJoinCheck.removeFromOnline(ip, p);
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
