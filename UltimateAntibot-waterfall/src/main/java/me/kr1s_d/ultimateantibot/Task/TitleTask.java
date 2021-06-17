package me.kr1s_d.ultimateantibot.Task;

import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import me.kr1s_d.ultimateantibot.Utils.utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TitleTask {
    private final UltimateAntibotWaterfall plugin;
    private final ProxiedPlayer player;
    private final Title title;
    private final List<ProxiedPlayer> toggledTitle;
    private final Counter counter;
    private ScheduledTask scheduledTask;

    public TitleTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player, List<ProxiedPlayer> toggledTitle){
        this.plugin = plugin;
        this.player = player;
        this.title = ProxyServer.getInstance().createTitle();
        this.toggledTitle = toggledTitle;
        this.counter = plugin.getCounter();
    }

    public void Notification(){
        long botTotal = counter.getTotalBot();
        title.fadeIn(0);
        title.fadeOut(0);
        title.stay(10);
        title.title((BaseComponent) new TextComponent(utils.colora("&a" + botTotal + " Blocked Connections")));
        title.subTitle((BaseComponent) new TextComponent(utils.colora(utils.prefix() + "&6Mitigation in progress")));
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
        },1, 1, TimeUnit.MILLISECONDS);
    }
}
