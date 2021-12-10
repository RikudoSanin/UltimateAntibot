package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SlowJoinCheck {
    private final AntibotManager antibotManager;
    private final Map<String, Set<ProxiedPlayer>> maxAccountIp;
    private final ConfigManager configManager;


    public SlowJoinCheck(UltimateAntibotWaterfall plugin){
        this.antibotManager = plugin.getAntibotManager();
        this.maxAccountIp = new HashMap<>();
        this.configManager = plugin.getConfigManager();
    }

    /**
     * Chek Online Aomount
     */

    public Set<ProxiedPlayer> getOnlineAccountAmount(String ip){
        return maxAccountIp.getOrDefault(ip, new HashSet<>());
    }

    public void resetAccounts(String ip){
        maxAccountIp.remove(ip);
    }

    public void maxAccountCheck(String ip, ProxiedPlayer player) {
        if(configManager.isSlowMode_enabled()) {
            Set<ProxiedPlayer> newList = getOnlineAccountAmount(ip);
            newList.add(player);
            maxAccountIp.put(ip, newList);
            if (getOnlineAccountAmount(ip).size() > configManager.getSlowMode_limit()) {
                resetAccounts(ip);
                antibotManager.enableAntibotMode();
                Utils.disconnectPlayerFromIp(ip, MessageManager.getAccountOnlineMsg());
                if (configManager.isSlowMode_blacklist_limit()) {
                    antibotManager.addBlackList(ip);
                    antibotManager.removeWhitelist(ip);
                }
            }
        }
    }

    public void removeFromOnline(String ip, ProxiedPlayer p){
        getOnlineAccountAmount(ip).remove(p);
    }


}
