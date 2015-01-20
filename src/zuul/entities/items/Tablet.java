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
				return "You've played at World of Zuul";
			// If the game is not world of zuul, the player forget a lesson
			Game.getPlayer().forgetALesson();		
			return "You've played a game and you've forgotten a lesson";
		}
		else
		{
			Course l = new Course((Math.random() < 0.5), 1);
			return "You've learned something new";
		}
	}
	
}
