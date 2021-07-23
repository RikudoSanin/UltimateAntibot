package me.kr1s_d.ultimateantibot.spigot;

import me.kr1s_d.ultimateantibot.spigot.Commands.antibotCommand;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.Event.PingListener;
import me.kr1s_d.ultimateantibot.spigot.Event.PreloginListener;
import me.kr1s_d.ultimateantibot.spigot.Filter.LogFilter;
import me.kr1s_d.ultimateantibot.spigot.Thread.UltimateThreadCore;
import me.kr1s_d.ultimateantibot.spigot.Utils.*;
import me.kr1s_d.ultimateantibot.spigot.service.QueueService;
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
    private Metrics metrics;
    private Updater updater;
    private FilesUpdater filesUpdater;
    private LogFilter logFilter;
    private QueueService queueService;

    @Override
    public void onEnable() {
        config = new Config(this, "config");
        message = new Config(this, "messages");
        whitelist = new Config(this, "whitelist");
        reload();
    }

    @Override
    public void onDisable() {

    }

    public void reload(){
        Bukkit.getScheduler().cancelTasks(this);
        loadWhitelist();
        updater = new Updater(this);
        metrics = new Metrics(this, 11777);
        antibotManager = new AntibotManager(this);
        counter = new Counter();
        core = new UltimateThreadCore(this);
        core.enable();
        core.heartBeatMinimal();
        core.hearthBeatMaximal();
        core.hearthBeatExaminal();
        logFilter = new LogFilter(this);
        filesUpdater = new FilesUpdater(this);
        filesUpdater.check();
        ((Logger)LogManager.getRootLogger()).addFilter(logFilter);
        Utils.debug(Utils.prefix() + "&aLoaded Filter");
        queueService = new QueueService(this);
        Utils.debug(Utils.prefix() + "&aLoaded $1 Whitelisted Ips".replace("$1", String.valueOf(antibotManager.getWhitelist().size())));
        sendLogo();
        Bukkit.getPluginManager().registerEvents(new PreloginListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PingListener(this), this);
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
    public void loadWhitelist(){
        Utils.debug(Utils.prefix() + "&aWhitelist Loading data not Set!");
        Utils.debug(Utils.prefix() + "&cAborting");
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
}
