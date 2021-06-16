package me.kr1s_d.ultimateantibot.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class utils {

    public static void sendActionBar(ProxiedPlayer p, String msg){
        p.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(utils.colora(msg)));
    }

    public static String colora(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void debug(String a){
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(utils.colora(a)));
    }

    public static String prefix(){
        return "&6&lA&E&LB &8» &7";
    }
}
