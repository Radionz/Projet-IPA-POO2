package zuul.rooms;

import java.util.ArrayList;
import java.util.Random;

import zuul.Game;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Library extends Room{



	public Library(String description) {
		super(description);
		this.actions = new ArrayList<String>();
        actions.add("readBook");
    }

    public String readBook(){
    	return "WOW I read a book !";    	
    }

    public boolean checkForschedule() {
        Random random = new Random();
        int hour = random.nextInt(24);
        return hour==1;
    }
    
    /**
     * @author Adrien Boucher
     */
    @Override
    public boolean canEnter(){
    	if(!checkForschedule())
    	{
    		System.out.println(Game.getConst().get("no_access_library"));
    		return false;
    	}
    	return true;    	
    }
    
    
    

}
