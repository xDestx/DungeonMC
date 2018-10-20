package com.xdest.mcrpg.trade;

import org.bukkit.inventory.ItemStack;

public interface TradeItem {

	
	public int getLevelRequirement();
	
	public int getCostRequirement();
	
	public int getBuyPrice();
	
	public int getStock();
	
	public ItemStack getItem();
	
	public String exportString();
	
	public ItemStack[] getRecipe();
	
	public static TradeItem fromString(String s) {
		if(!s.startsWith("cti_")) {
			return null;
		}
		s = s.substring(4);
		String itemType = s.split(":")[0];
		switch(itemType.toLowerCase()) {
		case "custom":
			String itemValue = s.substring(7);
			return CustomTradeItem.fromString(itemValue);
		case "vanilla":
			String itemValue1 = s.substring(8);
			return DefaultTradeItem.fromString(itemValue1);
		default:
			return null;
		}
	}
}
