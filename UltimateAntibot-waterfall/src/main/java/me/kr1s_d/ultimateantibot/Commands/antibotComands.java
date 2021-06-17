package me.kr1s_d.ultimateantibot.Commands;

import me.kr1s_d.ultimateantibot.AntibotManager;
import me.kr1s_d.ultimateantibot.Task.ActionBarTask;
import me.kr1s_d.ultimateantibot.Task.TitleTask;
import me.kr1s_d.ultimateantibot.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.Utils.Counter;
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
    private final List<ProxiedPlayer> toggleplayeractionbar;
    private final List<ProxiedPlayer> toggledplayertitle;
    private final Counter counter;

    public antibotComands(UltimateAntibotWaterfall plugin){
        super("ultimateantibot", "", "uab");
        this.plugin = plugin;
        this.messages = plugin.getMessageYml();
        this.antibotManager = plugin.getAntibotManager();
        this.toggleplayeractionbar = new ArrayList<>();
        this.toggledplayertitle = new ArrayList<>();
        this.counter = plugin.getCounter();
    }

    /**
     *
     * /uab toggle actionbar/title
     * /uab help
     * /uab reload
     * /uab antibotmode on/off
     * /uab clearwhitelist
     * /uab clearblacklist
     * /uab addblacklist <ip>
     * /uab removeblacklist <ip>
     * /uab addwhitelist <ip>
     * /uab removewhitelist <ip>
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + messages.getString("args_error"))));
            return;
        }
        switch (args[0].toLowerCase()){
            case "help":
                sender.sendMessage(new TextComponent("§8§l§n___________________________________________"));
                sender.sendMessage(new TextComponent(""));
                sender.sendMessage(new TextComponent("§f§lRunning §4§lUltimate§c§lAnti§f§lBot §r§7- V" + plugin.getDescription().getVersion()));
                for (String msg : messages.getStringList("help")) {
                    sender.sendMessage(new TextComponent(utils.colora(msg)));
                }
                sender.sendMessage(new TextComponent("§8§l§n___________________________________________"));
                return;

            case "antibotmode":
                if(sender.hasPermission("ab.antibotmode") || sender.hasPermission("ab.admin")) {
                    if (args[1].equalsIgnoreCase("on")) {
                        antibotManager.setAntibotModeStatus(true);
                        sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "AntibotMode &aon")));
                    }
                    if (args[1].equalsIgnoreCase("off")) {
                        antibotManager.setAntibotModeStatus(false);
                        sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "AntibotMode &coff")));
                    }
                }
                return;

            case "toggle":
                if(sender.hasPermission("ab.toggle") || sender.hasPermission("ab.admin")){
                switch (args[1].toLowerCase()) {
                    case "actionbar":
                        if (sender instanceof ProxiedPlayer) {
                            if (!toggleplayeractionbar.contains(sender)) {
                                new ActionBarTask(plugin, (ProxiedPlayer) sender, toggleplayeractionbar).startGorilla();
                                sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "Actionbar notification &aon")));
                                toggleplayeractionbar.add((ProxiedPlayer) sender);
                            } else {
                                sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "Actionbar notification &coff")));
                                toggleplayeractionbar.remove((ProxiedPlayer) sender);
                            }
                        } else {
                            sender.sendMessage(new TextComponent(utils.colora("&CYou are not a player!")));
                        }
                        return;
                    case "title":
                        if (sender instanceof ProxiedPlayer) {
                            if (!toggledplayertitle.contains(sender)) {
                                new TitleTask(plugin, (ProxiedPlayer) sender, toggledplayertitle).start();
                                toggledplayertitle.add((ProxiedPlayer) sender);
                                sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "Title notification &aon")));
                            } else {
                                sender.sendMessage(new TextComponent(utils.colora(utils.prefix() + "Title notification &coff")));
                                toggledplayertitle.remove((ProxiedPlayer) sender);
                            }
                        } else {
                            sender.sendMessage(new TextComponent(utils.colora("&CYou are not a player!")));
                        }
                        return;
                    default:
                        sender.sendMessage(new TextComponent("§8§l§n___________________________________________"));
                        sender.sendMessage(new TextComponent(""));
                        sender.sendMessage(new TextComponent("§f§lRunning §4§lUltimate§c§lAnti§f§lBot §r§7- V" + plugin.getDescription().getVersion()));
                        for (String msg : messages.getStringList("help")) {
                            sender.sendMessage(new TextComponent(utils.colora(msg)));
                        }
                        sender.sendMessage(new TextComponent("§8§l§n___________________________________________"));
                    }
                }
                return;
            case "clearwhitelist":
                if(sender.hasPermission("ab.clear") || sender.hasPermission("ab.admin")){
                    antibotManager.getWhitelist().clear();
                }
                    return;
            case "clearblacklist":
                if(sender.hasPermission("ab.clear") || sender.hasPermission("ab.admin")){
                    antibotManager.getBlacklist().clear();
                }
                return;
            case "addwhitelist":
                if(sender.hasPermission("ab.addwhitelist") || sender.hasPermission("ab.admin")){
                    antibotManager.addWhitelist(args[1]);
                }
            case "addblacklist":
                if(sender.hasPermission("ab.addblacklist") || sender.hasPermission("ab.admin")){
                    antibotManager.addBlackList(args[1]);
                }
                return;
            case "removewhitelist":
                if(sender.hasPermission("ab.removewhitelist") || sender.hasPermission("ab.admin")){
                    antibotManager.removeWhitelist(args[1]);
                }
                return;
            case "removeblacklist":
                if(sender.hasPermission("ab.removeblacklist") || sender.hasPermission("ab.admin")){
                    antibotManager.removeBlackList(args[1]);
                }
            case "stats":
                if(sender.hasPermission("ab.stats") || sender.hasPermission("ab.admin")){
                    sender.sendMessage(new TextComponent("§8§l§n___________________________________________"));
                    sender.sendMessage(new TextComponent(""));
                    sender.sendMessage(new TextComponent("§f§lRunning §4§lUltimate§c§lAnti§f§lBot §r§7- V" + plugin.getDescription().getVersion()));
                    for (String msg : messages.getStringList("stats")) {
                        sender.sendMessage(new TextComponent(utils.colora(msg)
                                .replace("$1", String.valueOf(counter.getBotSecond()))
                                .replace("$2", String.valueOf(counter.getPingSecond()))
                                .replace("$3", String.valueOf(antibotManager.getQueue().size()))
                                .replace("$4", String.valueOf(counter.getJoined().size()))
                                .replace("$5", String.valueOf(counter.getFirstjoin().size()))
                                .replace("$6", String.valueOf(antibotManager.getWhitelist().size()))
                                .replace("$7", String.valueOf(antibotManager.getBlacklist().size()))
                                .replace("$8", String.valueOf(counter.getTotalBot()))
                                .replace("$9", String.valueOf(counter.getTotalPing()))
                        ));
                    }
                    sender.sendMessage(new TextComponent("§8§l§n___________________________________________"));
                }
                return;
            case "reload":
                if(sender.hasPermission("ab.admin") || sender.hasPermission("ab.admin")){
                    plugin.reload();
                    sender.sendMessage(new TextComponent(utils.colora(messages.getString("reload"))));
                }
        }
    }
}
