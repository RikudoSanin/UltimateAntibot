package me.kr1s_d.ultimateantibot.spigot.Checks;

import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisconnectCheck {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final Config config;


    public DisconnectCheck(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.config = plugin.getConfigYml();
    }

    public boolean isEnabled(){
        return config.getBoolean("checks.slowmode.enable");
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
                    plugin.getCounter().analyzeHard(ip, (int) config.getLong("blacklist.strange"));
                }
            }

        }.runTaskTimer(plugin, 0, Math.round((float) config.getLong("checks.slowmode.duration") * 20L));
    }
}
