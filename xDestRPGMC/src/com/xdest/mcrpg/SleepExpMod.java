package com.xdest.mcrpg;

public class SleepExpMod implements ExpMod {

	private String name;
	private float mod;
	
	public SleepExpMod(String name, float modifier) {
		this.name = name;
		this.mod = modifier;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getId() {
		return "sleep-exp-mod";
	}

	@Override
	public float getModifier() {
		return 1 + mod;
	}
	
	public float getRawMod() {
		return mod;
	}

	@Override
	public int getModType() {
		return ExpMod.TYPE_MULTIPLICATIVE;
	}

	@Override
	public boolean canStack() {
		return false;
	}

	@Override
	public void setName(String s) {
		this.name = s;
	}

}
