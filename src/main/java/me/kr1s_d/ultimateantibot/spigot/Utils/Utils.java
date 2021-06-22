package me.kr1s_d.ultimateantibot.spigot.Utils;

import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut){
            player.sendTitle(colora(title), colora(subtitle), fadeIn, stay, fadeOut);
    }

    public static String prefix(){
        return "&6&lA&E&LB&r &8» &7";
    }

    public static void debug(String str){
        Bukkit.getConsoleSender().sendMessage(colora(str));
    }

    public static void sendActionbar(Player player, String message) {
        if (player == null || message == null)
            return;
        String nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
        nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);
        if (!nmsVersion.startsWith("v1_9_R") && !nmsVersion.startsWith("v1_8_R")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colora(message)));
            return;
        }
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Class<?> ppoc = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
            Class<?> packet = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
            Class<?> chat = Class.forName("net.minecraft.server." + nmsVersion + (nmsVersion.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
            Class<?> chatBaseComponent = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
            Method method = null;
            if (nmsVersion.equalsIgnoreCase("v1_8_R1"))
                method = chat.getDeclaredMethod("a", String.class);
            Object object = nmsVersion.equalsIgnoreCase("v1_8_R1") ? chatBaseComponent.cast(method.invoke(chat, "{'text': '" + message + "'}")) : chat.getConstructor(new Class[] { String.class }).newInstance(message);
            Object packetPlayOutChat = ppoc.getConstructor(new Class[] { chatBaseComponent, byte.class }).newInstance(object, (byte) 2);
            Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            Object iCraftPlayer = handle.invoke(craftPlayer);
            Field playerConnectionField = iCraftPlayer.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(iCraftPlayer);
            Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", packet);
            sendPacket.invoke(playerConnection, packetPlayOutChat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String colora(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
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
    public static Integer getServerVersion() {
        String versionStr = JavaPlugin.getPlugin(UltimateAntibotSpigot.class).getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (versionStr == null)
            return 0;
        if (versionStr.startsWith("v1_7"))
            return 17;
        if (versionStr.startsWith("v1_8"))
            return 18;
        if (versionStr.startsWith("v1_9"))
            return 19;
        if (versionStr.startsWith("v1_10"))
            return 110;
        if (versionStr.startsWith("v1_11"))
            return 111;
        if (versionStr.startsWith("v1_12"))
            return 112;
        if (versionStr.startsWith("v1_13"))
            return 113;
        if (versionStr.startsWith("v1_14"))
            return 114;
        if (versionStr.startsWith("v1_15"))
            return 115;
        if (versionStr.startsWith("v1_16"))
            return 116;
        if (versionStr.startsWith("v1_17"))
            return 117;
        return 0;
    }
    public static String getIP(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        if(JavaPlugin.getPlugin(UltimateAntibotSpigot.class).getConfigYml().getBoolean("debug")){
            debug(prefix() + "Detected IP: " + ip);
        }
        return ip;
    }
}
