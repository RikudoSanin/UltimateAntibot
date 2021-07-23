package me.kr1s_d.ultimateantibot.bungee;

import me.kr1s_d.ultimateantibot.bungee.Commands.antibotComands;
import me.kr1s_d.ultimateantibot.bungee.Database.Config;
import me.kr1s_d.ultimateantibot.bungee.Event.PingListener;
import me.kr1s_d.ultimateantibot.bungee.Event.PreloginEventListener;
import me.kr1s_d.ultimateantibot.bungee.Filter.LoadFilter;
import me.kr1s_d.ultimateantibot.bungee.Thread.UltimateThreadCore;
import me.kr1s_d.ultimateantibot.bungee.Utils.*;
import me.kr1s_d.ultimateantibot.bungee.service.QueueService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public final class UltimateAntibotWaterfall extends Plugin {

    private Counter counter;
    private UltimateThreadCore core;
    private AntibotManager antibotManager;
    private Config configmanager;
    private Configuration config;
    private Configuration message;
    private Configuration whitelist;
    private Configuration blacklist;
    private Metrics metrics;
    private Updater updater;
    private FilesUpdater filesUpdater;
    private LoadFilter loadFilter;
    private QueueService queueService;

    @Override
    public void onEnable() {
        this.configmanager = new Config(this);
        configmanager.createConfiguration("%datafolder%/config.yml");
        configmanager.createConfiguration("%datafolder%/messages.yml");
        configmanager.createConfiguration("%datafolder%/whitelist.yml");
       // configmanager.createConfiguration("%datafolder%/blacklist.yml");
        reload();
    }

    public void reload(){
        ProxyServer.getInstance().getScheduler().cancel(this);
        config = configmanager.getConfiguration("%datafolder%/config.yml");
        message = configmanager.getConfiguration("%datafolder%/messages.yml");
        whitelist = configmanager.getConfiguration("%datafolder%/whitelist.yml");
        //blacklist = configmanager.getConfiguration("%datafolder%/blacklist.yml");
        loadWhitelist();
        updater = new Updater(this);
        metrics = new Metrics(this, 11712);
        antibotManager = new AntibotManager(this);
        counter = new Counter();
        core = new UltimateThreadCore(this);
        core.enable();
        core.heartBeatMinimal();
        core.hearthBeatMaximal();
        core.hearthBeatExaminal();
        utils.debug(utils.prefix() + "&aLoaded $1 Whitelisted Ips".replace("$1", String.valueOf(antibotManager.getWhitelist().size())));
        configmanager = new Config(this);
        filesUpdater = new FilesUpdater(this);
        filesUpdater.check();
        loadFilter = new LoadFilter(this);
        loadFilter.setupFilter();
        queueService = new QueueService(this);
        getProxy().getPluginManager().registerCommand(this, new antibotComands(this));
        getProxy().getPluginManager().registerListener(this, new PingListener(this));
        getProxy().getPluginManager().registerListener(this, new PreloginEventListener(this));
        sendLogo();
        utils.debug(utils.colora(utils.prefix() + "&aRunning version " + this.getDescription().getVersion()));
        utils.debug(utils.colora(utils.prefix() + "&aEnabled"));
    }

    @Override
    public void onDisable() {
        utils.debug("&aSaving Files");
        utils.debug("&AThanks for choosing us!");
        for(String str : antibotManager.getWhitelist()){
            message.set("data." + str, 0);
        }
        configmanager.saveConfiguration(whitelist,"%datafolder%/whitelist.yml");
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

    public Configuration getMessageYml() {
        return message;
    }

    public Configuration getWhitelistYml() {
        return whitelist;
    }

    public Config getConfigmanager() {
        return configmanager;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void sendLogo(){
        utils.debug(utils.prefix() + "&a _    _         ____ ");
        utils.debug(utils.prefix() + "&a| |  | |  /\\   |  _ \\ ");
        utils.debug(utils.prefix() + "&a| |  | | /  \\  | |_) |");
        utils.debug(utils.prefix() + "&a| |  | |/ /\\ \\ |  _ <");
        utils.debug(utils.prefix() + "&a| |__| / ____ \\| |_) |");
        utils.debug(utils.prefix() + "&a\\____/_/     \\_\\____/");
    }
    public void loadWhitelist(){
        utils.debug(utils.prefix() + "&aWhitelist Loading data not Set!");
        utils.debug(utils.prefix() + "&cAborting");
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
}
