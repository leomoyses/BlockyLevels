package com.galaxycraft.blockylevels;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockyLevelsCommandExecutor implements CommandExecutor {
	
	Logger log = Logger.getLogger("Minecraft");
	
	static BlockyLevels plugin;
	
	public static Permission permission;

	public BlockyLevelsCommandExecutor(BlockyLevels plugin) {
		this.plugin = plugin;
	}
	
	public static boolean isAuthorized(final Player player, final String node) {
		if (player.isOp()) {
			return true;
		} else if (player.hasPermission(node)) {
			return true;
		} else if (plugin.setupPermissions()) {
			if (BlockyLevels.permission.has(player, node)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel,
			String[] args) {
		
		int minLevel;
		minLevel = plugin.getConfig().getInt("config.minlevel", 1);
		int blockID;
		blockID = plugin.getConfig().getInt("config.blockid", 19);		
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		
		String playername = sender.getName();
		
		int currentLevel = player.getLevel();
		PlayerInventory inventory = player.getInventory();
		
		String help1m1 = ChatColor.GOLD + "--- BlockyLevels Help (1/1) ---";
		String help1m2 = ChatColor.DARK_RED + "-- Commands:";
		String help1m3 = ChatColor.GREEN + "/bl block" + ChatColor.RED + " - Converts all your levels to the block set in the config.";
		String help1m4 = ChatColor.GREEN + "/bl level [How many you want]" + ChatColor.RED + " - Converts your blocks set in the config to levels.";
		String help1m5 = ChatColor.YELLOW + "/bl reload" + ChatColor.LIGHT_PURPLE + " - Reloads the plugin with the edited config.yml file.";
		String help1m6 = ChatColor.YELLOW + "/bl set [Config Name]" + ChatColor.LIGHT_PURPLE + " - Edit the config.yml file in-game.";
		
		String help2m1 = ChatColor.GOLD + "--- BlockyLevels Set Command Help (1/1) ---";
		String help2m2 = ChatColor.DARK_RED + "-- Commands:";
		String help2m3 = ChatColor.YELLOW + "/bl set [Config Name]" + ChatColor.LIGHT_PURPLE + " - Edit the config.yml file in-game.";
		String help2m4 = ChatColor.DARK_GREEN + "Example:" + ChatColor.RED + " If you want to change the blockID on the config.yml, type " + ChatColor.GREEN + " /bl set blockid [ID]" + ChatColor.RED + ".";
		
		String help3m1 = ChatColor.GOLD + "--- BlockyLevels Level Command Help (1/1) ---";
		String help3m2 = ChatColor.DARK_RED + "-- Commands:";
		String help3m3 = ChatColor.GREEN + "/bl level [How many you want]" + ChatColor.RED + " - Converts your blocks set in the config to levels.";
		String help3m4 = ChatColor.DARK_GREEN + "Example:" + ChatColor.RED + " If you have 10 blocks in a stack, type" + ChatColor.GREEN + " /bl level 10" + ChatColor.RED + " to trade 10 blocks for 10 levels.";
		
		
		if(cmd.getName().equalsIgnoreCase("bl")){
			if(args.length < 1){
				sender.sendMessage(help1m1);
				sender.sendMessage(help1m2);
				sender.sendMessage(help1m3);
				sender.sendMessage(help1m4);
				sender.sendMessage(help1m5);
				sender.sendMessage(help1m6);
				return true;
			}
			if(args[0].equalsIgnoreCase("block")){
				if (!isAuthorized(player, "blockylevels.block")){
					player.sendMessage(ChatColor.RED + "You don't have permissions for this command!");
					log.info("[BlockyLevels] " + playername + " has been denied to use /bl block.");
					return true;
				}else if(!(currentLevel >= minLevel)){
					sender.sendMessage(ChatColor.RED + "You need to be higher than level " + ChatColor.DARK_RED + minLevel + ".");
					log.info("[BlockyLevels] " + playername + " has too low level to convert his levels.");
					return true;
				}else{
					final ItemStack stack = new ItemStack(blockID);
					final String itemName = stack.getType().toString().toLowerCase().replace('_', ' ');
					inventory.addItem(new ItemStack(blockID, currentLevel));	
					sender.sendMessage(ChatColor.GREEN + "You converted " + ChatColor.DARK_GREEN + currentLevel + ChatColor.GREEN + " levels into " + ChatColor.DARK_GREEN + currentLevel + ChatColor.GREEN + " " + itemName + "ss.");
					log.info("[BlockyLevels] " + playername + " has converted " + currentLevel + " levels into " + currentLevel + " " + itemName + "ss.");
					((Player) sender).setLevel(0);
					return true;
				}
			}
				if(args[0].equalsIgnoreCase("level")){
					final ItemStack stack = new ItemStack(blockID);
					final String itemName = stack.getType().toString().toLowerCase().replace('_', ' ');

					if(args.length < 2){
						sender.sendMessage(help3m1);
						sender.sendMessage(help3m2);
						sender.sendMessage(help3m3);
						sender.sendMessage(help3m4);
						return true;
					}
					
					int spongeArgs = Integer.parseInt(args[1]);

					if (!isAuthorized(player, "blockylevels.level")){
						player.sendMessage(ChatColor.RED + "You don't have permissions for this command!");
						log.info("[BlockyLevels] " + playername + " has been denied to use /bl level.");
						return true;
					}else if(spongeArgs > 64){
						sender.sendMessage(ChatColor.RED + "A stack of " + itemName + "s can have only up to 64 items!");
						log.info("[BlockyLevels] " + playername + " has tried to convert more than 64 " + itemName + "s to levels.");
						return true;
					}else if(!inventory.contains(new ItemStack(blockID, spongeArgs))){
						sender.sendMessage(ChatColor.RED + "You don't have " + ChatColor.GREEN + spongeArgs + ChatColor.RED + " " + itemName + "ss in a stack!");
						return true;
					}else{
						sender.sendMessage(ChatColor.GREEN + "You converted " + ChatColor.DARK_GREEN + spongeArgs + ChatColor.GREEN + " " + itemName + "s into " + ChatColor.DARK_GREEN + spongeArgs + ChatColor.GREEN + " levels.");
						log.info("[BlockyLevels] " + playername + " has converted " + spongeArgs + " " + itemName + "s into " + spongeArgs + "s levels.");
						inventory.removeItem(new ItemStack(blockID, spongeArgs));
						((Player) sender).setLevel((currentLevel + spongeArgs));
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("debug")){
					if(args[1].equalsIgnoreCase("plugin")){
							int debugArgs = Integer.parseInt(args[2]);
							int debugArgs2 = Integer.parseInt(args[3]);
							inventory.addItem(new ItemStack(debugArgs, debugArgs2));	
							return true;
					}
				}
				if(args[0].equalsIgnoreCase("reload")) {
					if (!isAuthorized(player, "blockylevels.reload")){
						player.sendMessage(ChatColor.RED + "You don't have permissions for this command!");
						log.info("[BlockyLevels] " + playername + " has been denied to use /bl reload.");
						return true;
					}else{
						plugin.reloadConfig();
						plugin.saveConfig();
						sender.sendMessage(ChatColor.YELLOW + "BlockyLevels reloaded successfully!");
						log.info("[BlockyLevels] reloaded successfully!");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("set")){
					if(!isAuthorized(player, "blockylevels.set")){
						player.sendMessage(ChatColor.RED + "You don't have permissions for this command!");
						log.info("[BlockyLevels] " + playername + " has been denied to use /bl set.");
						return true;
					}else{
						if(args.length < 3){
							sender.sendMessage(help2m1);
							sender.sendMessage(help2m2);
							sender.sendMessage(help2m3);
							sender.sendMessage(help2m4);
							return true;
						}else if(args[1].equalsIgnoreCase("blockid")){
							int blockIDArgs = Integer.parseInt(args[2]);
							log.info("[BlockyLevels] " + playername + " set config 'blockid' as " + blockIDArgs + ".");
							log.info("[BlockyLevels] Setting the new 'blockid' config as " + blockIDArgs + "...");
							plugin.getConfig().set("config.blockid", blockIDArgs);
							log.info("[BlockyLevels] Saving config.yml...");
							plugin.saveConfig();
							log.info("[BlockyLevels] Reloading config.yml...");
							plugin.reloadConfig();
							log.info("[BlockyLevels] New config 'blockid' set, saved, and reloaded as " + blockIDArgs + " successfully!");
							sender.sendMessage(ChatColor.YELLOW + "BlockyLevels config 'blockid' set as " + blockIDArgs + " successfully!");
							return true;
						}else if(args[1].equalsIgnoreCase("minlevel")){
							int minLevelArgs = Integer.parseInt(args[2]);
							log.info("[BlockyLevels] " + playername + " set config 'minlevel' as " + minLevelArgs + ".");
							log.info("[BlockyLevels] Setting the new 'minlevel' config as " + minLevelArgs + "...");
							plugin.getConfig().set("config.minlevel", minLevelArgs);
							log.info("[BlockyLevels] Saving config.yml...");
							plugin.saveConfig();
							log.info("[BlockyLevels] Reloading config.yml...");
							plugin.reloadConfig();
							log.info("[BlockyLevels] New config 'minlevel' set, saved, and reloaded as " + minLevelArgs + " successfully!");
							sender.sendMessage(ChatColor.YELLOW + "BlockyLevels config 'minlevel' set as " + minLevelArgs + " successfully!");
							return true;
						}						
					}
				}
			}
		sender.sendMessage(help1m1);
		sender.sendMessage(help1m2);
		sender.sendMessage(help1m3);
		sender.sendMessage(help1m4);
		return true;
	}

}
