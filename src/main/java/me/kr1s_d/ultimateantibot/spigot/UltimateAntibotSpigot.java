package me.kr1s_d.ultimateantibot.spigot;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.Checks.SlowJoinCheck;
import me.kr1s_d.ultimateantibot.spigot.Commands.antibotCommand;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.Event.AntibotModeListener;
import me.kr1s_d.ultimateantibot.spigot.Event.PingListener;
import me.kr1s_d.ultimateantibot.spigot.Event.PreloginListener;
import me.kr1s_d.ultimateantibot.spigot.Filter.LogFilter;
import me.kr1s_d.ultimateantibot.spigot.core.UltimateThreadCore;
import me.kr1s_d.ultimateantibot.spigot.Utils.*;
import me.kr1s_d.ultimateantibot.spigot.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.spigot.service.QueueService;
import me.kr1s_d.ultimateantibot.spigot.service.WhitelistService;
import me.kr1s_d.ultimateantibot.spigot.user.UserData;
import me.kr1s_d.ultimateantibot.spigot.user.UserInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class UltimateAntibotSpigot extends JavaPlugin {

    private Counter counter;
    private UltimateThreadCore core;
    private AntibotManager antibotManager;
    private Config config;
    private Config message;
    private Config whitelist;
    private Config blacklist;
    private Config database;
    private Metrics metrics;
    private Updater updater;
    private FilesUpdater filesUpdater;
    private LogFilter logFilter;
    private QueueService queueService;
    private UserInfo userInfo;
    private WhitelistService whitelistService;
    private SlowJoinCheck slowJoinCheck;
    private AntibotInfo antibotInfo;
    private UserData userData;

    @Override
    public void onEnable() {
        long a = System.currentTimeMillis();
        config = new Config(this, "config");
        message = new Config(this, "messages");
        whitelist = new Config(this, "whitelist");
        database = new Config(this, "database");
        reload();
        long b = System.currentTimeMillis() - a;
        Utils.debug("&eTook " + b + " ms");
    }

    @Override
    public void onDisable() {
        long a = System.currentTimeMillis();
        Utils.debug(Utils.prefix() + "&aSaving Files");
        Utils.debug(Utils.prefix() + "&AThanks for choosing us!");
        whitelistService.saveWhitelist();
        userInfo.save();
        long b = System.currentTimeMillis() - a;
        Utils.debug(Utils.prefix() + String.format("&eTook %s ms", b));
    }

    public void reload(){
        Bukkit.getScheduler().cancelTasks(this);
        new ConfigManager(this);
        updater = new Updater(this);
        metrics = new Metrics(this, 11777);
        antibotInfo = new AntibotInfo();
        antibotManager = new AntibotManager(this);
        counter = new Counter();
        core = new UltimateThreadCore(this);
        core.enable();
        core.heartBeatMinimal();
        core.hearthBeatMaximal();
        core.hearthBeatExaminal();
        userData = new UserData();
        logFilter = new LogFilter(this);
        filesUpdater = new FilesUpdater(this);
        filesUpdater.check();
        ((Logger)LogManager.getRootLogger()).addFilter(logFilter);
        Utils.debug(Utils.prefix() + "&aLoaded Filter");
        queueService = new QueueService(this);
        userInfo = new UserInfo(this);
        whitelistService = new WhitelistService(this);
        whitelistService.loadWhitelist();
        slowJoinCheck = new SlowJoinCheck(this);
        userInfo.loadFirstJoin();
        sendLogo();
        Bukkit.getPluginManager().registerEvents(new PreloginListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AntibotModeListener(this), this);
        getCommand("ultimateantibot").setExecutor(new antibotCommand(this));
        Utils.debug(Utils.colora(Utils.prefix() + "&aRunning version " + this.getDescription().getVersion()));
        Utils.debug(Utils.colora(Utils.prefix() + "&aEnabled"));
        new BukkitRunnable() {
            @Override
            public void run() {
                sendSpigotAlert();
            }
        }.runTaskLater(this, 40L);
    }

    public void sendLogo(){
        Utils.debug(Utils.prefix() + "&a _    _         ____ ");
        Utils.debug(Utils.prefix() + "&a| |  | |  /\\   |  _ \\ ");
        Utils.debug(Utils.prefix() + "&a| |  | | /  \\  | |_) |");
        Utils.debug(Utils.prefix() + "&a| |  | |/ /\\ \\ |  _ <");
        Utils.debug(Utils.prefix() + "&a| |__| / ____ \\| |_) |");
        Utils.debug(Utils.prefix() + "&a\\____/_/     \\_\\____/");
    }

    public FilesUpdater getFilesUpdater() {
        return filesUpdater;
    }

    public Updater getUpdater() {
        return updater;
    }

    public Config getConfigYml() {
        return config;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public UltimateThreadCore getCore() {
        return core;
    }

    public Counter getCounter() {
        return counter;
    }

    public Config getMessageYml() {
        return message;
    }

    public Config getWhitelist() {
        return whitelist;
    }

    public AntibotManager getAntibotManager() {
        return antibotManager;
    }

    public LogFilter getLogFilter() {
        return logFilter;
    }

    public void sendSpigotAlert(){
        Utils.debug(Utils.prefix() + "&cAlthough spigot support has been added,");
        Utils.debug(Utils.prefix() + "&cI absolutely do not recommend that you do not use a bungeecord proxy network for your server.");
        Utils.debug(Utils.prefix() + "&cSpigot has many processes to manage due to the various plugins");
        Utils.debug(Utils.prefix() + "&cmoreover the antibot to mitigate the attacks uses a system of tasks that could");
        Utils.debug(Utils.prefix() + "&cslow down servers with less powerful hardware");
        Utils.debug(Utils.prefix() + "&cUse safety! Use BungeeCord or Waterfall");
        //
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

    public Config getDatabase() {
        return database;
    }

    public UserData getUserData() {
        return userData;
    }
}
