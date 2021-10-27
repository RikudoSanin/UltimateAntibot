package me.kr1s_d.ultimateantibot.commons.message;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class MessageManager {

    private static double version;
    private static String args_error;
    private static String reload_msg;
    private static String onping_normal;
    private static String onping_ready;
    private static String title_title;
    private static String title_subtitle;
    private static String commands_perms;
    private static String commands_cleared;
    private static String commands_added;
    private static String commands_removed;
    private static String actionbar_no_attack;
    private static String actionbar_antibot_mode;
    private static String actionbar_handshake;
    private static String console_on_attack;
    private static String console_onHandShake;
    private static List<String> help_msg;
    private static List<String> stats_msg;
    private static String antibotmode_msg;
    private static String first_join_msg;
    private static String safe_mode_msg;
    private static String account_online_msg;
    private static String ping_msg;
    private static String timer_msg;
    private static String blacklisted_msg;
    private static String stuff_less;
    private static String stuff_plus;

    public static void init(UltimateAntibotSpigot plug){
        Config messages = plug.getMessageYml();
        version = messages.getDouble("version");
        args_error = messages.getString("args_error");
        reload_msg = messages.getString("reload");
        onping_normal = messages.getString("onping.normal");
        onping_ready = messages.getString("onping.ready");
        title_title = messages.getString("title.title");
        title_subtitle = messages.getString("title.subtitle");
        commands_perms = messages.getString("commands.perms");
        commands_cleared = messages.getString("commands.cleared");
        commands_added = messages.getString("commands.added");
        commands_removed = messages.getString("commands.removed");
        actionbar_no_attack = messages.getString("actionbar.no-attack");
        actionbar_antibot_mode = messages.getString("actionbar.antibot_mode");
        actionbar_handshake = messages.getString("actionbar.handshake");
        console_on_attack = messages.getString("console.on_attack");
        console_onHandShake = messages.getString("console.handshake");
        help_msg = messages.getStringList("help");
        stats_msg = messages.getStringList("stats");
        antibotmode_msg = convertToString(messages.getStringList("antibotmode"));
        first_join_msg = convertToString(messages.getStringList("first_join"));
        safe_mode_msg = convertToString(messages.getStringList("safe_mode"));
        account_online_msg = convertToString(messages.getStringList("account-online"));
        ping_msg = convertToString(messages.getStringList("ping"));
        timer_msg = convertToString(messages.getStringList("timer"));
        blacklisted_msg = convertToString(messages.getStringList("blacklisted"));
        stuff_less = convertToString(messages.getStringList("stuff.less"));
        stuff_plus = convertToString(messages.getStringList("stuff.plus"));

    }

    public static void init(UltimateAntibotWaterfall plug){
        Configuration messages = plug.getMessageYml();
        version = messages.getDouble("version");
        args_error = messages.getString("args_error");
        reload_msg = messages.getString("reload");
        onping_normal = messages.getString("onping.normal");
        onping_ready = messages.getString("onping.ready");
        title_title = messages.getString("title.title");
        title_subtitle = messages.getString("title.subtitle");
        commands_perms = messages.getString("commands.perms");
        commands_cleared = messages.getString("commands.cleared");
        commands_added = messages.getString("commands.added");
        commands_removed = messages.getString("commands.removed");
        actionbar_no_attack = messages.getString("actionbar.no-attack");
        actionbar_antibot_mode = messages.getString("actionbar.antibot_mode");
        actionbar_handshake = messages.getString("actionbar.handshake");
        console_on_attack = messages.getString("console.on_attack");
        console_onHandShake = messages.getString("console.handshake");
        help_msg = messages.getStringList("help");
        stats_msg = messages.getStringList("stats");
        antibotmode_msg = convertToString(messages.getStringList("antibotmode"));
        first_join_msg = convertToString(messages.getStringList("first_join"));
        safe_mode_msg = convertToString(messages.getStringList("safe_mode"));
        account_online_msg = convertToString(messages.getStringList("account-online"));
        ping_msg = convertToString(messages.getStringList("ping"));
        timer_msg = convertToString(messages.getStringList("timer"));
        blacklisted_msg = convertToString(messages.getStringList("blacklisted"));
        stuff_less = convertToString(messages.getStringList("stuff.less"));
        stuff_plus = convertToString(messages.getStringList("stuff.plus"));
    }

    public static double getVersion() {
        return version;
    }

    public static String getArgs_error() {
        return args_error;
    }

    public static String getReload_msg() {
        return reload_msg;
    }

    public static String getOnping_normal(String first, String second) {
        return onping_normal.replace("$2", second).replace("$1", first);
    }

    public static String getOnping_ready() {
        return onping_ready;
    }

    public static String getTitle_title() {
        return title_title;
    }

    public static String getTitle_subtitle() {
        return title_subtitle;
    }

    public static String getCommands_perms() {
        return commands_perms;
    }

    public static String getCommands_cleared(String what) {
        return commands_cleared.replace("$1", what);
    }

    public static String getCommands_added(String ip, String what) {
        return commands_added.replace("$2", what).replace("$1", ip);
    }

    public static String getCommands_removed(String ip, String what) {
        return commands_removed.replace("$2", what).replace("$1", ip);
    }

    public static String getActionbar_no_attack() {
        return actionbar_no_attack;
    }

    public static String getActionbar_antibot_mode() {
        return actionbar_antibot_mode;
    }

    public static String getActionbar_handshake() {
        return actionbar_handshake;
    }

    public static String getConsole_on_attack() {
        return console_on_attack;
    }

    public static String getConsole_onHandShake() {
        return console_onHandShake;
    }

    public static List<String> getHelp_msg() {
        return help_msg;
    }

    public static List<String> getStats_msg() {
        return stats_msg;
    }

    public static String getAntiBotModeMsg(String percent, String blacklisted_percent) {
        return antibotmode_msg.replace("$2", blacklisted_percent).replace("$1", percent);
    }

    public static String getFirstJoinMsg() {
        return first_join_msg;
    }

    public static String getSafeModeMsg() {
        return safe_mode_msg;
    }

    public static String getAccountOnlineMsg() {
        return account_online_msg;
    }

    public static String getPing_msg(String pings) {
        return ping_msg.replace("$1", pings);
    }

    public static String getTimer_msg(String timers) {
        return timer_msg.replace("$1", timers);
    }

    public static String getBlacklisted_msg(String estimated) {
        return blacklisted_msg.replace("$1", estimated);
    }

    public static String getStuff_less() {
        return stuff_less;
    }

    public static String getStuff_plus() {
        return stuff_plus;
    }

    private static String convertToString(List<String> stringList) {
        return String.join(System.lineSeparator(), stringList);
    }
}
