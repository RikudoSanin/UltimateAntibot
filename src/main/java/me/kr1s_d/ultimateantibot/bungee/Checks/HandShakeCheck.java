package me.kr1s_d.ultimateantibot.bungee.Checks;

import me.kr1s_d.ultimateantibot.bungee.AntibotManager;
import me.kr1s_d.ultimateantibot.bungee.UltimateAntibotWaterfall;
import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;
import me.kr1s_d.ultimateantibot.bungee.data.AntibotInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.protocol.packet.Handshake;

public class HandShakeCheck {
    private final AntibotManager antibotManager;
    private final AntibotInfo info;
    private final Configuration config;

    public HandShakeCheck(UltimateAntibotWaterfall plugin){
        this.antibotManager = plugin.getAntibotManager();
        this.info = plugin.getAntibotInfo();
        this.config = plugin.getConfigYml();
    }

    public void detect(PlayerHandshakeEvent e){
        Handshake handshake = e.getHandshake();

        PendingConnection connection = e.getConnection();
        long currentHandShake = info.getHandShakeSecond();
        if (handshake.getRequestedProtocol() > 2) {
            handshake.setRequestedProtocol(1); // converting to ping
            if (config.getBoolean("handshakemode.blacklist_protocol")) {
                antibotManager.addBlackList(Utils.getIP(connection));
            }
        }
        if(currentHandShake > config.getLong("handshakemode.trigger")) {
            if (!antibotManager.isHandShakeModeOnline() && !antibotManager.isOnline() && !antibotManager.isSafeAntiBotModeOnline() && !antibotManager.isPingModeOnline()) {
                antibotManager.enableHandShakeMode();
            }
        }
    }
}
