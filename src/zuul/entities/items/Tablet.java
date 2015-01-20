package zuul.entities.items;

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
			return "You've played a game";
		}
		else
		{
			Course l = new Course((Math.random() < 0.5), 1);
			return "You've learned something new";
		}
	}
	
}
