package me.kr1s_d.ultimateantibot.spigot.task;



import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Counter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class TempJoin {
    private final UltimateAntibotSpigot plugin;
    private final Player player;
    private final Counter counter;

    public TempJoin(UltimateAntibotSpigot plugin, Player player){
        this.plugin = plugin;
        this.player = player;
        this.counter = plugin.getCounter();
    }

    public void clear(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    counter.getJoined().remove(player);
                }
            }
        }.runTaskLater(plugin, 30 * 20L);
    }
}
