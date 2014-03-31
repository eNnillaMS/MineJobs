package info.hexanet.eNnillaMS.MineJobs;
import info.hexanet.eNnillaMS.MineJobs.classes.Conf;
import info.hexanet.eNnillaMS.MineJobs.classes.Job;
import info.hexanet.eNnillaMS.MineJobs.classes.Lang;
import info.hexanet.eNnillaMS.MineJobs.classes.Player;
import info.hexanet.eNnillaMS.MineJobs.classes.SignC;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
public class playerCommands implements CommandExecutor{
    private final MineJobs Main;
    private final Conf Config;
    private final Lang Lang;
    private final Map<String, Player> Players;
    private final Map<String, Job> Jobs;
    private final Map<Location, SignC> Signs;
    public playerCommands(MineJobs main) {
        Main = main;
        Config = main.Config;
        Lang = main.Lang;
        Players = main.Players;
        Jobs = main.Jobs;
        Signs = main.Signs;
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
        if (sender.hasPermission("MineJobs.player.help")){
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            if(sender.hasPermission("MineJobs.player.getJob")) sender.sendMessage(Lang.CommandOutput[4][0]);
            if(sender.hasPermission("MineJobs.player.quitJob")) sender.sendMessage(Lang.CommandOutput[4][1]);
            if(sender.hasPermission("MineJobs.player.listJobs")){
                sender.sendMessage(Lang.CommandOutput[4][2]);
                sender.sendMessage(Lang.CommandOutput[4][3]);
            }
            if(sender.hasPermission("MineJobs.player.info")) sender.sendMessage(Lang.CommandOutput[4][4]);
            if(sender.hasPermission("MineJobs.admin.help")) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[4][5]);
            if(sender.hasPermission("MineJobs.customs.help") && Config.UseCustoms) sender.sendMessage(ChatColor.BLUE + Lang.CommandOutput[4][6]);
            sender.sendMessage(Lang.CommandOutput[4][7]);
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
    }
    public void getJob(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 2 || args.length > 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(Lang.CommandOutput[0][0]);
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null){
                if (job.AddPlayerCountCheck(Players)){
                    Player player = Players.get(sender.getName());
                    if (args.length == 3) player = Players.get(args[2]);
                    if (player != null){
                        if ((player.Jobs.size() < Config.MaxJobs) || Config.MaxJobs == 0){
                            if (!player.Jobs.contains(job.Name)){
                                if (!job.IsCustom || !job.Locked || player.Invites.contains(job.Name)){
                                    if ((player.Name.equals(sender.getName()) && sender.hasPermission("MineJobs.player.getJob")) || (!player.Name.equals(sender.getName()) && sender.hasPermission("MineJobs.player.getJob"))){
                                        if (Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[0]);
                                        if (player.Invites.contains(job.Name)) player.Invites.remove(job.Name);
                                        player.Jobs.add(job.Name);
                                        Main.saveConfigs(sender);
                                        if (player.Name.equals(sender.getName())) sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[0][1]);
                                        else sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[0][2].replace("%PLAYER%", player.Name));
                                    } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
                                } else if (player.Name.equals(sender.getName())) sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][6]); else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][9].replace("%PLAYER%", player.Name));
                            } else if (player.Name.equals(sender.getName())) sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][5]); else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][8].replace("%PLAYER%", player.Name));
                        } else if (player.Name.equals(sender.getName())) sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][4]); else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][7].replace("%PLAYER%", player.Name));
                    } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[10]);
                } else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[0][3]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void quitJob(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 2 || args.length > 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(Lang.CommandOutput[1][0]);
        } else {
            Player player = Players.get(sender.getName());
            if (args.length == 3) player = Players.get(args[2]);
            if (player != null){
                if (player.Jobs.contains(args[1].toUpperCase())){
                    if (!Config.ForcedJobs.contains(args[1].toUpperCase())){
                        if ((player.Name.equals(sender.getName()) && sender.hasPermission("MineJobs.player.quitJob")) || (!player.Name.equals(sender.getName()) && sender.hasPermission("MineJobs.player.quitJob.other"))){
                            if (Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[1]);
                            player.Jobs.remove(args[1].toUpperCase());
                            Main.saveConfigs(sender);
                            if (player.Name.equals(sender.getName())) sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[1][1].replace("%JOB%", args[1].toUpperCase()));
                            else sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[1][2].replace("%JOB%", args[1].toUpperCase()).replace("%PLAYER%", player.Name));
                        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
                    } else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[1][5]);
                } else if (player.Name.equals(sender.getName())) sender.sendMessage(ChatColor.RED + Lang.CommandOutput[1][3]); else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[1][4].replace("%PLAYER%", player.Name));
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[10]);
        }
    }
    public void listJobs(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.player.listJobs")){
            Player plyr = Players.get(sender.getName());
            String plyrJobs = plyr.Jobs.toString().replace("[", "").replace("]", "");
            String plyrInvs = plyr.Invites.toString().replace("[", "").replace("]", "");
            String jobs = "";
            for (Map.Entry<String, Job> j:Jobs.entrySet()) {
                if (j.getValue().IsCustom){
                    if (j.getValue().Locked) jobs += ChatColor.RED + j.getValue().Name + "  ";
                    else jobs += ChatColor.BLUE + j.getValue().Name + "  ";
                } else jobs += ChatColor.GOLD + j.getValue().Name + "  ";
            }
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            sender.sendMessage("  " + ChatColor.GREEN + Lang.CommandOutput[2][0]);
            sender.sendMessage("    " + plyrJobs);
            sender.sendMessage("  " + Lang.CommandOutput[2][1]);
            sender.sendMessage("  " + ChatColor.GREEN + Lang.CommandOutput[2][2]);
            sender.sendMessage("    " + Lang.CommandOutput[2][5] + "        " + ChatColor.GOLD + Lang.CommandOutput[2][6] + "  " + ChatColor.BLUE + Lang.CommandOutput[2][7] + "  " + ChatColor.RED + Lang.CommandOutput[2][8]);
            sender.sendMessage("    " + jobs);
            sender.sendMessage("  " + ChatColor.GREEN + Lang.CommandOutput[2][3]);
            sender.sendMessage("    " + plyrInvs);
            sender.sendMessage("  " + Lang.CommandOutput[2][4]);
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
    }
    public void currentJobs(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.player.listJobs")){
            Player plyr = Players.get(sender.getName());
            String plyrJobs = plyr.Jobs.toString().replace("[", "").replace("]", "");
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            sender.sendMessage("  " + ChatColor.GREEN + Lang.CommandOutput[2][0]);
            sender.sendMessage("    " + plyrJobs);
            sender.sendMessage("  " + Lang.CommandOutput[2][4]);
            sender.sendMessage("  " + Lang.CommandOutput[2][1]);
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
    }
    public void info(CommandSender sender, Command cmd, String label, String[] args){
        if(sender.hasPermission("MineJobs.player.info")){
            if(args.length == 2 && !args[1].equalsIgnoreCase("?")){
                Job job = Jobs.get(args[1].toUpperCase());
                if (job != null){
                    String n, o = "", l = ChatColor.GOLD + "false", d = ChatColor.RED + "Off", p = ChatColor.GOLD + "Unlimited",
                            w = ChatColor.RED + "None", br = ChatColor.GREEN + "", pl = ChatColor.GREEN + "", mo = ChatColor.GREEN + "",
                            fi = ChatColor.GREEN + "", to = ChatColor.GREEN + "", cr = ChatColor.GREEN + "", sm = ChatColor.GREEN + "",
                            po = ChatColor.GREEN + "", en = "";
                    if (job.IsCustom) n = ChatColor.BLUE + job.Name; else n = ChatColor.GOLD + job.Name;
                    if (job.IsCustom) o = " (" + Lang.CommandOutput[3][2] + job.Owner + ")";
                    if (job.Locked) l = ChatColor.RED + "true ";
                    if ((Config.UseDeathLosses.equals("job") && job.DeathLosses) || Config.UseDeathLosses.equals("always")) d = ChatColor.GOLD + String.valueOf(Config.DeathLoss);
                    if (job.MaxPlayers != 0){
                        int x = job.MaxPlayers, y = job.playerCount(Players), z = (int) y / x * 100;
                        if (z <= 25) p = ChatColor.GREEN + String.valueOf(y) + "/" + String.valueOf(x);
                        else if (z > 25 && z <= 75) p = ChatColor.BLUE + String.valueOf(y) + "/" + String.valueOf(x);
                        else p = ChatColor.RED + String.valueOf(y) + "/" + String.valueOf(x);
                    }
                    if (!job.Worlds.isEmpty()) w = ChatColor.GOLD + job.Worlds.toString().replace("[", "").replace("]", "");
                    for (Map.Entry<String, Double> a:job.Break.entrySet()) br += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Place.entrySet()) pl += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Mobs.entrySet()) mo += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Fish.entrySet()) fi += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Tools.entrySet()) to += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Craft.entrySet()) cr += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Smelt.entrySet()) sm += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    for (Map.Entry<String, Double> a:job.Brew.entrySet()) po += a.getKey().replace("_", " ").replace(":", "_") + ": " + String.valueOf(a.getValue()) + ", ";
                    if (job.EnchantEnabled) en = ChatColor.GOLD + String.valueOf(job.EnchantPay);
                    sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
                    sender.sendMessage(ChatColor.WHITE + "  " + Lang.CommandOutput[3][1] + n + o);
                    sender.sendMessage(ChatColor.WHITE + "  " + Lang.CommandOutput[3][3] + l + ChatColor.WHITE + "            " + Lang.CommandOutput[3][4] + d);
                    sender.sendMessage(ChatColor.WHITE + "  " + Lang.CommandOutput[3][5] + p);
                    sender.sendMessage(ChatColor.WHITE + "  " + Lang.CommandOutput[3][6] + w);
                    sender.sendMessage(ChatColor.WHITE + "  " + Lang.CommandOutput[3][7]);
                    if (!br.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][8] + br);
                    if (!pl.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][9] + pl);
                    if (!mo.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][10] + mo);
                    if (!fi.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][11] + fi);
                    if (!to.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][12] + to);
                    if (!cr.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][13] + cr);
                    if (!sm.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][14] + sm);
                    if (!po.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][15] + po);
                    if (!en.equals(ChatColor.GREEN + "")) sender.sendMessage(ChatColor.WHITE + "    " + Lang.CommandOutput[3][16] + en);
                    sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
            } else sender.sendMessage(Lang.CommandOutput[3][0]);
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
    }
}