package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.Utils.utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TitleTask {
    private final UltimateAntibotWaterfall plugin;
    private final ProxiedPlayer player;
    private final Title title;
    private final List<ProxiedPlayer> toggledTitle;
    private final Counter counter;
    private ScheduledTask scheduledTask;
    private final AntibotManager antibotManager;
    private final Configuration message;

    public TitleTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player, List<ProxiedPlayer> toggledTitle){
        this.plugin = plugin;
        this.player = player;
        this.title = ProxyServer.getInstance().createTitle();
        this.toggledTitle = toggledTitle;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.message = plugin.getMessageYml();
    }

    public void Notification(){
        int blacklistAmount = antibotManager.getBlacklist().size();
        int queueAmount = antibotManager.getQueue().size();
        int totali = blacklistAmount + queueAmount;
        int percentualeBlacklistata = 0;
        if(blacklistAmount != 0 && totali != 0) {
            percentualeBlacklistata = Math.round((float) blacklistAmount / totali * 100);
        }
        long botTotal = counter.getTotalBot();
        title.fadeIn(0);
        title.fadeOut(0);
        title.stay(10);
        title.title((BaseComponent) new TextComponent(utils.colora(message.getString("title.title").replace("%blocked%", String.valueOf(botTotal)))));
        title.subTitle((BaseComponent) new TextComponent(utils.colora(utils.prefix() + message.getString("title.subtitle").replace("%ip%", String.valueOf(percentualeBlacklistata)))));
    }

    public void start(){
        scheduledTask  = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                Notification();
                if(!player.isConnected() || !toggledTitle.contains(player)){
                    scheduledTask.cancel();
                }
                if(plugin.getAntibotManager().isOnline() || plugin.getAntibotManager().isSafeAntiBotModeOnline()) {
                    player.sendTitle(title);
                }
            }
        },1, 50, TimeUnit.MILLISECONDS);
    }
}