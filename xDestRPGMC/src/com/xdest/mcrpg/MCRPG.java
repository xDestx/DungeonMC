package com.xdest.mcrpg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.xdest.mcrpg.trade.CustomItem;
import com.xdest.mcrpg.trade.CustomTradeItem;
import com.xdest.mcrpg.trade.DefaultTradeItem;
import com.xdest.mcrpg.trade.DefaultTradeListener;
import com.xdest.mcrpg.trade.TradeProfile;
import com.xdestcb.vault.VaultUser;

public class MCRPG extends JavaPlugin {

	private Map<String,RPGEntity> userSet;
	private Map<String,Map<String,String>> merchantEntityMap;
	
	@Override
	public void onEnable() {
		userSet = new HashMap<String,RPGEntity>();
		merchantEntityMap = new HashMap<String,Map<String,String>>();
		Bukkit.getPluginManager().registerEvents(new ExpListeners(this), this);
		Bukkit.getPluginManager().registerEvents(new DefaultTradeListener(this),this);
		for(Player p : Bukkit.getOnlinePlayers()) {
			userSet.put(p.getUniqueId().toString(), new RPGEntity(p.getUniqueId().toString()));
		}
		CustomItem.loadItems();
		TradeProfile.loadTradeProfiles();
		loadRPGEntities();
		loadMerchantEntities();
	}

	
	@Override
	public void onDisable() {
		CustomItem.exportItems();
		TradeProfile.exportTradeProfiles();
		saveMerchantEntities();
		saveRPGEntities();
		userSet = null;
		merchantEntityMap = null;
	}
	
	public void addMerchantEntity(String uuid, Map<String,String> meta) {
		this.merchantEntityMap.put(uuid, meta);
	}
	
	public void checkMerchantEntity(Entity e) {
		Map<String,String> data = merchantEntityMap.get(e.getUniqueId().toString());
		if(data != null) {
			for(String s : data.keySet()) {
				e.setMetadata(s, new FixedMetadataValue(this,data.get(s)));
			}
		}
	}
	
	public void loadMerchantEntities() {
		File f = new File(this.getDataFolder() + "/entities/merchants/");
		if(!f.exists()) {
			f.mkdirs();
		}
		try {
			File[] files = f.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.endsWith(".txt");
				}
				
			});
			merchantEntityMap = new HashMap<String,Map<String,String>>(); 
			for(File file : files) {
				Scanner s = new Scanner(new FileInputStream(file));
				Map<String,String> metaData = new HashMap<String,String>();
				while(s.hasNextLine()) {
					String line = s.nextLine();
					String key = line.split(":")[0];
					metaData.put(key, line.substring(key.length()+1));
					Logger.getLogger("Minecraft").info("[MerchantLoader] key: " + key + " --- " + line.substring(key.length()+1));
				}
				s.close();
				merchantEntityMap.put(file.getName().replace(".txt", ""), metaData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		World w = Bukkit.getWorlds().get(0);
		List<Entity> ent = w.getEntities();
		for(Entity entity : ent) {
			String id = entity.getUniqueId().toString();
			Map<String,String> meta = merchantEntityMap.get(id);
			if(meta != null) {
				Logger.getLogger("Minecraft").info("[MerchantLoader] Loading " + id);
				for(String str : meta.keySet()) {
					entity.setMetadata(str, new FixedMetadataValue(this, meta.get(str)));
					Logger.getLogger("Minecraft").info("[MerchantLoader] --- " + str + ":" + meta.get(str));
				}
			}
		}
		
		
	}
	
	public void saveMerchantEntities() {
		File source = new File(this.getDataFolder() + "/entities/merchants/");
		if(!source.exists()) {
			source.mkdirs();
		}
		try {
			for(String u : merchantEntityMap.keySet()) {
				File f = new File(source.getPath() + "/" + u + ".txt");
				FileWriter fw = new FileWriter(f);
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Map<String,String> metaTags = merchantEntityMap.get(u);
				for(String s : metaTags.keySet()) {
					fw.write(s + ":" + metaTags.get(s) + "\n");
				}
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//Misleading name
	protected void loadRPGEntities() {
		File f = new File(this.getDataFolder() + "/entities/exp/");
		if(!f.exists()) {
			f.mkdirs();
		}
		File[] files = f.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".txt");
			}
			
		});
		userSet = new HashMap<String, RPGEntity>(); 
		for(File file : files) {
			try {
				Scanner s = new Scanner(new FileInputStream(file));
				int xpVal;
				if(s.hasNextLine()) {
					String xp = s.nextLine();
					xpVal = Integer.parseInt(xp);
				} else {
					continue;
				}
				RPGEntity ent = new RPGEntity(file.getName().replace(".txt", ""));
				ent.setExp(xpVal);
				userSet.put(ent.getId(), ent);
				s.close();
			} catch (IOException | NumberFormatException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	protected void saveRPGEntities() {
		File source = new File(this.getDataFolder() + "/entities/exp/");
		if(!source.exists()) {
			source.mkdirs();
		}
		for(String id : userSet.keySet()) {
			try {
				File f = new File(source.getPath() + "/" + id + ".txt");
				FileWriter fw = new FileWriter(f);
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				fw.write(""+(int)userSet.get(id).getExp());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
	public void loadRPGEntity(String id) {
		//TODO
	}
	
	
	/**
	 * Get the RPG user with id. Players id is their uuid
	 * @param uuid
	 */
	public RPGEntity getRPGEntity(String uuid) {
		RPGEntity e = userSet.get(uuid);
		if(e != null) return e;
		e = new RPGEntity(uuid);
		userSet.put(uuid, e);
		return e;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (!(sender instanceof Player)) {
			return false;
		}
		if(label.equalsIgnoreCase("getlevel")) {
			int level = getRPGEntity((((Player)sender).getUniqueId().toString())).getLevel();
			sender.sendMessage(ChatColor.GOLD + "Player is level " + level);
			return true;
		}
		if(label.equalsIgnoreCase("setrpgexp")) {
			try {
				int xp = Integer.parseInt(args[0]);
				getRPGEntity((((Player)sender).getUniqueId().toString())).setExp(xp);
				sender.sendMessage(ChatColor.GOLD + "Player xp is " + xp);
				return true;
			} catch (Exception e) {
				//oof, not an int
				return false;
			}
		}
		if(label.equalsIgnoreCase("getrpgexp")) {
			int x = (int)getRPGEntity((((Player)sender).getUniqueId().toString())).getExp();
			sender.sendMessage(ChatColor.GOLD + "Player xp is " + x);
			return true;
		}
		if(label.equalsIgnoreCase("balance")) {
			int bal = (int)VaultUser.getVaultUser(((Player)sender).getUniqueId().toString()).getBalance();
			sender.sendMessage(ChatColor.GOLD + "You have " + ChatColor.GREEN + NumberFormat.getNumberInstance().format(bal) + ChatColor.GOLD + " shells");
			return true;
		}
		if(label.equalsIgnoreCase("setbal")) {
			try {
			int bal = Integer.parseInt(args[0]);
			VaultUser.getVaultUser(((Player)sender).getUniqueId().toString()).setValue(bal);
			sender.sendMessage(ChatColor.GOLD + "You have " + ChatColor.GREEN + NumberFormat.getNumberInstance().format(bal) + ChatColor.GOLD + " shells");
			return true;
			} catch (Exception e) {
				
			}
			return false;
		}
		if(label.equalsIgnoreCase("customitem")) {
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("add")) {
					String newItemId = args[1];
					ItemStack newItem = ((Player)sender).getInventory().getItemInMainHand();
					CustomItem.createNewItem(newItemId, newItem);
					sender.sendMessage(ChatColor.GREEN + "Created new item with id " + ChatColor.RED + newItemId);
					return true;
				} else if (args[0].equalsIgnoreCase("summon")) {
					ItemStack ci = CustomItem.getCustomItemById(args[1]);
					if(ci == null) {
						sender.sendMessage("This item doesn't exist!");
					} else {
						((Player)sender).getInventory().addItem(ci);
						sender.sendMessage(ChatColor.GREEN + "Summoned " + ChatColor.DARK_RED + args[1]);
					}
					return true;
				} else {
					return false;
				}
			} else if (args.length == 1) {
				if(args[0].equals("export")) {
					CustomItem.exportItems();
					sender.sendMessage(ChatColor.GREEN + "Items exported!");
					return true;
				} else if (args[0].equals("reload")) {
					CustomItem.loadItems();
					sender.sendMessage(ChatColor.GREEN + "Items reloaded!");
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		if(label.equalsIgnoreCase("tradeprofile")) {
			if(args[0].equalsIgnoreCase("create") && args.length >= 3) {
				String name = "";
				for(int i = 2; i < args.length; i++) {
					name+=args[i];
				}
				TradeProfile tp = new TradeProfile(args[1], name);
				TradeProfile.registerTradeProfile(tp);
				sender.sendMessage("Created profile " + tp.getName());
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("export")) {
					TradeProfile.exportTradeProfiles();
					sender.sendMessage(ChatColor.GREEN + "Exported trade profiles!");
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					TradeProfile.loadTradeProfiles();
					sender.sendMessage(ChatColor.GREEN + "Reloaded trade profiles!");
					return true;
				}
			} else if (args.length == 3) {
				if(args[0].equalsIgnoreCase("summon")) {
					TradeProfile target = TradeProfile.getTradeProfile(args[1]);
					if(target == null) {
						sender.sendMessage("Can't find trade profile!");
						return true;
					}
					String mobType = args[2];
					Player p = ((Player)sender);
					Entity e = p.getWorld().spawnEntity(p.getLocation(), EntityType.valueOf(mobType));
					e.setMetadata("trade-profile", new FixedMetadataValue(this, args[1]));
					e.setMetadata("cannot-target", new FixedMetadataValue(this, true));
					e.setCustomName(target.getName());
					e.setCustomNameVisible(true);
					e.setInvulnerable(true);
					((LivingEntity)e).setRemoveWhenFarAway(false);
					sender.sendMessage("Summoned " + mobType + " with trade profile " + args[1]);
					Map<String,String> v = new HashMap<String,String>();
					v.put("trade-profile", args[1]);
					v.put("cannot-target", "true");
					this.addMerchantEntity(e.getUniqueId().toString(), v);
					return true;
				}
			} else if (args.length == 7) {
				if(args[0].equalsIgnoreCase("add-v")) {
					try {
						TradeProfile target = TradeProfile.getTradeProfile(args[1]);
						if (target == null) {
							sender.sendMessage(ChatColor.RED + "This profile doesn't exist. Create it with /tradeprofile create <id> <name>");
							return true;
						}
						ItemStack newTradeItem = ((Player)sender).getInventory().getItemInMainHand();
						if(!(newTradeItem == null || (newTradeItem != null && newTradeItem.getType() == Material.AIR))) {
							//sender.sendMessage(ChatColor.RED + "You must hold the item in your hand");
							int cost = Integer.parseInt(args[2]);
							int levelReq = Integer.parseInt(args[3]);
							int buyBack = Integer.parseInt(args[4]);
							int stock = Integer.parseInt(args[5]);
							int stackCount = Integer.parseInt(args[6]);
							target.addTradeItem(new DefaultTradeItem(cost, levelReq, buyBack, stock, newTradeItem.getType(), stackCount));
							sender.sendMessage("Added!");
							return true;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
						sender.sendMessage(ChatColor.DARK_RED + "Invalid format. /tradeprofile add <tradeid> <cost> <level> <buy> <stock> [id?]");
						return true;
					}
				} else if(args[0].equalsIgnoreCase("add-c")) {
					try {
						TradeProfile target = TradeProfile.getTradeProfile(args[1]);
						if (target == null) {
							sender.sendMessage(ChatColor.RED + "This profile doesn't exist. Create it with /tradeprofile create <id> <name>");
							return true;
						}
						ItemStack ci = CustomItem.getCustomItemById(args[6]);
						if(ci != null) {
							//sender.sendMessage(ChatColor.RED + "You must hold the item in your hand");
							int cost = Integer.parseInt(args[2]);
							int levelReq = Integer.parseInt(args[3]);
							int buyBack = Integer.parseInt(args[4]);
							int stock = Integer.parseInt(args[5]);
							String customItemId = args[6];
							target.addTradeItem(new CustomTradeItem(cost, levelReq, buyBack, stock, customItemId));
							sender.sendMessage("Added!");
							return true;
						} else {
							sender.sendMessage("Couldn't find custom item " + args[6] +"!");
							return true;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
						sender.sendMessage(ChatColor.DARK_RED + "Invalid format. /tradeprofile add <tradeid> <cost> <level> <buy> <stock> [id?]");
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
}
