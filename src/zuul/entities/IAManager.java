package zuul.entities;

import java.util.ArrayList;
import java.util.Random;

import zuul.Game;
import zuul.io.Command;
import zuul.io.CommandWord;
import zuul.rooms.Room;

public class IAManager {
	private ArrayList<IA> listIA;
	private ArrayList<Room> rooms;
	
	private Game game;
	
	public IAManager(ArrayList<Room> rooms)
	{
		this.rooms = rooms;
		
		listIA = new ArrayList<IA>();
		addIA(new IA("Dorian le violent"));
		addIA(new IA("Toto"));
		addIA(new IA("Jean Gui"));
	}
	
	public void addIA(IA npc){ listIA.add(npc); }
	
	public void initIA(Room r)
	{
		for(int i=0; i<listIA.size(); i++)
			listIA.get(i).enter(r);
	}
	
	
	public IA getNextIA()
	{
		return listIA.get(new Random().nextInt(listIA.size()));
	}
	
	public Command getCommandForIA(IA ia)
	{		
		return new Command(CommandWord.GO, Room.Exits.getRandom().getValue());
	}
}
