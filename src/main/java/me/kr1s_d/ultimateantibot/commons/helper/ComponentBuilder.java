package me.kr1s_d.ultimateantibot.commons.helper;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class ComponentBuilder {

    public static BaseComponent buildShortComponent(String str){
        return new TextComponent(str);
    }

    public static BaseComponent buildLongComponent(List<String> str){
        return new TextComponent(convertToString(str));
    }

    private static String convertToString(List<String> stringList) {
        return String.join(System.lineSeparator(), stringList);
    }
}
