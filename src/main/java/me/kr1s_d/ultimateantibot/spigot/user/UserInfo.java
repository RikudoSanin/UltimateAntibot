package me.kr1s_d.ultimateantibot.spigot.user;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Map;

public class UserInfo {
    private final Config database;
    private final AntibotManager antibotManager;
    private final Config messages;
    private final UserData userData;
    private final ConfigManager configManager;

    public UserInfo(UltimateAntibotSpigot plugin) {
        this.database = plugin.getDatabase();
        this.antibotManager = plugin.getAntibotManager();
        this.messages = plugin.getMessageYml();
        this.userData = plugin.getUserData();
        this.configManager = plugin.getConfigManager();
    }

    /**
     * Metodi del First Join
     */
    public boolean checkFirstJoin(AsyncPlayerPreLoginEvent e, String ip){
        if(configManager.isFirstJoin_enabled()) {
            if (!antibotManager.isOnline() && userData.isFirstJoin(ip)) {
                antibotManager.removeWhitelist(ip);
                antibotManager.removeBlackList(ip);
                userData.setFirstJoin(ip, false);
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, (Utils.colora(Utils.convertToString(messages.getStringList("first_join")))));
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
            for(String str : database.asBukkitConfig().getConfigurationSection("data").getKeys(false)){
                String ip = deAdapt(str);
                boolean status = database.getBoolean("data." + ip + ".firstJoin");
                if (antibotManager.getWhitelist().contains(ip)) {
                    userData.getFirstJoinHashMap().put(ip, status);
                }
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
    public void save(){
        try {
            for(Map.Entry<String, Boolean> map : userData.getFirstJoinHashMap().entrySet()){
                String ip = adapt(map.getKey());
                boolean status = map.getValue();
                String path = String.format("data.%s.firstJoin", ip);
                database.asBukkitConfig().set(path, status);
            }
            Utils.debug(Utils.prefix() + "&ajoin database has been saved");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "Error during saving userdata!");
            Utils.debug(Utils.prefix() + "&c" + e.getMessage());
        }
        database.save();
    }
}
