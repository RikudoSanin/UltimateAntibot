package me.kr1s_d.ultimateantibot.spigot.Checks;

import me.kr1s_d.ultimateantibot.spigot.UltimateAntibotSpigot;

import java.util.HashMap;
import java.util.Map;

public class UltimateAnalyzer {
    private final Map<String, Integer> analyzer;

    public UltimateAnalyzer(UltimateAntibotSpigot Plugin){
        this.analyzer = new HashMap<>();
    }

    public void analyzeHard(String ip, int increaser){
        if(analyzer.containsKey(ip)){
            int count = analyzer.get(ip);
            analyzer.remove(ip);
            analyzer.put(ip, count + increaser);
        }else{
            analyzer.put(ip, 1);
        }
    }

    public int getAnalyzeStatus(String ip){
        if (analyzer.containsKey(ip)){
            return analyzer.get(ip);
        }
        return 0;
    }

    public boolean isAnalyzed(String ip){
        return analyzer.containsKey(ip);
    }

    public void reset(String ip){
        analyzer.remove(ip);
    }
}
