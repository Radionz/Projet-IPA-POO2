package zuul.entities;

import zuul.Game;
import zuul.rooms.Room;

public class IA extends Player implements NPC{
	
	public IA(String playerName) {
		super(playerName, null);
	}
	
	public IA(String playerName, Room room) {
		super(playerName, room);
	}

}
