package me.kr1s_d.ultimateantibot.spigot.Task;


import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoWhitelistTask {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final Player player;
    private final ConfigManager configManager;

    public AutoWhitelistTask(UltimateAntibotSpigot plugin, Player player){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.player = player;
        this.configManager = plugin.getConfigManager();
    }

    /**
     *
     * playtime_for_whitelist: 2 minutes default config
     *
     */
    public void start(){
        new BukkitRunnable() {
            @Override
            public void run() {
                String ip = Utils.getIP(player);
                if(player.isOnline()){
                    antibotManager.addWhitelist(ip);
                }
                antibotManager.getQueue().remove(ip);
            }
        }.runTaskLater(plugin, configManager.getPlaytime_whitelist() * 20L * 60L);
    }
}
