package com.xdest.mcrpg;

import java.util.LinkedList;
import java.util.List;

public class RPGEntity {

	private float xp;
	private List<ExpMod> mods;
	private ArmorExpMod armorMod;
	private String id;
	
	public RPGEntity(String id) {
		this.id = id;
		mods = new LinkedList<ExpMod>();
		armorMod = new ArmorExpMod();
		mods.add(armorMod);
		xp = 0;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void awardExp(float xp) {
		this.xp+=(xp*getFinalMod());
	}
	
	public void awardExp(float xp, boolean ignoreMods) {
		if(ignoreMods) {
			this.xp+=xp;
		} else {
			awardExp(xp);
		}
	}
	
	public float getExp() {
		return this.xp;
	}
	
	public ArmorExpMod getArmorExpMod() {
		return this.armorMod;
	}
	
	/**
	 * Please don't use this unless needed
	 * @param xp
	 */
	public void setExp(float xp) {
		this.xp = xp;
	}
	
	
	public int getLevel() {
		//xp for lvl 1 = 100
		//first 10 lvl is 1.5x per level
		int base = 100;
		int clvl = 0;
		int cxp = 0;
		while (cxp <= (int)xp) {
			double reduce = 0.23*(clvl/50.0);
			if(reduce > 0.23) {
				reduce = 0.23;
			}
			double multival = 1.3-reduce;
			cxp+=base*Math.pow(multival,clvl);
			clvl++;
			if(cxp >= (int)xp) {
				return clvl;
			}
		}
		return 0;
	}
	
	public boolean addExpMod(ExpMod e) {
		if(e.canStack()) {
			this.mods.add(e);
			return true;
		} else {
			for(ExpMod m : mods) {
				if(m.getId().equalsIgnoreCase(e.getId())) {
					return false;
				}
			}
			this.mods.add(e);
			return true;
		}
	}
	
	
	public ExpMod removeExpMod(ExpMod e) {
		if(this.mods.remove(e)) {
			return e;
		}
		return null;
	}
	/**
	 * Remove Exp Mod by name or Id
	 * @param id
	 * @param isName Is it a name? otherwise id
	 * @return
	 */
	public ExpMod removeExpMod(String id, boolean isName) {
		if(isName) {
			for(ExpMod m : mods) {
				if(m.getName().equalsIgnoreCase(id)) {
					mods.remove(m);
					return m;
				}
			}
		} else {
			for(ExpMod m : mods) {
				if(m.getId().equalsIgnoreCase(id)) {
					mods.remove(m);
					return m;
				}
			}
		}
		return null;
	}
	
	public ExpMod getExpMod(String id, boolean isName) {
		if(isName) {
			for(int i = 0; i < mods.size(); i++) {
				ExpMod m = mods.get(i);
				if(m.getName().equalsIgnoreCase(id)) {
					return mods.get(i);
				}
			}
		} else {
			for(int i = 0; i < mods.size(); i++) {
				ExpMod m = mods.get(i);
				if(m.getId().equalsIgnoreCase(id)) {
					return mods.get(i);
				}
			}
		}
		return null;
	}
	
	public float getFinalMod() {
		float initial = 1f;
		float additiveTotal = 0f;
		float multiplicativeTotal = 1f;
		for(ExpMod m : mods) {
			if(m.getModType() == ExpMod.TYPE_ADDITIVE) {
				additiveTotal+=m.getModifier();
			} else if (m.getModType() == ExpMod.TYPE_MULTIPLICATIVE) {
				multiplicativeTotal *= m.getModifier();
			}
		}
		return (additiveTotal+initial) * multiplicativeTotal;
	}
	
}
