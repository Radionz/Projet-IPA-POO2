package zuul.rooms;

import java.util.ArrayList;

import zuul.entities.items.Item;

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
        actions.add("switchOn");
    }

    /**
     * method dynamically called
     * switch on the light in the room
     * @return the string "Lights ON !"
     */
    public String switchOn(){
        actions.add("switchOff");
        actions.remove("switchOn");
    	this.light = true;
    	return "Lights ON !";
    }

    /**
     *  method dynamically called
     * switch off the light in the room
     * @return the string "Lights OFF !"
     */
    public String switchOff(){
    	actions.remove("switchOff");
        actions.add("switchOn");
    	this.light = true;
    	return "Lights OFF !";
    }

    /**
     * method dynamically called
     * invert light switch
     * on => off
     * off => on
     */
    public void switchLight() {
        this.light = !this.light;
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
