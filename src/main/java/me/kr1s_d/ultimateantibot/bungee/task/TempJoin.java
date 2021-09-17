package me.kr1s_d.ultimateantibot.bungee.task;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class TempJoin {
    private final UltimateAntibotWaterfall plugin;
    private final ProxiedPlayer player;
    private final Counter counter;

    public TempJoin(UltimateAntibotWaterfall plugin, ProxiedPlayer player){
        this.plugin = plugin;
        this.player = player;
        this.counter = plugin.getCounter();
    }

    public void clear(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(player.getPing() != 0) {
                    counter.getJoined().remove(player);
                }
            }
        }, 30, TimeUnit.SECONDS);
    }

}
