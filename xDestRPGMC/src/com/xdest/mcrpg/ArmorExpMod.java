package com.xdest.mcrpg;

public class ArmorExpMod implements ExpMod {

	private float mod;
	
	public void setModifier(float mod) {
		this.mod = mod;
	}
	
	@Override
	public String getName() {
		return "Armor EXP";
	}

	@Override
	public String getId() {
		return "rpg-armor-exp";
	}

	@Override
	public float getModifier() {
		return mod;
	}

	@Override
	public int getModType() {
		return ExpMod.TYPE_ADDITIVE;
	}

	@Override
	public boolean canStack() {
		return false;
	}

	@Override @Deprecated
	public void setName(String s) {
		
	}

}
