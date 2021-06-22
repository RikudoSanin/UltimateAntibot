package me.kr1s_d.ultimateantibot.spigot.Task;



import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;

public class SafemodeDisableListener {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;

    public SafemodeDisableListener(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
    }

    public void start(){
        new BukkitRunnable() {
            @Override
            public void run() {
                antibotManager.setSafeAntiBotMode(false);
                antibotManager.setModeType(ModeType.OFFLINE);
            }
        }.runTaskLater(plugin, plugin.getConfigYml().getLong("safemode.keep") * 20L);
    }
}
