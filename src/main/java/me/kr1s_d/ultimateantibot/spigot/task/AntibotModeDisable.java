package me.kr1s_d.ultimateantibot.spigot.task;

import me.kr1s_d.ultimateantibot.spigot.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

public class AntibotModeDisable {

    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;
    private final AntibotInfo antibotInfo;

    public AntibotModeDisable(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
        this.antibotInfo = plugin.getAntibotInfo();
    }

    public void disable(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(antibotInfo.getJoinSecond() > configManager.getAntiBotMode_trigger()){
                    disable();
                    antibotManager.setModeType(ModeType.ANTIBOTMODE);
                    return;
                }
                antibotManager.setAntibotModeStatus(false);
                plugin.getAntibotManager().setModeType(ModeType.OFFLINE);
            }
        }.runTaskLater(plugin, configManager.getAntiBotMode_keep() * 20L);
    }
}
