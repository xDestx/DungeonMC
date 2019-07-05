package com.xdest.mcrpg.util;

import org.bukkit.Location;
import org.bukkit.World;

public class RectangularRegion implements WorldRegion {

	private Location corner1, corner2;
	private int c1x, c1y, c1z, c2x, c2y, c2z;
	
	public RectangularRegion(Location l1, Location l2) throws MismatchedWorldException {
		if(l2.getWorld().equals(l1.getWorld())) {
			corner1 = l1;
			corner2 = l2;
			c1x = l1.getBlockX();
			c1y = l1.getBlockY();
			c1z = l1.getBlockZ();
			c2x = l2.getBlockX();
			c2y = l2.getBlockY();
			c2z = l2.getBlockZ();
			fixCoords();
		} else {
			throw new MismatchedWorldException();
		}
	}
	
	private void fixCoords() {
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
	}
	
	@Override
	public boolean contains(Location l) {
		if(l.getWorld().equals(getWorld())) {
			int lx, ly, lz;
			lx = l.getBlockX();
			ly = l.getBlockY();
			lz = l.getBlockZ();
			if (c1x + lx <= c2x) {
				if(c1y + ly <= c2y) {
					if(c1z + lz <= c2z) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public World getWorld() {
		return corner1.getWorld();
	}

}
