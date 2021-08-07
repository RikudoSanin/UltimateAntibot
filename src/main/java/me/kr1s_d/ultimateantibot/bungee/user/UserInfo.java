package me.kr1s_d.ultimateantibot.bungee.user;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.Database.Config;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.config.Configuration;

import java.util.Map;


public class UserInfo {

    private final Configuration database;
    private final Configuration config;
    private final AntibotManager antibotManager;
    private final Configuration messages;
    private final UserData userData;

    public UserInfo(UltimateAntibotWaterfall plugin) {
        this.database = plugin.getDatabaseYml();
        this.config = plugin.getConfigYml();
        this.antibotManager = plugin.getAntibotManager();
        this.messages = plugin.getMessageYml();
        this.userData = plugin.getUserData();
    }

    /**
     * Metodi del First Join
     */
    public boolean checkFirstJoin(PreLoginEvent e, String ip){
        if(config.getBoolean("checks.first_join.enabled")) {
            if (!antibotManager.isOnline() && userData.isFirstJoin(ip)) {
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
    public String adapt(String str){
        return str.replace(".", ",");
    }

    public String deAdapt(String str){
        return str.replace(",", ".");
    }

    /**
     * Caricamento dei dati
     */

    public void loadFirstJoin(){
        try {
            for(String str : database.getSection("data").getKeys()){
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
            for(Map.Entry<String, Boolean> map : userData.getFirstJoinHashMap().entrySet()){
                String ip = adapt(map.getKey());
                boolean status = map.getValue();
                String path = String.format("data.%s.firstJoin", ip);
                database.set(path, status);
            }
            Utils.debug(Utils.prefix() + "&ajoin database has been saved");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "Error during saving userdata!");
            Utils.debug(Utils.prefix() + "&c" + e.getMessage());
        }
        configmanager.saveConfiguration(database,"%datafolder%/database.yml");
    }
}
