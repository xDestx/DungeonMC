package com.xdest.mcrpg.town;

import java.util.Set;

import org.bukkit.Location;

import com.xdest.mcrpg.util.RectangularRegion;

public class Inn {
	
	
	private Set<String> roomIds;
	private RectangularRegion region;
	private String id;
	
	public Inn(RectangularRegion r, String id) {
		region = r;
		this.id = id;
	}
	
	protected void addRoom(String ir) {
		roomIds.add(ir);
	}
	
	public boolean hasRoom(String id) {
		return roomIds.contains(id);
	}
	
	public boolean contains(Location l1) {
		return region.contains(l1);
	}
	
}
