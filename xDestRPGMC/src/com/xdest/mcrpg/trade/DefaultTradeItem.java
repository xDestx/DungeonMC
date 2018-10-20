package com.xdest.mcrpg.trade;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DefaultTradeItem extends AbstractTradeItem {

	private final Material m;
	private final int stackSize;
	
	public DefaultTradeItem(int cost, int levelReq, int buyPrice, int stock, Material itemMaterial, int stackSize) {
		super(cost, levelReq, buyPrice, stock);
		this.m = itemMaterial;
		this.stackSize = stackSize;
	}

	public String exportString() {
		return "cti_vanilla:" + this.cost + "," + this.lvl + "," + this.buyPrice + "," + this.restingStock + "," + this.m + "," + this.stackSize;
	}
	
	@Override
	public ItemStack getItem() {
		return new ItemStack(m,stackSize);
	}
	
	public static DefaultTradeItem fromString(String s) {
		//cost,level,buyPrice,stock,material,stackSize
		String[] vals = s.split(",");
		try {
			int c = Integer.parseInt(vals[0]);
			int l = Integer.parseInt(vals[1]);
			int b = Integer.parseInt(vals[2]);
			int st = Integer.parseInt(vals[3]);
			String material = vals[4];
			int count = Integer.parseInt(vals[5]);
			return new DefaultTradeItem(c,l,b,st,Material.getMaterial(material),count);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

}
