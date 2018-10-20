package com.xdest.mcrpg.trade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.xdest.mcrpg.RPGEntity;

public class TradeProfile {
	
	private Set<TradeItem> items;
	private final String id, name;
	
	public TradeProfile(String id, String name) {
		this.id = id;
		items = new HashSet<TradeItem>();
		this.name = name;
	}
	
	public TradeProfile(String id, String name, Set<TradeItem> items) {
		this.id = id;
		this.items = items;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Set<TradeItem> getTradeItems() {
		return this.items;
	}
	
	public void addTradeItem(TradeItem i) {
		this.items.add(i);
	}
	
	public void showToPlayer(Player player, RPGEntity pl) {
		final List<MerchantRecipe> k = new LinkedList<MerchantRecipe>();
		for(TradeItem i : items) {
			int stock = (i.getStock() == -1 ? 999999:i.getStock());
			ItemStack[] recipe = i.getRecipe();
			MerchantRecipe r;
			if(recipe.length == 2) 
				r = new MerchantRecipe(recipe[1],stock);
			else
				r = new MerchantRecipe(recipe[2],stock);
			r.addIngredient(recipe[0]);
			if(i.getLevelRequirement() > pl.getLevel()) {
				ItemStack nothx = new ItemStack(Material.BARRIER, 1);
				ItemMeta im = nothx.getItemMeta();
				im.setDisplayName(ChatColor.RED + "Required Level: " + i.getLevelRequirement());
				nothx.setItemMeta(im);
				r.addIngredient(nothx);
			} else {
				if(recipe.length == 3) {
					r.addIngredient(recipe[1]);
				}
			}
			k.add(r);
		}
		Merchant m = Bukkit.createMerchant(getName());
		m.setRecipes(k);
		player.openMerchant(m, true);
	}
	
	
	public List<String> exportProfile() {
		List<String> exportList = new LinkedList<String>();
		exportList.add(id);
		exportList.add(name);
		for(TradeItem i : items) {
			exportList.add(i.exportString());
		}
		return exportList;
	}
	
	public static ItemStack[] getTradeValueAsItems(int cost) {
		int totalPris = 0; //128q
		int totalQuartz = 0; //128s
		int totalShells = 0;
		totalPris = (cost/(16384));
		cost-=totalPris*16384;
		totalQuartz=cost/128;
		cost-=totalQuartz*128;
		totalShells = cost;
		if(totalPris > 64 && totalQuartz > 0) {
			totalPris++;
			if(totalPris > 128) {
				totalPris = 128;
			}
			totalQuartz = 0;
		} else if (totalPris < 64 && totalQuartz > 0 && totalShells > 0) {
			totalQuartz++;
			if(totalQuartz > 64) {
				totalPris++;
				totalQuartz=0;
			}
			totalShells=0;
		}
		if(totalQuartz > 64 && totalShells > 0) {
			totalQuartz++;
			if(totalQuartz > 128) {
				totalQuartz-=128;
				totalPris++;
			}
			totalShells = 0;
		}
		if(totalPris > 0 && totalQuartz > 0) {
			return new ItemStack[] {new ItemStack(Material.PRISMARINE_SHARD, totalPris),new ItemStack(Material.QUARTZ, totalQuartz)};
		} else if (totalQuartz > 0 && totalShells > 0) {
			return new ItemStack[] {new ItemStack(Material.QUARTZ, totalQuartz),new ItemStack(Material.NAUTILUS_SHELL, totalShells)};
		} else if (totalPris > 0 && totalShells > 0) {
			return new ItemStack[] {new ItemStack(Material.PRISMARINE_SHARD, totalPris),new ItemStack(Material.NAUTILUS_SHELL, totalShells)};
		} else if (totalPris > 0 && totalPris <= 64 ) {
			return new ItemStack[] {new ItemStack(Material.PRISMARINE_SHARD, totalPris)};
		} else if (totalPris > 64) {
				return new ItemStack[] {new ItemStack(Material.PRISMARINE_SHARD, 64),new ItemStack(Material.PRISMARINE_SHARD, totalPris-64)};
		} else if (totalQuartz > 0 && totalQuartz <= 64 ) {
			return new ItemStack[] {new ItemStack(Material.QUARTZ, totalQuartz)};
		} else if (totalQuartz > 64) {
				return new ItemStack[] {new ItemStack(Material.QUARTZ, 64),new ItemStack(Material.QUARTZ, totalQuartz-64)};
		} else if (totalShells > 0 && totalShells <= 64 ) {
			return new ItemStack[] {new ItemStack(Material.NAUTILUS_SHELL, totalShells)};
		} else if (totalShells > 64) {
				return new ItemStack[] {new ItemStack(Material.NAUTILUS_SHELL, 64),new ItemStack(Material.NAUTILUS_SHELL, totalShells-64)};
		} 
		return null;
	}
	
	private static Map<String,TradeProfile> customProfiles = null;
	
	public static TradeProfile fromList(List<String> items) {
		String profileId = items.get(0);
		String profileName = items.get(1);
		Set<TradeItem> tis = new HashSet<TradeItem>();
		for(int i = 2; i < items.size(); i++) {
			TradeItem ti = TradeItem.fromString(items.get(i));
			if(ti != null) {
				tis.add(ti);
			} else {
				Logger.getLogger("Minecraft").severe("Failed to add item " + items.get(i));
			}
		}
		return new TradeProfile(profileId,profileName,tis);
	}
	
	public static void loadTradeProfiles() {
		TradeProfile.customProfiles = new HashMap<String,TradeProfile>();
		
		File source = new File(Bukkit.getPluginManager().getPlugin("xDestMCRPG").getDataFolder().getPath() + "/trade-profiles/");
		if(!source.exists()) {
			source.mkdirs();
			return;
		}
		File[] profiles = source.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".tp");
			}
			
		});
		for(File f : profiles) {
			try {
				Scanner sc = new Scanner(new FileInputStream(f));
				List<String> pf = new LinkedList<String>();
				while(sc.hasNextLine()) {
					pf.add(sc.nextLine());
				}
				TradeProfile tp = TradeProfile.fromList(pf);
				if(tp == null) {
					System.out.println("Failed to load profile??? " + pf.get(0));
				}
				TradeProfile.customProfiles.put(tp.getId(),tp);
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	

	public static TradeProfile getTradeProfile(String id) {
		return customProfiles.get(id);
	}
	
	public static void registerTradeProfile(TradeProfile tp) {
		customProfiles.put(tp.getId(), tp);
	}
	
	public static void exportTradeProfiles() {
		//for each custom item, save under id as key using ItemStack.SERIALIZE
		File destination = new File(Bukkit.getPluginManager().getPlugin("xDestMCRPG").getDataFolder().getPath() + "/trade-profiles/");
		if(!destination.exists()) {
			destination.mkdirs();
		}
		for(String key : TradeProfile.customProfiles.keySet()) {
			File profileDest = new File(destination.getPath() + "/" + key + ".tp");
			if(!profileDest.exists()) {
				try {
					profileDest.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
			try {
				FileWriter fos = new FileWriter(profileDest);
				List<String> export = TradeProfile.customProfiles.get(key).exportProfile();
				for(String s : export) {
					try {
						fos.write(s + "\n");
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
