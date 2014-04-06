package info.hexanet.eNnillaMS.MineJobs;
import info.hexanet.eNnillaMS.MineJobs.classes.Conf;
import info.hexanet.eNnillaMS.MineJobs.classes.Job;
import info.hexanet.eNnillaMS.MineJobs.classes.Lang;
import info.hexanet.eNnillaMS.MineJobs.classes.Player;
import info.hexanet.eNnillaMS.MineJobs.classes.SignC;
import info.hexanet.eNnillaMS.MineJobs.events.PayoutEvents;
import info.hexanet.eNnillaMS.MineJobs.events.SignEvents;
import info.hexanet.eNnillaMS.MineJobs.events.UtilEvents;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLoader;
public final class MineJobs extends JavaPlugin implements Listener, CommandExecutor{
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    public final String blockOwnerKey = "MJ:wopwopwop";
    public Conf Config;
    public Lang Lang;
    public Map<String, Job> Jobs = new HashMap<>();
    public Map<String, Player> Players = new HashMap<>();
    public Map<Location, SignC> Signs = new HashMap<>();
    private boolean run = true;
    private String errors = "[MineJobs] ConfigLoader has found the following errors:";
    private final int[] backup = new int[5];
    
    @Override public void onEnable(){
        run = loadConfigs();
        if (run) {
            getServer().getPluginManager().registerEvents(this, this);
            if (!setupEconomy()){
                log.severe(Lang.GeneralErrors[0]);
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            this.getServer().getPluginManager().registerEvents(new PayoutEvents(this), this);
            this.getServer().getPluginManager().registerEvents(new UtilEvents(this), this);
            this.getServer().getPluginManager().registerEvents(new SignEvents(this), this);
            getCommand("minejobs").setExecutor(this);
            getCommand("mj").setExecutor(new playerCommands(this));
            getCommand("mja").setExecutor(this);
            getCommand("mjc").setExecutor(this);
            setupPermissions();
            setupChat();
        }
    }
    @Override public void onDisable(){
        if (run) { saveConfigs(null); }
    }
    @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("minejobs")){
            String a = ChatColor.RED + "false", b = ChatColor.RED + "false", c = ChatColor.RED + "OFF  ", d = ChatColor.RED + "false", 
                    e = ChatColor.RED + "false", f = ChatColor.RED + "false";
            if (Config.UseSigns) a = ChatColor.GREEN + "true ";
            if (Config.UseCustoms) b = ChatColor.GREEN + "true ";
            if (Config.UseDeathLosses.equalsIgnoreCase("job")) c = ChatColor.BLUE + "JOBS "; else if (Config.UseDeathLosses.equalsIgnoreCase("always")) c = ChatColor.GREEN + "ON   ";
            if (Config.UseCmdEconomy) d = ChatColor.GREEN + "true ";
            if (Config.SpawnerMobPayout) e = ChatColor.GREEN + "true ";
            if (Config.DebugOutput) f = ChatColor.GREEN + "true ";
            sender.sendMessage(new String[]{
                ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.",
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[0] + ChatColor.BLUE + this.getDescription().getVersion(),
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[1] + ChatColor.RED + this.getDescription().getAuthors(),
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[2] + a + "     " + Lang.MineJobOutput[3] + b,
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[4] + c + "      " + Lang.MineJobOutput[5] + d,
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[6] + e + "   " + Lang.MineJobOutput[7] + f,
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[8] + Config.DefaultJobs,
                ChatColor.GOLD  + "  " + Lang.MineJobOutput[9] + Config.ForcedJobs,
                ChatColor.WHITE + "  " + Lang.MineJobOutput[10],
                ChatColor.GREEN + ".oOo______________________________________________oOo."
            });
        } else if (!cmd.getName().equalsIgnoreCase("mjc") || Config.UseCustoms){
            jobCommands doCmd = new jobCommands(this, cmd);
            try {
                switch (args[0].toLowerCase()){
                    case "create": doCmd.create(sender, cmd, label, args); break;
                    case "delete": doCmd.delete(sender, cmd, label, args); break;
                    case "rename": doCmd.rename(sender, cmd, label, args); break;
                    case "setmax": doCmd.setMax(sender, cmd, label, args); break;
                    case "addobj": doCmd.addObj(sender, cmd, label, args); break;
                    case "delobj": doCmd.delObj(sender, cmd, label, args); break;
                    case "editobj": doCmd.editObj(sender, cmd, label, args); break;
                    case "setench": doCmd.setEnchant(sender, cmd, label, args); break;
                    case "addworld": doCmd.addWorld(sender, cmd, label, args); break;
                    case "rmworld": doCmd.remWorld(sender, cmd, label, args); break;
                    case "pdlt": doCmd.togglePDL(sender, cmd, label, args); break;
                    case "setowner": if (Config.UseCustoms) doCmd.setOwner(sender, cmd, label, args); break;
                    case "togglelock": if (Config.UseCustoms) doCmd.toggleLock(sender, cmd, label, args); break;
                    case "kick": if (Config.UseCustoms) doCmd.kickPlayer(sender, cmd, label, args); break;
                    case "invite": if (Config.UseCustoms) doCmd.invitePlayer(sender, cmd, label, args); break;
                    case "upgrade": if (Config.UseCustoms) doCmd.upgrade(sender, cmd, label, args); break;
                    case "reload": if (cmd.getName().equalsIgnoreCase("mja") && sender.hasPermission("MineJobs.admin.reload")){ if (loadConfigs()) sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[23][0]); else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[23][1]);} else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]); break;
                    default:
                        if (cmd.getName().equalsIgnoreCase("mja")){
                            doCmd.showHelpA(sender, cmd, label, args);
                        } else if (cmd.getName().equalsIgnoreCase("mjc")){
                            doCmd.showHelpC(sender, cmd, label, args);
                        } break;
                }
            } catch (ClassCastException e) {
                sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[9]);
            }
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[1]);
        return true;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    public boolean loadConfigs(){
        //----------------------------------------Main Config and Launguage File
        File mainConFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "config.yml");
        if (!mainConFile.exists()) saveDefaultConfig();
        YamlConfiguration mainConf = YamlConfiguration.loadConfiguration(mainConFile);
        
        try {
            File langFile = new File(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "locale"), mainConf.getString("locale") + ".yml");
            if (!langFile.exists()){
                langFile = new File(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "locale"), "EN.yml");
                if (!langFile.exists()) saveResource("locale/EN.yml", false);
            }
            YamlConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
            try { Lang = new Lang(lang); }
            catch (NullPointerException ex){
                saveResource("locale/EN.yml", true);
                langFile = new File(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "locale"), "EN.yml");
                lang = YamlConfiguration.loadConfiguration(langFile);
                Lang = new Lang(lang);
                errors += Lang.ConfigErrors[1];
            }
        } catch (Exception ex){
            errors += "\n    Error loading language files; plugin shutting down!\n        Cause: " + ex.getCause();
            ex.printStackTrace();
            killPlugin(errors);
            return false;
        }
        try {
            String locale; try { locale = mainConf.getString("locale"); } catch (Exception ex){ locale = "EN"; backup[0] = 1; errors += Lang.ConfigErrors[2]; }
            boolean signs; try { signs = mainConf.getBoolean("useSigns"); } catch (Exception ex){ signs = false; backup[0] = 1; errors += Lang.ConfigErrors[3]; }
            boolean customs; try { customs = mainConf.getBoolean("useCustoms"); } catch (Exception ex){ customs = false; backup[0] = 1; errors += Lang.ConfigErrors[4]; }
            String pklYN; try { pklYN = mainConf.getString("deathLosses.enable").toLowerCase(); } catch (Exception ex){ pklYN = "never"; backup[0] = 1; errors += Lang.ConfigErrors[8]; }
            double pklCash; try { pklCash = mainConf.getDouble("deathLosses.loss"); } catch (Exception ex){ pklCash = 0; backup[0] = 1; errors += Lang.ConfigErrors[9]; }
            boolean spawnerCash; try { spawnerCash = mainConf.getBoolean("spawnerMobPayout"); } catch (Exception ex){ spawnerCash = false; backup[0] = 1; errors += Lang.ConfigErrors[5]; }
            List<String> defaults; try { defaults = mainConf.getStringList("defaultJobs"); } catch (Exception ex){ defaults = new ArrayList<>(); backup[0] = 1; errors += Lang.ConfigErrors[11]; }
            for (int i = 0; i < defaults.size(); i++) { defaults.set(i, defaults.get(i).toUpperCase()); }
            List<String> forced; try { forced = mainConf.getStringList("forcedJobs"); } catch (Exception ex){ forced = new ArrayList<>(); backup[0] = 1; errors += Lang.ConfigErrors[12]; }
            for (int i = 0; i < forced.size(); i++) { forced.set(i, forced.get(i).toUpperCase()); }
            boolean debugOutput; try { debugOutput = mainConf.getBoolean("debugOutput"); } catch (Exception ex){ debugOutput = false; backup[0] = 1; errors += Lang.ConfigErrors[7]; }
            boolean ecoYN; try { ecoYN = mainConf.getBoolean("UseCmdEconomy"); } catch (Exception ex){ ecoYN = false; backup[0] = 1; errors += Lang.ConfigErrors[6]; }
            double[] eco;
            Map<String, Integer> maxJobs = new HashMap<>(); try{ for (String k:mainConf.getConfigurationSection("maxJobsPerPlayer").getKeys(false)) maxJobs.put(k, mainConf.getInt("maxJobsPerPlayer." + k)); } catch (Exception ex){ maxJobs.put("default", 3); }
            try {
                if (ecoYN){
                    eco = new double[]{mainConf.getDouble("CmdEconomy.getJob"), mainConf.getDouble("CmdEconomy.quitJob"),
                        mainConf.getDouble("CmdEconomy.createJob"), mainConf.getDouble("CmdEconomy.deleteJob"),
                        mainConf.getDouble("CmdEconomy.renameJob"), mainConf.getDouble("CmdEconomy.setJobOwner"),
                        mainConf.getDouble("CmdEconomy.lockJob"), mainConf.getDouble("CmdEconomy.makeSign"),
                        mainConf.getDouble("CmdEconomy.breakSign")};
                } else eco = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            } catch (Exception ex){
                eco = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            }
            Config = new Conf(locale, signs, customs, pklYN, pklCash, spawnerCash, maxJobs, defaults, forced, ecoYN, eco, debugOutput);
        } catch (Exception ex){
            errors += Lang.ConfigErrors[13];
            run = false;
        }
        //-----------------------------------------------------------Player File
        File playerConFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "players.yml");
        if (!playerConFile.exists()) saveResource("players.yml", false);
        YamlConfiguration playerConf = YamlConfiguration.loadConfiguration(playerConFile);
        try {
            for (String name:playerConf.getConfigurationSection("players").getKeys(false)){
                List<String> pJobs; try { pJobs = playerConf.getStringList("players." + name + ".jobs"); } catch (Exception ex){ pJobs = new ArrayList<>(); backup[1] = 1; errors += Lang.ConfigErrors[14].replace("%PLYR%", name); }
                for (int i = 0; i < pJobs.size(); i++) { pJobs.set(i, pJobs.get(i).toUpperCase()); }
                List<String> invites; try { invites = playerConf.getStringList("players." + name + ".invites"); } catch (Exception ex){ invites = new ArrayList<>(); backup[1] = 1; errors += Lang.ConfigErrors[15].replace("%PLYR%", name); }
                for (int i = 0; i < invites.size(); i++) { invites.set(i, invites.get(i).toUpperCase()); }
                Player temp = new Player(name, pJobs, invites);
                Players.put(name, temp);
            }
        } catch (Exception ex){
            errors += Lang.ConfigErrors[16];
            backup[1] = 1;
        }
        //------------------------------------------------------Jobs and Customs
        File jobConFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "jobs.yml");
        if (!jobConFile.exists()) saveResource("jobs.yml", false);
        YamlConfiguration jobConf = YamlConfiguration.loadConfiguration(jobConFile);
        try {
            for (String name:jobConf.getConfigurationSection("jobs").getKeys(false)){
                name = name.toUpperCase();
                int maxPlayers; try {maxPlayers = jobConf.getInt("jobs." + name + ".maxplayers");}catch(Exception ex){maxPlayers = 0;}
                Map<String, Double> Break = getMap(jobConf, "jobs." + name + ".break", name);
                Map<String, Double> place = getMap(jobConf, "jobs." + name + ".place", name);
                Map<String, Double> mobs = getMap(jobConf, "jobs." + name + ".mobs", name);
                Map<String, Double> fish = getMap(jobConf, "jobs." + name + ".fish", name);
                Map<String, Double> tools = getMap(jobConf, "jobs." + name + ".tools", name);
                Map<String, Double> craft = getMap(jobConf, "jobs." + name + ".craft", name);
                Map<String, Double> smelt = getMap(jobConf, "jobs." + name + ".smelt", name);
                Map<String, Double> brew = getMap(jobConf, "jobs." + name + ".brew", name);
                boolean ench;
                double enchPay;
                try {
                    ench = jobConf.getBoolean("jobs." + name + ".enchant.active");
                    if (ench) enchPay = jobConf.getDouble("jobs." + name + ".enchant.payPerLevel");
                    else enchPay = 0;
                } catch (ClassCastException ex){
                    errors += Lang.ConfigErrors[18].replace("%JOB%", name).replace("%FILE%", "JOBS.YML");
                    backup[2] = 1;
                    ench = false;
                    enchPay = 0;
                } catch (Exception ex){
                    ench = false;
                    enchPay = 0;
                }
                List<String> worlds = new ArrayList<>(); try{ worlds = jobConf.getStringList("jobs." + name + ".worlds"); } catch (Exception ex){ for (World w :this.getServer().getWorlds()){ worlds.add(w.getName()); } }
                boolean pkl; try { pkl = jobConf.getBoolean("jobs." + name + ".pkl"); } catch (Exception ex){ pkl = false; }
                if(Break.isEmpty() && place.isEmpty() && mobs.isEmpty() && fish.isEmpty() && tools.isEmpty() && craft.isEmpty() && smelt.isEmpty() && brew.isEmpty() && ench == false)
                    errors += Lang.ConfigErrors[21].replace("%JOB%", name).replace("%FILE%", "JOBS.YML");
                if(worlds.isEmpty()) errors += Lang.ConfigErrors[22].replace("%JOB%", name).replace("%FILE%", "JOBS.YML");
                Job temp = new Job(name, maxPlayers, Break, place, mobs, fish, tools, craft, brew, smelt, ench, enchPay, worlds, pkl);
                Jobs.put(name, temp);
            }
        } catch (Exception ex){
            errors += Lang.ConfigErrors[19];
            backup[2] = 1;
        }
        if (Config.UseCustoms){
            File cJobConFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "customJobs.yml");
            if (!cJobConFile.exists()) saveResource("customJobs.yml", false);
            YamlConfiguration cJobConf = YamlConfiguration.loadConfiguration(cJobConFile);
            try {
                for (String name:cJobConf.getConfigurationSection("customs").getKeys(false)){
                    try {
                        name = name.toUpperCase();
                        String owner = cJobConf.getString("customs." + name + ".owner");
                        boolean locked; try { locked = cJobConf.getBoolean("customs." + name + ".locked"); } catch (Exception ex){ locked = false; }
                        int maxPlayers; try { maxPlayers = cJobConf.getInt("customs." + name + ".maxplayers"); } catch (Exception ex){ maxPlayers = 0; }
                        Map<String, Double> Break = getMap(cJobConf, "customs." + name + ".break", name);
                        Map<String, Double> place = getMap(cJobConf, "customs." + name + ".place", name);
                        Map<String, Double> mobs = getMap(cJobConf, "customs." + name + ".mobs", name);
                        Map<String, Double> fish = getMap(cJobConf, "customs." + name + ".fish", name);
                        Map<String, Double> tools = getMap(cJobConf, "customs." + name + ".tools", name);
                        Map<String, Double> craft = getMap(cJobConf, "customs." + name + ".craft", name);
                        Map<String, Double> smelt = getMap(cJobConf, "customs." + name + ".smelt", name);
                        Map<String, Double> brew = getMap(cJobConf, "customs." + name + ".brew", name);
                        boolean ench;
                        double enchPay;
                        try {
                            ench = cJobConf.getBoolean("customs." + name + ".enchant.active");
                            if (ench) enchPay = cJobConf.getDouble("customs." + name + ".enchant.payPerLevel");
                            else enchPay = 0;
                        } catch (ClassCastException ex){
                            errors += Lang.ConfigErrors[18].replace("%JOB%", name).replace("%FILE%", "CUSTOMJOBS.YML");
                            backup[3] = 1;
                            ench = false;
                            enchPay = 0;
                        } catch (Exception ex){
                            ench = false;
                            enchPay = 0;
                        }
                        List<String> worlds = new ArrayList<>(); try { worlds = cJobConf.getStringList("customs." + name + ".worlds"); } catch (Exception ex){ for (World w:this.getServer().getWorlds()){ worlds.add(w.getName()); } }
                        boolean pkl; try { pkl = cJobConf.getBoolean("customs." + name + ".pkl"); } catch (Exception ex){ pkl = false; }
                        if(Break.isEmpty() && place.isEmpty() && mobs.isEmpty() && fish.isEmpty() && tools.isEmpty() && craft.isEmpty() && smelt.isEmpty() && brew.isEmpty() && ench == false)
                            errors += Lang.ConfigErrors[21].replace("%JOB%", name).replace("%FILE%", "CUSTOMJOBS.YML");
                        if(worlds.isEmpty()) errors += Lang.ConfigErrors[22].replace("%JOB%", name).replace("%FILE%", "CUSTOMJOBS.YML");
                        Job temp = new Job(name, owner, locked, maxPlayers, Break, place, mobs, fish, tools, craft, brew, smelt, ench, enchPay, worlds, pkl);
                        Jobs.put(name, temp);
                    } catch (Exception ex){
                        backup[3] = 1;
                        errors += Lang.ConfigErrors[23].replace("%JOB%", name).replace("%FILE%", "CUSTOMJOBS.YML");
                    }
                }
            } catch (Exception ex){
                errors += Lang.ConfigErrors[20];
                backup[3] = 1;
            }
        }
        //-----------------------------------------------------------------Signs
        if (Config.UseSigns){
            File signConFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "signs.yml");
            if (!signConFile.exists()) saveResource("signs.yml", false);
            YamlConfiguration signConf = YamlConfiguration.loadConfiguration(signConFile);
            try {
                for (String signNum:signConf.getConfigurationSection("signs").getKeys(false)){
                    try {
                        double x = signConf.getDouble("signs." + signNum + ".x");
                        double y = signConf.getDouble("signs." + signNum + ".y");
                        double z = signConf.getDouble("signs." + signNum + ".z");
                        World world = this.getServer().getWorld(signConf.getString("signs." + signNum + ".world"));
                        Location pos = new Location(world, x, y, z);
                        String sType = signConf.getString("signs." + signNum + ".type").toUpperCase();
                        String sJob = signConf.getString("signs." + signNum + ".job").toUpperCase();
                        SignC temp = new SignC(pos, sType, sJob);
                        Signs.put(pos, temp);
                    } catch (Exception ex){
                        errors += Lang.ConfigErrors[24].replace("%SIGN%", signNum);
                        backup[4] = 1;
                    }
                }
            } catch (Exception ex){
                errors += Lang.ConfigErrors[25];
                backup[4] = 1;
            }
        }
        if (backup[0] == 1) backupFile("config.yml");
        if (backup[1] == 1) backupFile("players.yml");
        if (backup[2] == 1) backupFile("jobs.yml");
        if (backup[3] == 1) backupFile("customJobs.yml");
        if (backup[4] == 1) backupFile("signs.yml");
        if (!run){
            killPlugin(errors);
            return false;
        } else if (!errors.equals("[MineJobs] ConfigLoader has found the following errors:")){
            log.severe(errors);
        }
        return true;
    }
    public Map<String, Double> getMap(YamlConfiguration jobConf, String path, String name){
        Map<String, Double> temp = new HashMap<>();
        try {
            for (int i = 0; i < jobConf.getConfigurationSection(path).getKeys(false).size(); i++){
                try {
                    String key = jobConf.getConfigurationSection(path).getKeys(false).toArray(new String[0])[i];
                    temp.put(key, jobConf.getDouble(path + "." + key));
                } catch (Exception ex){ errors += Lang.ConfigErrors[17].replace("%TYPE%", "BREAK").replace("%FILE%", "JOBS.YML").replace("%N%", String.valueOf(i)).replace("%JOB%", name); backup[2] = 1; }
            }
        } catch (Exception ex){ temp = new HashMap<>(); }
        return temp;
    }
    public void backupFile(String fileName){
        File fil = new File(new File(getDataFolder().getParentFile(), "MineJobs"), fileName);
        String fname = fileName + ".broken";
        try {
            File f2 = new File(new File(getDataFolder().getParentFile(), "MineJobs"), fname);
            if (f2.exists()){
                int num = 1;
                while (f2.exists()){
                    num++;
                    fname = fileName + ".broke" + String.valueOf(num);
                    f2 = new File(new File(getDataFolder().getParentFile(), "MineJobs"), fname);
                }
            }
            if(!f2.exists()) f2.createNewFile();
            FileChannel source = null;
            FileChannel destination = null;
            try {
                source = new FileInputStream(fil).getChannel();
                destination = new FileOutputStream(f2).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if(source != null) source.close();
                if(destination != null) destination.close();
            }
        }
        catch (IOException ex) { errors += Lang.ConfigErrors[23].replace("%FILE%", fname); }
    }
    public void killPlugin(String output){
        run = false;
        log.severe(output);
        PluginLoader asdf = new JavaPluginLoader(this.getServer());
        asdf.disablePlugin(this);
    }
    public YamlConfiguration[] makeYamlJ(Map<String, Job> map){
        YamlConfiguration[] temp = {new YamlConfiguration(), new YamlConfiguration()};
        for (Job job:map.values()){
            int c = 0;
            String top = "jobs.";
            if (job.IsCustom){
                c = 1;
                top = "customs.";
                temp[c].set(top + job.Name + ".owner", job.Owner);
                temp[c].set(top + job.Name + ".locked", job.Locked);
            }
            temp[c].set(top + job.Name + ".maxplayers", job.MaxPlayers);
            for (Map.Entry<String, Double> br:job.Break.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".break." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Place.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".place." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Mobs.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".mobs." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Fish.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".fish." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Tools.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".tools." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Craft.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".craft." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Smelt.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".smelt." + br.getKey(), Double.valueOf(br.getValue()));
            for (Map.Entry<String, Double> br:job.Brew.entrySet()) if (!br.getKey().equals("") && Double.valueOf(br.getValue()) != 0) temp[c].set(top + job.Name + ".brew." + br.getKey(), Double.valueOf(br.getValue()));
            if (job.EnchantEnabled){
                temp[c].set(top + job.Name + ".enchant.active", job.EnchantEnabled);
                temp[c].set(top + job.Name + ".enchant.payPerLevel", job.EnchantPay);
            }
            temp[c].set(top + job.Name + ".worlds", job.Worlds);
            temp[c].set(top + job.Name + ".pkl", job.DeathLosses);
        }
        if (temp[0].getKeys(true).isEmpty()) temp[0].createSection("jobs");
        if (temp[1].getKeys(true).isEmpty()) temp[1].createSection("customs");
        return temp;
    }
    public YamlConfiguration makeYamlP(Map<String, Player> map){
        YamlConfiguration temp = new YamlConfiguration();
        for (Player plyr:map.values()){
            temp.set("players." + plyr.Name + ".jobs", plyr.Jobs);
            temp.set("players." + plyr.Name + ".invites", plyr.Invites);
        }
        if (temp.getKeys(true).isEmpty()) temp.createSection("players");
        return temp;
    }
    public YamlConfiguration makeYamlS(Map<Location, SignC> map){
        YamlConfiguration temp = new YamlConfiguration();
        int signNum = 0;
        for (SignC sign:map.values()){
            temp.set("signs.sign" + signNum + ".x", sign.Pos.getX());
            temp.set("signs.sign" + signNum + ".y", sign.Pos.getY());
            temp.set("signs.sign" + signNum + ".z", sign.Pos.getZ());
            temp.set("signs.sign" + signNum + ".world", sign.Pos.getWorld().getName());
            temp.set("signs.sign" + signNum + ".type", sign.Type);
            temp.set("signs.sign" + signNum + ".job", sign.Job);
            signNum++;
        }
        if (temp.getKeys(true).isEmpty()) temp.createSection("signs");
        return temp;
    }
    public void saveConfigs(CommandSender sender){
        try {
            Config.getYaml().save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "config.yml"));
            makeYamlJ(Jobs)[0].save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "jobs.yml"));
            if (Config.UseCustoms) makeYamlJ(Jobs)[1].save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "customJobs.yml"));
            makeYamlP(Players).save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "players.yml"));
            if (Config.UseSigns) makeYamlS(Signs).save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "signs.yml"));
        } catch (IOException e){
            String message = Lang.ConfigErrors[0];
            log.severe(message);
            if (sender != null){
                sender.sendMessage(ChatColor.DARK_RED + message);
            }
        }
    }
}

//MAKE SURE ALL VARIABLES ARE USED PROPERLY
//WE NEED TO GO BACK TO THE LANG CLASS AND FILE AFTER REWORKING THE COMMANDS
//AND SCANNER BECAUSE OF CHANGED STRINGS.
//ENSURE THAT ALL VARIABLE CHANGES TO CLASSES ARE FORMATTED CORRECTLY FOR
//RECALL USE.

/*           To-Do List
 - Job leveling                                                     ----------
 - Regions Support                                                  ----------

[v4.0.0]
    * Modified the entire config system.
    ** **config.yml:** Renamed 90% of the variables, made the economy section optional. You don't really need it there anymore unless it's enabled.
    ** **players.yml:** Actually no changes here. Huh.
    ** **jobs.yml:** Job types no longer need to be listed if they're empty; nor does anything else. 'maxplayers' defaults to unlimited. 'pkl' will default to false. 'worlds' defaults to accepting all worlds.
    ** **customJobs.yml:** Same as with jobs.yml. 'locked' defaults to false. Important note: Custom Jobs MUST have a listed owner. Otherwise payouts will throw errors.
    ** **signs.yml:** I actually made a system for this one. Love it.
    ** locale/**EN.yml:** Completely reorganized. 100% guaruntee you'll need to regenerate this just like the rest.
    ** locale/**template.yml:** You can get rid of this now. It's trash.
    * Updated all the event handlers.
    ** Added checks where I previously missed checks.
    ** **Fixed the issue with protected land money glitches.**
    ** SOO many fewer lines of code.
    * Updated all the commands
    ** Player commands' line count went down by 1/2.
    ** One file for both mja and mjc
    ** Maximum jobs are now permissions based
    * Added new command "/minejobs" that displays info about plugin and main configs.
*/