package me.kr1s_d.ultimateantibot.spigot.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;

public class Updater {
    private final UltimateAntibotSpigot plugin;
    private String localversion;
    private String newVersion;

    public Updater(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
        this.localversion = plugin.getDescription().getVersion();
        this.newVersion = "not_found";
    }

    private boolean isAvailable;


    public boolean isAvailable() {
        return isAvailable;
    }


    public void checkNotification() {
        if(isAvailable()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Utils.debug(Utils.prefix() + "&eNew Update Found!");
                    Utils.debug(Utils.prefix() + "&EI suggest you to update plugin!");
                    Utils.debug(Utils.prefix() + "&EYour version $1, new Version $2"
                            .replace("$1", localversion)
                            .replace("$2", newVersion)
                    );
                }
            }.runTaskTimer(plugin, 0, 1200L * 20L);
        }
    }

    public void check() {
        isAvailable = checkUpdate();
    }

    private boolean checkUpdate() {
        try {
            String localVersion = plugin.getDescription().getVersion();
            localversion = localVersion;
            String id = "93439";
            String url = "https://api.spigotmc.org/legacy/update.php?resource=";
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url + id).openConnection();
            connection.setRequestMethod("GET");
            String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            newVersion = raw;
            if(!localVersion.equalsIgnoreCase(raw))
                return true;

        } catch (IOException e) {
            return false;
        }
        return false;
    }
    private String col(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

