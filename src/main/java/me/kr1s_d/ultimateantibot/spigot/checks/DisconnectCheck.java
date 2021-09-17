package me.kr1s_d.ultimateantibot.spigot.checks;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisconnectCheck {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;

    public DisconnectCheck(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
    }

    public boolean isEnabled(){
        return configManager.isSlowMode_enabled();
    }

    public void check(Player player) {
        String ip = Utils.getIP(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() && isEnabled() && antibotManager.isOnline()) {
                    antibotManager.getWhitelist().remove(ip);
                    antibotManager.getBlacklist().remove(ip);
                    antibotManager.getQueue().remove(ip);
                }
            }

        }.runTaskTimer(plugin, 0, configManager.getSlowMode_duration() * 20L);
    }
}
