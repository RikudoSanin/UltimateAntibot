package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.task.TimedWhitelist;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.helper.ComponentBuilder;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class AuthCheck {
    private final UltimateAntibotWaterfall plugin;
    private final AntibotManager antibotManager;
    private final List<String> pendingChecks;
    private final List<String> completedChecksWaiting;
    private final List<String> pingCheckCompleted;
    private final ConfigManager configManager;
    private final Map<String, Integer> pingMap;
    private final Map<String, Integer> requiredPing;

    public AuthCheck(UltimateAntibotWaterfall plugin){
        this.plugin = plugin;
        this.antibotManager = plugin.getAntibotManager();
        this.pendingChecks = new ArrayList<>();
        this.completedChecksWaiting = new ArrayList<>();
        this.pingCheckCompleted = new ArrayList<>();
        this.configManager = plugin.getConfigManager();
        this.pingMap = new HashMap<>();
        this.requiredPing = new HashMap<>();
        loadTask();
    }

    /**
     * Controlla se pendingcheck non contiene
     * l'ip, in tal caso inizia la verifica
     */
    public void startCountDown(String ip, int waitTime){
        if(!pendingChecks.contains(ip)){
            pendingChecks.add(ip);
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                completedChecksWaiting.add(ip);
                betweenCountDown(ip);
            }, waitTime, TimeUnit.SECONDS);
        }
    }

    /**
     * controlla se l'ip è pronto per entrare
     */

    public boolean isWaitingResponse(String ip){
        return completedChecksWaiting.contains(ip);
    }

    /**
     * Controlla se l'ip è in attesa di verifica
     */

    public boolean isPending(String ip){
        return pendingChecks.contains(ip);
    }

    public void betweenCountDown(String ip){
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            completedChecksWaiting.remove(ip);
            pendingChecks.remove(ip);
        }, configManager.getAuth_between(), TimeUnit.MILLISECONDS);
    }

    /**
     * Controlla se il ping check è stato eseguito
     */

    public boolean hasCompletedPingCheck(String ip){
        return pingCheckCompleted.contains(ip);
    }

    public void reset(String ip){
        pendingChecks.remove(ip);
        completedChecksWaiting.remove(ip);
        pingCheckCompleted.remove(ip);
        pingMap.remove(ip);
        requiredPing.remove(ip);
    }
    /**
     * Controlla se il player quando si connette ha
     * Esattamente quel numero di ping, in questo caso
     * lo aggiunge nella lista dei completati
     * altrimenti lo resetta
     */

    public void checkForJoin(PreLoginEvent e, String ip){
        if(isWaitingResponse(ip)){
            antibotManager.addWhitelist(ip);
            new TimedWhitelist(plugin).check(ip);
            reset(ip);
            return;
        }
        if(hasCompletedPingCheck(ip)){
            int check_timer = ThreadLocalRandom.current().nextInt(configManager.getAuth_TimerMin_Max()[0], configManager.getAuth_TimerMin_Max()[1]);
            if(!pingMap.get(ip).equals(requiredPing.get(ip))){
                reset(ip);
            }
            startCountDown(ip, check_timer);
            e.setCancelReason(ComponentBuilder.buildShortComponent(Utils.colora(MessageManager.getTimer_msg(String.valueOf(check_timer + 1)))));
            e.setCancelled(true);
        }else{
            int check_ping = ThreadLocalRandom.current().nextInt(configManager.getAuth_PingMin_Max()[0], configManager.getAuth_PingMin_Max()[1]);
            reset(ip);
            startPing(ip, check_ping);
            e.setCancelReason(ComponentBuilder.buildShortComponent(Utils.colora(
                    MessageManager.getPing_msg(String.valueOf(check_ping))
            )));
            e.setCancelled(true);
        }
    }

    /**
     * Controlla se le mappe contengono i ping precisi del giocatore
     * quando si connette al server, in questo caso lo aggiunge alla
     * lista di coloro che hanno completato il check
     * altrimenti resetta le mappe per l'ip interesssato
     */

    public void startPing(String ip, int times){
        if(pingMap.containsKey(ip) && requiredPing.containsKey(ip) && !pingMap.get(ip).equals(requiredPing.get(ip))){
            reset(ip);
            return;
        }
        if(pingMap.containsKey(ip) && requiredPing.containsKey(ip) && pingMap.get(ip).equals(requiredPing.get(ip))){
            pingCheckCompleted.add(ip);
        }else{
            pingMap.put(ip, 0);
            requiredPing.put(ip, times);
            pingCheckCompleted.remove(ip);
        }
    }

    /**
     * Regista i ping dei giocatori
     */

    public void onPing(ProxyPingEvent e, String ip){
        if(pingMap.containsKey(ip)){
            pingMap.put(ip, pingMap.get(ip) + 1);
            if(antibotManager.isOnline() && configManager.isAuth_ping_interface()){
                ServerPing ping = e.getResponse();
                ping.getVersion().setProtocol(0);
                if(pingMap.get(ip).equals(requiredPing.get(ip))){
                    ping.getVersion().setName(Utils.colora(MessageManager.getOnping_ready()));
                }else{
                    ping.getVersion().setName(Utils.colora(
                            MessageManager.getOnping_normal(
                                    String.valueOf(pingMap.get(ip)),
                                    String.valueOf(requiredPing.get(ip))
                            )
                    ));
                }
            }
            if(pingMap.get(ip).equals(requiredPing.get(ip))) {
                pingCheckCompleted.add(ip);
            }
        }
    }

    private String convertToString(List<String> stringList) {
        return String.join(System.lineSeparator(), stringList);
    }

    private void loadTask(){
        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(!antibotManager.isAntiBotModeOnline()){
                    return;
                }
                pendingChecks.clear();
                completedChecksWaiting.clear();
                pingCheckCompleted.clear();
                pingMap.clear();
                requiredPing.clear();
            }
        }, 0, configManager.getTaskManager_auth(), TimeUnit.SECONDS);
    }
}
