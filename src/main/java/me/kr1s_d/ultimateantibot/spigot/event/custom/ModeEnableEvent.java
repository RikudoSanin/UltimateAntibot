package me.kr1s_d.ultimateantibot.spigot.event.custom;


import me.kr1s_d.ultimateantibot.commons.ModeType;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import me.kr1s_d.ultimateantibot.spigot.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ModeEnableEvent extends Event implements Cancellable {

    private final UltimateAntibotSpigot antibotSpigot;
    private final AntibotManager antibotManager;
    private final Counter counter;
    private final ModeType modeType;
    private final Config messages;
    private boolean cancelled;
    private final UserData userData;
    private static final HandlerList HANDLER = new HandlerList();


    public ModeEnableEvent(UltimateAntibotSpigot plugin, ModeType modeType){
        this.antibotSpigot = plugin;
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

    public List<Player> getJoinedPlayers(){
        return counter.getJoined();
    }

    public void disconnectBots(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : counter.getJoined()){
                    if(antibotManager.getWhitelist().contains(Utils.getIP(p))){
                        return;
                    }
                    userData.setFirstJoin(Utils.getIP(p), true);
                    p.kickPlayer(Utils.convertToString(Utils.coloralista(messages.getStringList("safe_mode"))));
                }
                counter.getJoined().clear();
            }
        }.runTaskLater(antibotSpigot, 2L);
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

    @Override
    public HandlerList getHandlers() {
        return HANDLER;
    }

    public static HandlerList getHandlerList() {
        return HANDLER;
    }
}
