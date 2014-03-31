public class oldPC{
    public static final Logger log = Logger.getLogger("Minecraft");
    private final MineJobs main;
    public playerCommands_old(MineJobs main) {
		this.main = main;
    }
    @Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
            showHelp(sender, cmd, label, args);
        } else if (args[0].equalsIgnoreCase("getJob")){
            getJob(sender, cmd, label, args);
        } else if (args[0].equalsIgnoreCase("quitJob")){
            quitJob(sender, cmd, label, args);
        } else if (args[0].equalsIgnoreCase("listJobs")){
            listJobs(sender, cmd, label, args);
        } else if (args[0].equalsIgnoreCase("myjobs")){
            currentJobs(sender, cmd, label, args);
        } else if (args[0].equalsIgnoreCase("info")){
            info(sender, cmd, label, args);
        } else {
            showHelp(sender, cmd, label, args);
        }
        return true;
    }
    public void showHelp(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("mj")){
            if (sender.hasPermission("MineJobs.player.help")){
                sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
                if(sender.hasPermission("MineJobs.admin.help")){
                    sender.sendMessage(ChatColor.GOLD + main.lang.getString("HelpOutput.mja"));
                }
                if(sender.hasPermission("MineJobs.customs.help") && main.mainConfig.getBoolean("customJobs") == true){
                    sender.sendMessage(ChatColor.BLUE + main.lang.getString("HelpOutput.mjc"));
                }
                if(sender.hasPermission("MineJobs.player.getJob")){
                    sender.sendMessage(main.lang.getString("HelpOutput.getJob"));
                }
                if(sender.hasPermission("MineJobs.player.quitJob")){
                    sender.sendMessage(main.lang.getString("HelpOutput.quitJob"));
                }
                if(sender.hasPermission("MineJobs.player.listJobs")){
                    sender.sendMessage(main.lang.getString("HelpOutput.myJobs"));
                    sender.sendMessage(main.lang.getString("HelpOutput.listJobs"));
                }
                if(sender.hasPermission("MineJobs.player.info")){
                    sender.sendMessage(main.lang.getString("HelpOutput.info"));
                }
                sender.sendMessage(main.lang.getString("HelpOutput.more"));
                sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("HelpOutput.error"));
        }
    }
    public void getJob(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 2 || args.length > 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(main.lang.getString("p.getJob.cu"));
        } else if (args.length == 2){
            String Ajob = args[1].toUpperCase();
            if (sender.hasPermission("MineJobs.player.getJob")){
                if (!main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs").contains(Ajob)){
                    if (main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs").size() < main.mainConfig.getInt("maxjobs") || main.mainConfig.getInt("maxjobs") == 0){
                        int totPlyrs = 0;
                        for (String plyr:main.playerConfig.getConfigurationSection("players").getKeys(false)){
                            if (main.playerConfig.getStringList("players." + plyr + ".jobs").contains(Ajob)) totPlyrs++;
                        }
                        if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                            if (main.jobConfig.getInt("jobs." + Ajob + ".maxplayers") > totPlyrs || main.jobConfig.getInt("jobs." + Ajob + ".maxplayers") == 0){
                                List newList = main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs");
                                newList.add(Ajob);
                                main.playerConfig.set("players." + sender.getName().toLowerCase() + ".jobs", newList);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.getJob.success") + newList);
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.TooManyPeople"));
                        } else if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                            if (main.customJobConfig.getInt("customs." + Ajob + ".maxplayers") > totPlyrs || main.customJobConfig.getInt("customs." + Ajob + ".maxplayers") == 0){
                                if (!sender.getName().toLowerCase().equalsIgnoreCase(main.customJobConfig.getString("customs." + Ajob + ".owner"))){
                                    if (main.customJobConfig.getBoolean("customs." + Ajob + ".locked") == false || main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".invites").contains(Ajob)){
                                        if (main.mainConfig.getBoolean("economy.active")){
                                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.getJob"));
                                        }
                                        List newList = main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs");
                                        newList.add(Ajob);
                                        main.playerConfig.set("players." + sender.getName().toLowerCase() + ".jobs", newList);
                                        if (main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".invites").contains(Ajob)){
                                            List invList = main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".invites");
                                            invList.remove(Ajob);
                                            main.playerConfig.set("players." + sender.getName().toLowerCase() + ".invites", invList);
                                        }
                                        main.saveConfigs(sender);
                                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.getJob.success") + newList);
                                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.LockedJob"));
                                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.SelfJoin"));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.TooManyPeople"));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.JobNotFound"));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.TooManyJobs"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.AlreadyHave"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.NoPermisisons"));
        } else {
            String Ajob = args[1].toUpperCase();
            String Aplyr = args[2].toLowerCase();
            if (sender.hasPermission("MineJobs.player.getJob.others")){
                if (!main.playerConfig.getStringList("players." + Aplyr + ".jobs").contains(Ajob)){
                    if (main.playerConfig.getStringList("players." + Aplyr + ".jobs").size() < main.mainConfig.getInt("maxjobs") || main.mainConfig.getInt("maxjobs") == 0){
                        int totPlyrs = 0;
                        for (String plyr:main.playerConfig.getConfigurationSection("players").getKeys(false)){
                            if (main.playerConfig.getStringList("players." + plyr + ".jobs").contains(Ajob)) totPlyrs++;
                        }
                        if (main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                            if (main.jobConfig.getInt("jobs." + Ajob + ".maxplayers") > totPlyrs || main.jobConfig.getInt("jobs." + Ajob + ".maxplayers") == 0){
                                List newList = main.playerConfig.getStringList("players." + Aplyr + ".jobs");
                                newList.add(Ajob);
                                main.playerConfig.set("players." + Aplyr + ".jobs", newList);
                                main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.getJob.success") + newList);
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.TooManyPeople"));
                        } else if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                            if (main.customJobConfig.getInt("customs." + Ajob + ".maxplayers") > totPlyrs || main.customJobConfig.getInt("customs." + Ajob + ".maxplayers") == 0){
                                if (!Aplyr.equalsIgnoreCase(main.customJobConfig.getString("customs." + Ajob + ".owner"))){
                                    if (main.customJobConfig.getBoolean("customs." + Ajob + ".locked") == false || main.playerConfig.getStringList("players." + Aplyr + ".invites").contains(Ajob)){
                                        if (main.mainConfig.getBoolean("economy.active")){
                                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.getJob"));
                                        }
                                        List newList = main.playerConfig.getStringList("players." + Aplyr + ".jobs");
                                        newList.add(Ajob);
                                        main.playerConfig.set("players." + Aplyr + ".jobs", newList);
                                        if (main.playerConfig.getStringList("players." + Aplyr + ".invites").contains(Ajob)){
                                            List invList = main.playerConfig.getStringList("players." + Aplyr + ".invites");
                                            invList.remove(Ajob);
                                            main.playerConfig.set("players." + Aplyr + ".invites", invList);
                                        }
                                        main.saveConfigs(sender);
                                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.getJob.success") + newList);
                                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.LockedJobOther").replace("%PLAYER%", Aplyr));
                                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.SelfJoinOther").replace("%PLAYER%", Aplyr));
                            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.TooManyPeople"));
                        } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.JobNotFound"));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.TooManyJobsOther").replace("%PLAYER%", Aplyr));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.AlreadyHaveOther").replace("%PLAYER%", Aplyr));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.getJob.NoPermissionsB"));
        }
    }
    public void quitJob(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 2 || args.length > 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(main.lang.getString("p.quitJob.cu"));
        } else if (args.length == 2){
            String Ajob = args[1].toUpperCase();
            if (sender.hasPermission("MineJobs.player.quitJob")){
                if (main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs").contains(Ajob)){
                    if (!main.mainConfig.getStringList("forcedJobs").contains(Ajob)){
                        if (main.mainConfig.getBoolean("economy.active")){
                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.quitJob"));
                        }
                        List newList = main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs");
                        newList.remove(Ajob);
                        main.playerConfig.set("players." + sender.getName().toLowerCase() + ".jobs", newList);
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.quitJob.success").replace("%JOB%", Ajob));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.quitJob.ForcedJob"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.quitJob.DontHaveJob"));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.quitJob.NoPermissions"));
        } else {
            String Ajob = args[1].toUpperCase();
            String Aplyr = args[2].toLowerCase();
            if (sender.hasPermission("MineJobs.player.quitJob.others")){
                if (main.playerConfig.getStringList("players." + Aplyr + ".jobs").contains(Ajob)){
                    if (main.mainConfig.getStringList("forcedJobs").contains(Ajob)){
                        if (main.mainConfig.getBoolean("economy.active")){
                            MineJobs.econ.withdrawPlayer(sender.getName(), main.mainConfig.getDouble("economy.quitJob"));
                        }
                        List newList = main.playerConfig.getStringList("players." + Aplyr + ".jobs");
                        newList.remove(Ajob);
                        main.playerConfig.set("players." + Aplyr + ".jobs", newList);
                        main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.quitJob.successOther").replace("%PLAYER%", Aplyr).replace("%JOB%", Ajob));
                    } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.quitJob.ForcedJob"));
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.quitJob.DontHaveJobOther").replace("%PLAYER%", Aplyr));
            } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.quitJob.NoPermissionsOther"));
        }
    }
    public void listJobs(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.player.listJobs")){
            String plyrJobs = "    ";
            for (String job:main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs")) {
                plyrJobs += job + "  ";
            }
            String plyrInvs = "    ";
            for (String job:main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".invites")) {
                plyrInvs += job + "  ";
            }
            String Jobs = "    ";
            for (String job:main.jobConfig.getConfigurationSection("jobs").getKeys(false)) {
                Jobs += ChatColor.GOLD + job + "  ";
            }
            for (String job:main.customJobConfig.getConfigurationSection("customs").getKeys(false)) {
                if (main.customJobConfig.getBoolean("customs." + job + ".locked") == false) Jobs += ChatColor.BLUE + job + "  ";
                if (main.customJobConfig.getBoolean("customs." + job + ".locked") == true) Jobs += ChatColor.DARK_BLUE + job + "  ";
            }
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.listJobs.YourJobs"));
            sender.sendMessage(plyrJobs);
            sender.sendMessage(main.lang.getString("p.listJobs.QuitInst"));
            sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.listJobs.Availible"));
            sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.listJobs.Legend.lbl") + ChatColor.GOLD + main.lang.getString("p.listJobs.Legend.admn") + ChatColor.BLUE + main.lang.getString("p.listJobs.Legend.cust") + ChatColor.DARK_BLUE + main.lang.getString("p.listJobs.Legend.lock"));
            sender.sendMessage(Jobs);
            sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.listJobs.Invite"));
            sender.sendMessage(plyrInvs);
            sender.sendMessage(main.lang.getString("p.listJobs.GetInst"));
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.listJobs.NoPermissions"));
    }
    public void currentJobs(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.player.listJobs")){
            String plyrJobs = "    ";
            for (String job:main.playerConfig.getStringList("players." + sender.getName().toLowerCase() + ".jobs")) {
                plyrJobs += job + "  ";
            }
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            sender.sendMessage(ChatColor.GREEN + main.lang.getString("p.listJobs.YourJobs"));
            sender.sendMessage(plyrJobs);
            sender.sendMessage(main.lang.getString("p.listJobs.GetInst"));
            sender.sendMessage(main.lang.getString("p.listJobs.QuitInst"));
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.listJobs.NoPermissions"));
    }
    public void info(CommandSender sender, Command cmd, String label, String[] args){
        if(sender.hasPermission("MineJobs.player.info")){
            if(args.length == 2 && !args[1].equalsIgnoreCase("?")){
                String Ajob = args[1].toUpperCase();
                if(main.jobConfig.getConfigurationSection("jobs").getKeys(false).contains(Ajob)){
                    sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.JobName") + ChatColor.GOLD + Ajob);
                    if (main.mainConfig.getString("pklosses.enable").equals("job")){
                        if (main.jobConfig.getBoolean("jobs." + Ajob + ".pkl") == false){
                            sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.pkl") + ChatColor.GOLD + main.lang.getString("p.info.pkln"));
                        } else {
                            sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.pkl") + ChatColor.GOLD + main.mainConfig.getDouble("pklosses.loss"));
                        }
                    }
                    String type = "";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".break").getKeys(false).isEmpty()) type += "Break";
                    if (type.contains("Break") && (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".place").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty() || main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".place").getKeys(false).isEmpty()) type += "Place";
                    if (type.contains("Place") && (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty() || main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active") == true)) type += " / ";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false).isEmpty()) type += "Kill";
                    if (type.contains("Kill") && (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty() || main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false).isEmpty()) type += "Fish";
                    if (type.contains("Fish") && (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty() || main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false).isEmpty()) type += "Craft";
                    if (type.contains("Craft") && (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty() || main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty()) type += "Smelt";
                    if (type.contains("Smelt") && (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty() || main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty()) type += "Brew";
                    if (type.contains("Brew") && main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active") == true) type += " / ";
                    if (main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active") == true) type += "Enchant";
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Type") + ChatColor.WHITE + type);
                    String max = Integer.toString(main.jobConfig.getInt("jobs." + Ajob + ".maxplayers"));
                    if(max.equals("0")) max = "Unlimited"; 
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Max") + ChatColor.WHITE + max);
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Worlds") + ChatColor.WHITE + main.jobConfig.getStringList("jobs." + Ajob + ".worlds"));
                    String Tools ="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".tools").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".tools").getKeys(false)){
                            String sect = "jobs." + Ajob + ".tools.";
                            Tools += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Tools") + ChatColor.WHITE + Tools);
                    }
                    String Break="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".break").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".break").getKeys(false)){
                            String sect = "jobs." + Ajob + ".break.";
                            Break += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Break") + ChatColor.WHITE + Break);
                    }
                    String Place="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".place").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".place").getKeys(false)){
                            String sect = "jobs." + Ajob + ".place.";
                            Place += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Place") + ChatColor.WHITE + Place);
                    }
                    String Mobs ="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".mobs").getKeys(false)){
                            String sect = "jobs." + Ajob + ".mobs.";
                            Mobs += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Kill") + ChatColor.WHITE + Mobs);
                    }
                    String Fish ="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".fish").getKeys(false)){
                            String sect = "jobs." + Ajob + ".fish.";
                            Fish += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Fish") + ChatColor.WHITE + Fish);
                    }
                    String Craft ="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".craft").getKeys(false)){
                            String sect = "jobs." + Ajob + ".craft.";
                            Craft += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Craft") + ChatColor.WHITE + Craft);
                    }
                    String Smelt ="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".smelt").getKeys(false)){
                            String sect = "jobs." + Ajob + ".smelt.";
                            Smelt += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Smelt") + ChatColor.WHITE + Smelt);
                    }
                    String Brew ="";
                    if (!main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false).isEmpty()){
                        for (String Item:main.jobConfig.getConfigurationSection("jobs." + Ajob + ".brew").getKeys(false)){
                            String sect = "jobs." + Ajob + ".brew.";
                            Brew += Item + ": " + Double.toString(main.jobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Brew") + ChatColor.WHITE + Brew);
                    }
                    if (!main.jobConfig.getBoolean("jobs." + Ajob + ".enchant.active") == false){
                        double Enchant = main.jobConfig.getDouble("jobs." + Ajob + ".enchant.payPerLevel");
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Enchant") + ChatColor.WHITE + Enchant);
                    }
                    sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
                } else if (main.customJobConfig.getConfigurationSection("customs").getKeys(false).contains(Ajob)){
                    sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.JobName") + ChatColor.BLUE + Ajob);
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Owner") + ChatColor.WHITE + main.customJobConfig.getString("customs." + Ajob + ".owner"));
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Locked") + ChatColor.WHITE + main.customJobConfig.getString("customs." + Ajob + ".locked"));
                    String type = "";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".break").getKeys(false).isEmpty()) type += "Break";
                    if (type.contains("Break") && (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".place").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty() || main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".place").getKeys(false).isEmpty()) type += "Place";
                    if (type.contains("Place") && (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty() || main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active") == true)) type += " / ";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false).isEmpty()) type += "Kill";
                    if (type.contains("Kill") && (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty() || main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false).isEmpty()) type += "Fish";
                    if (type.contains("Fish") && (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty() || main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false).isEmpty()) type += "Craft";
                    if (type.contains("Craft") && (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty() || !main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty() || main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty()) type += "Smelt";
                    if (type.contains("Smelt") && (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty() || main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active")) == true) type += " / ";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty()) type += "Brew";
                    if (type.contains("Brew") && main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active") == true) type += " / ";
                    if (main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active") == true) type += "Enchant";
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Type") + ChatColor.WHITE + type);
                    String max = Integer.toString(main.customJobConfig.getInt("customs." + Ajob + ".maxplayers"));
                    if(max.equals("0")) max = "Unlimited"; 
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Max") + ChatColor.WHITE + max);
                    sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Worlds") + ChatColor.WHITE + main.customJobConfig.getStringList("customs." + Ajob + ".worlds"));
                    String Tools ="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".tools").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".tools").getKeys(false)){
                            String sect = "customs." + Ajob + ".tools.";
                            Tools += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Tools") + ChatColor.WHITE + Tools);
                    }
                    String Break="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".break").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".break").getKeys(false)){
                            String sect = "customs." + Ajob + ".break.";
                            Break += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Break") + ChatColor.WHITE + Break);
                    }
                    String Place="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".place").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".place").getKeys(false)){
                            String sect = "customs." + Ajob + ".place.";
                            Place += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Place") + ChatColor.WHITE + Place);
                    }
                    String Mobs ="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".mobs").getKeys(false)){
                            String sect = "customs." + Ajob + ".mobs.";
                            Mobs += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Kill") + ChatColor.WHITE + Mobs);
                    }
                    String Fish ="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".fish").getKeys(false)){
                            String sect = "customs." + Ajob + ".fish.";
                            Fish += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Fish") + ChatColor.WHITE + Fish);
                    }
                    String Craft ="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".craft").getKeys(false)){
                            String sect = "customs." + Ajob + ".craft.";
                            Craft += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Craft") + ChatColor.WHITE + Craft);
                    }
                    String Smelt ="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".smelt").getKeys(false)){
                            String sect = "customs." + Ajob + ".smelt.";
                            Smelt += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Smelt") + ChatColor.WHITE + Smelt);
                    }
                    String Brew ="";
                    if (!main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false).isEmpty()){
                        for (String Item:main.customJobConfig.getConfigurationSection("customs." + Ajob + ".brew").getKeys(false)){
                            String sect = "customs." + Ajob + ".brew.";
                            Brew += Item + ": " + Double.toString(main.customJobConfig.getDouble(sect + Item)) + ", ";
                        }
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Brew") + ChatColor.WHITE + Brew);
                    }
                    if (!main.customJobConfig.getBoolean("customs." + Ajob + ".enchant.active") == false){
                        double Enchant = main.customJobConfig.getDouble("customs." + Ajob + ".enchant.payPerLevel");
                        sender.sendMessage(ChatColor.GRAY + main.lang.getString("p.info.Enchant") + ChatColor.WHITE + Enchant);
                    }
                    sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
                } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.info.JobNotFound"));
            } else sender.sendMessage(main.lang.getString("p.info.cu"));
        } else sender.sendMessage(ChatColor.RED + main.lang.getString("p.info.NoPermissions"));
    }
}