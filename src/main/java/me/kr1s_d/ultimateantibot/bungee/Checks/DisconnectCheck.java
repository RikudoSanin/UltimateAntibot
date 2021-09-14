package me.kr1s_d.ultimateantibot.bungee.Checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class DisconnectCheck {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public DisconnectCheck(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
    }

    public boolean isEnabled(){
        return configManager.isSlowMode_enabled();
    }

    public void checkDisconnect(ProxiedPlayer player){
        String ip = Utils.getIP(player);
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(!player.isConnected() && isEnabled() && antibotManager.isOnline()){
                    antibotManager.getWhitelist().remove(ip);
                    antibotManager.getBlacklist().remove(ip);
                    antibotManager.getQueue().remove(ip);
                }
            }
        },configManager.getSlowMode_duration(), TimeUnit.SECONDS);
    }
}
