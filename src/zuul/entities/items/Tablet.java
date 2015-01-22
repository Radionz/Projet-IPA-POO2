package zuul.entities.items;

import zuul.Game;
import zuul.io.IO;
import zuul.studies.Lesson;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Tablet extends Item{
	
	public Tablet()
	{
		super("Tablet", 0);
	}
	
	@Override
	public String use()
	{
		if(Math.random() < 0.5)
		{
			if(Math.random() > 0.75)
				return (String) Game.getConst().get("play_game_zuul");
			// If the game is not world of zuul, the player forget a lesson
			Game.getPlayer().forgetALesson();		
			return (String) Game.getConst().get("play_game");
		}
		else
		{
			System.err.println("TODO : implement lesson on tablet");
			return "You've learned something new";
		}
	}
	
}
