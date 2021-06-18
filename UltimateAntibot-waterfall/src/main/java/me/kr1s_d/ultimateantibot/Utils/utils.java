package me.kr1s_d.ultimateantibot.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return "&6&lA&E&LB&r &8» &7";
    }
    public static List<String> coloralista(List<String> a){
        List<String> ready =  new ArrayList<>();
        for(String s : a){
            ready.add(utils.colora(s));
        }
        return ready;
    }

    public static List<String> coloraListaConReplaceUnaVolta(List<String> lista, String variable, String replacement){
        List<String> ready =  new ArrayList<>();
        for(String s : lista){
            ready.add(utils.colora(s).replace(variable, replacement));
        }
        return ready;
    }

    public static List<String> coloraListaConReplaceDueVolte(List<String> lista, String variable, String replacement, String variable2, String replace2){
        List<String> ready =  new ArrayList<>();
        for(String s : lista){
            ready.add(utils.colora(s).replace(variable, replacement).replace(variable2, replace2));
        }
        return ready;
    }
}
