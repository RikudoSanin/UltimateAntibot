package me.kr1s_d.ultimateantibot.bungee.user;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.database.Config;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class UserInfo {

    private final Configuration database;
    private final AntibotManager antibotManager;
    private final Configuration messages;
    private final UserData userData;
    private final ConfigManager configManager;
    private int removed;
    private int line;
    private ScheduledTask task;

    public UserInfo(UltimateAntibotWaterfall plugin) {
        this.database = plugin.getDatabaseYml();
        this.antibotManager = plugin.getAntibotManager();
        this.messages = plugin.getMessageYml();
        this.userData = plugin.getUserData();
        this.configManager = plugin.getConfigManager();
        this.removed = 0;
        this.line = -1;
    }

    /**
     * Metodi del First Join
     */
    public boolean checkFirstJoin(PreLoginEvent e, String ip){
        if(configManager.isFirstJoin_enabled() && !antibotManager.isAntiBotModeOnline()) {
            if (userData.isFirstJoin(ip)) {
                antibotManager.removeWhitelist(ip);
                antibotManager.removeBlackList(ip);
                userData.setFirstJoin(ip, false);
                e.setCancelReason(new TextComponent(Utils.colora(Utils.convertToString(messages.getStringList("first_join")))));
                e.setCancelled(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Adapter
     *
     */
    private String adapt(String str){
        return str.replace(".", ",");
    }

    private String deAdapt(String str){
        return str.replace(",", ".");
    }

    /**
     * Caricamento dei dati
     */

    public void loadFirstJoin(){
        try {
            for(String str : database.getSection("data").getKeys()) {
                String ip = deAdapt(str);
                boolean status = database.getBoolean("data." + ip + ".firstJoin");
                userData.getFirstJoinHashMap().put(ip, status);
            }
            Utils.debug(Utils.prefix() + "&a" + userData.getFirstJoinHashMap().size() + " Joindata have been loaded intro the Framework");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "&cError during Database Loading!");
            Utils.debug(Utils.prefix() + "&c " + e.getMessage());
            Utils.debug(Utils.prefix() + "&eError type: First Join, is database corrupted?");
            e.printStackTrace();
        }
    }

    /**
     * Salvataggio dei dati
     */
    public void save(Config configmanager){
        try {
            if(userData.getFirstJoinHashMap().isEmpty()){
                configmanager.saveConfiguration(database,"%datafolder%/database.yml");
                return;
            }
            for(Map.Entry<String, Boolean> map : userData.getFirstJoinHashMap().entrySet()){
                String ip = adapt(map.getKey());
                boolean status = map.getValue();
                String path = String.format("data.%s.firstJoin", ip);
                database.set(path, status);
            }
            Utils.debug(Utils.prefix() + "&aJoin database has been saved");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "&cError during saving userdata!");
            Utils.debug(Utils.prefix() + "&c" + e.getMessage());
        }
        configmanager.saveConfiguration(database,"%datafolder%/database.yml");
    }
}
