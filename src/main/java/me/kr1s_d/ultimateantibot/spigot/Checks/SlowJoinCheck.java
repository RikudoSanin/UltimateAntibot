package me.kr1s_d.ultimateantibot.spigot.Checks;



import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;

import java.util.HashMap;
import java.util.Map;

public class SlowJoinCheck {
    private final Config config;
    private final Config message;
    private final AntibotManager antibotManager;
    private final Map<String, Integer> maxAccountIp;
    private final int accountLimit;
    private final boolean isEnabled;


    public SlowJoinCheck(UltimateAntibotSpigot plugin){
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

    public int getOnlineAccountAmount(String ip){
        return maxAccountIp.getOrDefault(ip, 0);
    }

    public void resetAccounts(String ip){
        maxAccountIp.remove(ip);
    }

    public void maxAccountCheck(String ip) {
        if(isEnabled) {
            maxAccountIp.put(ip, getOnlineAccountAmount(ip) + 1);
            if (getOnlineAccountAmount(ip) >= accountLimit) {
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


}
