package me.kr1s_d.ultimateantibot.bungee.Checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SlowJoinCheck {
    private final Configuration config;
    private final Configuration message;
    private final AntibotManager antibotManager;
    private final Map<String, Set<ProxiedPlayer>> maxAccountIp;
    private final int accountLimit;
    private final boolean isEnabled;


    public SlowJoinCheck(UltimateAntibotWaterfall plugin){
        this.config = plugin.getConfigYml();
        this.message = plugin.getMessageYml();
        this.antibotManager = plugin.getAntibotManager();
        this.maxAccountIp = new HashMap<>();
        this.accountLimit = config.getInt("checks.slowmode.limit");
        this.isEnabled = config.getBoolean("checks.slowmode.enable");
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
        if(isEnabled) {
            Set<ProxiedPlayer> newList = getOnlineAccountAmount(ip);
            newList.add(player);
            maxAccountIp.put(ip, newList);
            if (getOnlineAccountAmount(ip).size() >= accountLimit) {
                resetAccounts(ip);
                antibotManager.enableAntibotMode();
                Utils.disconnectPlayerFromIp(ip, message.getStringList("account-online"));
                if (config.getBoolean("checks.slowmode.blacklist_on_limit")) {
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
