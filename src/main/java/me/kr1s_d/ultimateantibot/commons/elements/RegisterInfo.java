package me.kr1s_d.ultimateantibot.commons.elements;

public class RegisterInfo {

    private final String ip;
    private final String pass;

    public RegisterInfo(String ip, String pass) {
        this.ip = ip;
        this.pass = pass;
    }

    public String getIp() {
        return ip;
    }

    public String getPass() {
        return pass;
    }

    public boolean isSimilar(RegisterInfo info){
        return info.getIp().equals(ip);
    }
}
