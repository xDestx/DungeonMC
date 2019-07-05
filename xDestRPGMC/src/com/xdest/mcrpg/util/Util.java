package com.xdest.mcrpg.util;

public class Util {

	public static int[] fixCoords(int c1x, int c1y, int c1z, int c2x, int c2y, int c2z) {
		if(c1x > c2x) {
			int x = c1x;
			c1x = c2x;
			c2x = x;
		}
		if(c1y > c2y) {
			int y = c1y;
			c1y = c2y;
			c2y = y;
		}
		if(c1z > c2z) {
			int z = c1z;
			c1z = c2z;
			c2z = z;
		}
		return new int[] {c1x,c1y,c1z,c2x,c2y,c2z};
	}
	
	public static int[] fixCoords(int...cs) {
		if(cs.length != 6) throw new IndexOutOfBoundsException();
		return fixCoords(cs[0],cs[1],cs[2],cs[3],cs[4],cs[5]);
	}
	
}
