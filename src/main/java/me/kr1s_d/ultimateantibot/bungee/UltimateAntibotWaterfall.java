package me.kr1s_d.ultimateantibot.bungee;

import me.kr1s_d.ultimateantibot.bungee.checks.HandShakeCheck;
import me.kr1s_d.ultimateantibot.bungee.checks.SlowJoinCheck;
import me.kr1s_d.ultimateantibot.bungee.commands.antibotCommands;
import me.kr1s_d.ultimateantibot.bungee.database.Config;
import me.kr1s_d.ultimateantibot.bungee.event.AntibotModeListener;
import me.kr1s_d.ultimateantibot.bungee.event.HandShakeListener;
import me.kr1s_d.ultimateantibot.bungee.event.PingListener;
import me.kr1s_d.ultimateantibot.bungee.event.MainEventListener;
import me.kr1s_d.ultimateantibot.bungee.filter.FilterManager;
import me.kr1s_d.ultimateantibot.bungee.core.UltimateThreadCore;
import me.kr1s_d.ultimateantibot.bungee.utils.*;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.bungee.service.QueueService;
import me.kr1s_d.ultimateantibot.bungee.service.WhitelistService;
import me.kr1s_d.ultimateantibot.bungee.user.UserData;
import me.kr1s_d.ultimateantibot.bungee.user.UserInfo;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public final class UltimateAntibotWaterfall extends Plugin {

    public static UltimateAntibotWaterfall instance;
    private Counter counter;
    private UltimateThreadCore core;
    private AntibotManager antibotManager;
    private Config configmanager;
    private Configuration config;
    private Configuration message;
    private Configuration whitelist;
    private Configuration blacklist;
    private Configuration database;
    private ConfigManager configManage;
    private Metrics metrics;
    private Updater updater;
    private FilesUpdater filesUpdater;
    private FilterManager filterManager;
    private QueueService queueService;
    private UserInfo userInfo;
    private WhitelistService whitelistService;
    private SlowJoinCheck slowJoinCheck;
    private AntibotInfo antibotInfo;
    private UserData userData;
    private HandShakeCheck handShakeCheck;

    // TODO: 18/10/2021 Rifare i comandi 
    // TODO: 18/10/2021 Aggiornare MessageManager A tutto il pl 
    
    @Override
    public void onEnable() {
        instance = this;
        long a = System.currentTimeMillis();
        this.configmanager = new Config(this);
        configmanager.createConfiguration("%datafolder%/config.yml");
        configmanager.createConfiguration("%datafolder%/messages.yml");
        configmanager.createConfiguration("%datafolder%/whitelist.yml");
       // configmanager.createConfiguration("%datafolder%/blacklist.yml");
        configmanager.createConfiguration("%datafolder%/database.yml");
        reload();
        long b = System.currentTimeMillis() - a;
        Utils.debug("&eTook " + b + " ms");
    }

    public void reload(){
        ProxyServer.getInstance().getScheduler().cancel(this);
        config = configmanager.getConfiguration("%datafolder%/config.yml");
        message = configmanager.getConfiguration("%datafolder%/messages.yml");
        whitelist = configmanager.getConfiguration("%datafolder%/whitelist.yml");
        //blacklist = configmanager.getConfiguration("%datafolder%/blacklist.yml");
        database = configmanager.getConfiguration("%datafolder%/database.yml");
        configManage = new ConfigManager(this);
        counter = new Counter();
        updater = new Updater(this);
        metrics = new Metrics(this, 11712);
        antibotInfo = new AntibotInfo();
        antibotManager = new AntibotManager(this);
        core = new UltimateThreadCore(this);
        core.enable();
        core.heartBeatMinimal();
        core.hearthBeatMaximal();
        core.hearthBeatExaminal();
        userData = new UserData();
        configmanager = new Config(this);
        filesUpdater = new FilesUpdater(this);
        filesUpdater.check();
        filterManager = new FilterManager(this);
        filterManager.setupFilter();
        queueService = new QueueService(this);
        userInfo = new UserInfo(this);
        whitelistService = new WhitelistService(this);
        whitelistService.loadWhitelist();
        slowJoinCheck = new SlowJoinCheck(this);
        handShakeCheck = new HandShakeCheck(this);
        userInfo.loadFirstJoin();
        MessageManager.init(this);
        getProxy().getPluginManager().registerCommand(this, new antibotCommands(this));
        getProxy().getPluginManager().registerListener(this, new PingListener(this));
        getProxy().getPluginManager().registerListener(this, new MainEventListener(this));
        getProxy().getPluginManager().registerListener(this, new AntibotModeListener(this));
        getProxy().getPluginManager().registerListener(this, new HandShakeListener(this));
        sendLogo();
        Utils.debug(Utils.colora(Utils.prefix() + "&aRunning version " + this.getDescription().getVersion()));
        Utils.debug(Utils.colora(Utils.prefix() + "&aEnabled"));
    }

    @Override
    public void onDisable() {
        long a = System.currentTimeMillis();
        Utils.debug(Utils.prefix() + "&aSaving Files");
        Utils.debug(Utils.prefix() + "&AThanks for choosing us!");
        whitelistService.saveWhitelist(configmanager);
        userInfo.save(configmanager);
        long b = System.currentTimeMillis() - a;
        Utils.debug(Utils.prefix() + String.format("&eTook %s ms", b));
    }

    public Counter getCounter() {
        return counter;
    }

    public UltimateThreadCore getCore() {
        return core;
    }

    public AntibotManager getAntibotManager() {
        return antibotManager;
    }

    public Configuration getConfigYml() {
        return config;
    }

    public Configuration getBlacklistYml() {
        return blacklist;
    }

    /**
     * Use MessageManager for messaging
     */
    @Deprecated
    public Configuration getMessageYml() {
        return message;
    }

    public Configuration getWhitelistYml() {
        return whitelist;
    }

    public Configuration getDatabaseYml() {
        return database;
    }

    public Config getConfigmanager() {
        return configmanager;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void sendLogo(){
        Utils.debug(Utils.prefix() + "&a _    _         ____ ");
        Utils.debug(Utils.prefix() + "&a| |  | |  /\\   |  _ \\ ");
        Utils.debug(Utils.prefix() + "&a| |  | | /  \\  | |_) |");
        Utils.debug(Utils.prefix() + "&a| |  | |/ /\\ \\ |  _ <");
        Utils.debug(Utils.prefix() + "&a| |__| / ____ \\| |_) |");
        Utils.debug(Utils.prefix() + "&a\\____/_/     \\_\\____/");
    }

    public Updater getUpdater() {
        return updater;
    }

    public FilesUpdater getFilesUpdater() {
        return filesUpdater;
    }

    public QueueService getQueueService() {
        return queueService;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public SlowJoinCheck getSlowJoinCheck() {
        return slowJoinCheck;
    }

    public AntibotInfo getAntibotInfo() {
        return antibotInfo;
    }

    public UserData getUserData() {
        return userData;
    }

    public HandShakeCheck getHandShakeCheck() {
        return handShakeCheck;
    }

    public FilterManager getFilterManager() {
        return filterManager;
    }

    public ConfigManager getConfigManager() {
        return configManage;
    }
}
