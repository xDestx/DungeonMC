package com.xdest.mcrpg.trade;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CustomItem {
	
	private static Map<String,ItemStack> customItems = null;
	
	public static ItemStack getCustomItemById(String id) {
		return customItems.get(id);
	}
	
	public static void loadItems() {
		CustomItem.customItems = new HashMap<String,ItemStack>();
		//for each
		//ItemStack.deserialize()
		FileConfiguration fg;
		
		File source = new File(Bukkit.getPluginManager().getPlugin("xDestMCRPG").getDataFolder().getPath() + "/custom-item/");
		if(!source.exists()) {
			source.mkdirs();
			return;
		}
		File[] profiles = source.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".yml");
			}
			
		});
		for(File f : profiles) {
			try {
				fg = new YamlConfiguration();
				fg.load(f);
				String itemId = fg.getString("id");
				ItemStack item = fg.getItemStack("item");
				if(item != null) {
					CustomItem.customItems.put(itemId, item);
					Logger.getLogger("Minecraft").warning("[CustomItem] Loaded item " + itemId);
				} else {
					Logger.getLogger("Minecraft").warning("[CustomItem] Failed to load custom item " + itemId);
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} catch (NullPointerException e) {
				e.printStackTrace();
				continue;
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	public static void exportItems() {
		//for each custom item, save under id as key using ItemStack.SERIALIZE
		File destination = new File(Bukkit.getPluginManager().getPlugin("xDestMCRPG").getDataFolder().getPath() + "/custom-item/");
		if(!destination.exists()) {
			destination.mkdirs();
		}
		for(String key : CustomItem.customItems.keySet()) {
			File itemDest = new File(destination.getPath() + "/" + key + ".yml");
			FileConfiguration fg = new YamlConfiguration();
			if(!itemDest.exists()) {
				try {
					itemDest.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
			try {
				fg.set("item", customItems.get(key));
				fg.set("id", key);
				fg.save(itemDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createNewItem(String itemId, ItemStack item) {
		customItems.put(itemId, item);
	}
	
}
