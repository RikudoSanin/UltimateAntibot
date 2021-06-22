package me.kr1s_d.ultimateantibot.bungee.Filter;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;

import java.util.List;
import java.util.Locale;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class BungeeFilter implements Filter {
    private final UltimateAntibotWaterfall plugin;

    public BungeeFilter(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        List<String> lista = plugin.getConfigYml().getStringList("filter");
        String message = record.getMessage();
        for(String str: lista) {
            if (message.toLowerCase().contains(str.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
