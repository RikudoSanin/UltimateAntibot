package me.kr1s_d.ultimateantibot.spigot.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;

public class Updater {
    private String url = "https://api.spigotmc.org/legacy/update.php?resource=";
    private String id = "93439";
    private final UltimateAntibotSpigot plugin;

    public Updater(UltimateAntibotSpigot plugin){
        this.plugin = plugin;
    }

    private boolean isAvailable;

    public void UpdateChecker() {

    }


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
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url + id).openConnection();
            connection.setRequestMethod("GET");
            String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

            String remoteVersion;
            if(raw.contains("-")) {
                remoteVersion = raw.split("-")[0].trim();
            } else {
                remoteVersion = raw;
            }

            if(!localVersion.equalsIgnoreCase(remoteVersion))
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

