package me.kr1s_d.ultimateantibot.bungee.user;

import me.kr1s_d.ultimateantibot.bungee.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    private final Map<String, Boolean> firstJoin;

    public UserData(){
        this.firstJoin = new HashMap<>();
    }

    public boolean isFirstJoin(String ip){
       if(firstJoin.containsKey(ip)){
          return firstJoin.get(ip);
       }else{
           firstJoin.put(ip, true);
           return true;
       }
    }

    public void setFirstJoin(String str, boolean status){
        firstJoin.put(str, status);
    }

    public Map<String, Boolean> getFirstJoinHashMap() {
        return firstJoin;
    }

}
