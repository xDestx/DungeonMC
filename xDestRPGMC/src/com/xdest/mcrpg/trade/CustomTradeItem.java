package com.xdest.mcrpg.trade;

import org.bukkit.inventory.ItemStack;

public class CustomTradeItem extends AbstractTradeItem {
	
	private final String customItemId;
	
	/**
	 * 
	 * @param cost
	 * @param levelReq
	 * @param buyPrice
	 * @param stock -1 for infinite
	 */
	public CustomTradeItem(int cost, int levelReq, int buyPrice, int stock, String customItemId) {
		super(cost,levelReq,buyPrice,stock);
		this.customItemId = customItemId;
	}
	
	public String getItemId() {
		return this.customItemId;
	}
	
	public ItemStack getItem() {
		return CustomItem.getCustomItemById(customItemId);
	}
	
	@Override
	public String exportString() {
		return "cti_custom:" + this.cost + "," + this.lvl + "," + this.buyPrice + "," + this.restingStock + "," + this.customItemId;
	}
	
	
	public static CustomTradeItem fromString(String s) {
		//cost,level,buyPrice,stock,id
		String[] vals = s.split(",");
		try {
			int c = Integer.parseInt(vals[0]);
			int l = Integer.parseInt(vals[1]);
			int b = Integer.parseInt(vals[2]);
			int st = Integer.parseInt(vals[3]);
			String id = vals[4];
			return new CustomTradeItem(c,l,b,st,id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static TradeItem fromStringSwap(String s) {
		String[] vals = s.split(",");
		try {
			int c = Integer.parseInt(vals[0]);
			int l = Integer.parseInt(vals[1]);
			int b = Integer.parseInt(vals[2]);
			int st = Integer.parseInt(vals[3]);
			String id = vals[4];
			return new CustomTradeItem(c,l,b,st,id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
