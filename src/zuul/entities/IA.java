package zuul.entities;

import java.util.ArrayList;
import java.util.Random;

import zuul.Game;
import zuul.io.Command;
import zuul.rooms.Room;

public class IA extends Player implements NPC{

	public IA(String playerName) {
		super(playerName, null);
	}
	
	public IA(String playerName, Room room) {
		super(playerName, room);
	}
	
	/**
	 * Test if the IA can pick up an item
	 * @param ia
	 * @return	true if there is a least one item to pick up.
	 */
	public boolean canPickUpItem()
	{
		return currentRoom.getItemsList().size() > 0;
	}
	
	
	public Command generateValidGoCommand()
	{		
		return new Command("go", currentRoom.getRandomDirection());
	}
	
	public Command generateDoCommand()
	{
		ArrayList<String> actions = currentRoom.getActions();
		
		return (actions.size() == 0) ? null : 
				new Command("action", actions.get(new Random().nextInt(actions.size())));						
	}
	
	public Command getAnswerCommand()
	{
		return new Command("answer", (Math.random() > 0.5 ? "true" : "false"));
	}
	
	public Command learn()
	{
		if(currentRoom.getActions().contains("learn"))
			return new Command("action", "learn");
		
		return null;
	}


}
