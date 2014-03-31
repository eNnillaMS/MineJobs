public class oldMJ{
    
    
    
    
    
    
    
    @EventHandler public void ToolEnchanted(EnchantItemEvent event){
        String playerName = event.getEnchanter().getName().toLowerCase();
        if(!event.getEnchanter().getGameMode().equals(GameMode.CREATIVE) && playerConfig.getConfigurationSection("players").getKeys(false).contains(playerName)){
            for (String job:playerConfig.getStringList("players." + playerName + ".jobs")) {
                if(jobConfig.getConfigurationSection("jobs").getKeys(false).contains(job.toUpperCase())){
                    if(jobConfig.getBoolean("jobs." + job.toUpperCase() + ".enchant.active") == true && jobConfig.getStringList("jobs." + job.toUpperCase() + ".worlds").contains(event.getEnchantBlock().getWorld().getName().toLowerCase())){
                       double value = jobConfig.getDouble("jobs." + job.toUpperCase() + ".enchant.payPerLevel");
                       econ.depositPlayer(playerName, value * event.getExpLevelCost());
                    }
                } else if (mainConfig.getBoolean("customJobs") == true && customJobConfig.getConfigurationSection("customs").getKeys(false).contains(job.toUpperCase())){
                    if(customJobConfig.getBoolean("customs." + job.toUpperCase() + ".enchant.active") == true && customJobConfig.getStringList("customs." + job.toUpperCase() + ".worlds").contains(event.getEnchantBlock().getWorld().getName().toLowerCase())){
                        double value = customJobConfig.getDouble("customs." + job.toUpperCase() + ".enchant.payPerLevel");
                        econ.withdrawPlayer(customJobConfig.getString("customs." + job.toUpperCase() + ".owner"), value * event.getExpLevelCost());
                        econ.depositPlayer(playerName, value * event.getExpLevelCost());
                    }
                }
            }
        }
    }
    
    @EventHandler public void BlockPushed(BlockPistonExtendEvent event){
        for (Block bl:event.getBlocks()){
            if (bl.getPistonMoveReaction().toString().equals("MOVE")){
                for (String nm:playerConfig.getConfigurationSection("players").getKeys(false)){
                    if (bl.hasMetadata("MJ:" + nm)){
                        Block blN = bl.getWorld().getBlockAt(bl.getLocation().add(event.getDirection().getModX(), event.getDirection().getModY(), event.getDirection().getModZ()));
                        blN.setMetadata("MJ:" + nm, new FixedMetadataValue(this, nm));
                    }
                }
            }
        }
    }
    @EventHandler public void BlockPulled(BlockPistonRetractEvent event){
        if (event.getBlock().getPistonMoveReaction().toString().equals("MOVE")){
            for (String nm:playerConfig.getConfigurationSection("players").getKeys(false)){
                if (event.getBlock().hasMetadata("MJ:" + nm)){
                    Block blN = event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().add(event.getDirection().getModX(), event.getDirection().getModY(), event.getDirection().getModZ()));
                    blN.setMetadata("MJ:" + nm, new FixedMetadataValue(this, nm));
                }
            }
        }
    }
    @EventHandler public void MobSpawned(CreatureSpawnEvent event){
        if (!mainConfig.getBoolean("spawnerCash")){
            if (event.getSpawnReason().toString().equals("SPAWNER") || event.getSpawnReason().toString().equals("SPAWNER_EGG") || event.getSpawnReason().toString().equals("EGG")){
                event.getEntity().setMetadata("MJ:FAKE", new FixedMetadataValue(this, "FAKE"));
            }
        }
    }
    
    @EventHandler public void NewPlayerJoin(PlayerJoinEvent event){
        if (!playerConfig.getConfigurationSection("players").getKeys(false).contains(event.getPlayer().getName().toLowerCase())){
            playerConfig.createSection("players." + event.getPlayer().getName().toLowerCase() + ".jobs");
            for (String job:mainConfig.getStringList("defaultJobs")){
                List newList = playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs");
                newList.add(job.toUpperCase());
                playerConfig.set("players." + event.getPlayer().getName().toLowerCase() + ".jobs", newList);
            }
            for (String job:mainConfig.getStringList("forcedJobs")){
                List newList = playerConfig.getStringList("players." + event.getPlayer().getName().toLowerCase() + ".jobs");
                newList.add(job.toUpperCase());
                playerConfig.set("players." + event.getPlayer().getName().toLowerCase() + ".jobs", newList);
            }
            playerConfig.createSection("players." + event.getPlayer().getName().toLowerCase() + ".invites");
            saveConfigs(event.getPlayer());
        }
    }
}