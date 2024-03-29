package me.kr1s_d.ultimateantibot.bungee.data;

public class AntibotInfo {

    private long joinSecond;
    private long botSecond;
    private long pingSecond;
    private long checkSecond;
    private long handShakeSecond;

    public AntibotInfo() {
        this.joinSecond = 0L;
        this.botSecond = 0L;
        this.pingSecond = 0L;
        this.checkSecond = 0L;
        this.handShakeSecond = 0L;
    }

    public long getJoinSecond() {
        return joinSecond;
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

    public void setJoinSecond(long joinSecond) {
        this.joinSecond = joinSecond;
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

    public long getHandShakeSecond() {
        return handShakeSecond;
    }

    public void setHandShakeSecond(long handShakeSecond) {
        this.handShakeSecond = handShakeSecond;
    }
}
