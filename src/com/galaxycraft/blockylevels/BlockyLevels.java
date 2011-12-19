package com.galaxycraft.blockylevels;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockyLevels extends JavaPlugin {
	
    public static Permission permission = null;
    public static Economy economy = null;

    public Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
    public Boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	protected FileConfiguration config;
	
	Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onDisable() {
		log.info("[BlockyLevels] is saving it's data...");
		saveConfig();
		log.info("[BlockyLevels] the data has been saved succesfully!");
		log.info("[BlockyLevels] has been disabled!");
		
	}

	@Override
	public void onEnable() {		
		this.getCommand("bl").setExecutor(new BlockyLevelsCommandExecutor(this));
		
		setupPermissions();
		setupEconomy();
		
		log.info("[BlockyLevels] has been enabled!");
		log.info("[BlockyLevels] Current Version: 0.99 Beta");
		
		try{
			config = getConfig();
		File BlockyLevels = new File("plugins" + File.separator + "BlockyLevels" + File.separator + "config.yml");
		BlockyLevels.mkdir();
		if(!config.contains("config.minlevel")){
		    config.set("config.minlevel", 1);
		}
		if(!config.contains("config.blockid")){
		    config.set("config.blockid", 19);
		}
		if(!config.contains("economy.enabled")){
		    config.set("economy.enabled", false);
		}
		if(!config.contains("economy.currency")){
		    config.set("economy.currency", "Dollars");
		}
		if(!config.contains("economy.blockprice")){
		    config.set("economy.blockprice", 100);
		}
		if(!config.contains("economy.levelprice")){
		    config.set("economy.levelprice", 100);
		}
		saveConfig();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		
	}
}