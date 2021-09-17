package me.kr1s_d.ultimateantibot.bungee.event;

import me.kr1s_d.ultimateantibot.bungee.checks.HandShakeCheck;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class HandShakeListener implements Listener {

    private final Counter counter;
    private final HandShakeCheck handShakeCheck;

    public HandShakeListener(UltimateAntibotWaterfall plugin){
        this.counter = plugin.getCounter();
        this.handShakeCheck = plugin.getHandShakeCheck();
    }


    @EventHandler
    public void onHandShake(PlayerHandshakeEvent e){
        counter.increaseHandShake();
        handShakeCheck.detect(e);
    }
}
