package info.hexanet.eNnillaMS.MineJobs.classes;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
public class Conf {
    public String Locale; //IgnoreCase
    public String UseDeathLosses; //LOWERCASE
    public List<String> DefaultJobs; //UPPERCASE
    public List<String> ForcedJobs; //UPPERCASE
    public Map<String, Integer> MaxJobs;
    public double DeathLoss;
    public double[] Eco;
    public boolean UseSigns;
    public boolean UseCustoms;
    public boolean SpawnerMobPayout;
    public boolean UseCmdEconomy;
    public boolean DebugOutput;
    
    public Conf(String locale, boolean signs, boolean customs, String pklYN, double pklCash,
            boolean spawnerCash, Map<String, Integer> maxJobs, List<String> defaults, List<String> forced, boolean ecoYN,
            double[] eco, boolean debugOutput){
        Locale = locale;
        UseDeathLosses = pklYN;
        DefaultJobs = defaults;
        ForcedJobs = forced;
        MaxJobs = maxJobs;
        DeathLoss = pklCash;
        Eco = eco;
        UseSigns = signs;
        UseCustoms = customs;
        SpawnerMobPayout = spawnerCash;
        UseCmdEconomy = ecoYN;
        DebugOutput = debugOutput;
    }
    
    public YamlConfiguration getYaml(){
        YamlConfiguration temp = new YamlConfiguration();
        temp.set("locale", Locale);
        temp.set("useSigns", UseSigns);
        temp.set("useCustoms", UseCustoms);
        temp.set("useCmdEconomy", UseCmdEconomy);
        temp.set("deathLosses.enable", UseDeathLosses);
        temp.set("deathLosses.loss", DeathLoss);
        temp.set("spawnerMobPayout", SpawnerMobPayout);
        for (Map.Entry<String, Integer> e:MaxJobs.entrySet()) temp.set("maxJobsPerPlayer." + e.getKey(), e.getValue());
        if (MaxJobs.isEmpty()) temp.createSection("maxJobsPerPlayer");
        temp.set("defaultJobs", DefaultJobs);
        temp.set("forcedJobs", ForcedJobs);
        temp.set("debugOutput", DebugOutput);
        if (UseCmdEconomy){
            temp.set("CmdEconomy.getJob", Eco[0]);
            temp.set("CmdEconomy.quitJob", Eco[1]);
            temp.set("CmdEconomy.createJob", Eco[2]);
            temp.set("CmdEconomy.deleteJob", Eco[3]);
            temp.set("CmdEconomy.renameJob", Eco[4]);
            temp.set("CmdEconomy.setJobOwner", Eco[5]);
            temp.set("CmdEconomy.lockJob", Eco[6]);
            temp.set("CmdEconomy.makeSign", Eco[7]);
            temp.set("CmdEconomy.breakSign", Eco[8]);
        }
        return temp;
    }
    public int getJobLimit(Player player, org.bukkit.entity.Player plyr){
        if (plyr.hasPermission("MineJobs.player.JobLimit.unlimited")) return 0;
        int biggest = 0;
        for (Map.Entry<String, Integer> e:MaxJobs.entrySet()){
            if (plyr.hasPermission("MineJobs.player.JobLimit." + e.getKey()) && e.getValue() > biggest) biggest = e.getValue();
        }
        return biggest;
    }
}