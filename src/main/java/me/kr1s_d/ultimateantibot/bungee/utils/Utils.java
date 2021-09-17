package me.kr1s_d.ultimateantibot.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void sendActionBar(ProxiedPlayer p, String msg){
        p.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colora(msg)));
    }

    public static String colora(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void debug(String a){
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(colora(a)));
    }

    public static String prefix(){
        return "&6&lA&E&LB&r &8» &7";
    }

    public static List<String> coloralista(List<String> a){
        List<String> ready =  new ArrayList<>();
        for(String s : a){
            ready.add(colora(s));
        }
        return ready;
    }

    public static List<String> coloraListaConReplaceUnaVolta(List<String> lista, String variable, String replacement){
        List<String> ready =  new ArrayList<>();
        for(String s : lista){
            ready.add(colora(s).replace(variable, replacement));
        }
        return ready;
    }

    public static List<String> coloraListaConReplaceDueVolte(List<String> lista, String variable, String replacement, String variable2, String replace2){
        List<String> ready =  new ArrayList<>();
        for(String s : lista){
            ready.add(colora(s).replace(variable, replacement).replace(variable2, replace2));
        }
        return ready;
    }

    public static String getIP(ProxiedPlayer player){
        return player.getAddress().getAddress().toString();
    }

    public static String getIP(PendingConnection connection){
        return connection.getAddress().getAddress().toString();
    }

    public static String convertToString(List<String> stringList) {
        return String.join(System.lineSeparator(), stringList);
    }

    public static void disconnectPlayerFromIp(String ip, List<String> reason){
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            if(getIP(p).equalsIgnoreCase(ip)){
                p.disconnect(new TextComponent(colora(convertToString(reason))));
            }
        }
    }
}
