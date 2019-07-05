package com.xdest.mcrpg.util;

import org.bukkit.Location;
import org.bukkit.World;

public class RectangularRegion implements WorldRegion {

	private Location corner1, corner2;
	private int[] coords;
	/*
	 * 0 c1 x
	 * 1 c1 y
	 * 2 c1 z
	 * 3 c2 x
	 * 4 c2 y
	 * 5 c2 z
	 * 
	 */
	
	public RectangularRegion(Location l1, Location l2) throws MismatchedWorldException {
		if(l2.getWorld().equals(l1.getWorld())) {
			corner1 = l1.clone();
			corner2 = l2.clone();
			coords[0] = l1.getBlockX();
			coords[1] = l1.getBlockY();
			coords[2] = l1.getBlockZ();
			coords[3] = l2.getBlockX();
			coords[4] = l2.getBlockY();
			coords[5] = l2.getBlockZ();
			coords = Util.fixCoords(coords);
			corner1.setX(coords[0]);
			corner1.setY(coords[1]);
			corner1.setZ(coords[2]);
			corner2.setX(coords[3]);
			corner2.setY(coords[4]);
			corner2.setZ(coords[5]);
		} else {
			throw new MismatchedWorldException();
		}
	}
	
	
	
	@Override
	public boolean contains(Location l) {
		if(l.getWorld().equals(getWorld())) {
			int lx, ly, lz;
			lx = l.getBlockX();
			ly = l.getBlockY();
			lz = l.getBlockZ();
			if (coords[0] + lx <= coords[3]) {
				if(coords[1] + ly <= coords[4]) {
					if(coords[2] + lz <= coords[5]) {
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
