package me.kr1s_d.ultimateantibot;

import me.kr1s_d.ultimateantibot.Commands.antibotComands;
import me.kr1s_d.ultimateantibot.Database.Config;
import me.kr1s_d.ultimateantibot.Event.PingListener;
import me.kr1s_d.ultimateantibot.Event.PreloginEventListener;
import me.kr1s_d.ultimateantibot.Thread.UltimateThreadCore;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import me.kr1s_d.ultimateantibot.Utils.Metrics;
import me.kr1s_d.ultimateantibot.Utils.utils;
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

    @Override
    public void onEnable() {
        this.configmanager = new Config(this);
        configmanager.createConfiguration("%datafolder%/config.yml");
        configmanager.createConfiguration("%datafolder%/messages.yml");
       // configmanager.createConfiguration("%datafolder%/whitelist.yml");
       // configmanager.createConfiguration("%datafolder%/blacklist.yml");
        reload();
    }

    public void reload(){
        ProxyServer.getInstance().getScheduler().cancel(this);
        config = configmanager.getConfiguration("%datafolder%/config.yml");
        message = configmanager.getConfiguration("%datafolder%/messages.yml");
        //whitelist = configmanager.getConfiguration("%datafolder%/whitelist.yml");
        //blacklist = configmanager.getConfiguration("%datafolder%/blacklist.yml");
        metrics = new Metrics(this, 11712);
        antibotManager = new AntibotManager(this);
        counter = new Counter();
        core = new UltimateThreadCore(this);
        core.enable();
        core.heartBeatMinimal();
        core.hearthBeatMaximal();
        core.hearthBeatExaminal();
        utils.debug(utils.prefix() + "&aLoaded $1 Whitelisted Ips".replace("$1", String.valueOf(antibotManager.getWhitelist().size())));
        utils.debug(utils.prefix() + "&aLoaded $1 Blacklisted Ips".replace("$1", String.valueOf(antibotManager.getBlacklist().size())));
        configmanager = new Config(this);
        getProxy().getPluginManager().registerCommand(this, new antibotComands(this));
        getProxy().getPluginManager().registerListener(this, new PingListener(this));
        getProxy().getPluginManager().registerListener(this, new PreloginEventListener(this));
        utils.debug(utils.colora(utils.prefix() + "&aRunning on version $1".replace("$1", ProxyServer.getInstance().getVersion())));
        utils.debug(utils.colora(utils.prefix() + "&aEnabled"));
    }

    @Override
    public void onDisable() {
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
}
