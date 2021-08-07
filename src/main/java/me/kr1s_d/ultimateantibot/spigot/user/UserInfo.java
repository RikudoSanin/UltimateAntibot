package me.kr1s_d.ultimateantibot.spigot.user;

import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.util.Map;

public class UserInfo {
    private final Config database;
    private final Config config;
    private final AntibotManager antibotManager;
    private final Config messages;
    private final UserData userData;

    public UserInfo(UltimateAntibotSpigot plugin) {
        this.database = plugin.getDatabase();
        this.config = plugin.getConfigYml();
        this.antibotManager = plugin.getAntibotManager();
        this.messages = plugin.getMessageYml();
        this.userData = plugin.getUserData();
    }

    /**
     * Metodi del First Join
     */
    public boolean checkFirstJoin(AsyncPlayerPreLoginEvent e, String ip){
        if(config.getBoolean("checks.first_join.enabled")) {
            if (!antibotManager.isOnline() && userData.isFirstJoin(ip)) {
                antibotManager.removeWhitelist(ip);
                antibotManager.removeBlackList(ip);
                userData.setFirstJoin(ip, false);
                e.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, (Utils.colora(Utils.convertToString(messages.getStringList("first_join")))));
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
