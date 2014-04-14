package info.hexanet.eNnillaMS.MineJobs;
import info.hexanet.eNnillaMS.MineJobs.classes.Conf;
import info.hexanet.eNnillaMS.MineJobs.classes.Job;
import info.hexanet.eNnillaMS.MineJobs.classes.Lang;
import info.hexanet.eNnillaMS.MineJobs.classes.Player;
import info.hexanet.eNnillaMS.MineJobs.classes.SignC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
public class jobCommands{
    private final MineJobs Main;
    private final Conf Config;
    private final Lang Lang;
    private final Map<String, Player> Players;
    private final Map<String, Job> Jobs;
    private final Map<Location, SignC> Signs;
    private final ChatColor clr;
    public jobCommands(MineJobs main, Command cmd) {
        Main = main;
        Config = main.Config;
        Lang = main.Lang;
        Players = main.Players;
        Jobs = main.Jobs;
        Signs = main.Signs;
        if (cmd.getName().equalsIgnoreCase("mja")) clr = ChatColor.GOLD;
        else clr = ChatColor.BLUE;
    }
    public void showHelpA(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.admin.help")){
            boolean a = sender.hasPermission("MineJobs.admin.create"), b = sender.hasPermission("MineJobs.admin.delete"), 
                    c = sender.hasPermission("MineJobs.admin.rename"), d = sender.hasPermission("MineJobs.admin.setMax"), 
                    e = sender.hasPermission("MineJobs.admin.addObj"), f = sender.hasPermission("MineJobs.admin.delObj"), 
                    g = sender.hasPermission("MineJobs.admin.editObj"), h = sender.hasPermission("MineJobs.admin.setEnch"), 
                    i = sender.hasPermission("MineJobs.admin.addWorld"), j = sender.hasPermission("MineJobs.admin.delWorld"), 
                    k = sender.hasPermission("MineJobs.admin.togglePDL"), l = sender.hasPermission("MineJobs.admin.reload"), 
                    m = (Config.UseCustoms && sender.hasPermission("MineJobs.admin.upgrade"));
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            if(a) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][0]);
            if(m) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][1]);
            if(b) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][2]);
            if(c) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][3]);
            if(d) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][4]);
            if(e) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][5]);
            if(g) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][6]);
            if(f) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][7]);
            if(h) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][8]);
            if(i) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][9]);
            if(j) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][10]);
            if(k) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][11]);
            if(l) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[21][12]);
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
    }
    public void showHelpC(CommandSender sender, Command cmd, String label, String[] args){
        if (sender.hasPermission("MineJobs.custom.help")){
            boolean a = sender.hasPermission("MineJobs.custom.create"), b = sender.hasPermission("MineJobs.custom.delete"), 
                    c = sender.hasPermission("MineJobs.custom.rename"), d = sender.hasPermission("MineJobs.custom.setMax"), 
                    e = sender.hasPermission("MineJobs.custom.addObj"), f = sender.hasPermission("MineJobs.custom.editObj"), 
                    g = sender.hasPermission("MineJobs.custom.delObj"), h = sender.hasPermission("MineJobs.custom.setEnch"), 
                    i = sender.hasPermission("MineJobs.custom.addWorld"), j = sender.hasPermission("MineJobs.custom.delWorld"), 
                    k = sender.hasPermission("MineJobs.custom.togglePDL"), l = sender.hasPermission("MineJobs.custom.setOwner"), 
                    m = sender.hasPermission("MineJobs.custom.toggleLock"), n = sender.hasPermission("MineJobs.custom.kickPlayer"), 
                    o = sender.hasPermission("MineJobs.custom.invite");
            sender.sendMessage(ChatColor.GREEN + ".oOo___________________MineJobs___________________oOo.");
            if (a) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][0]);
            if (b) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][1]);
            if (c) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][2]);
            if (d) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][3]);
            if (e) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][4]);
            if (f) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][5]);
            if (g) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][6]);
            if (h) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][7]);
            if (i) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][8]);
            if (j) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][9]);
            if (k) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][10]);
            if (l) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][11]);
            if (m) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][12]);
            if (n) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][13]);
            if (o) sender.sendMessage(ChatColor.GOLD + Lang.CommandOutput[22][14]);
            sender.sendMessage(ChatColor.GREEN + ".oOo______________________________________________oOo.");
        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
    }
    public void create(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[5][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job == null){
                List<String> wl = new ArrayList<>(); for (World w:Main.getServer().getWorlds()) wl.add(w.getName());
                if (cmd.getName().equals("mja") && sender.hasPermission("MineJobs.admin.create")){
                    job = new Job(args[1].toUpperCase(), 0, new HashMap<String, Double>(), new HashMap<String, Double>(), new HashMap<String, Double>(),
                            new HashMap<String, Double>(), new HashMap<String, Double>(), new HashMap<String, Double>(), new HashMap<String, Double>(),
                            new HashMap<String, Double>(), false, 0.0, wl, false);
                } else if (cmd.getName().equals("mjc") && Config.UseCustoms && sender.hasPermission("MineJobs.custom.create")){
                    job = new Job(args[1].toUpperCase(), sender.getName(), false, 0, new HashMap<String, Double>(), new HashMap<String, Double>(),
                            new HashMap<String, Double>(), new HashMap<String, Double>(), new HashMap<String, Double>(), new HashMap<String, Double>(),
                            new HashMap<String, Double>(), new HashMap<String, Double>(), false, 0.0, wl, false);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
                if (job != null){
                    if (cmd.getName().equals("mjc") && Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[2]);
                    Jobs.put(args[1].toUpperCase(), job);
                    Main.saveConfigs(sender);
                    sender.sendMessage(clr + Lang.CommandOutput[5][1].replace("%JOB%", job.Name));
                }
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[4]);
        }
    }
    public void upgrade(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[6][0]);
        } else {
            if (sender.hasPermission("MineJobs.admin.upgrade")){
                Job job = Jobs.get(args[1].toUpperCase());
                if (job != null && cmd.getName().equalsIgnoreCase("mja") && job.IsCustom){
                    job.Owner = "";
                    job.Locked = false;
                    job.IsCustom = false;
                    Main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[6][1].replace("%JOB%", job.Name));
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
        }
    }
    public void delete(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[7][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.delete")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.delete"))){
                    if (job.IsCustom && Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[3]);
                    Config.ForcedJobs.remove(job.Name);
                    Config.DefaultJobs.remove(job.Name);
                    for (Map.Entry<String, Player> p:Players.entrySet()){
                        p.getValue().Jobs.remove(job.Name);
                        p.getValue().Invites.remove(job.Name);
                    }
                    for (Map.Entry<Location, SignC> s:Signs.entrySet()){
                        if (s.getValue().Job.equals(job.Name)) {
                            s.getKey().getBlock().breakNaturally();
                            Signs.remove(s.getKey());
                        }
                    }
                    Jobs.remove(job.Name);
                    Main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[7][1].replace("%JOB%", job.Name));
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void rename(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[8][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.rename")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.rename"))){
                    if (job.IsCustom && Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[4]);
                    for (Map.Entry<String, Player> p:Players.entrySet()){
                        if (p.getValue().Jobs.remove(job.Name)) p.getValue().Jobs.add(args[2].toUpperCase());
                        if (p.getValue().Invites.remove(job.Name)) p.getValue().Invites.add(args[2].toUpperCase());
                    }
                    Sign o;
                    for (Map.Entry<Location, SignC> s:Signs.entrySet()){
                        if (s.getValue().Job.equals(job.Name)){
                            o = (Sign) s.getKey().getBlock();
                            o.setLine(2, args[2].toUpperCase());
                            s.getValue().Job = args[2].toUpperCase();
                        }
                    }
                    if (Config.ForcedJobs.remove(job.Name)) Config.ForcedJobs.add(args[2].toUpperCase());
                    if (Config.DefaultJobs.remove(job.Name)) Config.DefaultJobs.add(args[2].toUpperCase());
                    job.Name = args[2].toUpperCase();
                    Jobs.put(job.Name, job);
                    Jobs.remove(args[1].toUpperCase());
                    Main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[8][1].replace("%JOB%", args[1].toUpperCase()).replace("%JOB2%", job.Name));
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void setMax(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[9][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.setMax")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.setMax"))){
                    try {
                        job.MaxPlayers = Integer.valueOf(args[2]);
                        Main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[9][1].replace("%VALUE%", args[2]));
                    } catch (Exception ex){
                        sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[9]);
                    }
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void addObj(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[10][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.addObj")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.addObj"))){
                    for (int i = 2; i < args.length; i++){
                        String[] split = args[i].split("-");
                        try {
                            double value = 0; try { value = Double.valueOf(split[2]); } catch (Exception ex) { split[0] = "BVE"; }
                            boolean go = true;
                            switch (split[0].toLowerCase()){
                                case "break": if (!job.Break.containsKey(split[1].toUpperCase())) job.Break.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "place": if (!job.Place.containsKey(split[1].toUpperCase())) job.Place.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "mobs": if (!job.Mobs.containsKey(split[1].toUpperCase())) job.Mobs.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "fish": if (!job.Fish.containsKey(split[1].toUpperCase())) job.Fish.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "tools": if (!job.Tools.containsKey(split[1].toUpperCase())) job.Tools.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "craft": if (!job.Craft.containsKey(split[1].toUpperCase())) job.Craft.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "smelt": if (!job.Smelt.containsKey(split[1].toUpperCase())) job.Smelt.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "brew": if (!job.Brew.containsKey(split[1].toUpperCase())) job.Brew.put(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[6].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "bve": sender.sendMessage(ChatColor.RED + Lang.CommandOutput[10][2].replace("%ARG%", args[i])); go = false; break;
                                default: sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[11].replace("%ARG%", args[i])); go = false; break;
                            }
                            if (go){
                                Main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[10][1].replace("%ITEM%", split[1].toUpperCase()).replace("%JOB%", job.Name));
                            }
                        } catch (Exception ex) { sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[12].replace("%ARG%", args[i])); }
                    }
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void delObj(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[11][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.delObj")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.delObj"))){
                    for (int i = 2; i < args.length; i++){
                        String[] split = args[i].split("-");
                        try {
                            boolean go = true;
                            switch (split[0].toLowerCase()){
                                case "break": if (job.Break.containsKey(split[1].toUpperCase())) job.Break.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "place": if (job.Place.containsKey(split[1].toUpperCase())) job.Place.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "mobs": if (job.Mobs.containsKey(split[1].toUpperCase())) job.Mobs.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "fish": if (job.Fish.containsKey(split[1].toUpperCase())) job.Fish.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "tools": if (job.Tools.containsKey(split[1].toUpperCase())) job.Tools.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "craft": if (job.Craft.containsKey(split[1].toUpperCase())) job.Craft.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "smelt": if (job.Smelt.containsKey(split[1].toUpperCase())) job.Smelt.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "brew": if (job.Brew.containsKey(split[1].toUpperCase())) job.Brew.remove(split[1].toUpperCase()); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                default:  sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[11].replace("%ARG%", args[i])); go = false; break;
                            }
                            if (go){
                                Main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[11][1].replace("%ITEM%", split[1].toUpperCase()).replace("%JOB%", job.Name));
                            }
                        } catch (Exception ex) { sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[12].replace("%ARG%", args[i])); }
                    }
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void editObj(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[12][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.editObj")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.editObj"))){
                    for (int i = 2; i < args.length; i++){
                        String[] split = args[i].split("-");
                        try {
                            double value = 0; try { value = Double.valueOf(split[2]); } catch (Exception ex) { split[0] = "BVE"; }
                            boolean go = true;
                            switch (split[0].toLowerCase()){
                                case "break": if (job.Break.containsKey(split[1].toUpperCase())) job.Break.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "place": if (job.Place.containsKey(split[1].toUpperCase())) job.Place.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "mobs": if (job.Mobs.containsKey(split[1].toUpperCase())) job.Mobs.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "fish": if (job.Fish.containsKey(split[1].toUpperCase())) job.Fish.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "tools": if (job.Tools.containsKey(split[1].toUpperCase())) job.Tools.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "craft": if (job.Craft.containsKey(split[1].toUpperCase())) job.Craft.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "smelt": if (job.Smelt.containsKey(split[1].toUpperCase())) job.Smelt.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "brew": if (job.Brew.containsKey(split[1].toUpperCase())) job.Brew.replace(split[1].toUpperCase(), value); else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[5].replace("%ITEM%", split[1].toUpperCase())); break;
                                case "bve": sender.sendMessage(ChatColor.RED + Lang.CommandOutput[12][2].replace("%ARG%", args[i])); go = false; break;
                                default: sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[11].replace("%ARG%", args[i])); go = false; break;
                            }
                            if (go){
                                Main.saveConfigs(sender);
                                sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[12][1].replace("%ITEM%", split[1].toUpperCase()).replace("%VALUE%", String.valueOf(value)));
                            }
                        } catch (Exception ex) { sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[12].replace("%ARG%", args[i])); }
                    }
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void setEnchant(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[13][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.setEnch")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.setEnch"))){
                    double value; try { value = Double.valueOf(args[2]); } catch (Exception ex){ sender.sendMessage(ChatColor.RED + Lang.CommandOutput[13][2].replace("%NUM%", args[2])); return; }
                    job.EnchantEnabled = (value != 0);
                    job.EnchantPay = value;
                    Main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[13][1].replace("%VALUE%", args[2]));
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void addWorld(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[14][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.addWorld")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.addWorld"))){
                    if (args.length >= 3){
                        for (int i = 2; i < args.length; i++){
                            if (Main.getServer().getWorld(args[i]) != null){
                                if (!job.Worlds.contains(Main.getServer().getWorld(args[i]).getName())){
                                    job.Worlds.add(Main.getServer().getWorld(args[i]).getName());
                                    Main.saveConfigs(sender);
                                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[14][1].replace("%WORLD%", Main.getServer().getWorld(args[i]).getName()));
                                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[8].replace("%WORLD%", Main.getServer().getWorld(args[i]).getName()));
                            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[13].replace("%WORLD%", args[i]));
                        }
                    } else {
                        String w = Main.getServer().getPlayer(sender.getName()).getWorld().getName();
                        if (!job.Worlds.contains(w)){
                            job.Worlds.add(w);
                            Main.saveConfigs(sender);
                            sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[14][1].replace("%WORLD%", w));
                        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[8].replace("%WORLD%", w));
                    }
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void remWorld(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length < 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[15][0].replace("%CMD%", cmd.getName()));
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null && ((cmd.getName().equalsIgnoreCase("mja") && !job.IsCustom) || (cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom))){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.delWorld")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.delWorld"))){
                    if (args.length >= 3){
                        for (int i = 2; i < args.length; i++){
                            if (Main.getServer().getWorld(args[i]) != null){
                                if (job.Worlds.contains(Main.getServer().getWorld(args[i]).getName())){
                                    job.Worlds.remove(Main.getServer().getWorld(args[i]).getName());
                                    Main.saveConfigs(sender);
                                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[15][1].replace("%WORLD%", Main.getServer().getWorld(args[i]).getName()));
                                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[7].replace("%WORLD%", Main.getServer().getWorld(args[i]).getName()));
                            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[13].replace("%WORLD%", args[i]));
                        }
                    } else {
                        String w = Main.getServer().getPlayer(sender.getName()).getWorld().getName();
                        if (job.Worlds.contains(w)){
                            job.Worlds.remove(w);
                            Main.saveConfigs(sender);
                            sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[15][1].replace("%WORLD%", w));
                        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[7].replace("%WORLD%", w));
                    }
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void togglePDL(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[16][0].replace("%CMD%", cmd.getName()));
        } else {
            if (Config.UseDeathLosses.equalsIgnoreCase("job")){
                Job job = Jobs.get(args[1]);
                if (job != null && cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom){
                    if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.togglePDL")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.togglePDL"))){
                        job.DeathLosses = !job.DeathLosses;
                        Main.saveConfigs(sender);
                        if (job.DeathLosses) sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[16][1]);
                        else sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[16][2]);
                    } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
            } else sender.sendMessage(ChatColor.RED + Lang.CommandOutput[16][3].replace("%VALUE%", Config.UseDeathLosses));
        }
    }
    public void setOwner(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[17][0]);
        } else {
            Job job = Jobs.get(args[1]);
            if (job != null && cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom){
                if (sender.hasPermission("MineJobs.custom.setOwner") && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass"))){
                    org.bukkit.entity.Player plyr = Main.getServer().getPlayer(args[2]);
                    if (plyr != null){
                        if (Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[5]);
                        job.Owner = plyr.getName();
                        Main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[17][1].replace("%JOB%", job.Name).replace("%PLAYER%", plyr.getName()));
                    } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[10]);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void toggleLock(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 2 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[18][0]);
        } else {
            Job job = Jobs.get(args[1]);
            if (job != null && cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom){
                if (sender.hasPermission("MineJobs.custom.toggleLock") && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass"))){
                    if (Config.UseCmdEconomy) Main.econ.withdrawPlayer(sender.getName(), Config.Eco[6]);
                    job.Locked = !job.Locked;
                    Main.saveConfigs(sender);
                    if (job.Locked) sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[18][1]);
                    else sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[18][2]);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void kickPlayer(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[19][0]);
        } else {
            Job job = Jobs.get(args[1]);
            if (job != null && cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom){
                if (sender.hasPermission("MineJobs.custom.kickPlayer") && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass"))){
                    org.bukkit.entity.Player plyr = Main.getServer().getPlayer(args[2]);
                    if (plyr != null){
                        Player player = Players.get(plyr.getName());
                        if (player != null){
                            if (player.Jobs.contains(job.Name)) player.Jobs.remove(job.Name);
                            Main.saveConfigs(sender);
                            sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[19][1]);
                        } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[10]);
                    } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[10]);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void invitePlayer(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[20][0]);
        } else {
            Job job = Jobs.get(args[1]);
            if (job != null && cmd.getName().equalsIgnoreCase("mjc") && job.IsCustom){
                if (sender.hasPermission("MineJobs.custom.invitePlayer") && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass"))){
                    Player player = Players.get(args[2]);
                    if (player != null){
                        if (!player.Invites.contains(job.Name)) player.Invites.add(job.Name);
                        Main.saveConfigs(sender);
                        sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[20][1]);
                    } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[10]);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
}