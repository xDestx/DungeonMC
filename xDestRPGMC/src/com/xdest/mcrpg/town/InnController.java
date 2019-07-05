package com.xdest.mcrpg.town;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.xdest.mcrpg.util.MismatchedWorldException;
import com.xdest.mcrpg.util.RectangularRegion;

public class InnController {
	//test
	
	private static Map<String,Inn> inns;
	private static Map<String,InnRoom> rooms;
	
	static {
		inns = new HashMap<String,Inn>();
		rooms = new HashMap<String,InnRoom>();
	}
	
	
	public static int createInn(String innId, Location c1, Location c2) {
		return 0;
	}
	
	/**
	 * 
	 * @param innId
	 * @param c1
	 * @param c2
	 * @return 0 if success, 1 if inn not found
	 */
	public static int createRoom(String innId, String roomId, Location c1, Location c2) throws MismatchedWorldException {
		Inn inn = inns.get(innId);
		if(inn != null) {
			if(!inn.hasRoom(roomId) && inn.contains(c1) && inn.contains(c2)) {
				RectangularRegion irr = new RectangularRegion(c1,c2);
				InnRoom ir = new InnRoom(irr, innId, roomId);
				rooms.put(roomId, ir);
				inn.addRoom(roomId);
				return 0;
			}
			
			return 2;
		}
		return 1;
	}
}
