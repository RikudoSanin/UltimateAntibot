package me.kr1s_d.ultimateantibot.bungee.Filter;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.utils;
import net.md_5.bungee.api.ProxyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class LoadFilter {
    private final BungeeFilter bungeeFilter;
    private final WaterfallFilter waterfallFilter;

    public LoadFilter(UltimateAntibotWaterfall plugin){
        this.bungeeFilter = new BungeeFilter(plugin);
        this.waterfallFilter = new WaterfallFilter(plugin);
    }

    public void setupFilter(){
        utils.debug(utils.prefix() + "Loading Filter...");
        try{
            utils.debug(utils.prefix() + "&aBungeeFilter Loaded");
            ProxyServer.getInstance().getLogger().setFilter(bungeeFilter);
            ((Logger) LogManager.getRootLogger()).addFilter(waterfallFilter);
        }catch (Exception e){
            utils.debug(utils.prefix() + "&aWaterfallFilter Loaded");
            ((Logger) LogManager.getRootLogger()).addFilter(waterfallFilter);
        }
        utils.debug(utils.prefix() + "Filter Loaded");
    }
}
