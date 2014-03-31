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
            sender.sendMessage(clr + Lang.CommandOutput[6][0].replace("%CMD%", cmd.getName()));
        } else {
            
        }
    }
    /*public void upgrade(CommandSender sender, Command cmd, String label, String[] args){
        
    }*/
}