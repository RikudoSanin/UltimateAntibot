package me.kr1s_d.ultimateantibot.commons.config;

import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.spigot.Database.Config;
import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class ConfigManager {
    private int antiBotMode_keep;
    private int antiBotMode_trigger;
    private int safeMode_percent;
    private int safeMode_modifier;
    private int safeMode_keep;
    private int pingMode_keep;
    private int pingMode_trigger;
    private boolean pingMode_sendInfo;
    private int handShakeMode_keep;
    private int handShakeMode_trigger;
    private boolean handShakeMode_blacklistProtocol;
    private boolean handShakeMode_enabled;
    private int playtime_whitelist;
    private int taskManager_clearCache;
    private int taskManager_queue;
    private int taskManager_account;
    private boolean firstJoin_enabled;
    private int slowMode_duration;
    private boolean slowMode_disconnect;
    private int slowMode_limit;
    private boolean slowMode_blacklist_limit;
    private boolean slowMode_enabled;
    private int account_limit;
    private boolean account_isEnabled;
    private int timer_min;
    private int timer_max;
    private int timer_between;
    private int timer_repeat;
    private boolean timer_enabled;
    private List<String> filter;

    public ConfigManager(UltimateAntibotWaterfall plugin){
        Configuration config = plugin.getConfigYml();
        antiBotMode_keep = config.getInt("antibotmode.keep");
        antiBotMode_trigger = config.getInt("antibotmode.trigger");
        safeMode_percent = config.getInt("safemode.percent");
        safeMode_modifier = config.getInt("safemode.modifier");
        safeMode_keep = config.getInt("safemode.keep");
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
        account_limit = config.getInt("checks.account.limit");
        account_isEnabled = config.getBoolean("checks.account.enable");
        firstJoin_enabled = config.getBoolean("checks.first_join.enabled");
        slowMode_duration = config.getInt("checks.slowmode.duration");
        slowMode_disconnect = config.getBoolean("checks.slowmode.disconnect");
        slowMode_limit = config.getInt("checks.slowmode.limit");
        slowMode_blacklist_limit = config.getBoolean("checks.slowmode.blacklist_on_limit");
        slowMode_enabled = config.getBoolean("checks.slowmode.enable");
        account_limit = config.getInt("checks.account.limit");
        account_isEnabled = config.getBoolean("checks.account.enable");
        timer_min = config.getInt("checks.timer.min");
        timer_max = config.getInt("checks.timer.max");
        timer_between = config.getInt("checks.timer.between");
        timer_repeat = config.getInt("checks.timer.repeat");
        timer_enabled = config.getBoolean("checks.timer.enable");
        filter = config.getStringList("filter");
    }

    public ConfigManager(UltimateAntibotSpigot plugin){
        Config config = plugin.getConfigYml();
        antiBotMode_keep = config.getInt("antibotmode.keep");
        antiBotMode_trigger = config.getInt("antibotmode.trigger");
        safeMode_percent = config.getInt("safemode.percent");
        safeMode_modifier = config.getInt("safemode.modifier");
        safeMode_keep = config.getInt("safemode.keep");
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
        account_limit = config.getInt("checks.account.limit");
        account_isEnabled = config.getBoolean("checks.account.enable");
        firstJoin_enabled = config.getBoolean("checks.first_join.enabled");
        slowMode_duration = config.getInt("checks.slowmode.duration");
        slowMode_disconnect = config.getBoolean("checks.slowmode.disconnect");
        slowMode_limit = config.getInt("checks.slowmode.limit");
        slowMode_blacklist_limit = config.getBoolean("checks.slowmode.blacklist_on_limit");
        slowMode_enabled = config.getBoolean("checks.slowmode.enable");
        account_limit = config.getInt("checks.account.limit");
        account_isEnabled = config.getBoolean("checks.account.enable");
        timer_min = config.getInt("checks.timer.min");
        timer_max = config.getInt("checks.timer.max");
        timer_between = config.getInt("checks.timer.between");
        timer_repeat = config.getInt("checks.timer.repeat");
        timer_enabled = config.getBoolean("checks.timer.enable");
        filter = config.getStringList("filter");
    }

    public int getAntiBotMode_keep() {
        return antiBotMode_keep;
    }

    public void setAntiBotMode_keep(int antiBotMode_keep) {
        this.antiBotMode_keep = antiBotMode_keep;
    }

    public int getAntiBotMode_trigger() {
        return antiBotMode_trigger;
    }

    public void setAntiBotMode_trigger(int antiBotMode_trigger) {
        this.antiBotMode_trigger = antiBotMode_trigger;
    }

    public int getSafeMode_percent() {
        return safeMode_percent;
    }

    public void setSafeMode_percent(int safeMode_percent) {
        this.safeMode_percent = safeMode_percent;
    }

    public int getSafeMode_modifier() {
        return safeMode_modifier;
    }

    public void setSafeMode_modifier(int safeMode_modifier) {
        this.safeMode_modifier = safeMode_modifier;
    }

    public int getSafeMode_keep() {
        return safeMode_keep;
    }

    public void setSafeMode_keep(int safeMode_keep) {
        this.safeMode_keep = safeMode_keep;
    }

    public int getPingMode_keep() {
        return pingMode_keep;
    }

    public void setPingMode_keep(int pingMode_keep) {
        this.pingMode_keep = pingMode_keep;
    }

    public int getPingMode_trigger() {
        return pingMode_trigger;
    }

    public void setPingMode_trigger(int pingMode_trigger) {
        this.pingMode_trigger = pingMode_trigger;
    }

    public boolean isPingMode_sendInfo() {
        return pingMode_sendInfo;
    }

    public void setPingMode_sendInfo(boolean pingMode_sendInfo) {
        this.pingMode_sendInfo = pingMode_sendInfo;
    }

    public int getHandShakeMode_keep() {
        return handShakeMode_keep;
    }

    public void setHandShakeMode_keep(int handShakeMode_keep) {
        this.handShakeMode_keep = handShakeMode_keep;
    }

    public int getHandShakeMode_trigger() {
        return handShakeMode_trigger;
    }

    public void setHandShakeMode_trigger(int handShakeMode_trigger) {
        this.handShakeMode_trigger = handShakeMode_trigger;
    }

    public boolean isHandShakeMode_blacklistProtocol() {
        return handShakeMode_blacklistProtocol;
    }

    public void setHandShakeMode_blacklistProtocol(boolean handShakeMode_blacklistProtocol) {
        this.handShakeMode_blacklistProtocol = handShakeMode_blacklistProtocol;
    }

    public boolean isHandShakeMode_enabled() {
        return handShakeMode_enabled;
    }

    public void setHandShakeMode_enabled(boolean handShakeMode_enabled) {
        this.handShakeMode_enabled = handShakeMode_enabled;
    }

    public int getPlaytime_whitelist() {
        return playtime_whitelist;
    }

    public void setPlaytime_whitelist(int playtime_whitelist) {
        this.playtime_whitelist = playtime_whitelist;
    }

    public int getTaskManager_clearCache() {
        return taskManager_clearCache;
    }

    public void setTaskManager_clearCache(int taskManager_clearCache) {
        this.taskManager_clearCache = taskManager_clearCache;
    }

    public int getTaskManager_queue() {
        return taskManager_queue;
    }

    public void setTaskManager_queue(int taskManager_queue) {
        this.taskManager_queue = taskManager_queue;
    }

    public int getTaskManager_account() {
        return taskManager_account;
    }

    public void setTaskManager_account(int taskManager_account) {
        this.taskManager_account = taskManager_account;
    }

    public boolean isFirstJoin_enabled() {
        return firstJoin_enabled;
    }

    public void setFirstJoin_enabled(boolean firstJoin_enabled) {
        this.firstJoin_enabled = firstJoin_enabled;
    }

    public int getSlowMode_duration() {
        return slowMode_duration;
    }

    public void setSlowMode_duration(int slowMode_duration) {
        this.slowMode_duration = slowMode_duration;
    }

    public boolean isSlowMode_disconnect() {
        return slowMode_disconnect;
    }

    public void setSlowMode_disconnect(boolean slowMode_disconnect) {
        this.slowMode_disconnect = slowMode_disconnect;
    }

    public int getSlowMode_limit() {
        return slowMode_limit;
    }

    public void setSlowMode_limit(int slowMode_limit) {
        this.slowMode_limit = slowMode_limit;
    }

    public boolean isSlowMode_blacklist_limit() {
        return slowMode_blacklist_limit;
    }

    public void setSlowMode_blacklist_limit(boolean slowMode_blacklist_limit) {
        this.slowMode_blacklist_limit = slowMode_blacklist_limit;
    }

    public boolean isSlowMode_enabled() {
        return slowMode_enabled;
    }

    public void setSlowMode_enabled(boolean slowMode_enabled) {
        this.slowMode_enabled = slowMode_enabled;
    }

    public int getAccount_limit() {
        return account_limit;
    }

    public void setAccount_limit(int account_limit) {
        this.account_limit = account_limit;
    }

    public boolean isAccount_isEnabled() {
        return account_isEnabled;
    }

    public void setAccount_isEnabled(boolean account_isEnabled) {
        this.account_isEnabled = account_isEnabled;
    }

    public int getTimer_min() {
        return timer_min;
    }

    public void setTimer_min(int timer_min) {
        this.timer_min = timer_min;
    }

    public int getTimer_max() {
        return timer_max;
    }

    public void setTimer_max(int timer_max) {
        this.timer_max = timer_max;
    }

    public int getTimer_between() {
        return timer_between;
    }

    public void setTimer_between(int timer_between) {
        this.timer_between = timer_between;
    }

    public int getTimer_repeat() {
        return timer_repeat;
    }

    public void setTimer_repeat(int timer_repeat) {
        this.timer_repeat = timer_repeat;
    }

    public boolean isTimer_enabled() {
        return timer_enabled;
    }

    public void setTimer_enabled(boolean timer_enabled) {
        this.timer_enabled = timer_enabled;
    }

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }
}
