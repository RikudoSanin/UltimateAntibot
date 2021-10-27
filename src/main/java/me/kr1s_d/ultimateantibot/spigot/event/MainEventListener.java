package me.kr1s_d.ultimateantibot.spigot.event;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.checks.*;
import me.kr1s_d.ultimateantibot.spigot.service.QueueService;
import me.kr1s_d.ultimateantibot.spigot.task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.spigot.task.TempJoin;
import me.kr1s_d.ultimateantibot.spigot.user.UserInfo;
import me.kr1s_d.ultimateantibot.spigot.utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class MainEventListener implements Listener {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final AuthCheck authCheck;
    private final QueueService queueService;
    private final SlowJoinCheck slowJoinCheck;
    private final SuperJoinCheck superJoinCheck;
    private final UserInfo userInfo;
    private final NameChangerCheck nameChangerCheck;
    private final ConfigManager configManager;

    public MainEventListener(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.authCheck = new AuthCheck(plugin);
        this.queueService = plugin.getQueueService();
        this.slowJoinCheck = plugin.getSlowJoinCheck();
        this.superJoinCheck = new SuperJoinCheck(plugin);
        this.userInfo = plugin.getUserInfo();
        this.nameChangerCheck =  new NameChangerCheck(plugin);
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreloginEvent(AsyncPlayerPreLoginEvent e) {
        String ip = e.getAddress().getHostAddress();
        int blacklistAmount = antibotManager.getBlacklist().size();
        int queueAmount = antibotManager.getQueue().size();
        int totali = blacklistAmount + queueAmount;
        int percentualeBlacklistata = 0;
        if (blacklistAmount != 0 && totali != 0) {
            percentualeBlacklistata = Math.round((float) blacklistAmount / totali * 100);
        }
        if(!antibotManager.getWhitelist().contains(ip)) {
            counter.addJoinSecond(1);
        }
        if (!antibotManager.getBlacklist().contains(ip) && !antibotManager.getWhitelist().contains(ip)) {
            counter.addChecks(1);
        }
        if (antibotManager.isOnline()) {
            counter.addTotalBot(1);
            counter.addBotSecond(1);
        }

        /**
         * queue clear
         */
        if (antibotManager.getWhitelist().contains(ip) || antibotManager.getBlacklist().contains(ip)) {
            antibotManager.removeQueue(ip);
        }

        /**
         * Check if is Whitelisted Or Blacklisted
         */

        if (antibotManager.getBlacklist().contains(ip)) {
            antibotManager.removeWhitelist(ip);
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Utils.colora(MessageManager.getBlacklisted_msg(blacklisttime())));
            antibotManager.removeWhitelist(ip);
            return;
        }

        if (antibotManager.getWhitelist().contains(ip)) {
            return;
        }

        /**
         * First Join
         */
        if (userInfo.checkFirstJoin(e, ip)) {
            return;
        }
        /**
         * if queue whitelist & blacklist doesn't contains ip
         * add to queue & to queue service
         */
        if (!antibotManager.getQueue().contains(ip)) {
            if (!antibotManager.getWhitelist().contains(ip)) {
                if (!antibotManager.getBlacklist().contains(ip)) {
                    antibotManager.addQueue(ip);
                    queueService.addToQueueService(ip);
                }
            }
        }

        /**
         * NameChangerCheck & SuperjoinCheck
         */
        if (antibotManager.isAntiBotModeOnline()) {
            superJoinCheck.increaseConnection(ip);
            nameChangerCheck.detect(ip, e.getName());
        }

        /**
         * antibotmode enable
         */
        if (counter.getJoinPerSecond() > configManager.getAntiBotMode_trigger()) {
            if (!antibotManager.isOnline()) {
                antibotManager.enableAntibotMode();
            }
        }

        /**
         * Auth Check
         */
        if (percentualeBlacklistata >= configManager.getAuth_enableCheckPercent() && antibotManager.isOnline() && configManager.isAuth_isEnabled()) {
            authCheck.checkForJoin(e, ip);
            return;
        }

        /**
         * antibotmode checks
         */

        if(antibotManager.isOnline()){
            if(antibotManager.getWhitelist().contains(ip)){
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
            }else {
                e.disallow(
                        AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        Utils.colora(MessageManager.getAntiBotModeMsg(String.valueOf(configManager.getAuth_enableCheckPercent()), String.valueOf(percentualeBlacklistata))));
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
    public void onPing(ServerListPingEvent e){
        String ip = e.getAddress().getHostAddress();
        authCheck.onPing(e, ip);
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
        if(!antibotManager.isOnline()){
            return plugin.getMessageYml().getString("stuff.less") + " " + timemax + "m";
        }
        if(antibotManager.isOnline()){
            return plugin.getMessageYml().getString("stuff.plus") + " " + timemax + "m";
        }
        return timemax + "m";
    }

}
