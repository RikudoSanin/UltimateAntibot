package me.kr1s_d.ultimateantibot.spigot.checks;

import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import org.bukkit.entity.Player;


import java.util.*;

public class SlowJoinCheck {
    private final AntibotManager antibotManager;
    private final Map<String, Set<Player>> maxAccountIp;
    private final ConfigManager configManager;

    public SlowJoinCheck(UltimateAntibotSpigot plugin){
        this.antibotManager = plugin.getAntibotManager();
        this.maxAccountIp = new HashMap<>();
        this.configManager = plugin.getConfigManager();
    }

    /**
     * Chek Online Aomount
     */

    public Set<Player> getOnlineAccountAmount(String ip){
        return maxAccountIp.getOrDefault(ip, new HashSet<>());
    }

    public void resetAccounts(String ip){
        maxAccountIp.remove(ip);
    }

    public void maxAccountCheck(String ip, Player player) {
        if(configManager.isSlowMode_enabled()) {
            Set<Player> newList = getOnlineAccountAmount(ip);
            newList.add(player);
            maxAccountIp.put(ip, newList);
            if (getOnlineAccountAmount(ip).size() > configManager.getSlowMode_limit()) {
                resetAccounts(ip);
                antibotManager.enableAntibotMode();
                Utils.disconnectPlayerFromIp(ip, Utils.colora(MessageManager.getSafeModeMsg()));
                if (configManager.isSlowMode_blacklist_limit()) {
                    antibotManager.addBlackList(ip);
                    antibotManager.removeWhitelist(ip);
                }
            }
        }
    }

    public void removeFromOnline(String ip, Player p){
        getOnlineAccountAmount(ip).remove(p);
    }

}
