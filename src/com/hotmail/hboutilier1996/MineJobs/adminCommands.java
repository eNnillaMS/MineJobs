package com.hotmail.hboutilier1996.MineJobs;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
public class adminCommands implements CommandExecutor{
    public static final Logger log = Logger.getLogger("Minecraft");
    private final MineJobs main;
    public adminCommands(MineJobs main) {
	this.main = main;
    }
    @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
            showHelp(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("create")){
            create(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("upgrade")){
            if (main.mainConfig.getBoolean("customJobs") == true){
                upgrade(sender, cmd, label, args);
            } else sender.sendMessage(ChatColor.BLUE + main.lang.getString("Errors.CustomsDisabledWarning"));
        } else if(args[0].equalsIgnoreCase("delete")){
            delete(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("rename")){
            rename(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("setMax")){
            setMax(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("addObj")){
            addObj(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("editObj")){
            editObj(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("delObj")){
            delObj(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("addWorld")){
            addWorld(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("rmWorld")){
            rmWorld(sender, cmd, label, args);
        } else if(args[0].equalsIgnoreCase("reload")){
            reload(sender, cmd, label, args);
        } else {
            showHelp(sender, cmd, label, args);
        }
        return true;
    }
    public void showHelp(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("mja")){
            if (sender.hasPermission("MineJobs.admin.help")){
                sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
                if(sender.hasPermission("MineJobs.admin.create")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Ecreate"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.create"));
                }
                if(sender.hasPermission("MineJobs.admin.upgrade") && main.mainConfig.getBoolean("customJobs") == true){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Eupgrade"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.upgrade"));
                }
                if(sender.hasPermission("MineJobs.admin.delete")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Edelete"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.delete"));
                }
                if(sender.hasPermission("MineJobs.admin.rename")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Erename"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.rename"));
                }
                if(sender.hasPermission("MineJobs.admin.setMax")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Esetmax"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.setmax"));
                }
                if(sender.hasPermission("MineJobs.admin.addObj")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Eaddobj"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.addobj"));
                }
                if(sender.hasPermission("MineJobs.admin.editObj")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Eeditobj"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.editobj"));
                }
                if(sender.hasPermission("MineJobs.admin.delObj")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Edelobj"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.delobj"));
                }
                if(sender.hasPermission("MineJobs.admin.addWorld")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Eaddworld"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.addworld"));
                }
                if(sender.hasPermission("MineJobs.admin.rmWorld")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.Ermworld"));
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.a.rmworld"));
                }
                sender.sendMessage(main.lang.getString("HelpOutput.a.more"));
                sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("HelpOutput.error"));
        }
    }
    public void create(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.create")){
            if (args.length < 2 || args[1].equalsIgnoreCase("?") || args.length > 2){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.create.cua"));
            } else if (args.length == 2){
                String Ajob = args[1].toUpperCase();
                if (!main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob) && !main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    main.jobConfig.createSection("jobs." + Ajob);
                    main.jobConfig.set("jobs." + Ajob + ".maxplayers", 0);
                    main.jobConfig.createSection("jobs." + Ajob + ".break");
                    main.jobConfig.createSection("jobs." + Ajob + ".place");
                    main.jobConfig.createSection("jobs." + Ajob + ".mobs");
                    main.jobConfig.createSection("jobs." + Ajob + ".fish");
                    main.jobConfig.createSection("jobs." + Ajob + ".tools");
                    main.jobConfig.createSection("jobs." + Ajob + ".craft");
                    main.jobConfig.createSection("jobs." + Ajob + ".smelt");
                    main.jobConfig.createSection("jobs." + Ajob + ".brew");
                    main.jobConfig.createSection("jobs." + Ajob + ".enchant");
                    main.jobConfig.set("jobs." + Ajob + ".enchant.active", false);
                    main.jobConfig.set("jobs." + Ajob + ".enchant.payPerLevel", 0);
                    main.jobConfig.createSection("jobs." + Ajob + ".worlds");
                    main.jobConfig.createSection("jobs." + Ajob + ".pkl");
                    main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.create.success").replace("%JOB%", Ajob));
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.create.success2a"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobExists"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.create.NoPermissions"));
    }
    public void upgrade(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.upgrade")){
            if (args.length != 2 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.upgrade.cu"));
            } else {
                String Ajob = args[1].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    main.jobConfig.createSection("jobs." + Ajob);
                    main.jobConfig.set("jobs." + Ajob + ".maxplayers", main.customJobConfig.getInt("customs." + Ajob + ".maxplayers"));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".break").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".break." + item, main.customJobConfig.getDouble("customs." + Ajob + ".break." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".place").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".place." + item, main.customJobConfig.getDouble("customs." + Ajob + ".place." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".mobs." + item, main.customJobConfig.getDouble("customs." + Ajob + ".mobs." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".fish." + item, main.customJobConfig.getDouble("customs." + Ajob + ".fish." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".tools").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".tools." + item, main.customJobConfig.getDouble("customs." + Ajob + ".tools." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".craft." + item, main.customJobConfig.getDouble("customs." + Ajob + ".craft." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".smelt." + item, main.customJobConfig.getDouble("customs." + Ajob + ".smelt." + item));
                    for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false)) main.jobConfig.set("jobs." + Ajob + ".brew." + item, main.customJobConfig.getDouble("customs." + Ajob + ".brew." + item));
                    main.jobConfig.set("jobs." + Ajob + ".enchant.active", main.customJobConfig.getStringList("customs." + Ajob + ".enchant.active"));
                    main.jobConfig.set("jobs." + Ajob + ".enchant.payPerLevel", main.customJobConfig.getStringList("customs." + Ajob + ".enchant.payPerLevel"));
                    main.jobConfig.set("jobs." + Ajob + ".worlds", main.customJobConfig.getStringList("customs." + Ajob + ".worlds"));
                    main.jobConfig.set("jobs." + Ajob + ".pkl", main.customJobConfig.getBoolean("customs." + Ajob + ".pkl"));
                    main.customJobConfig.set("customs." + Ajob, null);
                    main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.upgrade.success").replace("%JOB%", Ajob));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.upgrade.NoPermissions"));
    }
    public void delete(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.delete")){
            if (args.length < 2 || args[1].equalsIgnoreCase("?") || args.length > 2){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.delete.cua"));
            } else if (args.length == 2){
                String Ajob = args[1].toUpperCase();
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    main.jobConfig.set("jobs." + Ajob, null);
                    for(String plyr:main.playerConfig.getConfigurationSection("players").getKeys(false)){
                        if (main.playerConfig.getStringList("players." + plyr + ".jobs").contains(Ajob)){
                            List newList = main.playerConfig.getStringList("players." + plyr + ".jobs");
                            newList.remove(Ajob);
                            main.playerConfig.set("players." + plyr + ".jobs", newList);
                        }
                    }
                    main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.delete.success").replace("%JOB%", Ajob));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.delete.NoPermissions"));
    }
    public void rename(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.rename")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?") || args.length > 3){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.rename.cua"));
            } else {
                String Ajob = args[1].toUpperCase();
                String AjobNew = args[2].toUpperCase();
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    main.jobConfig.createSection("jobs." + AjobNew);
                    main.jobConfig.set("jobs." + AjobNew + ".maxplayers", main.jobConfig.getInt("jobs." + Ajob + ".maxplayers"));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".break").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".break." + item, main.jobConfig.getDouble("jobs." + Ajob + ".break." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".place").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".place." + item, main.jobConfig.getDouble("jobs." + Ajob + ".place." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".mobs." + item, main.jobConfig.getDouble("jobs." + Ajob + ".mobs." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".fish." + item, main.jobConfig.getDouble("jobs." + Ajob + ".fish." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".tools").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".tools." + item, main.jobConfig.getDouble("jobs." + Ajob + ".tools." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".craft." + item, main.jobConfig.getDouble("jobs." + Ajob + ".craft." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".smelt." + item, main.jobConfig.getDouble("jobs." + Ajob + ".smelt." + item));
                    for (String item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false)) main.jobConfig.set("jobs." + AjobNew + ".brew." + item, main.jobConfig.getDouble("jobs." + Ajob + ".brew." + item));
                    main.jobConfig.set("jobs." + AjobNew + ".enchant.active", main.jobConfig.getStringList("jobs." + Ajob + ".enchant.active"));
                    main.jobConfig.set("jobs." + AjobNew + ".enchant.payPerLevel", main.jobConfig.getStringList("jobs." + Ajob + ".enchant.payPerLevel"));
                    main.jobConfig.set("jobs." + AjobNew + ".worlds", main.jobConfig.getStringList("jobs." + Ajob + ".worlds"));
                    main.jobConfig.set("jobs." + AjobNew + ".pkl", main.jobConfig.getBoolean("jobs." + Ajob + ".pkl"));
                    main.jobConfig.set("jobs." + Ajob, null);
                    for(String plyr:main.playerConfig.getConfigurationSection("players").getKeys(false)){
                        if (main.playerConfig.getStringList("players." + plyr + ".jobs").contains(Ajob)){
                            List newList = main.playerConfig.getStringList("players." + plyr + ".jobs");
                            newList.remove(Ajob);
                            newList.add(AjobNew);
                            main.playerConfig.set("players." + plyr + ".jobs", newList);
                        }
                    }
                    main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.rename.success").replace("%JOB%", Ajob).replace("%JOB2%", AjobNew));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.rename.NoPermissions"));
    }
    public void setMax(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.setMax")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?") || args.length > 3){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.setmax.cua"));
            } else {
                String Ajob = args[1].toUpperCase();
                int Anum;
                try {
                    Anum = Integer.valueOf(args[2]);
                } catch (ClassCastException e){
                    sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setmax.MustBeNumber"));
                    return;
                }
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    main.jobConfig.set("jobs." + Ajob + ".maxplayers", Anum);
                    main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.setmax.success").replace("%VALUE%", String.valueOf(Anum)));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setmax.NoPermissions"));
    }
    public void addObj(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.addObj")){
            if (args.length <= 2 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.addobj.cua"));
            } else if (args.length >= 3){
                String Ajob = args[1].toUpperCase();
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    for(int i = 0; i <= args.length - (3); i++){
                        int ind1 = args[2 + i].indexOf(":");
                        int ind2 = args[2 + i].lastIndexOf(":");
                        String type = args[2 + i].substring(0, ind1).toLowerCase();
                        String item = args[2 + i].substring(ind1 + 1, ind2).toUpperCase();
                        double value;
                        try {
                            value = Double.valueOf(args[2 + i].substring(ind2 + 1));
                        } catch (ClassCastException e){
                            sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addobj.MustBeNumber"));
                            return;
                        }
                        if (type.equalsIgnoreCase("break") || type.equalsIgnoreCase("place") || type.equalsIgnoreCase("mobs") || type.equalsIgnoreCase("fish") || type.equalsIgnoreCase("tools") || type.equalsIgnoreCase("craft") || type.equalsIgnoreCase("smelt") || type.equalsIgnoreCase("brew")){
                            if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + "." + type).getKeys(false).contains(item)){
                                main.jobConfig.set("jobs." + Ajob + "." + type + "." + item, value);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.addobj.success").replace("%ITEM%", item).replace("%JOB%", Ajob));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.ItemExists").replace("%ITEM%", item));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadType").replace("%TYPE%", type).replace("%ITEM%", item));
                    }
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addobj.NoPermissions"));
    }
    public void delObj(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.delObj")){
            if (args.length <= 2 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.delobj.cua"));
            } else if (args.length > 2){
                String Ajob = args[1].toUpperCase();
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    for(int i = 0; i <= args.length - (3); i++){
                        int ind1 = args[2 + i].indexOf(":");
                        String type = args[2 + i].substring(0, ind1).toLowerCase();
                        String item = args[2 + i].substring(ind1 + 1).toUpperCase();
                        if (type.equalsIgnoreCase("break") || type.equalsIgnoreCase("place") || type.equalsIgnoreCase("mobs") || type.equalsIgnoreCase("fish") || type.equalsIgnoreCase("tools") || type.equalsIgnoreCase("craft") || type.equalsIgnoreCase("smelt") || type.equalsIgnoreCase("brew")){
                            if (main.jobConfig.getConfigurationSection("jobs." + Ajob + "." + type).getKeys(false).contains(item)){
                                main.jobConfig.set("jobs." + Ajob + "." + type + "." + item, null);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.delobj.success").replace("%ITEM%", item).replace("%JOB%", Ajob));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.ItemNotFound").replace("%ITEM%", item));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadType").replace("%TYPE%", type).replace("%ITEM%", item));
                    }
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.delobj.NoPermissions"));
    }
    public void editObj(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.editObj")){
            if (args.length < 5 || args[1].equalsIgnoreCase("?") || args.length > 5){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.editobj.cua"));
            } else if (args.length == 5){
                String Ajob = args[1].toUpperCase();
                String Atype = args[2].toLowerCase();
                String Aitem = args[3].toUpperCase();
                String bool = "wop";
                double Anum = -1;
                if (args[4].equalsIgnoreCase("true") || args[4].equalsIgnoreCase("false")){
                    bool = args[4].toLowerCase();
                } else {
                    try {
                        Anum = Double.valueOf(args[4]);
                    } catch (ClassCastException e) {
                        sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.MustBeNumber"));
                        return;
                    }
                }
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    if (Atype.equalsIgnoreCase("break") || Atype.equalsIgnoreCase("place") || Atype.equalsIgnoreCase("mobs") || Atype.equalsIgnoreCase("fish") || Atype.equalsIgnoreCase("tools") || Atype.equalsIgnoreCase("craft") || Atype.equalsIgnoreCase("smelt") || Atype.equalsIgnoreCase("brew")){
                        if (main.jobConfig.getConfigurationSection("jobs." + Ajob + "." + Atype).getKeys(false).contains(Aitem)){
                            if (Anum != -1){
                                double oldValue = main.jobConfig.getDouble("jobs." + Ajob + "." + Atype + "." + Aitem);
                                main.jobConfig.set("jobs." + Ajob + "." + Atype + "." + Aitem, Anum);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.editobj.success").replace("%ITEM%", Aitem).replace("%VALUE%", String.valueOf(oldValue)).replace("%VALUE2%", String.valueOf(Anum)).replace("%JOB%", Ajob).replace("%TYPE%", Atype));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.MustBeNumber"));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.ItemNotFound").replace("%ITEM%", Aitem));
                    } else if(Atype.equalsIgnoreCase("enchant")) {
                        if (Aitem.equalsIgnoreCase("active")){
                            if (!bool.equals("wop")){
                                main.jobConfig.set("jobs." + Ajob + ".enchant.active", Boolean.valueOf(bool));
                                main.saveConfigs(sender);
                                switch (String.valueOf(main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active"))){
                                    case "true":
                                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.editobj.EnchantOn"));
                                        break;
                                    case "false":
                                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.editobj.EnchantOff"));
                                        break;
                                }
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.BadBool"));
                        } else if (Aitem.equalsIgnoreCase("payPerLevel")){
                            if (Anum != -1){
                                double oldValue = main.jobConfig.getDouble("jobs." + Ajob + ".enchant.payPerLevel");
                                main.jobConfig.set("jobs." + Ajob + ".enchant.payPerLevel", Anum);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.editobj.PayPerLevel").replace("%VALUE%", String.valueOf(oldValue)).replace("%VALUE2%", String.valueOf(Anum)).replace("%JOB%", Ajob));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.MustBeNumber"));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.BadItem"));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadType").replace("%TYPE%", Atype).replace(": %ITEM%", ""));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.NoPermissions"));
    }
    public void addWorld(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.addWorld")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.addworld.cua"));
            } else if (args.length >= 3){
                String Ajob = args[1].toUpperCase();
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    for(int i = 0; i <= args.length - (3); i++){
                        String world = args[2 + i].toLowerCase();
                        if (!main.jobConfig.getStringList("jobs." + Ajob + ".worlds").contains(world)){
                            List newList = main.jobConfig.getStringList("jobs." + Ajob + ".worlds");
                            newList.add(world);
                            main.jobConfig.set("jobs." + Ajob + ".worlds", newList);
                            main.saveConfigs(sender);
                            sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.addworld.success").replace("%WORLD%", world).replace("%JOB%", Ajob));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.WorldExists").replace("%WORLD%", world));
                    }
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addworld.NoPermissions"));
    }
    public void rmWorld(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.rmWorld")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?") || args.length > 3){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.rmworld.cua"));
            } else if (args.length == 3){
                String job = args[1].toUpperCase();
                String world = args[2].toLowerCase();
                if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job)){
                    if (main.jobConfig.getStringList("jobs." + job + ".worlds").contains(world)){
                        List worlds = main.jobConfig.getStringList("jobs." + job + ".worlds");
                        worlds.remove(world);
                        main.jobConfig.set("jobs." + job + ".worlds", worlds);
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.rmworld.success").replace("%WORLD%", world).replace("%JOB%", job));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.WorldNotListed"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.rmworld.NoPermissions"));
    }
    public void reload(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.reload")){
            if (main.loadConfigs()){
                sender.sendMessage(ChatColor.GOLD + main.lang.getString("ac.reload.Success"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.reload.Failed"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.reload.NoPermissions"));
    }
}