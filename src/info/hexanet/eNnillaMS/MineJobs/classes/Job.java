package info.hexanet.eNnillaMS.MineJobs.classes;
import java.util.List;
import java.util.Map;
public class Job {
    public String Name;
    public int MaxPlayers;
    public Map<String, Double> Break;
    public Map<String, Double> Mobs;
    public Map<String, Double> Tools;
    public Map<String, Double> Place;
    public Map<String, Double> Fish;
    public Map<String, Double> Craft;
    public Map<String, Double> Brew;
    public Map<String, Double> Smelt;
    public boolean EnchantEnabled;
    public double EnchantPay;
    public List<String> Worlds;
    public boolean DeathLosses;
    public boolean IsCustom;
    public String Owner;
    public boolean Locked;
    
    public Job(String name, int maxPlayers, Map<String, Double> breakables, Map<String, Double> place, Map<String, Double> mobs, Map<String, Double> fish,
             Map<String, Double> tools, Map<String, Double> craft, Map<String, Double> brew, Map<String, Double> smelt, boolean enchantYN, double enchantPay,
             List<String> worlds, boolean deathLoss){
        IsCustom = false;
        Name = name;
        Owner = "";
        Locked = false;
        MaxPlayers = maxPlayers;
        Break = breakables;
        Place = place;
        Mobs = mobs;
        Fish = fish;
        Tools = tools;
        Craft = craft;
        Brew = brew;
        Smelt = smelt;
        EnchantEnabled = enchantYN;
        EnchantPay = enchantPay;
        Worlds = worlds;
        DeathLosses = deathLoss;
    }
    public Job(String name, String owner, boolean isLocked, int maxPlayers, Map<String, Double> breakables, Map<String, Double> place,
             Map<String, Double> mobs, Map<String, Double> fish, Map<String, Double> tools, Map<String, Double> craft,
             Map<String, Double> brew, Map<String, Double> smelt, boolean enchantYN, double enchantPay, List<String> worlds, boolean deathLoss){
        IsCustom = true;
        Name = name;
        Owner = owner;
        Locked = isLocked;
        MaxPlayers = maxPlayers;
        Break = breakables;
        Place = place;
        Mobs = mobs;
        Fish = fish;
        Tools = tools;
        Craft = craft;
        Brew = brew;
        Smelt = smelt;
        EnchantEnabled = enchantYN;
        EnchantPay = enchantPay;
        Worlds = worlds;
        DeathLosses = deathLoss;
    }
    
    public boolean AddPlayerCountCheck(Map<String, Player> Players){
        int i = 0;
        for (Map.Entry<String, Player> p:Players.entrySet()){
            if (p.getValue().Jobs.contains(Name)) i++;
        }
        return ((i < MaxPlayers) || MaxPlayers == 0);
    }
    public int playerCount(Map<String, Player> Players){
        int i = 0;
        for (Map.Entry<String, Player> p:Players.entrySet()){
            if (p.getValue().Jobs.contains(Name)) i++;
        }
        return i;
    }
}