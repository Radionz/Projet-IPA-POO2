package zuul.rooms;

import zuul.Game;
import zuul.Woz;
import zuul.entities.NPC;
import zuul.entities.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class LunchRoom extends Room{
    /**
     * Create a rooms described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     *
     * @param description The rooms's description.
     */
    public LunchRoom(String description) {
        super(description);this.actions = new ArrayList<String>();
        actions.add("drinkCoffee");
        actions.add("playBabyfoot");
    }

    public String drinkCoffee(Player player){
    	return player.use("coffee");
    }
    
    public String playBabyfoot(Player player){	
    	if(player instanceof NPC)
    		return player.getName() + Game.getConst().get("IA_play_babyfoot");
    		
		return Game.getConst().get("play_babyfoot") + 
				((player.forgetALesson()) ? "\n" 
				+ Game.getConst().get("lesson_forget") : "");
    }

    public boolean isForced(){
        return new Random().nextInt(5) <= 1;
    }
    
    /**
     * @author Adrien Boucher
     * 
     * Define what happen when the player enter the room
     */
    @Override
    public void enter(Player player){
    	super.enter(player);
    	if(isForced())
    		Woz.writeMsg(playBabyfoot(player));
    }
}
