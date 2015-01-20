package zuul.entities;

import java.util.ArrayList;

import zuul.Game;
import zuul.io.Command;
import zuul.io.CommandWord;
import zuul.rooms.Room;

public class IAManager {
	private ArrayList<IA> listIA;
	private Game game;
	
	public IAManager(Game game)
	{
		this.game = game;
		
		listIA = new ArrayList<IA>();
		IA a = new IA("IA");
		a.enter(game.getRooms().get(0));
		addIA(a);
	}
	
	public void addIA(IA npc){ listIA.add(npc); }
	
	
	public void actionTest()
	{
		Command c = new Command(CommandWord.GO, "west");
		game.processCommand(c, listIA.get(0));
	}
	
}
