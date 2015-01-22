package zuul.rooms;

import java.util.ArrayList;
import java.util.Random;

import zuul.Game;
import zuul.Woz;
import zuul.entities.Player;
import zuul.entities.NPC;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Library extends Room{



	public Library(String description) {
		super(description);
		this.actions = new ArrayList<String>();
        actions.add("readBook");
    }

    public String readBook(Player player){
    	if(player instanceof NPC)
    		return player.getName() + " " + Game.getConst().get("IA_read_book");
    	else
    		return (String)Game.getConst().get("player_read_book");
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
    public boolean canEnter(Player player){
    	if(!checkForschedule())
    	{
    		Woz.writeMsg(player.getName() + ", " + Game.getConst().get("no_access_library"));
    		return false;
    	}
    	return true;    	
    }
    
    
    

}
