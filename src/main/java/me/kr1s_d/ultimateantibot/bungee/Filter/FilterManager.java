package me.kr1s_d.ultimateantibot.bungee.Filter;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FilterManager {
    private UltimateAntibotWaterfall ultimateAntibotWaterfall;
    private final WaterfallFilter waterfallFilter;
    private final BungeeFilter bungeeFilter;

    public FilterManager(UltimateAntibotWaterfall plugin){
        this.ultimateAntibotWaterfall = plugin;
        this.waterfallFilter = new WaterfallFilter(plugin);
        this.bungeeFilter = new BungeeFilter(plugin);
    }

    public void setupFilter(){
        Utils.debug(Utils.prefix() + "Loading Filter...");
        try{
            Utils.debug(Utils.prefix() + "&aBungeeFilter Loaded");
            ProxyServer.getInstance().getLogger().setFilter(bungeeFilter);
            ((Logger) LogManager.getRootLogger()).addFilter(waterfallFilter);
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "&aWaterfallFilter Loaded");
            ((Logger) LogManager.getRootLogger()).addFilter(waterfallFilter);
        }
        Utils.debug(Utils.prefix() + "Filter Loaded");
    }
}
