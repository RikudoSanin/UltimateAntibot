package me.kr1s_d.ultimateantibot.bungee.event;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.checks.DisconnectCheck;
import me.kr1s_d.ultimateantibot.bungee.checks.NameChangerCheck;
import me.kr1s_d.ultimateantibot.bungee.checks.SlowJoinCheck;
import me.kr1s_d.ultimateantibot.bungee.checks.AuthCheck;
import me.kr1s_d.ultimateantibot.bungee.task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.bungee.task.TempJoin;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.service.QueueService;
import me.kr1s_d.ultimateantibot.bungee.user.UserInfo;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MainEventListener implements Listener {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final Configuration messages;
    private final AuthCheck authCheck;
    private final QueueService queueService;
    private final SlowJoinCheck slowJoinCheck;
    private final UserInfo userInfo;
    private final NameChangerCheck nameChangerDetection;
    private final ConfigManager configManager;

    public MainEventListener(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.messages = plugin.getMessageYml();
        this.authCheck = new AuthCheck(plugin);
        this.queueService = plugin.getQueueService();
        this.slowJoinCheck = plugin.getSlowJoinCheck();
        this.userInfo = plugin.getUserInfo();
        this.nameChangerDetection = new NameChangerCheck(plugin);
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
        if(antibotManager.isOnline()){
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
        if(antibotManager.isOnline()) {
            nameChangerDetection.detect(ip, e.getConnection().getName());
        }

        /**
         * antibotmode enable
         */
        if(counter.getJoinPerSecond() > configManager.getAntiBotMode_trigger()){
            if(!antibotManager.isOnline()) {
                antibotManager.enableAntibotMode();
            }
        }

        /**
         * Auth Check
         */

        if(percentualeBlacklistata >= configManager.getAuth_enableCheckPercent() && antibotManager.isOnline() && configManager.isAuth_isEnabled()){
            authCheck.checkForJoin(e, ip);
            return;
        }

        /**
         * antibotmode checks
         */

        if(antibotManager.isOnline()){
            if(antibotManager.getWhitelist().contains(ip)){
                e.setCancelled(false);
            }else {
                e.setCancelReason(new TextComponent(convertToString(Utils.coloralista(Utils.coloraListaConReplaceDueVolte(messages.getStringList("antibotmode"), "$1", String.valueOf(configManager.getAuth_enableCheckPercent()), "$2", String.valueOf(percentualeBlacklistata))))));
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
    public void onPing(ProxyPingEvent e){
        String ip =  e.getConnection().getAddress().getAddress().toString();
        authCheck.onPing(e, ip);
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
        if(!antibotManager.isOnline()){
            return plugin.getMessageYml().getString("stuff.less") + " " + timemax + "m";
        }
        if(antibotManager.isOnline()){
            return plugin.getMessageYml().getString("stuff.plus") + " " + timemax + "m";
        }
        return timemax + "m";
    }

}
