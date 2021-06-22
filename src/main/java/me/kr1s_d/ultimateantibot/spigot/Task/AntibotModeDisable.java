package me.kr1s_d.ultimateantibot.spigot.Task;

import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

public class AntibotModeDisable {

    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;

    public AntibotModeDisable(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void disable(){
        new BukkitRunnable() {
            @Override
            public void run() {
                antibotManager.setAntibotModeStatus(false);
                plugin.getAntibotManager().setModeType(ModeType.OFFLINE);
            }
        }.runTaskLater(plugin, plugin.getConfigYml().getLong("antibotmode.keep") * 20L);
    }
}
