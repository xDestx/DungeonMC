package com.xdest.mcrpg.quest;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.xdest.mcrpg.MCRPG;
import com.xdest.mcrpg.trade.CustomItem;

public abstract class AbstractQuest implements Quest {

	private final String questHolder,questId;
	private ItemStack[] rewards;
	private String[] customItemRewards;
	private int exp;
	private boolean awarded;
	protected MCRPG pl;
	
	public AbstractQuest(MCRPG pl, Player participant, String questId, int xpReward, ItemStack[] rewards, String[] customItemRewards) {
		this.questHolder = participant.getUniqueId().toString();
		this.questId = questId;
		this.exp = xpReward;
		this.rewards = rewards;
		this.customItemRewards = customItemRewards;
		this.awarded = false;
		this.pl = pl;
		if(rewards == null) {
			rewards = new ItemStack[0];
		}
		if(customItemRewards == null) {
			customItemRewards = new String[0];
		}
	}
	
	public int getExp() {
		return this.exp;
	}
	
	public boolean questFulfilled() {
		if(awarded) return false;
		for(ItemStack i : getRewards()) {
			if(i != null)
				Logger.getLogger("Minecraft").info(i.toString());
			else
				Logger.getLogger("Minecraft").info("item null");
		}
		Bukkit.getPlayer(UUID.fromString(questHolder)).getInventory().addItem(getRewards());
		pl.getRPGEntity(questHolder).awardExp(exp);
		awarded = true;
		return true;
	}
	
	public ItemStack[] getRewards() { 
		ItemStack[] rewardFin = new ItemStack[rewards.length + customItemRewards.length];
		int i = 0;
		for(i = 0; i < rewards.length; i++) {
			rewardFin[i] = rewards[i];
		}
		for (int k = 0; k < customItemRewards.length; k++) {
			ItemStack x = CustomItem.getCustomItemById(customItemRewards[k]);
			rewardFin[i+k] = x;
		}
		return rewardFin;
	}
	
	public String getId() {
		return this.questId;
	}
	
	public String getPlayerId() {
		return this.questHolder;
	}
	
	public boolean playerAwarded() {
		return awarded;
	}
	
}
