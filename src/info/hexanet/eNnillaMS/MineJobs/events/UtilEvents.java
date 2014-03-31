package info.hexanet.eNnillaMS.MineJobs.events;
import info.hexanet.eNnillaMS.MineJobs.MineJobs;
import info.hexanet.eNnillaMS.MineJobs.classes.Conf;
import info.hexanet.eNnillaMS.MineJobs.classes.Job;
import info.hexanet.eNnillaMS.MineJobs.classes.Lang;
import info.hexanet.eNnillaMS.MineJobs.classes.Player;
import info.hexanet.eNnillaMS.MineJobs.classes.SignC;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
public class UtilEvents implements Listener {
    private final MineJobs Main;
    private final Conf Config;
    private final Lang Lang;
    private final Map<String, Job> Jobs;
    private final Map<String, Player> Players;
    private final Map<Location, SignC> Signs;
    public UtilEvents(MineJobs main){
        Main = main;
        Config = main.Config;
        Lang = main.Lang;
        Jobs = main.Jobs;
        Players = main.Players;
        Signs = main.Signs;
    }
    @EventHandler(priority = EventPriority.MONITOR) public void BlockOpened(InventoryOpenEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            Inventory inv = event.getInventory();
            if (inv instanceof FurnaceInventory){
                Furnace furnace = (Furnace) inv.getHolder();
                if (furnace != null && furnace.getBurnTime() == 0){
                    Block furnaceBlock = furnace.getBlock();
                    if (!furnaceBlock.hasMetadata(Main.blockOwnerKey)) furnaceBlock.setMetadata(Main.blockOwnerKey, new FixedMetadataValue(Main, event.getPlayer().getName()));
                }
            } else if (inv instanceof BrewerInventory){
                BrewingStand brewer = (BrewingStand) inv.getHolder();
                if (brewer != null && brewer.getBrewingTime() == 0){
                    Block brewStand = brewer.getBlock();
                    if (!brewStand.hasMetadata(Main.blockOwnerKey)) brewStand.setMetadata(Main.blockOwnerKey, new FixedMetadataValue(Main, event.getPlayer().getName()));
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void BlockClosed(InventoryCloseEvent event){
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            Inventory inv = event.getInventory();
            if (inv instanceof FurnaceInventory){
                Furnace furnace = (Furnace) inv.getHolder();
                if (furnace != null && furnace.getBurnTime() == 0){
                    Block furnaceBlock = furnace.getBlock();
                    if (furnaceBlock.hasMetadata(Main.blockOwnerKey)) furnaceBlock.removeMetadata(Main.blockOwnerKey, Main);
                }
            } else if (inv instanceof BrewerInventory){
                BrewingStand brewer = (BrewingStand) inv.getHolder();
                if (brewer != null && brewer.getBrewingTime() == 0){
                    Block brewStand = brewer.getBlock();
                    if (brewStand.hasMetadata(Main.blockOwnerKey)) brewStand.removeMetadata(Main.blockOwnerKey, Main);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void NewPlayerJoin(PlayerJoinEvent event){
        if (!Players.containsKey(event.getPlayer().getName())){
            List<String> jobs = new ArrayList<>();
            for (String nd:Config.DefaultJobs) if (!jobs.contains(nd)) jobs.add(nd);
            for (String nf:Config.ForcedJobs) if (!jobs.contains(nf)) jobs.add(nf);
            Players.put(event.getPlayer().getName(), new Player(event.getPlayer().getName(), jobs, new ArrayList<String>()));
            Main.saveConfigs(event.getPlayer());
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void BlockPushed(BlockPistonExtendEvent event){
        int x = event.getDirection().getModX(), y = event.getDirection().getModY(), z = event.getDirection().getModZ();
        for (Block bl:event.getBlocks()){
            if (bl.getPistonMoveReaction().toString().equals("MOVE")){
                Block blN = bl.getRelative(x, y, z);
                for (Map.Entry<String, Player> nm:Players.entrySet()){
                    if (bl.hasMetadata("MJ:" + nm.getKey())){
                        blN.setMetadata("MJ:" + nm.getKey(), new FixedMetadataValue(Main, nm.getKey()));
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void BlockPulled(BlockPistonRetractEvent event){
        if (event.getBlock().getPistonMoveReaction().toString().equals("MOVE")){
            Block blN = event.getBlock().getRelative(event.getDirection().getModX(), event.getDirection().getModY(), event.getDirection().getModZ());
            for (Map.Entry<String, Player> nm:Players.entrySet()){
                if (event.getBlock().hasMetadata("MJ:" + nm.getKey())){
                    blN.setMetadata("MJ:" + nm.getKey(), new FixedMetadataValue(Main, nm.getKey()));
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR) public void MobSpawned(CreatureSpawnEvent event){
        if (!Config.SpawnerMobPayout){
            if (event.getSpawnReason().toString().equals("SPAWNER") || event.getSpawnReason().toString().equals("SPAWNER_EGG") || event.getSpawnReason().toString().equals("EGG")){
                event.getEntity().setMetadata("MJ:FAKE", new FixedMetadataValue(Main, "FAKE"));
            }
        }
    }
}