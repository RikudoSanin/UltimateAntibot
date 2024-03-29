package me.kr1s_d.ultimateantibot.bungee.event.custom;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.user.UserData;
import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.commons.helper.ComponentBuilder;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
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
    private boolean cancelled;
    private final UserData userData;


    public ModeEnableEvent(UltimateAntibotWaterfall plugin, ModeType modeType){
        this.antibotManager = plugin.getAntibotManager();
        this.counter = plugin.getCounter();
        this.modeType = modeType;
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
            p.disconnect(ComponentBuilder.buildShortComponent(Utils.colora(MessageManager.getSafeModeMsg())));
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

