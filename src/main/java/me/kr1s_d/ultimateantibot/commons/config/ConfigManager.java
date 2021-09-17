package me.kr1s_d.ultimateantibot.commons.config;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.commons.utils.Parser;
import me.kr1s_d.ultimateantibot.spigot.database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class ConfigManager {
    private final int antiBotMode_keep;
    private final int antiBotMode_trigger;
    private final int pingMode_keep;
    private final int pingMode_trigger;
    private final boolean pingMode_sendInfo;
    private final int handShakeMode_keep;
    private final int handShakeMode_trigger;
    private final boolean handShakeMode_blacklistProtocol;
    private final boolean handShakeMode_enabled;
    private final int playtime_whitelist;
    private final int taskManager_clearCache;
    private final int taskManager_queue;
    private final int taskManager_account;
    private final boolean firstJoin_enabled;
    private final int slowMode_duration;
    private final boolean slowMode_disconnect;
    private final int slowMode_limit;
    private final boolean slowMode_blacklist_limit;
    private final boolean slowMode_enabled;
    private final int account_limit;
    private final boolean account_isEnabled;
    private final int[] auth_PingMin_Max;
    private final int[] auth_TimerMin_Max;
    private final int auth_between;
    private final int auth_enableCheckPercent;
    private final boolean auth_ping_interface;
    private final boolean auth_isEnabled;
    private final List<String> filter;

    public ConfigManager(UltimateAntibotWaterfall plugin){
        Configuration config = plugin.getConfigYml();
        antiBotMode_keep = config.getInt("antibotmode.keep");
        antiBotMode_trigger = config.getInt("antibotmode.trigger");
        pingMode_keep = config.getInt("pingmode.keep");
        pingMode_trigger = config.getInt("pingmode.trigger");
        pingMode_sendInfo = config.getBoolean("pingmode.send_info");
        handShakeMode_keep = config.getInt("handshakemode.keep");
        handShakeMode_trigger = config.getInt("handshakemode.trigger");
        handShakeMode_blacklistProtocol = config.getBoolean("handshakemode.blacklist_protocol");
        handShakeMode_enabled = config.getBoolean("handshakemode.enabled");
        playtime_whitelist = config.getInt("playtime_for_whitelist");
        taskManager_clearCache = config.getInt("taskmanager.clearcache");
        taskManager_queue = config.getInt("taskmanger.queue");
        taskManager_account = config.getInt("taskmanager.account");
        firstJoin_enabled = config.getBoolean("checks.first_join.enabled");
        slowMode_duration = config.getInt("checks.slowmode.duration");
        slowMode_disconnect = config.getBoolean("checks.slowmode.disconnect");
        slowMode_limit = config.getInt("checks.slowmode.limit");
        slowMode_blacklist_limit = config.getBoolean("checks.slowmode.blacklist_on_limit");
        slowMode_enabled = config.getBoolean("checks.slowmode.enable");
        account_limit = config.getInt("checks.account.limit");
        account_isEnabled = config.getBoolean("checks.account.enable");
        auth_PingMin_Max = Parser.toIntArray(config.getString("checks.auth.ping").split("-"));
        auth_TimerMin_Max = Parser.toIntArray(config.getString("checks.auth.timer").split("-"));
        auth_between = config.getInt("checks.auth.between");
        auth_enableCheckPercent = config.getInt("checks.auth.percent");
        auth_ping_interface = config.getBoolean("checks.auth.ping_interface");
        auth_isEnabled = config.getBoolean("checks.auth.enabled");
        filter = config.getStringList("filter");
    }

    public ConfigManager(UltimateAntibotSpigot plugin){
        Config config = plugin.getConfigYml();
        antiBotMode_keep = config.getInt("antibotmode.keep");
        antiBotMode_trigger = config.getInt("antibotmode.trigger");
        pingMode_keep = config.getInt("pingmode.keep");
        pingMode_trigger = config.getInt("pingmode.trigger");
        pingMode_sendInfo = config.getBoolean("pingmode.send_info");
        handShakeMode_keep = config.getInt("handshakemode.keep");
        handShakeMode_trigger = config.getInt("handshakemode.trigger");
        handShakeMode_blacklistProtocol = config.getBoolean("handshakemode.blacklist_protocol");
        handShakeMode_enabled = config.getBoolean("handshakemode.enabled");
        playtime_whitelist = config.getInt("playtime_for_whitelist");
        taskManager_clearCache = config.getInt("taskmanager.clearcache");
        taskManager_queue = config.getInt("taskmanger.queue");
        taskManager_account = config.getInt("taskmanager.account");
        firstJoin_enabled = config.getBoolean("checks.first_join.enabled");
        slowMode_duration = config.getInt("checks.slowmode.duration");
        slowMode_disconnect = config.getBoolean("checks.slowmode.disconnect");
        slowMode_limit = config.getInt("checks.slowmode.limit");
        slowMode_blacklist_limit = config.getBoolean("checks.slowmode.blacklist_on_limit");
        slowMode_enabled = config.getBoolean("checks.slowmode.enable");
        account_limit = config.getInt("checks.account.limit");
        account_isEnabled = config.getBoolean("checks.account.enable");
        auth_PingMin_Max = Parser.toIntArray(config.getString("checks.auth.ping").split("-"));
        auth_TimerMin_Max = Parser.toIntArray(config.getString("checks.auth.timer").split("-"));
        auth_between = config.getInt("checks.auth.between");
        auth_enableCheckPercent = config.getInt("checks.auth.percent");
        auth_ping_interface = config.getBoolean("checks.auth.ping_interface");
        auth_isEnabled = config.getBoolean("checks.auth.enabled");
        filter = config.getStringList("filter");
    }

    public int[] getAuth_PingMin_Max() {
        return auth_PingMin_Max;
    }

    public int[] getAuth_TimerMin_Max() {
        return auth_TimerMin_Max;
    }

    public int getAuth_between() {
        return auth_between;
    }

    public int getAuth_enableCheckPercent() {
        return auth_enableCheckPercent;
    }

    public boolean isAuth_isEnabled() {
        return auth_isEnabled;
    }

    public boolean isAuth_ping_interface() {
        return auth_ping_interface;
    }

    public int getAntiBotMode_keep() {
        return antiBotMode_keep;
    }

    public int getAntiBotMode_trigger() {
        return antiBotMode_trigger;
    }

    public int getPingMode_keep() {
        return pingMode_keep;
    }

    public int getPingMode_trigger() {
        return pingMode_trigger;
    }

    public boolean isPingMode_sendInfo() {
        return pingMode_sendInfo;
    }

    public int getHandShakeMode_keep() {
        return handShakeMode_keep;
    }

    public int getHandShakeMode_trigger() {
        return handShakeMode_trigger;
    }

    public boolean isHandShakeMode_blacklistProtocol() {
        return handShakeMode_blacklistProtocol;
    }

    public boolean isHandShakeMode_enabled() {
        return handShakeMode_enabled;
    }

    public int getPlaytime_whitelist() {
        return playtime_whitelist;
    }

    public int getTaskManager_clearCache() {
        return taskManager_clearCache;
    }

    public int getTaskManager_queue() {
        return taskManager_queue;
    }

    public int getTaskManager_account() {
        return taskManager_account;
    }

    public boolean isFirstJoin_enabled() {
        return firstJoin_enabled;
    }

    public int getSlowMode_duration() {
        return slowMode_duration;
    }

    public boolean isSlowMode_disconnect() {
        return slowMode_disconnect;
    }

    public int getSlowMode_limit() {
        return slowMode_limit;
    }

    public boolean isSlowMode_blacklist_limit() {
        return slowMode_blacklist_limit;
    }

    public boolean isSlowMode_enabled() {
        return slowMode_enabled;
    }

    public int getAccount_limit() {
        return account_limit;
    }

    public boolean isAccount_isEnabled() {
        return account_isEnabled;
    }

    public List<String> getFilter() {
        return filter;
    }
}
