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
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
                } else if (cmd.getName().equals("mjc") && Config.UseCustoms && sender.hasPermission("MineJobs.customs.create")){
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
                if (job != null){
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
            if (job != null){
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
            if (job != null){
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
                    Main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[8][1].replace("%JOB%", args[1].toUpperCase()).replace("%JOB2%", job.Name));
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    public void setMax(CommandSender sender, Command cmd, String label, String[] args){
        if (args.length != 3 || args[1].equalsIgnoreCase("?")){
            sender.sendMessage(clr + Lang.CommandOutput[9][0]);
        } else {
            Job job = Jobs.get(args[1].toUpperCase());
            if (job != null){
                if ((!job.IsCustom && sender.hasPermission("MineJobs.admin.setMax")) || (job.IsCustom && (job.Owner.equals(sender.getName()) || sender.hasPermission("MineJobs.admin.customOwnerBypass")) && sender.hasPermission("MineJobs.custom.setMax"))){
                    job.MaxPlayers = Integer.valueOf(args[2]);
                    Main.saveConfigs(sender);
                    sender.sendMessage(ChatColor.GREEN + Lang.CommandOutput[9][1]);
                } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
            } else sender.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
        }
    }
    /*public void upgrade(CommandSender sender, Command cmd, String label, String[] args){
        
    }*/
    /*public void upgrade(CommandSender sender, Command cmd, String label, String[] args){
        
    }*/
    /*public void upgrade(CommandSender sender, Command cmd, String label, String[] args){
        
    }*/
}