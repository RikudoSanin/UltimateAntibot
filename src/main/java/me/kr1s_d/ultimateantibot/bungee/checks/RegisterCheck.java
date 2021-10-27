package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.elements.RegisterInfo;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RegisterCheck {

    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final ConfigManager configManager;
    private final Set<String> joined;
    private final Set<RegisterInfo> suspect;
    private final List<RegisterInfo> bots;
    private String lastPass;

    public RegisterCheck(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.configManager = plugin.getConfigManager();
        this.joined = new HashSet<>();
        this.suspect = new HashSet<>();
        this.bots  = new ArrayList<>();
        this.lastPass = "A";
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        loadTask();
    }

    public void registerPassword(String ip, String cmd){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        if(!joined.contains(ip)){
            return;
        }
        String[] paramCmd = cmd.split("\\s+");
        List<String> listenedCmd = configManager.getCmd_register();
        for(String s : listenedCmd){
            if(cmd.toLowerCase().startsWith(s)){
                RegisterInfo info = new RegisterInfo(ip, paramCmd[1]);
                suspect.add(info);
                checkRegister();
                lastPass = paramCmd[1];
                removeTask(info);
            }
        }
    }

    public void onLogin(String ip){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        joined.add(ip);
    }

    public void checkRegister(){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        for(RegisterInfo info : suspect){
            if(info.getPass().equals(lastPass)){
                bots.add(info);
            }
        }
        if(bots.size() > configManager.getSlowJoin_register_trigger()) {
            bots.forEach(b -> disconnect(b.getIp()));
            bots.clear();
        }
    }

    public void onUnLogin(String ip){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        suspect.forEach(rg -> {
            if(rg.getIp().equals(ip)){
                suspect.remove(rg);
            }
        });
    }

    public void onServerSwitch(String ip){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        suspect.forEach(rg -> {
            if(rg.getIp().equals(ip)){
                suspect.remove(rg);
            }
        });
    }

    private void disconnect(String ip){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            if(Utils.getIP(p).equalsIgnoreCase(ip)){
                if(configManager.isSlowJoin_register_kick()) {
                    p.disconnect(new TextComponent(Utils.colora(MessageManager.getSafeModeMsg())));
                }
            }
            if(configManager.isSlowJoin_register_blacklist()){
                antibotManager.blacklist(ip);
            }
            if(configManager.isSlowJoin_register_antibotmode()){
                antibotManager.enableSlowAntibotMode();
            }
            antibotManager.removeWhitelist(ip);
        }
    }

    public void clear(){
        if(!configManager.isSlowJoin_register_enabled()){
            return;
        }
        suspect.clear();
        bots.clear();
    }

    private void loadTask(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            if(!antibotManager.isAntiBotModeOnline()){
                return;
            }
            clear();
        }, 0, configManager.getTaskManager_register(), TimeUnit.SECONDS);
    }

    private void removeTask(RegisterInfo info){
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            suspect.remove(info);
            joined.remove(info.getIp());
        }, 0, configManager.getSlowJoin_register_time(), TimeUnit.SECONDS);
    }

}
