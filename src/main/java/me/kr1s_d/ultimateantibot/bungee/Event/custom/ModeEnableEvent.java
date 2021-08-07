package me.kr1s_d.ultimateantibot.bungee.Event.custom;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.user.UserData;
import me.kr1s_d.ultimateantibot.bungee.user.UserInfo;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class ModeEnableEvent extends Event implements Cancellable {

    private final AntibotManager antibotManager;
    private final Counter counter;
    private final ModeType modeType;
    private final Configuration messages;
    private boolean cancelled;
    private final UserData userData;


    public ModeEnableEvent(UltimateAntibotWaterfall plugin, ModeType modeType){
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.modeType = modeType;
        this.messages = plugin.getMessageYml();
        this.cancelled = false;
        this.userData = plugin.getUserData();
    }

    public AntibotManager getAntibotManager() {
        return antibotManager;
    }

    public Counter getCounter() {
        return counter;
    }

    public ModeType getEnabledMode() {
        return modeType;
    }

    public List<ProxiedPlayer> getJoinedPlayers(){
        return counter.getJoined();
    }

    public void disconnectBots(){
        for(ProxiedPlayer p : counter.getJoined()){
            if(antibotManager.getWhitelist().contains(Utils.getIP(p))){
                return;
            }
            userData.setFirstJoin(Utils.getIP(p), true);
            p.disconnect(new TextComponent(Utils.convertToString(Utils.coloralista(Utils.coloraListaConReplaceUnaVolta(messages.getStringList("safe_mode"), "$1", "3")))));
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        antibotManager.setAntibotModeStatus(false);
    }
}

