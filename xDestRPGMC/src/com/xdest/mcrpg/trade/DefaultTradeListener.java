package com.xdest.mcrpg.trade;

import java.text.NumberFormat;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import com.xdest.mcrpg.MCRPG;
import com.xdestcb.vault.VaultUser;

public class DefaultTradeListener implements Listener {

	private MCRPG pl;
	
	public DefaultTradeListener(MCRPG pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void shopKeeperTargetEvent(EntityTargetEvent e) {
		List<MetadataValue> vals = e.getEntity().getMetadata("cannot-target");
		if(vals != null && vals.size() != 0) {
			if(vals.get(0).asBoolean()) {
				//stop
				e.setTarget(null);
			}
		}
	}
	
	@EventHandler
	public void newChunkNewEntityNewMerchant(ChunkLoadEvent e) {
		Entity[] ent = e.getChunk().getEntities();
		for(Entity entity : ent) {
			pl.checkMerchantEntity(entity);
		}
	}
	
	@EventHandler
	public void interactShopEvent(PlayerInteractEntityEvent e) {
		List<MetadataValue> profileId = e.getRightClicked().getMetadata("trade-profile");
		if(profileId != null && !profileId.isEmpty()) {
			String id = profileId.get(0).asString();
			TradeProfile tp = TradeProfile.getTradeProfile(id);
			if(tp == null) return;
			e.setCancelled(true);
			tp.showToPlayer(e.getPlayer(), pl.getRPGEntity(e.getPlayer().getUniqueId().toString()));
		}
	}
	
	@EventHandler
	public void interactAtmEvent(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.WALL_SIGN) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Sign s = ((Sign)e.getClickedBlock().getState());
			String[] lines = s.getLines();
			if(lines[0].equalsIgnoreCase("[Withdraw]")) {
				if(e.getPlayer().getInventory().firstEmpty() == -1) {
					e.getPlayer().sendMessage(ChatColor.RED + "You need space in your inventory for this.");
					return;
				}
				String type = lines[1];
				int count = Integer.parseInt(lines[2]);
				int value;
				VaultUser u = VaultUser.getVaultUser(e.getPlayer().getUniqueId().toString());
				switch(type) { 
				case "Shells":
					value = count;
					if(u.getBalance() >= value) {
						u.removeCurrency(value);
						ItemStack is = new ItemStack(Material.NAUTILUS_SHELL, count);
						e.getPlayer().getInventory().addItem(is);
					} else {
						int countToDraw = (int) u.getBalance();
						u.removeCurrency(countToDraw);
						ItemStack is = new ItemStack(Material.NAUTILUS_SHELL, countToDraw);
						e.getPlayer().getInventory().addItem(is);
					}
					break;
				case "Quartz":
					value = count * 128;
					if(u.getBalance() >= value) {
						u.removeCurrency(value);
						ItemStack is = new ItemStack(Material.QUARTZ, count);
						e.getPlayer().getInventory().addItem(is);
					} else {
						int countToDraw = (int) u.getBalance()/128;
						if(countToDraw == 0) {
							e.getPlayer().sendMessage(ChatColor.RED + "You don't have this much");
							return;
						}
						u.removeCurrency(countToDraw*128);
						ItemStack is = new ItemStack(Material.QUARTZ, countToDraw);
						e.getPlayer().getInventory().addItem(is);
					}
					break;
				case "Prismarine":
					value = count * 128 * 128;
					if(u.getBalance() >= value) {
						u.removeCurrency(value);
						ItemStack is = new ItemStack(Material.PRISMARINE_SHARD, count);
						e.getPlayer().getInventory().addItem(is);
					} else {
						int countToDraw = (int) u.getBalance()/(128*128);
						if(countToDraw == 0) {
							e.getPlayer().sendMessage(ChatColor.RED + "You don't have this much");
							return;
						}
						u.removeCurrency(countToDraw*128*128);
						ItemStack is = new ItemStack(Material.PRISMARINE_SHARD, countToDraw);
						e.getPlayer().getInventory().addItem(is);
					}
					break;
				}
				e.getPlayer().sendMessage(ChatColor.GOLD + "You have " + ChatColor.GREEN + NumberFormat.getNumberInstance().format(u.getBalance()) + ChatColor.GOLD + " shells");
			} else if(lines[0].equalsIgnoreCase("[Deposit]")) {
				String type = lines[1];
				VaultUser u = VaultUser.getVaultUser(e.getPlayer().getUniqueId().toString());
				ItemStack handItem = e.getPlayer().getInventory().getItemInMainHand();
				int count = handItem.getAmount();
				switch(type) { 
				case "Shells":
					if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAUTILUS_SHELL) {
						u.addCurrency((int)count);
						handItem.setAmount(0);
						break;
					} else {
						e.getPlayer().sendMessage(ChatColor.RED + "Hold your deposit in your hand");
						break;
					}
				case "Quartz":
					if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.QUARTZ) {
						u.addCurrency((int)count*128);
						handItem.setAmount(0);
						break;
					} else {
						e.getPlayer().sendMessage(ChatColor.RED + "Hold your deposit in your hand");
						break;
					}
				case "Prismarine":
					if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PRISMARINE_SHARD) {
						u.addCurrency((int)count*128*128);
						handItem.setAmount(0);
						break;
					} else {
						e.getPlayer().sendMessage(ChatColor.RED + "Hold your deposit in your hand");
						break;
					}
				}
				e.getPlayer().sendMessage(ChatColor.GOLD + "You have " + ChatColor.GREEN + NumberFormat.getNumberInstance().format(u.getBalance()) + ChatColor.GOLD + " shells");
			}
		}
	}
	
}
