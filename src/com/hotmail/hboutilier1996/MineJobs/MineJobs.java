package com.hotmail.hboutilier1996.MineJobs;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
public final class MineJobs extends JavaPlugin implements Listener{
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    final String blockOwnerKey = "MJ:wopwopwop";
    public File mainConfigFile;
    public YamlConfiguration mainConfig;
    public File jobConfigFile;
    public YamlConfiguration jobConfig;
    public File customJobConfigFile;
    public YamlConfiguration customJobConfig;
    public File playerConfigFile;
    public YamlConfiguration playerConfig;
    public File signConfigFile;
    public YamlConfiguration signConfig;
    public File language;
    public YamlConfiguration lang;
    @Override public void onEnable(){
        loadConfigs();
        getServer().getPluginManager().registerEvents(this, this);
        if (!setupEconomy()){
            log.severe(lang.getString("Errors.NoVault"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("mj").setExecutor(new playerCommands(this));
        getCommand("mja").setExecutor(new adminCommands(this));
        getCommand("mjc").setExecutor(new customsCommands(this));
        setupPermissions();
        setupChat();
    }
    @Override public void onDisable(){
        saveConfigs(null);
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
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
        mainConfigFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "config.yml");
        jobConfigFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "jobs.yml");
        customJobConfigFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "customJobs.yml");
        playerConfigFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "players.yml");
        signConfigFile = new File(new File(getDataFolder().getParentFile(), "MineJobs"), "signs.yml");
        if (!mainConfigFile.exists()) {
            saveDefaultConfig();
        }
        if (!jobConfigFile.exists()) {
            saveResource("jobs.yml", false);
        }
        if (!customJobConfigFile.exists()) {
            saveResource("customJobs.yml", false);
        }
        if (!playerConfigFile.exists()) {
            saveResource("players.yml", false);
        }
        if (!signConfigFile.exists()) {
            saveResource("signs.yml", false);
        }
        mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
        jobConfig = YamlConfiguration.loadConfiguration(jobConfigFile);
        customJobConfig = YamlConfiguration.loadConfiguration(customJobConfigFile);
        playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);
        signConfig = YamlConfiguration.loadConfiguration(signConfigFile);
        if (!loadLang() || !scanConfig()) {
            JavaPluginLoader asdf = new JavaPluginLoader(this.getServer());
            asdf.disablePlugin(this);
            return false;
        }
        return true;
    }
    public boolean scanConfig(){
        boolean test = true;
        //                                                            mainConfig
        if (mainConfig != null){
            if (mainConfig.contains("locale")){
                if (mainConfig.getString("locale").equals("")){
                    mainConfig.set("locale", "EN");
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e053"));
                test = false;
            }
            if (mainConfig.contains("breakOutput")){
                if (!mainConfig.getString("breakOutput").equals("true") && !mainConfig.getString("breakOutput").equals("false")){
                    log.severe(lang.getString("Errors.ConfigScanner.e055"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e056"));
                test = false;
            }
            if (!mainConfig.contains("forcedJobs")){
                log.severe(lang.getString("Errors.ConfigScanner.e057"));
                test = false;
            }
            if (!mainConfig.contains("defaultJobs")){
                log.severe(lang.getString("Errors.ConfigScanner.e058"));
                test = false;
            }
            if (mainConfig.contains("economy")){
                if (mainConfig.contains("economy.active")){
                    if (!mainConfig.getString("economy.active").equals("true") && !mainConfig.getString("economy.active").equals("false")){
                        log.severe(lang.getString("Errors.ConfigScanner.e059"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e060"));
                    test = false;
                }
                if (mainConfig.contains("economy.getJob")){
                    try {
                        mainConfig.getDouble("economy.getJob");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e061"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e062"));
                    test = false;
                }
                if (mainConfig.contains("economy.quitJob")){
                    try {
                        mainConfig.getDouble("economy.quitJob");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e063"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e064"));
                    test = false;
                }
                if (mainConfig.contains("economy.createJob")){
                    try {
                        mainConfig.getDouble("economy.createJob");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e065"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e066"));
                    test = false;
                }
                if (mainConfig.contains("economy.deleteJob")){
                    try {
                        mainConfig.getDouble("economy.deleteJob");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e067"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e068"));
                    test = false;
                }
                if (mainConfig.contains("economy.renameJob")){
                    try {
                        mainConfig.getDouble("economy.renameJob");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e069"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e070"));
                    test = false;
                }
                if (mainConfig.contains("economy.setJobOwner")){
                    try {
                        mainConfig.getDouble("economy.setJobOwner");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e071"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e072"));
                    test = false;
                }
                if (mainConfig.contains("economy.lockJob")){
                    try {
                        mainConfig.getDouble("economy.lockJob");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e073"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e074"));
                    test = false;
                }
                if (mainConfig.contains("economy.makeSign")){
                    try {
                        mainConfig.getDouble("economy.makeSign");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e075"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e076"));
                    test = false;
                }
                if (mainConfig.contains("economy.breakSign")){
                    try {
                        mainConfig.getDouble("economy.breakSign");
                    } catch (Exception e) {
                        log.severe(lang.getString("Errors.ConfigScanner.e077"));
                        test = false;
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e078"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e079"));
                test = false;
            }
            if (mainConfig.contains("spawnerCash")){
                if (!mainConfig.getString("spawnerCash").equals("true") && !mainConfig.getString("spawnerCash").equals("false")){
                    mainConfig.set("spawnerCash", "false");
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e054"));
                test = false;
            }
            if (mainConfig.contains("use_signs")){
                try {
                    boolean a = mainConfig.getBoolean("use_signs");
                    mainConfig.set("use_signs", a);
                } catch (Exception e){
                    log.severe(lang.getString("Errors.ConfigScanner.e000"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e001"));
                test = false;
            }
            if (mainConfig.contains("customJobs")){
                try {
                    boolean b = mainConfig.getBoolean("customJobs");
                    mainConfig.set("customJobs", b);
                } catch (Exception e){
                    log.severe(lang.getString("Errors.ConfigScanner.e002"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e003"));
                test = false;
            }
            if (mainConfig.contains("pklosses")){
                if (mainConfig.contains("pklosses.enable")){
                    if (!(mainConfig.getString("pklosses.enable").equals("always") || mainConfig.getString("pklosses.enable").equals("never") || mainConfig.getString("pklosses.enable").equals("job"))){
                        log.severe(lang.getString("Errors.ConfigScanner.e049"));
                        mainConfig.set("pklosses.enable", "never");
                    }
                } else {
                    mainConfig.set("pklosses.enable", "never");
                }
                if (mainConfig.contains("pklosses.loss")){
                    try {
                        mainConfig.getDouble("pklosses.loss");
                    } catch (Exception e){
                        log.severe(lang.getString("Errors.ConfigScanner.e050"));
                        test = false;
                    }
                } else {
                    mainConfig.set("pklosses.loss", 50);
                }
            } else {
                mainConfig.set("pklosses.enable", "never");
                mainConfig.set("pklosses.loss", 50);
            }
            if (mainConfig.contains("maxjobs")){
                try {
                    mainConfig.getInt("maxjobs");
                } catch (Exception e){
                    log.severe(lang.getString("Errors.ConfigScanner.e004"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e005"));
                test = false;
            }
        } else {
            log.severe(lang.getString("Errors.ConfigScanner.e006"));
            test = false;
        }
        //                                                             jobConfig
        if (jobConfig != null){
            if (jobConfig.contains("jobs")){
                for (String job:jobConfig.getConfigurationSection("jobs").getKeys(false)){
                    if (jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("maxplayers")){
                        try {
                            jobConfig.getInt("jobs." + job + ".maxplayers");
                        } catch (Exception e){
                            log.severe(lang.getString("Errors.ConfigScanner.e008").replace("%JOB%", job));
                            test = false;
                        }
                    } else {
                        jobConfig.set("jobs." + job + ".maxplayers", 0);
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("break")){
                        jobConfig.set("jobs." + job + ".break", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".break").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".break." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".break." + item));
                                jobConfig.set("jobs." + job + ".break." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("place")){
                        jobConfig.set("jobs." + job + ".place", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".place").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".place." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".place." + item));
                                jobConfig.set("jobs." + job + ".place." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("mobs")){
                        jobConfig.set("jobs." + job + ".mobs", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".mobs").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".mobs." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".mobs." + item));
                                jobConfig.set("jobs." + job + ".mobs." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("fish")){
                        jobConfig.set("jobs." + job + ".fish", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".fish").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".fish." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".fish." + item));
                                jobConfig.set("jobs." + job + ".fish." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("tools")){
                        jobConfig.set("jobs." + job + ".tools", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".tools").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".tools." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".tools." + item));
                                jobConfig.set("jobs." + job + ".tools." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("craft")){
                        jobConfig.set("jobs." + job + ".craft", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".craft").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".craft." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".craft." + item));
                                jobConfig.set("jobs." + job + ".craft." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("brew")){
                        jobConfig.set("jobs." + job + ".brew", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".brew").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".brew." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".brew." + item));
                                jobConfig.set("jobs." + job + ".brew." + item, null);
                            }
                        }
                    }
                    if (!jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("smelt")){
                        jobConfig.set("jobs." + job + ".smelt", "");
                    } else {
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".smelt").getKeys(false)){
                            if (!item.equals(item.toUpperCase())){
                                jobConfig.set("jobs." + job + ".smelt." + item.toUpperCase(), jobConfig.getDouble("jobs." + job + ".smelt." + item));
                                jobConfig.set("jobs." + job + ".smelt." + item, null);
                            }
                        }
                    }
                    if (jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("enchant")){
                        if (jobConfig.getConfigurationSection("jobs." + job + ".enchant").getKeys(false).contains("active")){
                            try {
                                boolean c = jobConfig.getBoolean("jobs." + job + ".enchant.active");
                                jobConfig.set("jobs." + job + ".enchant.active", c);
                            } catch (Exception e){
                                log.severe(lang.getString("Errors.ConfigScanner.e017").replace("%JOB%", job));
                                test = false;
                            }
                        } else {
                            jobConfig.set("jobs." + job + ".enchant.active", false);
                        }
                        if (jobConfig.getConfigurationSection("jobs." + job + ".enchant").getKeys(false).contains("payPerLevel")){
                            try {
                                jobConfig.getInt("jobs." + job + ".enchant.payPerLevel");
                            } catch (Exception e){
                                log.severe(lang.getString("Errors.ConfigScanner.e018").replace("%JOB%", job));
                                test = false;
                            }
                        } else {
                            jobConfig.set("jobs." + job + ".enchant.payPerLevel", 0);
                        }
                    } else {
                        jobConfig.set("jobs." + job + ".enchant.active", false);
                        jobConfig.set("jobs." + job + ".enchant.payPerLevel", 0);
                    }
                    if (jobConfig.getConfigurationSection("jobs." + job).getKeys(true).size() == 12 && !jobConfig.getBoolean("jobs." + job + "enchant.active")){
                        log.severe(lang.getString("Errors.ConfigScanner.e019").replace("%JOB%", job));
                    }
                    if (jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("worlds")){
                        if (jobConfig.getStringList("jobs." + job + ".worlds").isEmpty()){
                            log.severe(lang.getString("Errors.ConfigScanner.e020").replace("%JOB%", job));
                        } else {
                            for (String world:jobConfig.getStringList("jobs." + job + ".worlds")){
                                if (!world.equals(world.toLowerCase())){
                                    List z = jobConfig.getStringList("jobs." + job + ".worlds");
                                    z.remove(world);
                                    z.add(world.toLowerCase());
                                    jobConfig.set("jobs." + job + ".worlds", z);
                                }
                            }
                        }
                    } else {
                        jobConfig.set("jobs." + job + ".worlds", getServer().getWorlds());
                    }
                    if (mainConfig.getString("pklosses.enable").equals("job")){
                        if (jobConfig.getConfigurationSection("jobs." + job).getKeys(false).contains("pkl")){
                            try {
                                jobConfig.getBoolean("jobs." + job + ".pkl");
                            } catch (Exception e){
                                log.severe(lang.getString("Errors.ConfigScanner.e051").replace("%JOB%", job));
                                test = false;
                            }
                        } else {
                            jobConfig.set("jobs." + job + ".pkl", false);
                        }
                    }
                    if (!job.equals(job.toUpperCase())){
                        jobConfig.createSection("jobs." + job.toUpperCase());
                        jobConfig.set("jobs." + job.toUpperCase() + ".maxplayers", jobConfig.getInt("jobs." + job + ".maxplayers"));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".break").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".break." + item, jobConfig.getDouble("jobs." + job + ".break." + item));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".place").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".place." + item, jobConfig.getDouble("jobs." + job + ".place." + item));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".mobs").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".mobs." + item, jobConfig.getDouble("jobs." + job + ".mobs." + item));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".tools").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".tools." + item, jobConfig.getDouble("jobs." + job + ".tools." + item));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".craft").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".craft." + item, jobConfig.getDouble("jobs." + job + ".craft." + item));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".smelt").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".smelt." + item, jobConfig.getDouble("jobs." + job + ".smelt." + item));
                        for (String item:jobConfig.getConfigurationSection("jobs." + job + ".brew").getKeys(false)) jobConfig.set("jobs." + job.toUpperCase() + ".brew." + item, jobConfig.getDouble("jobs." + job + ".brew." + item));
                        jobConfig.set("jobs." + job.toUpperCase() + ".enchant.active", jobConfig.getStringList("jobs." + job + ".enchant.active"));
                        jobConfig.set("jobs." + job.toUpperCase() + ".enchant.payPerLevel", jobConfig.getStringList("jobs." + job + ".enchant.payPerLevel"));
                        jobConfig.set("jobs." + job.toUpperCase() + ".worlds", jobConfig.getStringList("jobs." + job + ".worlds"));
                        jobConfig.set("jobs." + job.toUpperCase() + ".pkl", jobConfig.getBoolean("jobs." + job + ".pkl"));
                        jobConfig.set("jobs." + job, null);
                    }
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e022"));
                test = false;
            }
        } else {
            log.severe(lang.getString("Errors.ConfigScanner.e023"));
            test = false;
        }
        //                                                          playerConfig
        if (playerConfig != null){
            if (playerConfig.contains("players")){
                for (String plyrnm:playerConfig.getConfigurationSection("players").getKeys(false)){
                    if (!playerConfig.getConfigurationSection("players." + plyrnm).getKeys(false).contains("jobs")){
                        playerConfig.set("players." + plyrnm + ".jobs", "");
                    } else {
                        for (String job:playerConfig.getStringList("players." + plyrnm + ".jobs")){
                            if (!job.equals(job.toUpperCase())){
                                List y = playerConfig.getStringList("players." + plyrnm + ".jobs");
                                y.remove(job);
                                y.add(job.toUpperCase());
                                playerConfig.set("players." + plyrnm + ".jobs", y);
                            }
                        }
                    }
                    if (!playerConfig.getConfigurationSection("players." + plyrnm).getKeys(false).contains("invites")){
                        playerConfig.set("players." + plyrnm + ".invites", "");
                    } else {
                        for (String job:playerConfig.getStringList("players." + plyrnm + ".invites")){
                            if (!job.equals(job.toUpperCase())){
                                List y = playerConfig.getStringList("players." + plyrnm + ".invites");
                                y.remove(job);
                                y.add(job.toUpperCase());
                                playerConfig.set("players." + plyrnm + ".invites", y);
                            }
                        }
                    }
                    if (!plyrnm.equals(plyrnm.toLowerCase())){
                        playerConfig.set("players." + plyrnm.toLowerCase() + ".jobs", playerConfig.getStringList("players." + plyrnm + ".jobs"));
                        playerConfig.set("players." + plyrnm.toLowerCase() + ".invites", playerConfig.getStringList("players." + plyrnm + ".invites"));
                        playerConfig.set("players." + plyrnm, null);
                    }
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e026"));
                test = false;
            }
        } else {
            log.severe(lang.getString("Errors.ConfigScanner.e027"));
            test = false;
        }
        //                                                       customJobConfig
        if (mainConfig.getBoolean("customJobs") == true){
            if (customJobConfig != null){
                if (customJobConfig.contains("customs")){
                    for (String job:customJobConfig.getConfigurationSection("customs").getKeys(false)){
                        if (customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("maxplayers")){
                            try {
                                customJobConfig.getInt("customs." + job + ".maxplayers");
                            } catch (Exception e){
                                log.severe(lang.getString("Errors.ConfigScanner.e029").replace("%JOB%", job));
                                test = false;
                            }
                        } else {
                            customJobConfig.set("customs." + job + ".maxplayers", 0);
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("break")){
                            customJobConfig.set("customs." + job + ".break", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".break").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".break." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".break." + item));
                                    customJobConfig.set("customs." + job + ".break." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("place")){
                            customJobConfig.set("customs." + job + ".place", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".place").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".place." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".place." + item));
                                    customJobConfig.set("customs." + job + ".place." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("mobs")){
                            customJobConfig.set("customs." + job + ".mobs", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".mobs").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".mobs." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".mobs." + item));
                                    customJobConfig.set("customs." + job + ".mobs." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("fish")){
                            customJobConfig.set("customs." + job + ".fish", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".fish").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".fish." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".fish." + item));
                                    customJobConfig.set("customs." + job + ".fish." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("tools")){
                            customJobConfig.set("customs." + job + ".tools", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".tools").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".tools." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".tools." + item));
                                    customJobConfig.set("customs." + job + ".tools." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("craft")){
                            customJobConfig.set("customs." + job + ".craft", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".craft").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".craft." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".craft." + item));
                                    customJobConfig.set("customs." + job + ".craft." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("brew")){
                            customJobConfig.set("customs." + job + ".brew", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".brew").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".brew." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".brew." + item));
                                    customJobConfig.set("customs." + job + ".brew." + item, null);
                                }
                            }
                        }
                        if (!customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("smelt")){
                            customJobConfig.set("customs." + job + ".smelt", "");
                        } else {
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".smelt").getKeys(false)){
                                if (!item.equals(item.toUpperCase())){
                                    customJobConfig.set("customs." + job + ".smelt." + item.toUpperCase(), customJobConfig.getDouble("customs." + job + ".smelt." + item));
                                    customJobConfig.set("customs." + job + ".smelt." + item, null);
                                }
                            }
                        }
                        if (customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("enchant")){
                            if (customJobConfig.getConfigurationSection("customs." + job + "enchant").getKeys(false).contains("active")){
                                try {
                                    boolean d = customJobConfig.getBoolean("customs." + job + ".enchant.active");
                                    customJobConfig.set("customs." + job + ".enchant.active", d);
                                } catch (Exception e){
                                    log.severe(lang.getString("Errors.ConfigScanner.e038").replace("%JOB%", job));
                                    test = false;
                                }
                            } else {
                                customJobConfig.set("customs." + job + ".enchant.active", false);
                            }
                            if (customJobConfig.getConfigurationSection("customs." + job + "enchant").getKeys(false).contains("payPerLevel")){
                                try {
                                    customJobConfig.getInt("customs." + job + ".enchant.payPerLevel");
                                } catch (Exception e){
                                    log.severe(lang.getString("Errors.ConfigScanner.e039").replace("%JOB%", job));
                                    test = false;
                                }
                            } else {
                                customJobConfig.set("customs." + job + ".enchant.payPerLevel", 0);
                            }
                        } else {
                            customJobConfig.set("customs." + job + ".enchant.active", false);
                            customJobConfig.set("customs." + job + ".enchant.payPerLevel", 0);
                        }
                        if (customJobConfig.getConfigurationSection("customs." + job).getKeys(true).size() == 12 && !customJobConfig.getBoolean("customs." + job + "enchant.active")){
                            log.severe(lang.getString("Errors.ConfigScanner.e040").replace("%JOB%", job));
                        }
                        if (customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("worlds")){
                            if (customJobConfig.getStringList("customs." + job + ".worlds").isEmpty()){
                                log.severe(lang.getString("Errors.ConfigScanner.e041").replace("%JOB%", job));
                            }
                        } else {
                            customJobConfig.set("customs." + job + ".worlds", getServer().getWorlds());
                        }
                        if (mainConfig.getString("pklosses.enable").equals("job")){
                            if (customJobConfig.getConfigurationSection("customs." + job).getKeys(false).contains("pkl")){
                                try {
                                    customJobConfig.getBoolean("customs." + job + ".pkl");
                                } catch (Exception e){
                                    log.severe(lang.getString("Errors.ConfigScanner.e052").replace("%JOB%", job));
                                    test = false;
                                }
                            } else {
                                customJobConfig.set("customs." + job + ".pkl", false);
                            }
                        }
                        if (!job.equals(job.toUpperCase())){
                            customJobConfig.createSection("customs." + job.toUpperCase());
                            customJobConfig.set("customs." + job.toUpperCase() + ".maxplayers", customJobConfig.getInt("customs." + job + ".maxplayers"));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".break").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".break." + item, customJobConfig.getDouble("customs." + job + ".break." + item));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".place").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".place." + item, customJobConfig.getDouble("customs." + job + ".place." + item));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".mobs").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".mobs." + item, customJobConfig.getDouble("customs." + job + ".mobs." + item));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".tools").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".tools." + item, customJobConfig.getDouble("customs." + job + ".tools." + item));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".craft").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".craft." + item, customJobConfig.getDouble("customs." + job + ".craft." + item));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".smelt").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".smelt." + item, customJobConfig.getDouble("customs." + job + ".smelt." + item));
                            for (String item:customJobConfig.getConfigurationSection("customs." + job + ".brew").getKeys(false)) customJobConfig.set("customs." + job.toUpperCase() + ".brew." + item, customJobConfig.getDouble("customs." + job + ".brew." + item));
                            customJobConfig.set("customs." + job.toUpperCase() + ".enchant.active", customJobConfig.getStringList("customs." + job + ".enchant.active"));
                            customJobConfig.set("customs." + job.toUpperCase() + ".enchant.payPerLevel", customJobConfig.getStringList("customs." + job + ".enchant.payPerLevel"));
                            customJobConfig.set("customs." + job.toUpperCase() + ".worlds", customJobConfig.getStringList("customs." + job + ".worlds"));
                            customJobConfig.set("customs." + job.toUpperCase() + ".pkl", customJobConfig.getBoolean("customs." + job + ".pkl"));
                            customJobConfig.set("customs." + job, null);
                        }
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e043"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e044"));
                test = false;
            }
        }
        //                                                            signConfig
        if (mainConfig.getBoolean("use_signs") == true){
            if (signConfig != null){
                if (signConfig.contains("signs")){
                    if (!signConfig.contains("signs.get")){
                        signConfig.createSection("signs.get");
                    }
                    if (!signConfig.contains("signs.quit")){
                        signConfig.createSection("signs.quit");
                    }
                } else {
                    log.severe(lang.getString("Errors.ConfigScanner.e047"));
                    test = false;
                }
            } else {
                log.severe(lang.getString("Errors.ConfigScanner.e048"));
                test = false;
            }
        }
        return test == true;
    }
    public boolean loadLang(){
        boolean success = true;
        YamlConfiguration LST = null;
        try {
            switch(mainConfig.getString("locale")){
                case "EN":
                default:
                    language = new File(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "locale"), "EN.yml");
                    if (!language.exists()) {
                        saveResource("locale/EN.yml", false);
                    }
                    lang = YamlConfiguration.loadConfiguration(language);
            }
            File LSTF = new File(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "locale"), "template.yml");
            if (!LSTF.exists()) {
                saveResource("locale/template.yml", false);
            }
            LST = YamlConfiguration.loadConfiguration(LSTF);
        } catch (Exception e){
            success = false;
        }
        if (LST != null){
            for (String key:LST.getConfigurationSection("").getKeys(true)){
                if (lang.contains(key)){
                    if (lang.getString(key).equals("")){
                        lang.set(key, "ERROR MISSING OUTPUT STRING");
                    }
                } else lang.set(key, "ERROR MISSING OUTPUT STRING");
            }
        } else success = false;
        return success == true;
    }
    public void saveConfigs(CommandSender sender){
        try{
            mainConfig.save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "config.yml"));
            jobConfig.save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "jobs.yml"));
            customJobConfig.save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "customJobs.yml"));
            playerConfig.save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "players.yml"));
            signConfig.save(new File(new File(getDataFolder().getParentFile(), "MineJobs"), "signs.yml"));
        } catch (IOException e){
            String message = lang.getString("SevereErrors.SaveFail");
            log.severe(message);
            if (sender != null){
                sender.sendMessage(ChatColor.DARK_RED + message);
            }
        }
    }
    @EventHandler public void SignCreate(SignChangeEvent event){
        if (mainConfig.getBoolean("use_signs") == true){
            if(event.getLine(0).equalsIgnoreCase("[getajob]")){
                if (event.getPlayer().hasPermission("MineJobs.signs.makeGetSign")){
                    if (!event.getLine(2).isEmpty() && (jobConfig.getConfigurationSection("jobs").getKeys(false).contains(event.getLine(2).toUpperCase()) || customJobConfig.getConfigurationSection("customs").getKeys(false).contains(event.getLine(2).toUpperCase()))){
                        if (mainConfig.getBoolean("economy.active")){
                            econ.withdrawPlayer(event.getPlayer().getName(), mainConfig.getDouble("economy.makeSign"));
                        }
                        event.setLine(0, ChatColor.GREEN + "[Get A Job]");
                        event.setLine(1, "");
                        event.setLine(2, event.getLine(2).toUpperCase());
                        event.setLine(3, "");
                        String sign = event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ();
                        signConfig.set("signs.get." + sign, event.getLine(2) + "," + event.getLine(3));
                        saveConfigs(event.getPlayer());
                        event.getPlayer().sendMessage(ChatColor.GREEN + lang.getString("g.Signs.CreateSuccess"));
                    } else event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.JobNotFound"));
                } else event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.NoCreatePermissions"));
            } else if (event.getLine(0).equalsIgnoreCase("[quitajob]")){
                if (event.getPlayer().hasPermission("MineJobs.signs.makeQuitSign")){
                    if (!event.getLine(2).isEmpty() && (jobConfig.getConfigurationSection("jobs").getKeys(false).contains(event.getLine(2).toUpperCase()) || customJobConfig.getConfigurationSection("customs").getKeys(false).contains(event.getLine(2).toUpperCase()))){
                        if (mainConfig.getBoolean("economy.active")){
                            econ.withdrawPlayer(event.getPlayer().getName(), mainConfig.getDouble("economy.makeSign"));
                        }
                        event.setLine(0, ChatColor.RED + "[Quit A Job]");
                        event.setLine(1, "");
                        event.setLine(2, event.getLine(2).toUpperCase());
                        event.setLine(3, "");
                        String sign = event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ();
                        signConfig.set("signs.quit." + sign, event.getLine(2));
                        saveConfigs(event.getPlayer());
                        event.getPlayer().sendMessage(ChatColor.GREEN + lang.getString("g.Signs.CreateSuccess"));
                    } else event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.JobNotFound"));
                } else event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.NoCreatePermissions"));
            }
        }
    }
    @EventHandler public void SignInteract(PlayerInteractEvent event){
        if (mainConfig.getBoolean("use_signs") == true){
            if (event.getAction().toString().equalsIgnoreCase("RIGHT_CLICK_BLOCK") && (event.getClickedBlock().getTypeId() == 63 || event.getClickedBlock().getTypeId() == 68)){
                Sign sign = (Sign) event.getClickedBlock().getState();
                String locstr = event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ();
                if (signConfig.getConfigurationSection("signs.get").getKeys(false).contains(locstr)){
                    if (event.getPlayer().hasPermission("MineJobs.signs.useGetSign")){
                        String[] args = new String[2];
                        args[0] = "getJob";
                        args[1] = sign.getLine(2);
                        PluginCommand execCommand = Bukkit.getServer().getPluginCommand("mj");
                        execCommand.execute(event.getPlayer(), "mj", args);
                    } else event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.NoGetPermissions"));
                } else if (signConfig.getConfigurationSection("signs.quit").getKeys(false).contains(locstr)){
                    if (event.getPlayer().hasPermission("MineJobs.signs.useQuitSign")){
                        String[] args = new String[2];
                        args[0] = "quitJob";
                        args[1] = sign.getLine(2);
                        PluginCommand execCommand = Bukkit.getServer().getPluginCommand("mj");
                        execCommand.execute(event.getPlayer(), "mj", args);
                    } else event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.NoQuitPermissions"));
                }
            }
        }
    }
    @EventHandler public void BlockBroken(BlockBreakEvent event) {
        if (mainConfig.getBoolean("use_signs") == true){
            String locstr = event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ();
            if ((event.getBlock().getTypeId() == 63 || event.getBlock().getTypeId() == 68) && (signConfig.getConfigurationSection("signs.get").getKeys(false).contains(locstr) || signConfig.getConfigurationSection("signs.quit").getKeys(false).contains(locstr))){
                if (event.getPlayer().hasPermission("MineJobs.signs.breakSign")){
                    if (mainConfig.getBoolean("economy.active")){
                        econ.withdrawPlayer(event.getPlayer().getName(), mainConfig.getDouble("economy.breakSign"));
                    }
                    signConfig.set("signs.get." + locstr, null);
                    signConfig.set("signs.quit." + locstr, null);
                    event.getPlayer().sendMessage(ChatColor.GREEN + lang.getString("g.Signs.BreakSuccess"));
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + lang.getString("g.Signs.NoBreakPermissions"));
                }
            }
        }
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && playerConfig.getConfigurationSection("players").getKeys(false).contains(event.getPlayer().getName().toLowerCase())){
            for (String job:playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".break").getKeys(false).contains(event.getBlock().getType().toString()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        double mult;
                        if (jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".tools").getKeys(false).contains(event.getPlayer().getItemInHand().getType().toString())){
                            mult = jobConfig.getDouble("jobs." + job.toUpperCase() + ".tools." + event.getPlayer().getItemInHand().getType().toString());
                        } else {
                            mult = 1.00;
                        }
                        double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".break." + event.getBlock().getType().toString());
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) econ.depositPlayer(event.getPlayer().getName(), value * mult);
                        if (mainConfig.getBoolean("breakOutput")){
                            event.getPlayer().sendMessage(ChatColor.GOLD + lang.getString("g.BlockBroken").replace("%VALUE%", String.valueOf(value * mult)).replace("%ITEM%", event.getBlock().getType().toString()));
                        }
                    } else if (jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".break").getKeys(false).contains(event.getBlock().getType().toString()+ "_" + event.getBlock().getData()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        double mult;
                        if (jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".tools").getKeys(false).contains(event.getPlayer().getItemInHand().getType().toString())){
                            mult = jobConfig.getDouble("jobs." + job.toUpperCase() + ".tools." + event.getPlayer().getItemInHand().getType().toString());
                        } else {
                            mult = 1.00;
                        }
                        double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".break." + event.getBlock().getType().toString()+ "_" + event.getBlock().getData());
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) econ.depositPlayer(event.getPlayer().getName(), value * mult);
                        if (mainConfig.getBoolean("breakOutput")){
                            event.getPlayer().sendMessage(ChatColor.GOLD + lang.getString("g.BlockBroken").replace("%VALUE%", String.valueOf(value * mult)).replace("%ITEM%", event.getBlock().getType().toString()));
                        }
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".break").getKeys(false).contains(event.getBlock().getType().toString()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        double mult;
                        if (customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".tools").getKeys(false).contains(event.getPlayer().getItemInHand().getType().toString())){
                            mult = customJobConfig.getDouble("customs." + job.toUpperCase() + ".tools." + event.getPlayer().getItemInHand().getType().toString());
                        } else {
                            mult = 1.00;
                        }
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".break." + event.getBlock().getType().toString());
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value * mult);
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) econ.depositPlayer(event.getPlayer().getName(), value * mult);
                        if (mainConfig.getBoolean("breakOutput")){
                            event.getPlayer().sendMessage(ChatColor.GOLD + lang.getString("g.BlockBroken").replace("%VALUE%", String.valueOf(value * mult)).replace("%ITEM%", event.getBlock().getType().toString()));
                        }
                    } else if (customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".break").getKeys(false).contains(event.getBlock().getType().toString() + "_" + event.getBlock().getData()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        double mult;
                        if (customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".tools").getKeys(false).contains(event.getPlayer().getItemInHand().getType().toString())){
                            mult = customJobConfig.getDouble("customs." + job.toUpperCase() + ".tools." + event.getPlayer().getItemInHand().getType().toString());
                        } else {
                            mult = 1.00;
                        }
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".break." + event.getBlock().getType().toString() + "_" + event.getBlock().getData());
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value * mult);
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) econ.depositPlayer(event.getPlayer().getName(), value * mult);
                        if (mainConfig.getBoolean("breakOutput")){
                            event.getPlayer().sendMessage(ChatColor.GOLD + lang.getString("g.BlockBroken").replace("%VALUE%", String.valueOf(value * mult)).replace("%ITEM%", event.getBlock().getType().toString()));
                        }
                    }
                }
            }
        }
    }
    @EventHandler public void BlockPlaced(BlockPlaceEvent event){
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && playerConfig.getConfigurationSection("players").getKeys(false).contains(event.getPlayer().getName().toLowerCase())){
            for (String job:playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".place").getKeys(false).contains(event.getBlock().getType().toString()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        boolean pay = true;
                        for (String breakJob:playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs")){
                            for (String item:jobConfig.getConfigurationSection("jobs." + breakJob.toUpperCase() + ".break").getKeys(false)){
                                if (item.equalsIgnoreCase(String.valueOf(event.getBlock().getType().toString())) && pay == true){
                                    pay = false;
                                }
                            }
                        }
                        if (pay == true){
                            double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".place." + event.getBlock().getType().toString());
                            econ.depositPlayer(event.getPlayer().getName(), value);
                        }
                    } else if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".place").getKeys(false).contains(event.getBlock().getType().toString() + "_" + event.getBlock().getData()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        boolean pay = true;
                        for (String breakJob:playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs")){
                            for (String item:jobConfig.getConfigurationSection("jobs." + breakJob.toUpperCase() + ".break").getKeys(false)){
                                if (item.equalsIgnoreCase(String.valueOf(event.getBlock().getType().toString() + "_" + event.getBlock().getData())) && pay == true){
                                    pay = false;
                                }
                            }
                        }
                        if (pay == true){
                            double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".place." + event.getBlock().getType().toString() + "_" + event.getBlock().getData());
                            econ.depositPlayer(event.getPlayer().getName(), value);
                        }
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".place").getKeys(false).contains(event.getBlock().getType().toString()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        boolean pay = true;
                        for (String breakJob:playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs")){
                            for (String item:customJobConfig.getConfigurationSection("customs." + breakJob.toUpperCase() + ".break").getKeys(false)){
                                if (item.equalsIgnoreCase(String.valueOf(event.getBlock().getType().toString())) && pay == true){
                                    pay = false;
                                }
                            }
                        }
                        if (pay == true){
                            double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".place." + event.getBlock().getType().toString());
                            econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value);
                            econ.depositPlayer(event.getPlayer().getName(), value);
                        }
                    } else if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".place").getKeys(false).contains(event.getBlock().getType().toString() + "_" + event.getBlock().getData()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getPlayer().getWorld().getName().toLowerCase())){
                        boolean pay = true;
                        for (String breakJob:playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs")){
                            for (String item:customJobConfig.getConfigurationSection("customs." + breakJob.toUpperCase() + ".break").getKeys(false)){
                                if (item.equalsIgnoreCase(String.valueOf(event.getBlock().getType().toString() + "_" + event.getBlock().getData())) && pay == true){
                                    pay = false;
                                }
                            }
                        }
                        if (pay == true){
                            double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".place." + event.getBlock().getType().toString() + "_" + event.getBlock().getData());
                            econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value);
                            econ.depositPlayer(event.getPlayer().getName(), value);
                        }
                    }
                }
            }
        }
        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) event.getBlock().setMetadata("MJ:".concat(event.getPlayer().getName()), new FixedMetadataValue(this, event.getPlayer().getName()));
    }
    @EventHandler public void MobKilled(EntityDeathEvent event){
        if(event.getEntity().getKiller() != null){
            String pName = event.getEntity().getKiller().getName().toLowerCase();
            if(!event.getEntity().getKiller().getGameMode().toString().equalsIgnoreCase("CREATIVE") && playerConfig.getConfigurationSection("players").getKeys(false).contains(pName)){
                for (String job:playerConfig.getStringList("players." + pName + ".jobs")) {
                    String Ajob = job.toUpperCase();
                    if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                        if(jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false).contains(event.getEntityType().toString()) && jobConfig.getStringList("jobs." + Ajob + ".worlds").contains(event.getEntity().getKiller().getWorld().getName().toLowerCase())){
                            if (!event.getEntity().hasMetadata("MJ:FAKE")){
                                double value = jobConfig.getDouble("jobs." + Ajob + ".mobs." + event.getEntityType().toString());
                                econ.depositPlayer(pName, value);
                                String entity;
                                if (event.getEntityType().toString().equalsIgnoreCase("PLAYER")){
                                    entity = event.getEntity().getType().getName();
                                    switch (mainConfig.getString("pklosses.enable")) {
                                        case "always":
                                            econ.withdrawPlayer(entity, mainConfig.getDouble("pklosses.loss"));
                                            break;
                                        case "job":
                                            if (jobConfig.getBoolean("jobs." + Ajob + ".pkl")) econ.withdrawPlayer(entity, mainConfig.getDouble("pklosses.loss"));
                                            break;
                                        case "never":
                                        default:
                                            break;
                                    }
                                } else entity = event.getEntityType().toString();
                                String msg = lang.getString("g.MobKilled").replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", entity);
                                event.getEntity().getKiller().sendMessage(ChatColor.GOLD + msg);
                            }
                        }
                    } else if(mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                        if(customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false).contains(event.getEntityType().toString()) && customJobConfig.getStringList("customs." + Ajob + ".worlds").contains(event.getEntity().getKiller().getWorld().getName().toLowerCase())){
                            if (!event.getEntity().hasMetadata("MJ:FAKE")){
                                double value = customJobConfig.getDouble("customs." + Ajob + ".mobs." + event.getEntityType().toString());
                                econ.withdrawPlayer(customJobConfig.getString("customs." + Ajob + ".owner"), value);
                                econ.depositPlayer(pName, value);
                                String entity;
                                if (event.getEntityType().toString().equalsIgnoreCase("PLAYER")){
                                    entity = event.getEntity().getType().getName();
                                    switch (mainConfig.getString("pklosses.enable")) {
                                        case "always":
                                            econ.withdrawPlayer(entity, mainConfig.getDouble("pklosses.loss"));
                                            break;
                                        case "job":
                                            if (customJobConfig.getBoolean("customs." + Ajob + ".pkl")) econ.withdrawPlayer(entity, mainConfig.getDouble("pklosses.loss"));
                                            break;
                                        case "never":
                                        default:
                                            break;
                                    }
                                } else entity = event.getEntityType().toString();
                                String msg = lang.getString("g.MobKilled").replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", entity);
                                event.getEntity().getKiller().sendMessage(ChatColor.GOLD + msg);
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler public void BlockOpened(InventoryOpenEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            Inventory inv = event.getInventory();
            if (inv instanceof FurnaceInventory){
                Furnace furnace = (Furnace) inv.getHolder();
                if (furnace != null && furnace.getBurnTime() == 0){
                    Block furnaceBlock = furnace.getBlock();
                    if (!furnaceBlock.hasMetadata(blockOwnerKey)) furnaceBlock.setMetadata(blockOwnerKey, new FixedMetadataValue(this, event.getPlayer().getName()));
                }
            } else if (inv instanceof BrewerInventory){
                BrewingStand brewer = (BrewingStand) inv.getHolder();
                if (brewer != null && brewer.getBrewingTime() == 0){
                    Block brewStand = brewer.getBlock();
                    if (!brewStand.hasMetadata(blockOwnerKey)) brewStand.setMetadata(blockOwnerKey, new FixedMetadataValue(this, event.getPlayer().getName()));
                }
            }
        }
    }
    @EventHandler public void BlockClosed(InventoryCloseEvent event){
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            Inventory inv = event.getInventory();
            if (inv instanceof FurnaceInventory){
                Furnace furnace = (Furnace) inv.getHolder();
                if (furnace != null && furnace.getBurnTime() == 0){
                    Block furnaceBlock = furnace.getBlock();
                    if (furnaceBlock.hasMetadata(blockOwnerKey)) furnaceBlock.removeMetadata(blockOwnerKey, this);
                }
            } else if (inv instanceof BrewerInventory){
                BrewingStand brewer = (BrewingStand) inv.getHolder();
                if (brewer != null && brewer.getBrewingTime() == 0){
                    Block brewStand = brewer.getBlock();
                    if (brewStand.hasMetadata(blockOwnerKey)) brewStand.removeMetadata(blockOwnerKey, this);
                }
            }
        }
    }
    @EventHandler public void FurnaceUsed(FurnaceSmeltEvent event){
        String playerName;
        try {
            List<MetadataValue> wop = event.getBlock().getMetadata(blockOwnerKey);
            playerName = wop.get(0).asString().toLowerCase();
        } catch (IndexOutOfBoundsException e){
            return;
        }
        if(playerConfig.getConfigurationSection("players").getKeys(false).contains(playerName)){
            for (String job:playerConfig.getStringList("players." + playerName + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".smelt").getKeys(false).contains(event.getSource().getType().toString()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                       double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".smelt." + event.getSource().getType().toString());
                       econ.depositPlayer(playerName, value);
                    } else if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".smelt").getKeys(false).contains(event.getSource().getType().toString() + "_" + event.getSource().getData()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                       double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".smelt." + event.getSource().getType().toString() + "_" + event.getSource().getData());
                       econ.depositPlayer(playerName, value);
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".smelt").getKeys(false).contains(event.getSource().getType().toString()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".smelt." + event.getSource().getType().toString());
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value);
                        econ.depositPlayer(playerName, value);
                    } else if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".smelt").getKeys(false).contains(event.getSource().getType().toString() + "_" + event.getSource().getData()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".smelt." + event.getSource().getType().toString()+ "_" + event.getSource().getData());
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value);
                        econ.depositPlayer(playerName, value);
                    }
                }
            }
        }
    }
    @EventHandler public void PotionBrewed(BrewEvent event){
        String playerName;
        try {
            List<MetadataValue> wop = event.getBlock().getMetadata(blockOwnerKey);
            playerName = wop.get(0).asString().toLowerCase();
        } catch (IndexOutOfBoundsException e){
            return;
        }
        if(playerConfig.getConfigurationSection("players").getKeys(false).contains(playerName)){
            for (String job:playerConfig.getStringList("players." + playerName + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".brew").getKeys(false).contains(event.getContents().getIngredient().getType().toString()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                       double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".brew." + event.getContents().getIngredient().getType().toString());
                       econ.depositPlayer(playerName, value);
                    } else if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".brew").getKeys(false).contains(event.getContents().getIngredient().getType().toString() + "_" + event.getContents().getIngredient().getData()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                       double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".brew." + event.getContents().getIngredient().getType().toString() + "_" + event.getContents().getIngredient().getData());
                       econ.depositPlayer(playerName, value);
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".brew").getKeys(false).contains(event.getContents().getIngredient().getType().toString()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".brew." + event.getContents().getIngredient().getType().toString());
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value);
                        econ.depositPlayer(playerName, value);
                    } else if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".brew").getKeys(false).contains(event.getContents().getIngredient().getType().toString() + "_" + event.getContents().getIngredient().getData()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getBlock().getWorld().getName().toLowerCase())){
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".brew." + event.getContents().getIngredient().getType().toString() + "_" + event.getContents().getIngredient().getData());
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value);
                        econ.depositPlayer(playerName, value);
                    }
                }
            }
        }
    }
    @EventHandler public void ToolEnchanted(EnchantItemEvent event){
        String playerName = event.getEnchanter().getName().toLowerCase();
        if(!event.getEnchanter().getGameMode().equals(GameMode.CREATIVE) && playerConfig.getConfigurationSection("players").getKeys(false).contains(playerName)){
            for (String job:playerConfig.getStringList("players." + playerName + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getBoolean("jobs." + job.toUpperCase() + ".enchant.active") == true && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getEnchantBlock().getWorld().getName().toLowerCase())){
                       double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".enchant.payPerLevel");
                       econ.depositPlayer(playerName, value * event.getExpLevelCost());
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getBoolean("customs." + job.toUpperCase() + ".enchant.active") == true && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getEnchantBlock().getWorld().getName().toLowerCase())){
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".enchant.payPerLevel");
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value * event.getExpLevelCost());
                        econ.depositPlayer(playerName, value * event.getExpLevelCost());
                    }
                }
            }
        }
    }
    @EventHandler public void ItemCrafted(CraftItemEvent event){
        String playerName = event.getWhoClicked().getName().toLowerCase();
        if(!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && playerConfig.getConfigurationSection("players").getKeys(false).contains(playerName)){
            for (String job:playerConfig.getStringList("players." + playerName + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".craft").getKeys(false).contains(event.getCurrentItem().getType().toString()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getWhoClicked().getWorld().getName().toLowerCase())){
                        if (event.isShiftClick()){
                            event.setCancelled(true);
                            return;
                        }
                        double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".craft." + event.getCurrentItem().getType().toString());
                        econ.depositPlayer(playerName, value * event.getCurrentItem().getAmount());
                    } else if(jobConfig.getConfigurationSection("jobs." + job.toUpperCase() + ".craft").getKeys(false).contains(event.getCurrentItem().getType().toString() + "_" + event.getCurrentItem().getData()) && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getWhoClicked().getWorld().getName().toLowerCase())){
                        if (event.isShiftClick()){
                            event.setCancelled(true);
                            return;
                        }
                        double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".craft." + event.getCurrentItem().getType().toString() + "_" + event.getCurrentItem().getData());
                        econ.depositPlayer(playerName, value * event.getCurrentItem().getAmount());
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".craft").getKeys(false).contains(event.getCurrentItem().getType().toString()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getWhoClicked().getWorld().getName().toLowerCase())){
                        if (event.isShiftClick()){
                            event.setCancelled(true);
                            return;
                        }
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".craft." + event.getCurrentItem().getType().toString());
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value * event.getCurrentItem().getAmount());
                        econ.depositPlayer(playerName, value * event.getCurrentItem().getAmount());
                    } else if(customJobConfig.getConfigurationSection("customs." + job.toUpperCase() + ".craft").getKeys(false).contains(event.getCurrentItem().getType().toString() + "_" + event.getCurrentItem().getData()) && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getWhoClicked().getWorld().getName().toLowerCase())){
                        if (event.isShiftClick()){
                            event.setCancelled(true);
                            return;
                        }
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".craft." + event.getCurrentItem().getType().toString() + "_" + event.getCurrentItem().getData());
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value * event.getCurrentItem().getAmount());
                        econ.depositPlayer(playerName, value * event.getCurrentItem().getAmount());
                    }
                }
            }
        }
    }
    @EventHandler public void BlockPushed(BlockPistonExtendEvent event){
        for (Block bl:event.getBlocks()){
            if (bl.getPistonMoveReaction().toString().equals("MOVE")){
                for (String nm:playerConfig.getConfigurationSection("players").getKeys(false)){
                    if (bl.hasMetadata("MJ:" + nm)){
                        Block blN = bl.getWorld().getBlockAt(bl.getLocation().add(event.getDirection().getModX(), event.getDirection().getModY(), event.getDirection().getModZ()));
                        blN.setMetadata("MJ:" + nm, new FixedMetadataValue(this, nm));
                    }
                }
            }
        }
    }
    @EventHandler public void BlockPulled(BlockPistonRetractEvent event){
        if (event.getBlock().getPistonMoveReaction().toString().equals("MOVE")){
            for (String nm:playerConfig.getConfigurationSection("players").getKeys(false)){
                if (event.getBlock().hasMetadata("MJ:" + nm)){
                    Block blN = event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().add(event.getDirection().getModX(), event.getDirection().getModY(), event.getDirection().getModZ()));
                    blN.setMetadata("MJ:" + nm, new FixedMetadataValue(this, nm));
                }
            }
        }
    }
    @EventHandler public void MobSpawned(CreatureSpawnEvent event){
        if (!mainConfig.getBoolean("spawnerCash")){
            if (event.getSpawnReason().toString().equals("SPAWNER") || event.getSpawnReason().toString().equals("SPAWNER_EGG") || event.getSpawnReason().toString().equals("EGG")){
                event.getEntity().setMetadata("MJ:FAKE", new FixedMetadataValue(this, "FAKE"));
            }
        }
    }
    @EventHandler public void PlayerFished(PlayerFishEvent event){
        if (event.getCaught() != null){
            if (!event.getPlayer().getGameMode().toString().equals("CREATIVE")){
                String pName = event.getPlayer().getName().toLowerCase();
                if (playerConfig.getConfigurationSection("players").getKeys(false).contains(pName)){
                    for (String job:playerConfig.getStringList("players." + pName + ".jobs")){
                        String Ajob = job.toUpperCase();
                        if (jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob) && jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false).contains(event.getCaught().getType().toString())){
                            double value = jobConfig.getDouble("jobs." + Ajob + ".fish." + event.getCaught().getType().toString());
                            econ.depositPlayer(pName, value);
                            String entity = event.getCaught().getType().toString();
                            String msg = lang.getString("g.FishCaught").replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", entity);
                            event.getPlayer().sendMessage(ChatColor.GOLD + msg);
                        } else if (customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob) && customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false).contains(event.getCaught().getType().toString())){
                            double value = customJobConfig.getDouble("customs." + Ajob + ".fish." + event.getCaught().getType().toString());
                            econ.withdrawPlayer(customJobConfig.getString("customs." + Ajob + ".owner"), value);
                            econ.depositPlayer(pName, value);
                            String entity = event.getCaught().getType().toString();
                            String msg = lang.getString("g.FishCaught").replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", entity);
                            event.getPlayer().sendMessage(ChatColor.GOLD + msg);
                        }
                    }
                }
            }
        }
    }
    @EventHandler public void NewPlayerJoin(PlayerJoinEvent event){
        if (!playerConfig.getConfigurationSection("players").getKeys(false).contains(event.getPlayer().getName().toLowerCase())){
            playerConfig.createSection("players." + event.getPlayer().getName().toLowerCase() + ".jobs");
            for (String job:mainConfig.getStringList("defaultJobs")){
                List newList = playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs");
                newList.add(job.toUpperCase());
                playerConfig.set("players." + event.getPlayer().getName().toLowerCase() + ".jobs", newList);
            }
            for (String job:mainConfig.getStringList("forcedJobs")){
                List newList = playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs");
                newList.add(job.toUpperCase());
                playerConfig.set("players." + event.getPlayer().getName().toLowerCase() + ".jobs", newList);
            }
            playerConfig.createSection("players." + event.getPlayer().getName().toLowerCase() + ".invites");
            saveConfigs(event.getPlayer());
        }
    }
}

/*           To-Do List
 - Job leveling                                                     ----------
 - Regions Support                                                  ----------
 + Goals and limits                                                 ----------

        Planning / Changelog
jobs:
    JOB:
        GL:
            enabled: <true | false>
            timeFrame: <#><m | h | d>
            minAdds: 100
            maxAdds: 100
            payOn: <each | min | #>

[v3.0.2]
    Fixed the Fishing jobs that I pretty much failed at the first time around.
        General rule of thumb: The only FISH you can list is DROPPED_ITEM, but you can also list mobs like ZOMBIE.
    Added command pricing to Main Config File
        + Added economy section to config.yml containing nodes: active, getJob, quitJob, createJob, deleteJob, renameJob, setJobOwner, lockJob, makeSign, breakSign
    Added optional forced server jobs to Main Config File
        + Added StringList "forcedJobs" to config.yml
            MUST be filled out like a world list. DO NOT just replace [] with a job name.
    Added optional default server jobs to Main Config File
        + Added StringList "defaultJobs" to config.yml
            MUST be filled out like a world list. DO NOT just replace [] with a job name.
    Added OPTIONAL block break text output
        * Added g.BlockBroken to EN.yml and template.yml
        + Added boolean "breakOutput" to config.yml
    Updated Config Scanner for now fields.
        * Added Errors.ConfigScanner.e055 -> e079 to both EN.yml and template.yml
*/