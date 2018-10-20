package com.xdest.mcrpg.quest;

import org.bukkit.inventory.ItemStack;

public interface Quest {
	
	
	String getPlayerId();
	boolean questFulfilled();
	String getId();
	boolean questComplete();
	boolean playerAwarded();
	int getExp();
	ItemStack[] getRewards();
	
}
