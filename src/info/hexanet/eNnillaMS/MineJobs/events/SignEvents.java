package info.hexanet.eNnillaMS.MineJobs.events;
import info.hexanet.eNnillaMS.MineJobs.MineJobs;
import info.hexanet.eNnillaMS.MineJobs.classes.Conf;
import info.hexanet.eNnillaMS.MineJobs.classes.Job;
import info.hexanet.eNnillaMS.MineJobs.classes.Lang;
import info.hexanet.eNnillaMS.MineJobs.classes.Player;
import info.hexanet.eNnillaMS.MineJobs.classes.SignC;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
public class SignEvents implements Listener {
    private final MineJobs Main;
    private final Conf Config;
    private final Lang Lang;
    private final Map<String, Job> Jobs;
    private final Map<String, Player> Players;
    private final Map<Location, SignC> Signs;
    public SignEvents(MineJobs main){
        Main = main;
        Config = main.Config;
        Lang = main.Lang;
        Jobs = main.Jobs;
        Players = main.Players;
        Signs = main.Signs;
    }
    @EventHandler(priority = EventPriority.MONITOR) public void SignCreate(SignChangeEvent event){
        if (Config.UseSigns && (event.getLine(0).equalsIgnoreCase("[getajob]") || event.getLine(0).equalsIgnoreCase("[quitajob]"))){
            String type = (event.getLine(0).equalsIgnoreCase("[getajob]")) ? "GET" : "QUIT";
            org.bukkit.entity.Player plyr = event.getPlayer();
            if ((type.equals("GET") && plyr.hasPermission("MineJobs.signs.makeGetSign")) || (type.equals("QUIT") && plyr.hasPermission("MineJobs.signs.makeQuitSign"))){
                Job job = Jobs.get(event.getLine(2).toUpperCase());
                if (job != null){
                    if (Config.UseCmdEconomy) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), Config.Eco[7]);
                    if (type.equals("GET")) event.setLine(0, ChatColor.GREEN + "[Get A Job]");
                    else event.setLine(0, ChatColor.RED + "[Quit A Job]");
                    event.setLine(1, "");
                    event.setLine(2, job.Name.toUpperCase());
                    event.setLine(3, "");
                    Signs.put(event.getBlock().getLocation(), new SignC(event.getBlock().getLocation(), type, job.Name));
                    Main.saveConfigs(plyr);
                    plyr.sendMessage(ChatColor.GREEN + Lang.ActionSuccess[8]);
                } else plyr.sendMessage(ChatColor.RED + Lang.GeneralErrors[3]);
            } else plyr.sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
        } else Main.getLogger().severe(Lang.GeneralErrors[14].replace("%PLAYER%", event.getPlayer().getName()));
    }
    @EventHandler(priority = EventPriority.MONITOR) public void SignInteract(PlayerInteractEvent event){
        if (Config.UseSigns){
            if (event.getAction().toString().equalsIgnoreCase("RIGHT_CLICK_BLOCK") && (event.getClickedBlock().getType().toString().equals("SIGN") || event.getClickedBlock().getType().toString().equals("SIGN_POST"))){
                Location loc = new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ());
                SignC sign = Signs.get(loc);
                if (sign != null){
                    if ((sign.Type.equals("GET") && event.getPlayer().hasPermission("MineJobs.signs.useGetSign")) || (sign.Type.equals("QUIT") && event.getPlayer().hasPermission("MineJobs.signs.useQuitSign"))){
                        String[] args;
                        if (sign.Type.equals("GET")) args = new String[]{"getJob", sign.Job};
                        else args = new String[]{"quitJob", sign.Job};
                        PluginCommand execCommand = Bukkit.getServer().getPluginCommand("mj");
                        execCommand.execute(event.getPlayer(), "mj", args);
                    } else event.getPlayer().sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST) public void SignBroken(BlockBreakEvent event) {
        if (Config.UseSigns){
            Location loc = new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
            SignC sign = Signs.get(loc);
            if ((event.getBlock().getType().toString().equals("SIGN") || event.getBlock().getType().toString().equals("SIGN_POST")) && sign != null){
                if (event.getPlayer().hasPermission("MineJobs.signs.breakSign") && !event.isCancelled()){
                    if (Config.UseCmdEconomy) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(event.getPlayer().getUniqueId()), Config.Eco[8]);
                    Signs.remove(loc);
                    event.getPlayer().sendMessage(ChatColor.GREEN + Lang.ActionSuccess[9]);
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + Lang.GeneralErrors[2]);
                }
            }
        }
    }
}
