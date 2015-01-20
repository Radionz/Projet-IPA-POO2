package zuul.entities.items;

import zuul.Game;
import zuul.io.IO;
import zuul.studies.Course;

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
				return Game.getConst().get("play_game_zuul");
			// If the game is not world of zuul, the player forget a lesson
			Game.getPlayer().forgetALesson();		
			return Game.getConst().get("play_game");
		}
		else
		{
			Course l = new Course((Math.random() < 0.5), 1);
			return "You've learned something new";
		}
	}
	
}
