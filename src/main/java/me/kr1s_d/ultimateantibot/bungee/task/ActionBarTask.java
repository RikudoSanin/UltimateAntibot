package me.kr1s_d.ultimateantibot.bungee.task;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Counter;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.commons.message.MessageManager;
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
    private final AntibotInfo antibotInfo;

    public ActionBarTask(UltimateAntibotWaterfall plugin, ProxiedPlayer player, List<ProxiedPlayer> toggledPlayers){
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
        String actionbarOnAttack = Utils.colora(Utils.prefix() + MessageManager.getConsole_on_attack())
                .replace("$1", String.valueOf(botSec))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                .replace("$5", String.valueOf(checkSec))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        String actionbarOnSafe = Utils.colora(Utils.prefix() + MessageManager.getActionbar_no_attack())
                .replace("$1", String.valueOf(counter.getJoinPerSecond()))
                .replace("$2", String.valueOf(pingSec))
                .replace("$3", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                .replace("$4", String.valueOf(plugin.getAntibotManager().getWhitelist().size()))
                .replace("$5", String.valueOf(checkSec))
                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                ;
        if(plugin.getAntibotManager().isOnline() || plugin.getAntibotManager().isPingModeOnline()){
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbarOnAttack));
        }else {
            if (plugin.getAntibotManager().isHandShakeModeOnline()) {
                player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.colora(Utils.prefix() +
                        MessageManager.getConsole_onHandShake()
                                .replace("$1", String.valueOf(antibotInfo.getHandShakeSecond()))
                                .replace("$2", String.valueOf(plugin.getAntibotManager().getQueue().size()))
                                .replace("$3", String.valueOf(plugin.getAntibotManager().getBlacklist().size()))
                                .replace("$4", String.valueOf(counter.getCheckPerSecond()))
                                .replace("%type%", String.valueOf(plugin.getAntibotManager().getModeType()))
                )));
            } else {
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
        },1, 500, TimeUnit.MILLISECONDS);
    }
}
