package me.kr1s_d.ultimateantibot.spigot.task;

import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TitleTask {
    private final UltimateAntibotSpigot plugin;
    private final Player player;
    private final List<Player> toggledTitle;
    private final Counter counter;
    private final AntibotManager antibotManager;

    public TitleTask(UltimateAntibotSpigot plugin, Player player, List<Player> toggledTitle){
        this.plugin = plugin;
        this.player = player;
        this.toggledTitle = toggledTitle;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
    }

    public void start(){
        new BukkitRunnable() {
            @Override
            public void run() {
                int blacklistAmount = antibotManager.getBlacklist().size();
                int queueAmount = antibotManager.getQueue().size();
                int totali = blacklistAmount + queueAmount;
                int percentualeBlacklistata = 0;
                if(blacklistAmount != 0 && totali != 0) {
                    percentualeBlacklistata = Math.round((float) blacklistAmount / totali * 100);
                }
                long botTotal = counter.getTotalBot();
                String title = (Utils.colora(MessageManager.getTitle_title().replace("%blocked%", String.valueOf(botTotal))));
                String subtitle = (Utils.colora(Utils.prefix() + MessageManager.getTitle_subtitle()).replace("%ip%", String.valueOf(percentualeBlacklistata)));
                if(!player.isOnline() || !toggledTitle.contains(player)){
                    cancel();
                }
                if(plugin.getAntibotManager().isOnline()) {
                    Utils.sendTitle(player, title, subtitle, 0, 20, 0);
                }
            }
        }.runTaskTimer(plugin, 0, 10L);
    }
}
