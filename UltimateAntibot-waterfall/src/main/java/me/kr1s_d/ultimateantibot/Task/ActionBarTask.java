package me.kr1s_d.ultimateantibot.Task;

import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
import me.kr1s_d.ultimateantibot.Utils.utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActionBarTask {

    private final UltimateAntibotWaterfall plugin;
    private final ProxiedPlayer player;
    private final Counter counter;
    private final List<ProxiedPlayer> toggledPlayers;
    private ScheduledTask scheduledTask;
    private final Title title;

    public ActionBarTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player, List<ProxiedPlayer> toggledPlayers){
        this.plugin = plugin;
        this.player = player;
        this.counter = plugin.getCounter();
        this.toggledPlayers = toggledPlayers;
        this.title = ProxyServer.getInstance().createTitle();
    }

    public void mainData() {
        long botSec = counter.getBotSecond();
        long pingSec = counter.getPingSecond();
        long botTotal = counter.getTotalBot();
        long pingTotal = counter.getTotalPing();
        title.fadeIn(0);
        title.fadeOut(0);
        title.stay(10);
        title.title((BaseComponent) new TextComponent(utils.colora("&a" + botTotal + " Blocked Connections")));
        title.subTitle((BaseComponent) new TextComponent(utils.colora(utils.prefix() + "&6Mitigation in progress")));
        String actionbarOnAttack = utils.colora(utils.prefix() + "&6Bot &e$1/sec &b- &6Ping &e$2/sec &b- &6Queue &e$3 &b- &6Blacklist &e$4 » &a&lONLINE")
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                ;
        String actionbarOnSafe = utils.colora(utils.prefix() + "&6Join &e$1 &b- &6Ping &e$2 &b- &6Queue &e$3 - &6Whitelist &e$4 » &c&lOFFLINE")
                .replace("$1", String.valueOf(counter.getJoinPerSecond()))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getWhitelist().size()))
                ;
        if(plugin.getAntibotManager().isOnline()){
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbarOnAttack));
        }else {
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbarOnSafe));
        }
    }

    public void startGorilla(){
        scheduledTask  = ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(!toggledPlayers.contains(player) || !player.isConnected()){
                    scheduledTask.cancel();
                }
                mainData();
                if(plugin.getAntibotManager().isOnline()){
                    player.sendTitle(title);
                }

            }
        },1, 1, TimeUnit.MILLISECONDS);
    }

    private long media(List<Long> marks) {
        long sum = 0;
        for (int i=0; i< marks.size(); i++) {
            sum += i;
        }
        return sum / marks.size();
    }
}
