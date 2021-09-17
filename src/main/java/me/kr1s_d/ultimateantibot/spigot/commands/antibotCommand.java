package me.kr1s_d.ultimateantibot.spigot.commands;


import me.kr1s_d.ultimateantibot.spigot.AntibotManager;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.task.ActionBarTask;
import me.kr1s_d.ultimateantibot.spigot.task.TitleTask;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.utils.Counter;
import me.kr1s_d.ultimateantibot.spigot.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class antibotCommand implements CommandExecutor {

    private final UltimateAntibotSpigot plugin;
    private final Config messages;
    private final AntibotManager antibotManager;
    private final List<Player> toggleplayeractionbar;
    private final List<Player> toggledplayertitle;
    private final Counter counter;

    public antibotCommand(UltimateAntibotSpigot plugin){
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage((Utils.colora(Utils.prefix() + messages.getString("args_error"))));
            return true;
        }
        switch (args[0].toLowerCase()){
            case "help":
                sender.sendMessage("§8§l§n___________________________________________");
                sender.sendMessage("");
                sender.sendMessage("§f§lRunning §4§lUltimate§c§lAnti§f§lBot §r§7- V" + plugin.getDescription().getVersion());
                for (String msg : messages.getStringList("help")) {
                    sender.sendMessage(Utils.colora(msg));
                }
                sender.sendMessage("§8§l§n___________________________________________");
                return true;

            case "antibotmode":
                if(sender.hasPermission("ab.antibotmode") || sender.hasPermission("ab.admin")) {
                    if (args[1].equalsIgnoreCase("on")) {
                        antibotManager.setAntibotModeStatus(true);
                        sender.sendMessage(Utils.colora(Utils.prefix() + "AntibotMode &aon"));
                    }
                    if (args[1].equalsIgnoreCase("off")) {
                        antibotManager.setAntibotModeStatus(false);
                        sender.sendMessage(Utils.colora(Utils.prefix() + "AntibotMode &coff"));
                    }
                }
                return true;

            case "toggle":
                if(sender.hasPermission("ab.toggle") || sender.hasPermission("ab.admin")){
                    switch (args[1].toLowerCase()) {
                        case "actionbar":
                            if (sender instanceof Player) {
                                if (!toggleplayeractionbar.contains(sender)) {
                                    new ActionBarTask(plugin, (Player) sender, toggleplayeractionbar).startGorilla();
                                    sender.sendMessage(Utils.colora(Utils.prefix() + "Actionbar notification &aon"));
                                    toggleplayeractionbar.add((Player) sender);
                                } else {
                                    sender.sendMessage(Utils.colora(Utils.prefix() + "Actionbar notification &coff"));
                                    toggleplayeractionbar.remove((Player) sender);
                                }
                            } else {
                                sender.sendMessage(Utils.colora("&CYou are not a player!"));
                            }
                            return true;
                        case "title":
                            if(Utils.getServerVersion() != 18) {
                                if (sender instanceof Player) {
                                    if (!toggledplayertitle.contains(sender)) {
                                        new TitleTask(plugin, (Player) sender, toggledplayertitle).start();
                                        toggledplayertitle.add((Player) sender);
                                        sender.sendMessage(Utils.colora(Utils.prefix() + "Title notification &aon"));
                                    } else {
                                        sender.sendMessage(Utils.colora(Utils.prefix() + "Title notification &coff"));
                                        toggledplayertitle.remove((Player) sender);
                                    }
                                } else {
                                    sender.sendMessage(Utils.colora("&CYou are not a player!"));
                                }
                            }else{
                                sender.sendMessage(Utils.colora(Utils.prefix() + "Unsupported Server Version! Title works in 1.9+"));
                            }
                            return true;
                        default:
                            sender.sendMessage("§8§l§n___________________________________________");
                            sender.sendMessage("");
                            sender.sendMessage("§f§lRunning §4§lUltimate§c§lAnti§f§lBot §r§7- V" + plugin.getDescription().getVersion());
                            for (String msg : messages.getStringList("help")) {
                                sender.sendMessage(Utils.colora(msg));
                            }
                            sender.sendMessage("§8§l§n___________________________________________");
                    }
                }
                return true;
            case "clearwhitelist":
                if(sender.hasPermission("ab.clear") || sender.hasPermission("ab.admin")){
                    antibotManager.getWhitelist().clear();
                }
                return true;
            case "clearblacklist":
                if(sender.hasPermission("ab.clear") || sender.hasPermission("ab.admin")){
                    antibotManager.getBlacklist().clear();
                }
                return true;
            case "addwhitelist":
                if(sender.hasPermission("ab.addwhitelist") || sender.hasPermission("ab.admin")){
                    antibotManager.addWhitelist(args[1]);
                }
                return true;
            case "addblacklist":
                if(sender.hasPermission("ab.addblacklist") || sender.hasPermission("ab.admin")){
                    antibotManager.addBlackList(args[1]);
                }
                return true;
            case "removewhitelist":
                if(sender.hasPermission("ab.removewhitelist") || sender.hasPermission("ab.admin")){
                    antibotManager.removeWhitelist(args[1]);
                }
                return true;
            case "removeblacklist":
                if(sender.hasPermission("ab.removeblacklist") || sender.hasPermission("ab.admin")){
                    antibotManager.removeBlackList(args[1]);
                }
                return true;
            case "stats":
                if(sender.hasPermission("ab.stats") || sender.hasPermission("ab.admin")){
                    sender.sendMessage("§8§l§n___________________________________________");
                    sender.sendMessage("");
                    sender.sendMessage("§f§lRunning §4§lUltimate§c§lAnti§f§lBot §r§7- V" + plugin.getDescription().getVersion());
                    for (String msg : messages.getStringList("stats")) {
                        sender.sendMessage(Utils.colora(msg)
                                .replace("$1", String.valueOf(counter.getBotSecond()))
                                .replace("$2", String.valueOf(counter.getPingSecond()))
                                .replace("$3", String.valueOf(antibotManager.getQueue().size()))
                                .replace("$4", String.valueOf(counter.getJoined().size()))
                                .replace("$6", String.valueOf(antibotManager.getWhitelist().size()))
                                .replace("$7", String.valueOf(antibotManager.getBlacklist().size()))
                                .replace("$8", String.valueOf(counter.getTotalBot()))
                                .replace("$9", String.valueOf(counter.getTotalPing()))
                        );
                    }
                    sender.sendMessage("§8§l§n___________________________________________");
                }
                return true;
            case "reload":
                if(sender.hasPermission("ab.admin") || sender.hasPermission("ab.admin")){
                    plugin.reload();
                    sender.sendMessage(Utils.colora(messages.getString("reload")));
                }
                return true;
        }
        return true;
    }
}
