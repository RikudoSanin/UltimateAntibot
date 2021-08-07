package me.kr1s_d.ultimateantibot.spigot.data;

public class AntibotInfo {
    public long botSecond;
    public long pingSecond;
    public long checkSecond;

    public AntibotInfo() {
        this.botSecond = 0L;
        this.pingSecond = 0L;
        this.checkSecond = 0L;
    }

    public long getBotSecond() {
        return botSecond;
    }

    public long getPingSecond() {
        return pingSecond;
    }

    public long getCheckSecond() {
        return checkSecond;
    }

    public void setPingSecond(long pingSecond) {
        this.pingSecond = pingSecond;
    }

    public void setBotSecond(long botSecond) {
        this.botSecond = botSecond;
    }

    public void setCheckSecond(long checkSecond) {
        this.checkSecond = checkSecond;
    }
}
