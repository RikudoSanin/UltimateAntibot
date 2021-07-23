package me.kr1s_d.ultimateantibot.bungee.Task;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.Utils.utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
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

    public ActionBarTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player, List<ProxiedPlayer> toggledPlayers){
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
        String actionbarOnAttack = utils.colora(utils.prefix() + plugin.getMessageYml().getString("actionbar.antibot_mode"))
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        String actionbaronSafemodeattack = utils.colora(utils.prefix() + plugin.getMessageYml().getString("actionbar.safe_mode"))
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        String actionbarOnSafe = utils.colora(utils.prefix() + plugin.getMessageYml().getString("actionbar.no-attack"))
                .replace("$1", String.valueOf(counter.getJoinPerSecond()))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getWhitelist().size()))
                .replace("$5", String.valueOf(counter.getCheckPerSecond()))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        if(plugin.getAntibotManager().isOnline()){
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbarOnAttack));
        }else {
            if(plugin.getAntibotManager().isSafeAntiBotModeOnline() || plugin.getAntibotManager().isPingModeOnline()){
                player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbaronSafemodeattack));
            }else {
                player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbarOnSafe));
            }
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

            }
        },1, 40, TimeUnit.MILLISECONDS);
    }
}
