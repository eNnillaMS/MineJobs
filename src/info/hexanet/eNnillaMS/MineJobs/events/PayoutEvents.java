package info.hexanet.eNnillaMS.MineJobs.events;
import info.hexanet.eNnillaMS.MineJobs.MineJobs;
import info.hexanet.eNnillaMS.MineJobs.classes.Conf;
import info.hexanet.eNnillaMS.MineJobs.classes.Job;
import info.hexanet.eNnillaMS.MineJobs.classes.Lang;
import info.hexanet.eNnillaMS.MineJobs.classes.Player;
import info.hexanet.eNnillaMS.MineJobs.classes.SignC;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
public class PayoutEvents implements Listener {
    private final MineJobs Main;
    private final Conf Config;
    private final Lang Lang;
    private final Map<String, Job> Jobs;
    private final Map<String, Player> Players;
    private final Map<Location, SignC> Signs;
    public PayoutEvents(MineJobs main){
        Main = main;
        Config = main.Config;
        Lang = main.Lang;
        Jobs = main.Jobs;
        Players = main.Players;
        Signs = main.Signs;
    }
    @EventHandler(priority = EventPriority.MONITOR) public void BlockBroken(BlockBreakEvent event) {
        org.bukkit.entity.Player plyr = event.getPlayer();
        Player player = Players.get(plyr.getName());
        if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getBlock().getType().toString() + "#" + event.getBlock().getData()));
        if(!plyr.getGameMode().equals(GameMode.CREATIVE) && player != null){
            for (String jobS:player.Jobs){
                Job job = Jobs.get(jobS.toUpperCase());
                if (job != null){
                    Double value = job.Break.get(event.getBlock().getType().toString());
                    if (value == null) value = job.Break.get(event.getBlock().getType().toString() + "#" + event.getBlock().getData());
                    if (!event.isCancelled() && value != null && job.Worlds.contains(event.getBlock().getWorld().getName())){
                        Double mult = job.Tools.get(event.getPlayer().getItemInHand().getType().toString());
                        if (mult == null) mult = 1.00;
                        if (job.IsCustom && !event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value * mult);
                        if (!event.getBlock().hasMetadata("MJ:".concat(event.getPlayer().getName()))) Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value * mult);
                        if (Config.DebugOutput) plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[0].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", event.getBlock().getType().toString() + "#" + event.getBlock().getData()));
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void BlockPlaced(BlockPlaceEvent event){
        org.bukkit.entity.Player plyr = event.getPlayer();
        Player player = Players.get(plyr.getName());
        if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getBlock().getType().toString() + "#" + event.getBlock().getData()));
        if(!plyr.getGameMode().equals(GameMode.CREATIVE) && player != null){
            for (String jobS:player.Jobs) {
                Job job = Jobs.get(jobS.toUpperCase());
                if (job != null){
                    String block = event.getBlock().getType().toString();
                    String data = "#" + event.getBlock().getData();
                    if ((job.Place.containsKey(block) || job.Place.containsKey(block + data)) && job.Worlds.contains(event.getBlock().getWorld().getName())){
                        boolean pay = true;
                        for (String bJob:player.Jobs){
                            for (Map.Entry<String, Double> item:Jobs.get(bJob).Break.entrySet()){
                                if ((item.getKey().equalsIgnoreCase(block) || item.getKey().equalsIgnoreCase(block + data)) && pay == true){
                                    pay = false;
                                }
                            }
                        }
                        if (!event.isCancelled() && pay == true){
                            Double value = job.Place.get(block);
                            if (value == null) value = job.Place.get(block + data);
                            if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value);
                            Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value);
                            if (Config.DebugOutput) plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[1].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", block + data));
                        }
                    }
                }
            }
        }
        if (!event.getBlock().hasMetadata("MJ:".concat(plyr.getName()))) event.getBlock().setMetadata("MJ:".concat(plyr.getName()), new FixedMetadataValue(Main, plyr.getName()));
    }
    @EventHandler(priority = EventPriority.MONITOR) public void MobKilled(EntityDeathEvent event){
        if(event.getEntity().getKiller() != null){
            org.bukkit.entity.Player plyr = event.getEntity().getKiller();
            Player player = Players.get(plyr.getName());
            if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getEntityType().toString()));
            if(!plyr.getGameMode().toString().equalsIgnoreCase("CREATIVE") && player != null){
                for (String jobS:player.Jobs) {
                    Job job = Jobs.get(jobS);
                    if(job != null && job.Worlds.contains(event.getEntity().getKiller().getWorld().getName())){
                        String ename = event.getEntityType().toString();
                        if (ename.equalsIgnoreCase("PLAYER")){
                            UUID temp = event.getEntity().getUniqueId();
                            switch (Config.UseDeathLosses) {
                                case "always":
                                    Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(temp), Config.DeathLoss);
                                    break;
                                case "job":
                                    if (job.DeathLosses) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(temp), Config.DeathLoss);
                                    break;
                                case "never":
                                default:
                                    break;
                            }
                        }
                        Double value = job.Mobs.get(ename);
                        if(value != null){
                            if (!event.getEntity().hasMetadata("MJ:FAKE") || Config.SpawnerMobPayout){
                                Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value);
                                if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value);
                                plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[2].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", ename));
                            } /*HERE - not sure why this is here anymore - PREv4.0.0*/
                        }
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void PlayerFished(PlayerFishEvent event){
        if (event.getCaught() != null){
            org.bukkit.entity.Player plyr = event.getPlayer();
            Player player = Players.get(plyr.getName());
            if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getCaught().getType().toString()));
            if (!plyr.getGameMode().toString().equals("CREATIVE") && player != null){
                for (String jobS:player.Jobs){
                    Job job = Jobs.get(jobS);
                    if (!event.isCancelled() && job != null && job.Fish.containsKey(event.getCaught().getType().toString()) && job.Worlds.contains(event.getPlayer().getWorld().getName())){
                        Double value = job.Fish.get(event.getCaught().getType().toString());
                        Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value);
                        if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value);
                        event.getPlayer().sendMessage(ChatColor.GOLD + Lang.ActionSuccess[3].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", event.getCaught().getType().toString()));
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST) public void ItemCrafted(CraftItemEvent event){
        org.bukkit.entity.Player plyr = (org.bukkit.entity.Player) event.getWhoClicked();
        Player player = Players.get(plyr.getName());
        if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getCurrentItem().getType().toString() + "#" + event.getCurrentItem().getData()));
        if (!plyr.getGameMode().equals(GameMode.CREATIVE) && player != null){
            for (String jobS:player.Jobs){
                Job job = Jobs.get(jobS);
                if (job != null){
                    Double value = job.Craft.get(event.getCurrentItem().getType().toString());
                    if (value == null) job.Craft.get(event.getCurrentItem().getType().toString() + "#" + event.getCurrentItem().getData());
                    if (!event.isCancelled() && value != null && job.Worlds.contains(event.getWhoClicked().getWorld().getName())){
                        if (event.isShiftClick()){
                            event.setCancelled(true);
                            return;
                        }
                        Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value);
                        if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value);
                        if (Config.DebugOutput) plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[4].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", event.getCurrentItem().getType().toString() + "#" + event.getCurrentItem().getData()));
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void FurnaceUsed(FurnaceSmeltEvent event){
        org.bukkit.entity.Player plyr = null;
        try {
            List<MetadataValue> wop = event.getBlock().getMetadata(Main.blockOwnerKey);
            String playerName = wop.get(0).asString();
            Collection<?> playersOnline = new ArrayList<>();
            try {
                if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class)
                    playersOnline = ((Collection<?>)Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
                else
                    playersOnline = Arrays.asList((org.bukkit.entity.Player[])Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
            }
            catch (NoSuchMethodException | java.lang.reflect.InvocationTargetException | IllegalAccessException ex){} // can still never happen
            for (org.bukkit.entity.Player p:(Collection<? extends org.bukkit.entity.Player>) playersOnline){
                if (p.getName().equals(playerName)) plyr = p;
            }
        } catch (Exception e){ return; }
        if (plyr != null){
        if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getSource().getType().toString() + "#" + event.getSource().getData()));
            Player player = Players.get(plyr.getName());
            if(player != null){
                for (String jobS:player.Jobs){
                    Job job = Jobs.get(jobS);
                    if(job != null){
                        Double value = job.Smelt.get(event.getSource().getType().toString());
                        if (value == null) value = job.Smelt.get(event.getSource().getType().toString() + "#" + event.getSource().getData());
                        if(!event.isCancelled() && value != null && job.Worlds.contains(event.getBlock().getWorld().getName())){
                           Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value);
                           if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value);
                           if (Config.DebugOutput) plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[6].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", event.getSource().getType().toString() + "#" + event.getSource().getData()));
                        }
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void PotionBrewed(BrewEvent event){
        org.bukkit.entity.Player plyr = null;
        try {
            List<MetadataValue> wop = event.getBlock().getMetadata(Main.blockOwnerKey);
            String playerName = wop.get(0).asString();Collection<?> playersOnline = new ArrayList<>();
            try {
                if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class)
                    playersOnline = ((Collection<?>)Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
                else
                    playersOnline = Arrays.asList((org.bukkit.entity.Player[])Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).invoke(null, new Object[0]));
            }
            catch (NoSuchMethodException | java.lang.reflect.InvocationTargetException | IllegalAccessException ex){} // can still never happen
            for (org.bukkit.entity.Player p:(Collection<? extends org.bukkit.entity.Player>) playersOnline){
                if (p.getName().equals(playerName)) plyr = p;
            }
        } catch (Exception e){ return; }
        if (plyr != null){
            if (Config.DebugOutput) plyr.sendMessage(Lang.ActionSuccess[10].replace("%ITEM%", event.getContents().getIngredient().getType().toString() + "#" + event.getContents().getIngredient().getData()));
            Player player = Players.get(plyr.getName());
            if(player != null){
                for (String jobS:player.Jobs){
                    Job job = Jobs.get(jobS);
                    if(job != null){
                        Double value = job.Brew.get(event.getContents().getIngredient().getType().toString());
                        if (value == null) value = job.Brew.get(event.getContents().getIngredient().getType().toString() + "#" + event.getContents().getIngredient().getData());
                        if(!event.isCancelled() && value != null && job.Worlds.contains(event.getBlock().getWorld().getName())){
                           Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value);
                           if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value);
                           if (Config.DebugOutput) plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[7].replace("%VALUE%", String.valueOf(value)).replace("%ITEM%", event.getContents().getIngredient().getType().toString() + "#" + event.getContents().getIngredient().getData()));
                        }
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void ToolEnchanted(EnchantItemEvent event){
        org.bukkit.entity.Player plyr = event.getEnchanter();
        Player player = Players.get(plyr.getName());
        if (!plyr.getGameMode().equals(GameMode.CREATIVE) && player != null){
            for (String jobS:player.Jobs){
                Job job = Jobs.get(jobS);
                if (!event.isCancelled() && job != null && job.EnchantEnabled && job.Worlds.contains(event.getEnchantBlock().getWorld().getName())){
                    Double value = job.EnchantPay;
                    Main.econ.depositPlayer(Main.getServer().getOfflinePlayer(plyr.getUniqueId()), value * event.getExpLevelCost());
                    if (job.IsCustom) Main.econ.withdrawPlayer(Main.getServer().getOfflinePlayer(new UUID(0,0).fromString(job.Owner)), value * event.getExpLevelCost());
                    if (Config.DebugOutput) plyr.sendMessage(ChatColor.GOLD + Lang.ActionSuccess[7].replace("%NUM%", String.valueOf(event.getExpLevelCost())).replace("%VALUE%", String.valueOf(event.getExpLevelCost() * value)));
                }
            }
        }
    }
}
