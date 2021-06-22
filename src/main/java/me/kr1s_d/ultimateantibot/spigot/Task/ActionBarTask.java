package me.kr1s_d.ultimateantibot.spigot.Task;

import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ActionBarTask {
    private final UltimateAntibotSpigot plugin;
    private final Player player;
    private final Counter counter;
    private final List<Player> toggledPlayers;

    public ActionBarTask(UltimateAntibotSpigot plugin, Player player, List<Player> toggledPlayers){
        this.plugin = plugin;
        this.player = player;
        this.counter = plugin.getCounter();
        this.toggledPlayers = toggledPlayers;
    }

    public void mainData() {
        long botSec = counter.getBotSecond();
        long pingSec = counter.getPingSecond();
        long botTotal = counter.getTotalBot();
        long pingTotal = counter.getTotalPing();
        String actionbarOnAttack = Utils.colora(Utils.prefix() + plugin.getMessageYml().getString("actionbar.antibot_mode"))
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        String actionbaronSafemodeattack = Utils.colora(Utils.prefix() + plugin.getMessageYml().getString("actionbar.safe_mode"))
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        String actionbarOnSafe = Utils.colora(Utils.prefix() + plugin.getMessageYml().getString("actionbar.no-attack"))
                .replace("$1", String.valueOf(counter.getJoinPerSecond()))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getWhitelist().size()))
                .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        if(plugin.getAntibotManager().isOnline()){
                Utils.sendActionbar(player, actionbarOnAttack);
        }else {
            if(plugin.getAntibotManager().isSafeAntiBotModeOnline() || plugin.getAntibotManager().isPingModeOnline()){
                Utils.sendActionbar(player, actionbaronSafemodeattack);
            }else {
                Utils.sendActionbar(player, actionbarOnSafe);
            }
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
        }.runTaskTimer(plugin,0, 1L);
    }
}
