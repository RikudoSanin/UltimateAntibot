package me.kr1s_d.ultimateantibot.spigot.Utils;

import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.scheduler.BukkitRunnable;


public class FilesUpdater {
    private final UltimateAntibotSpigot plugin;
    private final Config config;
    private final Config messages;

    public FilesUpdater(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigYml();
        this.messages = plugin.getMessageYml();
    }
    public void a(String type){
        Utils.debug(Utils.prefix() + "&C-----ALERT----");
        Utils.debug(Utils.prefix() + "&CYour %s.yml is outdated!".replace("%s", type));
        Utils.debug(Utils.prefix()+ "&cRegen it and Restart");
        Utils.debug(Utils.prefix() + "&C-----ALERT----");
    }

    public void check(){
        if(config.getDouble("version") != 2.6){
            new BukkitRunnable() {
                @Override
                public void run() {
                    a("config");
                }
            }.runTaskTimer(plugin, 0, 20*60L);
        }
        if(messages.getDouble("version") != 2.3){
            new BukkitRunnable() {
                @Override
                public void run() {
                    a("messages");
                }
            }.runTaskTimer(plugin, 0, 20*60L);
        }
    }
}
