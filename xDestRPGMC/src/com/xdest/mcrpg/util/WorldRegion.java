package com.xdest.mcrpg.util;

import org.bukkit.Location;
import org.bukkit.World;

public interface WorldRegion {

	boolean contains(Location l);
	World getWorld();

}
