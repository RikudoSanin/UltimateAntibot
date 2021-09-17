package me.kr1s_d.ultimateantibot.spigot.task;

import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import me.kr1s_d.ultimateantibot.spigot.data.AntibotInfo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ActionBarTask {
    private final UltimateAntibotSpigot plugin;
    private final Player player;
    private final Counter counter;
    private final List<Player> toggledPlayers;
    private final AntibotInfo antibotInfo;

    public ActionBarTask(UltimateAntibotSpigot plugin, Player player, List<Player> toggledPlayers){
        this.plugin = plugin;
        this.player = player;
        this.counter = plugin.getCounter();
        this.toggledPlayers = toggledPlayers;
        this.antibotInfo = plugin.getAntibotInfo();
    }

    public void mainData() {
        long botSec = antibotInfo.getBotSecond();
        long pingSec = antibotInfo.getPingSecond();
        long checkSec = antibotInfo.getCheckSecond();
        long botTotal = counter.getTotalBot();
        long pingTotal = counter.getTotalPing();
        String actionbarOnAttack = Utils.colora(Utils.prefix() + plugin.getMessageYml().getString("actionbar.antibot_mode"))
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                .replace("$5", String.valueOf(checkSec))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        String actionbarOnSafe = Utils.colora(Utils.prefix() + plugin.getMessageYml().getString("actionbar.no-attack"))
                .replace("$1", String.valueOf(counter.getJoinPerSecond()))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getWhitelist().size()))
                .replace("$5", String.valueOf(checkSec))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        if(plugin.getAntibotManager().isOnline()){
                Utils.sendActionbar(player, actionbarOnAttack);
        }else {
            Utils.sendActionbar(player, actionbarOnSafe);
        }
    }

    public void startGorilla(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!toggledPlayers.contains(player) || !player.isOnline()){
                    this.cancel();
                }
                mainData();
            }
        }.runTaskTimer(plugin,0, 3L);
    }
}
