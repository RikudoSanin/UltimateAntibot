package me.kr1s_d.ultimateantibot.bungee.checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import me.kr1s_d.ultimateantibot.commons.config.ConfigManager;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.protocol.packet.Handshake;

public class HandShakeCheck {
    private final AntibotManager antibotManager;
    private final AntibotInfo info;
    private final ConfigManager configManager;

    public HandShakeCheck(UltimateAntibotWaterfall plugin){
        this.antibotManager = plugin.getAntibotManager();
        this.info = plugin.getAntibotInfo();
        this.configManager = plugin.getConfigManager();
    }

    public void detect(PlayerHandshakeEvent e){
        Handshake handshake = e.getHandshake();

        PendingConnection connection = e.getConnection();
        long currentHandShake = info.getHandShakeSecond();
        if (handshake.getRequestedProtocol() > 2) {
            handshake.setRequestedProtocol(1); // converting to ping
            if (configManager.isHandShakeMode_blacklistProtocol()) {
                antibotManager.addBlackList(Utils.getIP(connection));
            }
        }
        if(currentHandShake > configManager.getHandShakeMode_trigger()) {
            if (!antibotManager.isHandShakeModeOnline() && !antibotManager.isOnline() && !antibotManager.isPingModeOnline()) {
                antibotManager.enableHandShakeMode();
            }
        }
    }
}
