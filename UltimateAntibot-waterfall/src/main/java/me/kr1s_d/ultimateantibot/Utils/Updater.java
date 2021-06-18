package me.kr1s_d.ultimateantibot.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import javax.net.ssl.HttpsURLConnection;

public class Updater {
    private String url = "https://api.spigotmc.org/legacy/update.php?resource=";
    private String id = "88678";
    private final UltimateAntibotWaterfall plugin;

    public Updater(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
    }

    private boolean isAvailable;

    public void UpdateChecker() {

    }


    public boolean isAvailable() {
        return isAvailable;
    }


    public void startNotification() {
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                utils.debug(utils.prefix() + "&eNew Update Found!");
                utils.debug(utils.prefix() + "&EI suggest you to update plugin!");
            }
        }, 0, 20L, TimeUnit.MINUTES);
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
