package com.xdest.mcrpg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;

@SuppressWarnings("deprecation")
public class ExpListeners implements Listener {

	private MCRPG pl;
	private final Set<Material> armorMaterials;
	
	public ExpListeners(MCRPG pl) {
		this.pl = pl;
		armorMaterials = new HashSet<Material>();
		armorMaterials.add(Material.LEATHER_BOOTS);
		armorMaterials.add(Material.LEATHER_LEGGINGS);
		armorMaterials.add(Material.LEATHER_CHESTPLATE);
		armorMaterials.add(Material.LEATHER_HELMET);

		armorMaterials.add(Material.IRON_BOOTS);
		armorMaterials.add(Material.IRON_LEGGINGS);
		armorMaterials.add(Material.IRON_CHESTPLATE);
		armorMaterials.add(Material.IRON_HELMET);
		
		armorMaterials.add(Material.GOLDEN_BOOTS);
		armorMaterials.add(Material.GOLDEN_LEGGINGS);
		armorMaterials.add(Material.GOLDEN_CHESTPLATE);
		armorMaterials.add(Material.GOLDEN_HELMET);
		
		armorMaterials.add(Material.DIAMOND_BOOTS);
		armorMaterials.add(Material.DIAMOND_LEGGINGS);
		armorMaterials.add(Material.DIAMOND_CHESTPLATE);
		armorMaterials.add(Material.DIAMOND_HELMET);
		
		armorMaterials.add(Material.CHAINMAIL_BOOTS);
		armorMaterials.add(Material.CHAINMAIL_LEGGINGS);
		armorMaterials.add(Material.CHAINMAIL_CHESTPLATE);
		armorMaterials.add(Material.CHAINMAIL_HELMET);
		
		armorMaterials.add(Material.TRIDENT);
		armorMaterials.add(Material.WOODEN_SWORD);
		armorMaterials.add(Material.WOODEN_AXE);
		armorMaterials.add(Material.STONE_SWORD);
		armorMaterials.add(Material.STONE_AXE);
		armorMaterials.add(Material.IRON_SWORD);
		armorMaterials.add(Material.IRON_AXE);
		armorMaterials.add(Material.GOLDEN_SWORD);
		armorMaterials.add(Material.GOLDEN_AXE);
		armorMaterials.add(Material.DIAMOND_SWORD);
		armorMaterials.add(Material.DIAMOND_AXE);
		armorMaterials.add(Material.BOW);
		armorMaterials.add(Material.SHIELD);
	}
	
	@EventHandler
	public void sleepEvent(PlayerBedLeaveEvent e) {
		DyeColor c = ((Bed)e.getBed().getState()).getColor();
		long duration = 0l;
		SleepExpMod m;
		switch(c) {
		case BLACK:
			m = new SleepExpMod("Enhancement Bed I", 0.05f);
			duration = 25*60*20;
			break;
		case BLUE:
			m = new SleepExpMod("???? Bed I", 0.03f);
			duration = 15*60*20;
			break;
		case BROWN:
			return;
		case CYAN:
			m = new SleepExpMod("??? Bed III", 0.18f);
			duration = 30*60*20;
			break;
		case GRAY:
			m = new SleepExpMod("Enhancement Bed II", 0.1f);
			duration = 30*60*20;
			break;
		case GREEN:
			m = new SleepExpMod("???? Bed I", 0.25f);
			duration = 40*60*20;
			break;
		case LIGHT_BLUE:
			m = new SleepExpMod("???? Bed II", 0.09f);
			duration = 20*60*20;
			break;
		case LIGHT_GRAY:
			m = new SleepExpMod("Enhancement Bed III", 0.3f);
			duration = 40*60*20;
			break;
		case LIME:
			m = new SleepExpMod("???? Bed I", 0.1f);
			duration = 30*60*20;
			break;
		case MAGENTA:
			m = new SleepExpMod("???? Bed II", 0.09f);
			duration = 20*60*20;
			break;
		case ORANGE:
			m = new SleepExpMod("Attack Bed II", 0.09f);
			duration = 20*60*20;
			break;
		case PINK:
			m = new SleepExpMod("???? Bed I", 0.03f);
			duration = 15*60*20;
			break;
		case PURPLE:
			m = new SleepExpMod("??? Bed III", 0.18f);
			duration = 30*60*20;
			break;
		case RED:
			m = new SleepExpMod("Attack Bed III", 0.18f);
			duration = 30*60*20;
			break;
		case WHITE:
			m = new SleepExpMod("Enhancement Bed IV", 0.5f);
			duration = 50*60*20;
			break;
		case YELLOW:
			m = new SleepExpMod("Attack Bed I", 0.03f);
			duration = 15*60*20;
			break;
		default:
			return;
		}
		RPGEntity ent = pl.getRPGEntity(e.getPlayer().getUniqueId().toString());
		ExpMod xpm = ent.getExpMod(m.getId(), false);
		if(xpm == null || (xpm != null && xpm.getModifier() < m.getModifier())) {
			ent.addExpMod(m);
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {

				@Override
				public void run() {
					pl.getRPGEntity(e.getPlayer().getUniqueId().toString()).removeExpMod(m);
				}
				
			}, duration);
			e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Your rest rewards you with a " + ChatColor.GOLD + m.getRawMod()*100 + ChatColor.LIGHT_PURPLE + "% experience boost for " + ChatColor.GREEN + (duration/20/60) + ChatColor.LIGHT_PURPLE + " minutes!");
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		int v = 1;
		switch(e.getBlock().getType()) {
		case COAL_ORE:
			v = 2;
			break;
		case DIAMOND_ORE:
			v = 3;
			break;
		case GOLD_ORE:
			v = 3;
			break;
		case EMERALD_ORE:
			v = 3;
			break;
		case REDSTONE_ORE:
			v = 3;
			break;
		case LAPIS_ORE:
			v = 3;
			break;
		case IRON_ORE:
			v = 3;
			break;
		default:
			break;
		}
		pl.getRPGEntity(e.getPlayer().getUniqueId().toString()).awardExp(v);
	}
	
	@EventHandler
	public void onMobKill(EntityDeathEvent e) {
		if(e.getEntity().getKiller() != null) {
			String playerId = e.getEntity().getKiller().getUniqueId().toString();
			int award;
			switch(e.getEntity().getType()) {
			case ENDER_DRAGON:
				award = 500;
				break;
			case WITHER: 
				award = 750;
			default:
				award = 4;
			}
			List<MetadataValue> x = e.getEntity().getMetadata("add-exp");
			if(x != null) {
				for(MetadataValue k : x) {
					award+=k.asInt();
				}
			}
			pl.getRPGEntity(playerId).awardExp(award);
		}
	}
	
	
	@EventHandler
	public void onArmorEquipHand(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() != null && armorMaterials.contains(e.getItem().getType())) {
				updatePlayerArmorExp(e.getPlayer());
			}
		}
	}
	
	
	@EventHandler
	public void armorClick(InventoryClickEvent e) {
		if(e.getSlotType() == SlotType.ARMOR || (e.getCurrentItem() != null && armorMaterials.contains(e.getCurrentItem().getType()))) {
			if(e.getWhoClicked() instanceof Player) {
				Bukkit.getScheduler().runTask(pl, new Runnable() { @Override public void run() {updatePlayerArmorExp((Player)e.getWhoClicked());}});
			}
		}
	}
	
	
	@EventHandler
	public void armorBreak(PlayerItemBreakEvent e) {
		updatePlayerArmorExp(e.getPlayer());
	}
	
	private void updatePlayerArmorExp(Player p) {
		//check
		PlayerInventory inv = p.getInventory();
		ItemStack[] armorInv = new ItemStack[] {inv.getHelmet(),inv.getChestplate(),inv.getLeggings(),inv.getBoots(),inv.getItemInMainHand(),inv.getItemInOffHand()};
		RPGEntity ent = pl.getRPGEntity(p.getUniqueId().toString());
		float totalMod = 0;
		for(ItemStack i : armorInv) {
			if(i == null) continue;
			ItemMeta meta = i.getItemMeta();
			if(meta == null) continue;
			List<String> lore = meta.getLore();
			if(lore == null) continue;
			for(String s : lore) {
				if(s == null) continue;
				if(s.startsWith("! ")) {
					s = s.substring(2);
					String xpboost = s.substring(0, s.indexOf("%"));
					try {
						int toAdd = Integer.parseInt(xpboost.trim());
						totalMod+=((float)toAdd)/100f;
					} catch (NumberFormatException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		ent.getArmorExpMod().setModifier(totalMod);
	}
	
}
