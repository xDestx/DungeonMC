package com.xdest.mcrpg.quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.xdest.mcrpg.MCRPG;

public class KillQuest extends AbstractQuest {

	private EntityType[] types;
	private int killCountNeed, kills;
	private Listener questListener;
	
	public KillQuest(MCRPG pl, Player participant, String questId, EntityType[] killTypes, int count, int xpReward, ItemStack[] rewards, String[] customItemRewards) {
		super(pl,participant, "kill-quest-" + questId, xpReward, rewards, customItemRewards);
		killCountNeed = count;
		types = killTypes;
		kills = 0;
		
		Bukkit.getPluginManager().registerEvents(getQuestListener(), pl);
	}

	public Listener getQuestListener() {
		if(questListener == null) {
			questListener = new Listener() {
				@EventHandler
				public void killEvent(EntityDeathEvent e) {
					if(e.getEntity().getKiller() != null && e.getEntity().getKiller().getUniqueId().toString().equals(getPlayerId())) {
						for(EntityType t : types) {
							if(e.getEntityType() == t) {
								kills++;
								if(questComplete()) {
									questFulfilled();
									Bukkit.getScheduler().runTask(pl, new Runnable() {
										@Override
										public void run() {
											EntityDeathEvent.getHandlerList().unregister(questListener);
										}
									});
								}
							}
						}
					}
				}
			};
		}
		return questListener; 
	}
	
	@Override
	public boolean questComplete() {
		return kills >= killCountNeed;
	}

	public int getRequiredCount() {
		return this.killCountNeed;
	}
	
	public int getKills() {
		return this.kills;
	}

}
