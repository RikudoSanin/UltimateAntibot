package me.kr1s_d.ultimateantibot.bungee.service;

import me.kr1s_d.ultimateantibot.bungee.Database.Config;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import net.md_5.bungee.config.Configuration;

public class WhitelistService {

    private final AntibotManager antibotManager;
    private final Configuration whitelist;


    public WhitelistService(UltimateAntibotWaterfall plugin){
        this.antibotManager = plugin.getAntibotManager();
        this.whitelist = plugin.getWhitelistYml();
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

    public void saveWhitelist(Config configmanager){
        try {
            whitelist.set("data", antibotManager.getWhitelist());
            configmanager.saveConfiguration(whitelist,"%datafolder%/whitelist.yml");
            Utils.debug(Utils.prefix() + "&aWhitelist saved...");
        }catch (Exception e){
            Utils.debug(Utils.prefix() + "&cAn error occured while saving whitelist!");
            Utils.debug(Utils.prefix() + "&bInfo " + e.getMessage());
        }
    }
}
