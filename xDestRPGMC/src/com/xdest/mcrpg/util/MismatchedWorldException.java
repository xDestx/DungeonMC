package com.xdest.mcrpg.util;

public class MismatchedWorldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7623762418271006554L;
	
	public MismatchedWorldException() {
		super("Two locations provided are not in the same world.");
	}

}
