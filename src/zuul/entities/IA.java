package zuul.entities;

import zuul.Game;
import zuul.rooms.Room;

public class IA extends Player implements NPC{
	private Room currentRoom;
	
	public IA(String playerName) {
		super(playerName);
		currentRoom = null;
	}

}
