package com.xdest.mcrpg;

public interface ExpMod {

	public static final int TYPE_ADDITIVE = 0, TYPE_MULTIPLICATIVE = 1;
	
	String getName();
	String getId();
	float getModifier();
	int getModType();
	boolean canStack();
	void setName(String s);
}
