package me.kr1s_d.ultimateantibot.bungee.filter;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;

import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class BungeeFilter implements Filter {
    private final ConfigManager configManager;

    public BungeeFilter(UltimateAntibotWaterfall plugin){
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        List<String> lista = configManager.getFilter();
        String message = record.getMessage();
        for(String str: lista) {
            if (message.toLowerCase().contains(str.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
