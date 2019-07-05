package com.xdest.mcrpg.town;

import com.xdest.mcrpg.util.RectangularRegion;

public class InnRoom {
	

	
	
	private RectangularRegion region;
	private String innId, id;
	
	public InnRoom(RectangularRegion r, String id, String innId) {
		this.innId = innId;
		region = r;
		this.id = id;
	}
	
	
}
