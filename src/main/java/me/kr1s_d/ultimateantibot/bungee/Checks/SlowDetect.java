package me.kr1s_d.ultimateantibot.bungee.Checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class SlowDetect {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;

    public SlowDetect(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void check(ProxiedPlayer player){
        String ip = player.getAddress().getAddress().toString();
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(!player.isConnected() && plugin.getConfigYml().getBoolean("checks.slowmode.enable")){
                    antibotManager.getWhitelist().remove(ip);
                    antibotManager.getBlacklist().remove(ip);
                    antibotManager.getQueue().remove(ip);
                    plugin.getCounter().analyzeHard(ip, (int) plugin.getConfigYml().getLong("blacklist.strange"));
                }
            }
        }, plugin.getConfigYml().getLong("checks.slowmode.duration"), TimeUnit.SECONDS);
    }
}
