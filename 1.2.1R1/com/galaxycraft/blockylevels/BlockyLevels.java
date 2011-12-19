package com.galaxycraft.blockylevels;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockyLevels extends JavaPlugin {
	
    public static Permission permission = null;

    public Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
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
		
		log.info("[BlockyLevels] has been enabled!");
		log.info("[BlockyLevels] Current Version: 1.2.1 R1");
		
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
		saveConfig();
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		
	}
}