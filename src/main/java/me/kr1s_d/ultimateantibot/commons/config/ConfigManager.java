package me.kr1s_d.ultimateantibot.commons.config;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class ConfigManager {
    private static List<String> filter;

    public ConfigManager(UltimateAntibotWaterfall plugin){
        Configuration config = plugin.getConfigYml();
        filter = config.getStringList("filter");
    }

    public ConfigManager(UltimateAntibotSpigot plugin){
        Config config = plugin.getConfigYml();
        filter = config.getStringList("filter");
    }

    public static List<String> getFilterMessages() {
        return filter;
    }
}
