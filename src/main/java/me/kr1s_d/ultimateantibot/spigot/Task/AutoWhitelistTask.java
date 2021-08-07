package me.kr1s_d.ultimateantibot.spigot.Task;


import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoWhitelistTask {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final Player player;
    private final Config config;

    public AutoWhitelistTask(UltimateAntibotSpigot plugin, Player player){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.player = player;
        this.config = plugin.getConfigYml();
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
                if(!player.isOnline()){
                    if(!antibotManager.isOnline()){
                        plugin.getCounter().analyzeIP(ip);
                    }
                }
                if(player.isOnline()){
                    antibotManager.addWhitelist(ip);
                }
                antibotManager.getQueue().remove(ip);
            }
        }.runTaskLater(plugin, config.getLong("playtime_for_whitelist") * 20L * 60L);
    }
}
