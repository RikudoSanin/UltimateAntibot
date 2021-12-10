package me.kr1s_d.ultimateantibot.spigot.checks;


import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.task.TimedWhitelist;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AuthCheck {
    private final UltimateAntibotSpigot plugin;
    private final AntibotManager antibotManager;
    private final List<String> pendingChecks;
    private final List<String> completedChecksWaiting;
    private final List<String> pingCheckCompleted;
    private final ConfigManager configManager;
    private final Map<String, Integer> pingMap;
    private final Map<String, Integer> requiredPing;

    public AuthCheck(UltimateAntibotSpigot plugin){
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
            new BukkitRunnable() {
                @Override
                public void run() {
                    completedChecksWaiting.add(ip);
                    betweenCountDown(ip);
                }
            }.runTaskLater(plugin, waitTime * 20L);
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
        new BukkitRunnable() {
            @Override
            public void run() {
                completedChecksWaiting.remove(ip);
                pendingChecks.remove(ip);
            }
        }.runTaskLater(plugin, configManager.getAuth_between() * 20L / 1000);
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

    public void checkForJoin(AsyncPlayerPreLoginEvent e, String ip){
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
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Utils.colora(MessageManager.getTimer_msg(String.valueOf(check_timer + 1))));
        }else{
            int check_ping = ThreadLocalRandom.current().nextInt(configManager.getAuth_PingMin_Max()[0], configManager.getAuth_PingMin_Max()[1]);
            reset(ip);
            startPing(ip, check_ping);
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Utils.colora(MessageManager.getPing_msg(String.valueOf(check_ping))));
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

    public void onPing(ServerListPingEvent e, String ip){
        if(pingMap.containsKey(ip)){
            pingMap.put(ip, pingMap.get(ip) + 1);
            if(antibotManager.isOnline() && configManager.isAuth_ping_interface()){
                if(pingMap.get(ip).equals(requiredPing.get(ip))){
                    e.setMotd(Utils.colora(MessageManager.getOnping_ready()));
                }else{
                    e.setMotd(Utils.colora(MessageManager.getOnping_normal(String.valueOf(pingMap.get(ip)), String.valueOf(requiredPing.get(ip)))));
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
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!antibotManager.isOnline()) {
                    return;
                }
                pendingChecks.clear();
                completedChecksWaiting.clear();
                pingCheckCompleted.clear();
                pingMap.clear();
                requiredPing.clear();
            }
        }.runTaskTimer(plugin, 0, configManager.getTaskManager_auth() * 20L);
    }
}
