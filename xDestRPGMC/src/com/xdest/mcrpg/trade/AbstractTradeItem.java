package com.xdest.mcrpg.trade;

import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractTradeItem implements TradeItem {

	protected final int cost, lvl, buyPrice, restingStock;
	
	/**
	 * 
	 * @param cost
	 * @param levelReq
	 * @param buyPrice
	 * @param stock -1 for infinite
	 */
	public AbstractTradeItem(int cost, int levelReq, int buyPrice, int stock) {
		this.cost = cost;
		this.lvl = levelReq;
		this.buyPrice = buyPrice;
		this.restingStock = stock;
	}
	
	
	public int getLevelRequirement() {
		return this.lvl;
	}
	
	public int getCostRequirement() {
		return this.cost;
	}
	
	public int getBuyPrice() {
		return this.buyPrice;
	}
	
	public int getStock() {
		return this.restingStock;
	}

	public ItemStack[] getRecipe() {
		if(getCostRequirement() != 0) {
			ItemStack[] cost = TradeProfile.getTradeValueAsItems(getCostRequirement());
			if(cost == null || (cost != null && cost.length == 0)) {
				Logger.getLogger("Minecraft").severe("[AbstractTradeItem] Invalid cost? " + this.cost + " for item " + getItem().getItemMeta().getDisplayName());
				return null;
			}
			if(cost.length == 1)
				return new ItemStack[] {cost[0],getItem()};
			else
				return new ItemStack[] {cost[0],cost[1],getItem()};
		} else {
			ItemStack[] cost = TradeProfile.getTradeValueAsItems(getBuyPrice());
			if(cost == null || (cost != null && cost.length == 0)) {
				Logger.getLogger("Minecraft").severe("[AbstractTradeItem] Invalid buy price? " + this.getBuyPrice() + " for item " + getItem().getItemMeta().getDisplayName());
				return null;
			}
			return new ItemStack[] {getItem(),cost[0]};
		}
	}
	
}
