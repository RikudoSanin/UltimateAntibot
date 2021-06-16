package me.kr1s_d.ultimateantibot.Commands;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.Task.ActionBarTask;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class antibotComands extends Command {

    private final UltimateAntibotWaterfall plugin;
    private final Configuration messages;
    private final AntibotManager antibotManager;
    private final List<ProxiedPlayer> toggleplayer;

    public antibotComands(UltimateAntibotWaterfall plugin){
        super("ultimateantibot", "", "uab, ab");
        this.plugin = plugin;
        this.messages = plugin.getMessageYml();
        this.antibotManager = plugin.getAntibotManager();
        this.toggleplayer = new ArrayList<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + messages.getString("args_error"))));
            return;
        }
        switch (args[0].toLowerCase()){
            case "help":
                sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "/uab toggle")));
                sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "/uab antibotmode on/off")));
                return;

            case "antibotmode":
                if(args[1].equalsIgnoreCase("on")){
                    antibotManager.setAntibotModeStatus(true);
                    sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "AntibotMode &aon")));
                }
                if(args[1].equalsIgnoreCase("off")){
                    antibotManager.setAntibotModeStatus(false);
                    sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "AntibotMode &coff")));
                }
                return;

            case  "toggle":
                if(sender instanceof ProxiedPlayer) {
                    if (!toggleplayer.contains(sender)) {
                        new ActionBarTask(plugin, (ProxiedPlayer) sender, toggleplayer).startGorilla();
                        sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "Notification &aon")));
                        toggleplayer.add((ProxiedPlayer) sender);
                    }else{
                        sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "Notification &coff")));
                        toggleplayer.remove((ProxiedPlayer) sender);
                    }
                }else {
                    sender.sendMessage(new TextComponent(utils.colora("&CYou are not a player!")));
                }
                return;
            case "reload":
                if(sender.hasPermission("ab.admin")){
                    plugin.reload();
                    sender.sendMessage(new TextComponent(utils.colora(messages.getString("reload"))));
                }
                return;
        }
    }
}
