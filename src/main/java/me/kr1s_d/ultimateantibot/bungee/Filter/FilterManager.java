package me.kr1s_d.ultimateantibot.bungee.Filter;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class FilterManager {
    private final WaterfallFilter waterfallFilter;
    private final BungeeFilter bungeeFilter;

    public FilterManager(UltimateAntibotWaterfall plugin){
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
