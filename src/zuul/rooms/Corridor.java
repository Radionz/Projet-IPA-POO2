package zuul.rooms;

import java.util.ArrayList;

import zuul.Game;
import zuul.entities.Player;
import zuul.entities.items.Item;
import zuul.entities.NPC;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Corridor extends Room{

    private boolean light;

    /**
     * Create a rooms described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     *
     * @param description The rooms's description.
     */
    public Corridor(String description) {
        super(description);
        this.light = false;
        this.actions = new ArrayList<String>();
        actions.add("toggleLight");
    }

    
    /**
     * @author Adrien Boucher
     * 
     * Toggle the light -> off <-> on
     * 
     * @param player
     * @return
     */
    public String toggleLight(Player player){
    	this.light = !light;
    	
    	if(player instanceof NPC)
    		return player.getName() + " " + (light ? Game.getConst().get("IA_light_on") 
    				: Game.getConst().get("IA_light_off"));
    	else
    		return (String)(Game.getConst().get("self") +
    				(light ? (String)Game.getConst().get("light_on") 
    						: (String)Game.getConst().get("light_off")));
    } 


    /**
     * method allowing you to know if there are items in the room
     * but if the light is off (light == false) i return false
     * @param item Item to check
     * @return boolean true if an item is in the room and light is ON
     */
    public boolean hasItem(Item item){
        if(light)
            return this.items.contains(item);
        return false;
    }

    @Override
    public String getLongDescription() {
    	if(light)
    		return "You are " + description + " and the light is on.\n" + getItemString() + "\n" + getActionString() + "\n" + getExitString();
    	return "You are " + description + " and the light is off.\n" + getActionString() + "\n" + getExitString();
    }
    

}
