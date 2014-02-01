package com.hotmail.hboutilier1996.MineJobs;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
public class customsCommands implements CommandExecutor{
    public static final Logger log = Logger.getLogger("Minecraft");
    private final MineJobs main;
    public customsCommands(MineJobs main) {
		this.main = main;
    }
    @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (main.mainConfig.getBoolean("customJobs") == true){
            if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
                showHelp(sender, cmd, label, args);
            } else if(args[0].equalsIgnoreCase("create")){
                create(sender, cmd, label, args);
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
            } else if(args[0].equalsIgnoreCase("kick")){
                kick(sender, cmd, label, args);
            } else if(args[0].equalsIgnoreCase("invite")){
                invite(sender, cmd, label, args);
            } else if(args[0].equalsIgnoreCase("lock")){
                lock(sender, cmd, label, args);
            } else if(args[0].equalsIgnoreCase("setOwner")){
                setOwner(sender, cmd, label, args);
            } else {
                showHelp(sender, cmd, label, args);
            }
        } else sender.sendMessage(ChatColor.BLUE + main.lang.getString("Error.CustomsDisabledWarning"));
        return true;
    }
    public void showHelp(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("mjc")){
             if (sender.hasPermission("MineJobs.customs.help")){
                sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
                if(sender.hasPermission("MineJobs.customs.create")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Ecreate"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.create"));
                }
                if(sender.hasPermission("MineJobs.customs.delete")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Edelete"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.delete"));
                }
                if(sender.hasPermission("MineJobs.customs.rename")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Erename"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.rename"));
                }
                if(sender.hasPermission("MineJobs.customs.setOwner")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Esetowner"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.setowner"));
                }
                if(sender.hasPermission("MineJobs.customs.setMax")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Esetmax"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.setmax"));
                }
                if(sender.hasPermission("MineJobs.customs.addObj")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Eaddobj"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.addobj"));
                }
                if(sender.hasPermission("MineJobs.customs.editObj")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Eeditobj"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.editobj"));
                }
                if(sender.hasPermission("MineJobs.customs.delObj")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Edelobj"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.delobj"));
                }
                if(sender.hasPermission("MineJobs.customs.addWorld")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Eaddworld"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.addworld"));
                }
                if(sender.hasPermission("MineJobs.customs.rmWorld")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Ermworld"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.rmworld"));
                }
                if(sender.hasPermission("MineJobs.customs.lock")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Elock"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.lock"));
                }
                if(sender.hasPermission("MineJobs.customs.invite")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Einvite"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.invite"));
                }
                if(sender.hasPermission("MineJobs.customs.kick")){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.Ekick"));
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.c.kick"));
                }
                sender.sendMessage(main.lang.getString("HelpOutput.c.more"));
                sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("HelpOutput.error"));
        }
    }
    public void create(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.create")){
            if (args.length < 2 || args[1].equalsIgnoreCase("?") || args.length > 2){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.create.cuc"));
            } else if (args.length == 2){
                String Ajob = args[1].toUpperCase();
                if (!main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob) && !main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (main.mainConfig.getBoolean("economy.active")){
                        MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.createJob"));
                    }
                    main.customJobConfig.createSection("customs." + Ajob);
                    main.customJobConfig.set("customs." + Ajob + ".owner", sender.getName().toLowerCase());
                    main.customJobConfig.set("customs." + Ajob + ".locked", true);
                    main.customJobConfig.set("customs." + Ajob + ".maxplayers", 0);
                    main.customJobConfig.createSection("customs." + Ajob + ".break");
                    main.customJobConfig.createSection("customs." + Ajob + ".place");
                    main.customJobConfig.createSection("customs." + Ajob + ".mobs");
                    main.customJobConfig.createSection("customs." + Ajob + ".fish");
                    main.customJobConfig.createSection("customs." + Ajob + ".tools");
                    main.customJobConfig.createSection("customs." + Ajob + ".craft");
                    main.customJobConfig.createSection("customs." + Ajob + ".smelt");
                    main.customJobConfig.createSection("customs." + Ajob + ".brew");
                    main.customJobConfig.createSection("customs." + Ajob + ".enchant");
                    main.customJobConfig.set("customs." + Ajob + ".enchant.active", false);
                    main.customJobConfig.set("customs." + Ajob + ".enchant.payPerLevel", 0);
                    main.customJobConfig.createSection("customs." + Ajob + ".worlds");
                    main.customJobConfig.createSection("customs." + Ajob + ".pkl");
                    main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.create.success").replace("%JOB%", Ajob));
                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.create.success2c"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobExists"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.create.NoPermissions"));
    }
    public void delete(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.delete")){
            if (args.length < 2 || args[1].equalsIgnoreCase("?") || args.length > 2){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.delete.cuc"));
            } else if (args.length == 2){
                String Ajob = args[1].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (main.customJobConfig.getString("customs." + Ajob + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.delete.others")){
                        if (main.mainConfig.getBoolean("economy.active")){
                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.deleteJob"));
                        }
                        main.customJobConfig.set("customs." + Ajob, null);
                        for(String plyr:main.playerConfig.getConfigurationSection("players").getKeys(false)){
                            if (main.playerConfig.getStringList("players." + plyr + ".jobs").contains(Ajob)){
                                List newList = main.playerConfig.getStringList("players." + plyr + ".jobs");
                                newList.remove(Ajob);
                                main.playerConfig.set("players." + plyr + ".jobs", newList);
                            }
                        }
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.delete.success").replace("%JOB%", Ajob));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.delete.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.delete.NoPermissions"));
    }
    public void rename(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.rename")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?") || args.length > 3){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.rename.cuc"));
            } else {
                String Ajob = args[1].toUpperCase();
                String Anew = args[2].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (main.customJobConfig.getString("customs." + Ajob + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.rename.others")){
                        if (main.mainConfig.getBoolean("economy.active")){
                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.renameJob"));
                        }
                        main.customJobConfig.createSection("customs." + Anew);
                        main.customJobConfig.set("customs." + Anew + ".owner", main.customJobConfig.getString("customs." + Ajob + ".owner"));
                        main.customJobConfig.set("customs." + Anew + ".maxplayers", main.customJobConfig.getInt("customs." + Ajob + ".maxplayers"));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".break").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".break." + item, main.customJobConfig.getDouble("customs." + Ajob + ".break." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".place").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".place." + item, main.customJobConfig.getDouble("customs." + Ajob + ".place." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".mobs." + item, main.customJobConfig.getDouble("customs." + Ajob + ".mobs." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".fish." + item, main.customJobConfig.getDouble("customs." + Ajob + ".fish." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".tools").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".tools." + item, main.customJobConfig.getDouble("customs." + Ajob + ".tools." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".craft." + item, main.customJobConfig.getDouble("customs." + Ajob + ".craft." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".smelt." + item, main.customJobConfig.getDouble("customs." + Ajob + ".smelt." + item));
                        for (String item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false)) main.customJobConfig.set("customs." + Anew + ".brew." + item, main.customJobConfig.getDouble("customs." + Ajob + ".brew." + item));
                        main.customJobConfig.set("customs." + Anew + ".enchant.active", main.customJobConfig.getStringList("customs." + Ajob + ".enchant.active"));
                        main.customJobConfig.set("customs." + Anew + ".enchant.payPerLevel", main.customJobConfig.getStringList("customs." + Ajob + ".enchant.payPerLevel"));
                        main.customJobConfig.set("customs." + Anew + ".worlds", main.customJobConfig.getStringList("customs." + Ajob + ".worlds"));
                        main.customJobConfig.set("customs." + Anew + ".pkl", main.customJobConfig.getBoolean("customs." + Ajob + ".pkl"));
                        main.customJobConfig.set("customs." + Ajob, null);
                        for(String plyr:main.playerConfig.getConfigurationSection("players").getKeys(false)){
                            if (main.playerConfig.getStringList("players." + plyr + ".jobs").contains(Ajob)){
                                List newList = main.playerConfig.getStringList("players." + plyr + ".jobs");
                                newList.remove(Ajob);
                                newList.add(Anew);
                                main.playerConfig.set("players." + plyr + ".jobs", newList);
                            }
                        }
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.rename.success").replace("%JOB%", Ajob).replace("%JOB2%", Anew));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.rename.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.rename.NoPermissions"));
    }
    public void setMax(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.setMax")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?") || args.length > 3){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.setmax.cuc"));
            } else {
                String Ajob = args[1].toUpperCase();
                int Anum;
                try {
                    Anum = Integer.valueOf(args[2]);
                } catch (ClassCastException e){
                    sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setmax.MustBeNumber"));
                    return;
                }
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (main.customJobConfig.getString("customs." + Ajob + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.setMax.others")){
                        main.customJobConfig.set("customs." + Ajob + ".maxplayers", Anum);
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.setmax.success").replace("%VALUE%", String.valueOf(Anum)));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setmax.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setmax.NoPermissions"));
    }
    public void addObj(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.addObj")){
            if (args.length <= 2 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.addobj.cuc"));
            } else if (args.length >= 3){
                String job = args[1].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job)){
                    if (main.customJobConfig.getString("customs." + job + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.addObj.others")){
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
                                if (!main.customJobConfig.getConfigurationSection("customs." + job + "." + type).getKeys(false).contains(item)){
                                    main.customJobConfig.set("customs." + job + "." + type + "." + item, value);
                                    main.saveConfigs(sender);
                                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.addobj.success").replace("%ITEM%", item).replace("%JOB%", job));
                                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.ItemExists").replace("%ITEM%", item));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadType").replace("%TYPE%", type).replace("%ITEM%", item));
                        }
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addobj.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addobj.NoPermissions"));
    }
    public void delObj(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.delObj")){
            if (args.length <= 2 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.delobj.cuc"));
            } else if (args.length > 2){
                String job = args[1].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job)){
                    if (main.customJobConfig.getString("customs." + job + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.delObj.others")){
                        for(int i = 0; i <= args.length - (3); i++){
                            int ind1 = args[2 + i].indexOf(":");
                            String type = args[2 + i].substring(0, ind1).toLowerCase();
                            String item = args[2 + i].substring(ind1 + 1).toUpperCase();
                            if (type.equalsIgnoreCase("break") || type.equalsIgnoreCase("place") || type.equalsIgnoreCase("mobs") || type.equalsIgnoreCase("fish") || type.equalsIgnoreCase("tools") || type.equalsIgnoreCase("craft") || type.equalsIgnoreCase("smelt") || type.equalsIgnoreCase("brew")){
                                if (main.customJobConfig.getConfigurationSection("customs." + job + "." + type).getKeys(false).contains(item)){
                                    main.customJobConfig.set("customs." + job + "." + type + "." + item, null);
                                    main.saveConfigs(sender);
                                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.delobj.success").replace("%ITEM%", item).replace("%JOB%", job));
                                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.ItemNotFound").replace("%ITEM%", item));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadType").replace("%TYPE%", type).replace("%ITEM%", item));
                        }
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.delobj.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.delobj.NoPermissions"));
    }
    public void editObj(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.editObj")){
            if (args.length < 5 || args[1].equalsIgnoreCase("?") || args.length > 5){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.editobj.cuc"));
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
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (main.customJobConfig.getString("customs." + Ajob + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.editObj.others")){
                        if (Atype.equalsIgnoreCase("break") || Atype.equalsIgnoreCase("place") || Atype.equalsIgnoreCase("mobs") || Atype.equalsIgnoreCase("fish") || Atype.equalsIgnoreCase("tools") || Atype.equalsIgnoreCase("craft") || Atype.equalsIgnoreCase("smelt") || Atype.equalsIgnoreCase("brew")){
                            if (main.customJobConfig.getConfigurationSection("customs." + Ajob + "." + Atype).getKeys(false).contains(Aitem)){
                                if (Anum != -1){
                                    double oldValue = main.customJobConfig.getDouble("customs." + Ajob + "." + Atype + "." + Aitem);
                                    main.customJobConfig.set("customs." + Ajob + "." + Atype + "." + Aitem, Anum);
                                    main.saveConfigs(sender);
                                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.editobj.success").replace("%ITEM%", Aitem).replace("%VALUE%", String.valueOf(oldValue)).replace("%VALUE2%", String.valueOf(Anum)).replace("%JOB%", Ajob).replace("%TYPE%", Atype));
                                } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.MustBeNumber"));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.ItemNotFound").replace("%ITEM%", Aitem));
                        } else if(Atype.equalsIgnoreCase("enchant")) {
                            if (Aitem.equalsIgnoreCase("active")){
                                if (!bool.equals("wop")){
                                    main.customJobConfig.set("customs." + Ajob + ".enchant.active", Boolean.valueOf(bool));
                                    main.saveConfigs(sender);
                                    switch (String.valueOf(main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active"))){
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
                                    double oldValue = main.customJobConfig.getDouble("customs." + Ajob + ".enchant.payPerLevel");
                                    main.customJobConfig.set("customs." + Ajob + ".enchant.payPerLevel", Anum);
                                    main.saveConfigs(sender);
                                    sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.editobj.PayPerLevel").replace("%VALUE%", String.valueOf(oldValue)).replace("%VALUE2%", String.valueOf(Anum)).replace("%JOB%", Ajob));
                                } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.MustBeNumber"));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.BadItem"));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadType").replace("%TYPE%", Atype).replace(": %ITEM%", ""));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.editobj.NoPermissions"));
    }
    public void addWorld(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.addWorld")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.addworld.cuc"));
            } else if (args.length >= 3){
                String job = args[1].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job)){
                    if (main.customJobConfig.getString("customs." + job + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.addWorld.others")){
                        for(int i = 0; i <= args.length - (3); i++){
                            String world = args[2 + i].toLowerCase();
                            if (!main.customJobConfig.getStringList("customs." + job + ".worlds").contains(world)){
                                List newList = main.customJobConfig.getStringList("customs." + job + ".worlds");
                                newList.add(world);
                                main.customJobConfig.set("customs." + job + ".worlds", newList);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.addworld.success").replace("%WORLD%", world).replace("%JOB%", job));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.WorldExists").replace("%WORLD%", world));
                        }
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addworld.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.addworld.NoPermissions"));
    }
    public void rmWorld(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.rmWorld")){
            if (args.length < 3 || args[1].equalsIgnoreCase("?") || args.length > 3){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.rmworld.cuc"));
            } else if (args.length == 3){
                String job = args[1].toUpperCase();
                String world = args[2].toLowerCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job)){
                    if (main.customJobConfig.getString("customs." + job + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.rmWorld.others")){
                        if (main.customJobConfig.getStringList("customs." + job + ".worlds").contains(world)){
                            List worlds = main.customJobConfig.getStringList("customs." + job + ".worlds");
                            worlds.remove(world);
                            main.customJobConfig.set("customs." + job + ".worlds", worlds);
                            main.saveConfigs(sender);
                            sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.rmworld.success").replace("%WORLD%", world).replace("%JOB%", job));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.WorldNotListed"));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.rmworld.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.BadCMD"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.rmworld.NoPermissions"));
    }
    public void lock(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.lock")){
            if (args.length != 2 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.lock.cu"));
            } else {
                String Ajob = args[1].toUpperCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (sender.getName().toLowerCase().equalsIgnoreCase(main.customJobConfig.getString("customs." + Ajob + ".owner")) || sender.hasPermission("MineJobs.customs.lock.others")){
                        if (main.mainConfig.getBoolean("economy.active")){
                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.lockJob"));
                        }
                        main.customJobConfig.set("customs." + Ajob + ".locked", !main.customJobConfig.getBoolean("customs." + Ajob + ".locked"));
                        main.saveConfigs(sender);
                        switch (Boolean.toString(main.customJobConfig.getBoolean("customs." + Ajob + ".locked"))){
                            case "true":
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.lock.success1"));
                                break;
                            case "false":
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.lock.success2"));
                                break;
                        }
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.lock.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.lock.NoPermissions"));
    }
    public void invite(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.invite")){
            if (args.length != 3 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.invite.cu"));
            } else {
                String Ajob = args[1].toUpperCase();
                String Aplayer = args[2].toLowerCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (sender.getName().toLowerCase().equalsIgnoreCase(main.customJobConfig.getString("customs." + Ajob + ".owner")) || sender.hasPermission("MineJobs.customs.invite.others")){
                        int count = main.playerConfig.getStringList("players." + Aplayer + ".invites").size();
                        String[] playersInvites = main.playerConfig.getStringList("players." + Aplayer + ".invites").toArray(new String[count + 1]);
                        playersInvites[count] = Ajob;
                        main.playerConfig.set("players." + Aplayer + ".invites", playersInvites);
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.invite.success").replace("%PLAYER%", Aplayer));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.invite.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.invite.NoPermissions"));
    }
    public void kick(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.kick")){
            if (args.length != 3 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.kick.cu"));
            } else {
                String Aplayer = args[1].toLowerCase();
                String Ajob = args[2].toUpperCase();
                if (main.playerConfig.getConfigurationSection("players").getKeys(false).contains(Aplayer)){
                    if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                        if (main.customJobConfig.getString("customs." + Ajob + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.kick.others")){
                            if (main.playerConfig.getStringList("players." + Aplayer + ".jobs").contains(Ajob)){
                                List newList = main.playerConfig.getStringList("players." + Aplayer + ".jobs");
                                newList.remove(Ajob);
                                main.playerConfig.set("players." + Aplayer + ".jobs", newList);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.kick.success"));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.kick.PlayerNotOnJob"));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.kick.NoPermissionsC"));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.kick.PlayerNotListed"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.kick.NoPermissions"));
    }
    public void setOwner(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.customs.setOwner")){
            if (args.length != 3 || args[1].equalsIgnoreCase("?")){
                sender.sendMessage(ChatColor.BLUE + main.lang.getString("ac.setowner.cu"));
            } else {
                String Ajob = args[1].toUpperCase();
                String Aplayer = args[2].toLowerCase();
                if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    if (main.customJobConfig.getString("customs." + Ajob + ".owner").equalsIgnoreCase(sender.getName().toLowerCase()) || sender.hasPermission("MineJobs.customs.setOwner.others")){
                        if (main.mainConfig.getBoolean("economy.active")){
                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.setJobOwner"));
                        }
                        main.customJobConfig.set("customs." + Ajob + ".owner", Aplayer);
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("ac.setowner.success").replace("%JOB%", Ajob).replace("%PLAYER%", Aplayer));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setowner.NoPermissionsC"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("Errors.JobNotFound"));
            }
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("ac.setowner.NoPermissions"));
    }
}