package me.kr1s_d.ultimateantibot.spigot.Checks;

import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SlowDetect {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;

    public SlowDetect(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void check(Player player) {
        String ip = Utils.getIP(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() && plugin.getConfigYml().getBoolean("checks.slowmode.enable")) {
                    antibotManager.getWhitelist().remove(ip);
                    antibotManager.getBlacklist().remove(ip);
                    antibotManager.getQueue().remove(ip);
                    plugin.getCounter().analyzeHard(ip, (int) plugin.getConfigYml().getLong("blacklist.strange"));
                }
            }

        }.runTaskTimer(plugin, 0, Math.round((float) plugin.getConfigYml().getLong("checks.slowmode.duration") * 20L));
    }
}
