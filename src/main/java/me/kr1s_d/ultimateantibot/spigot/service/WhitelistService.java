package me.kr1s_d.ultimateantibot.spigot.service;


import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;

public class WhitelistService {
    private final AntibotManager antibotManager;
    private final Config whitelist;


    public WhitelistService(UltimateAntibotSpigot plugin){
        this.antibotManager = plugin.getAntibotManager();
        this.whitelist = plugin.getWhitelist();
    }

    public void loadWhitelist(){
        try {
            for(String ip : whitelist.getStringList("data")){
                antibotManager.addWhitelist(ip);
            }
            Utils.debug(Utils.prefix() + "&a" + antibotManager.getWhitelist().size() + " IPs have been loaded into the FrameWork");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "&cAn error occured while loading whitelist!");
            Utils.debug(Utils.prefix() + "&bInfo " + e.getMessage());
        }
    }

    public void saveWhitelist(){
        try {
            whitelist.asBukkitConfig().set("data", antibotManager.getWhitelist());
            whitelist.save();
            Utils.debug(Utils.prefix() + "&aWhitelist saved...");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "&cAn error occured while saving whitelist!");
            Utils.debug(Utils.prefix() + "&bInfo " + e.getMessage());
        }
    }
}
