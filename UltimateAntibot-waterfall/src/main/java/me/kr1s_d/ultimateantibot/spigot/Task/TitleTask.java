package me.kr1s_d.ultimateantibot.spigot.Task;

import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.Utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TitleTask {
    private final UltimateAntibotSpigot plugin;
    private final Player player;
    private final List<Player> toggledTitle;
    private final Counter counter;
    private final AntibotManager antibotManager;
    private final Config message;

    public TitleTask(UltimateAntibotSpigot plugin, Player player, List<Player> toggledTitle){
        this.plugin = plugin;
        this.player = player;
        this.toggledTitle = toggledTitle;
        this.counter = plugin.getCounter();
        this.antibotManager = plugin.getAntibotManager();
        this.message = plugin.getMessageYml();
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
                String title = (Utils.colora(message.getString("title.title").replace("%blocked%", String.valueOf(botTotal))));
                String subtitle = (Utils.colora(Utils.prefix() + message.getString("title.subtitle").replace("%ip%", String.valueOf(percentualeBlacklistata))));
                if(!player.isOnline() || !toggledTitle.contains(player)){
                    cancel();
                }
                if(plugin.getAntibotManager().isOnline() || plugin.getAntibotManager().isSafeAntiBotModeOnline()) {
                    Utils.sendTitle(player, title, subtitle, 0, 20, 0);
                }
            }
        }.runTaskTimer(plugin, 0, 10L);
    }
}
