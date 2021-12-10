package me.kr1s_d.ultimateantibot.bungee.event;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.checks.*;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.bungee.service.QueueService;
import me.kr1s_d.ultimateantibot.bungee.task.AutoWhitelistTask;
import me.kr1s_d.ultimateantibot.bungee.task.TempJoin;
import me.kr1s_d.ultimateantibot.bungee.user.UserInfo;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.helper.ComponentBuilder;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MainEventListener implements Listener {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final AuthCheck authCheck;
    private final PacketCheck packetCheck;
    private final RegisterCheck registerCheck;
    private final SuperJoinCheck superJoinCheck;
    private final QueueService queueService;
    private final SlowJoinCheck slowJoinCheck;
    private final UserInfo userInfo;
    private final NameChangerCheck nameChangerDetection;
    private final ConfigManager configManager;

    public MainEventListener(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.authCheck = new AuthCheck(plugin);
        this.packetCheck = new PacketCheck(plugin);
        this.registerCheck = new RegisterCheck(plugin);
        this.superJoinCheck = new SuperJoinCheck(plugin);
        this.queueService = plugin.getQueueService();
        this.slowJoinCheck = plugin.getSlowJoinCheck();
        this.userInfo = plugin.getUserInfo();
        this.nameChangerDetection = new NameChangerCheck(plugin);
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = -128)
    public void onPreloginEvent(PreLoginEvent e){
        String ip =  e.getConnection().getAddress().getAddress().toString();
        int blacklistAmount = antibotManager.getBlackListSize();
        int queueAmount = antibotManager.getQueueSize();
        int totali = blacklistAmount + queueAmount;
        int percentualeBlacklistata = 0;
        if(blacklistAmount != 0 && totali != 0) {
            percentualeBlacklistata = Math.round((float) blacklistAmount / totali * 100);
        }
        if(!antibotManager.isWhitelisted(ip)) {
            counter.addJoinSecond(1);
        }
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
        if(antibotManager.isWhitelisted(ip) || antibotManager.isBlacklisted(ip)){
            antibotManager.removeQueue(ip);
        }
        /**
         * Check if is Whitelisted Or Blacklisted
         */
        if(antibotManager.isBlacklisted(ip)){
            antibotManager.removeWhitelist(ip);
            e.setCancelReason(ComponentBuilder.buildShortComponent(Utils.colora(MessageManager.getBlacklisted_msg(blacklisttime()))));
            e.setCancelled(true);
            return;
        }
        if(antibotManager.isWhitelisted(ip)){
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
        if(!antibotManager.isQueued(ip) && !antibotManager.isWhitelisted(ip) && !antibotManager.isBlacklisted(ip)) {
            antibotManager.addQueue(ip);
            queueService.addToQueueService(ip);
        }

        /**
         * NameChangerDetection check & superjoin connections check
         */
        if(antibotManager.isAntiBotModeOnline()) {
            superJoinCheck.increaseConnection(ip);
            nameChangerDetection.detect(ip, e.getConnection().getName());
        }

        /**
         * antibotmode enable
         */
        if(counter.getJoinPerSecond() > configManager.getAntiBotMode_trigger()){
            if(!antibotManager.isAntiBotModeOnline()) {
                antibotManager.enableAntibotMode();
            }
        }

        /**
         * Auth Check
         */

        if(percentualeBlacklistata >= configManager.getAuth_enableCheckPercent() && antibotManager.isAntiBotModeOnline() && configManager.isAuth_isEnabled()){
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
                e.setCancelReason(ComponentBuilder.buildShortComponent(Utils.colora(MessageManager.getAntiBotModeMsg(String.valueOf(configManager.getAuth_enableCheckPercent()), String.valueOf(percentualeBlacklistata)))));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoginEvent(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        String ip = Utils.getIP(p);
        registerCheck.onLogin(ip);
        slowJoinCheck.maxAccountCheck(ip, p);
        packetCheck.registerJoin(ip);
        if(!antibotManager.isWhitelisted(ip)) {
            new AutoWhitelistTask(plugin, p).start();
            new DisconnectCheck(plugin).checkDisconnect(p);
            counter.addJoined(p);
            new TempJoin(plugin, p).clear();
        }
    }

    @EventHandler
    public void onChat(ChatEvent e){
        String ip = Utils.getIP(e.getSender());
        registerCheck.registerPassword(ip, e.getMessage());
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e){
        String ip = Utils.getIP(e.getPlayer());
        registerCheck.onServerSwitch(ip);
    }

    @EventHandler
    public void onSettings(SettingsChangedEvent e){
        String ip = Utils.getIP(e.getPlayer());
        packetCheck.registerPacket(ip);
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
        packetCheck.onUnLogin(ip);
        registerCheck.onUnLogin(ip);
    }

    private String convertToString(List<String> stringList) {
            return String.join(System.lineSeparator(), stringList );
    }

    private String blacklisttime(){
        int timemax = plugin.getConfigYml().getInt("taskmanager.clearcache") * 3;
        if(!antibotManager.isAntiBotModeOnline()){
            return plugin.getMessageYml().getString("stuff.less") + " " + timemax + "m";
        }
        if(antibotManager.isAntiBotModeOnline()){
            return plugin.getMessageYml().getString("stuff.plus") + " " + timemax + "m";
        }
        return timemax + "m";
    }

}
