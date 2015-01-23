package zuul.rooms;

import zuul.Game;
import zuul.Woz;
import zuul.entities.NPC;
import zuul.entities.Player;
import zuul.entities.items.Item;
import zuul.studies.Lesson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.HashMap;

/**
 * Class rooms - a rooms in an adventure game.
 * 
 * This class is part of the "World of Zuul" application. "World of Zuul" is a
 * very simple, text based adventure game.
 * 
 * A "rooms" represents one location in the scenery of the game. It is connected
 * to other rooms via exits. For each existing exit, the rooms stores a
 * reference to the neighboring rooms.
 * 
 * @author Nicolas Sarroche, Dorian Blanc
 * 
 */

public class Room {
	protected String description;
	protected int level;
	protected HashMap<Exits, Room> exits; // stores exits of this rooms
	protected ArrayList<Item> items;
	private ArrayList<Item> usableItems;
	protected ArrayList<String> actions;
	protected ArrayList<Player> playersInRoom;

	/**
	 * Create a rooms described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The rooms's description.
	 */
	public Room(String description) {
		this.description = description;
		exits = new HashMap<Exits, Room>();
		this.items = new ArrayList<>(100);
		this.usableItems = new ArrayList<>(100);
		this.actions = new ArrayList<>(100);
		this.playersInRoom = new ArrayList<Player>();
	}

	/**
	 * Define an exit from this rooms.
	 * 
	 * @param direction
	 *            The direction of the exit.
	 * @param neighbor
	 *            The rooms to which the exit leads.
	 */
	public void setExit(Exits direction, Room neighbor) {
		exits.put(direction, neighbor);
		neighbor.putExit(direction.getOpposite(), this);
	}

	public void putExit(Exits exit, Room room) {
		exits.put(exit.getOpposite(), room);
	}
	
	public String getRandomExitDirection()
	{
		Set<Exits> exitList = exits.keySet(); 
		return ((Exits)(exitList.toArray())[new Random().nextInt(exitList.size())]).getValue();
	}

	/**
	 * @return The short description of the rooms (the one that was defined in
	 *         the constructor).
	 */
	public String getShortDescription() {
		return description;
	}

	/**
	 * Return a description of the rooms in the form: You are in the kitchen.
	 * Exits: north west
	 * 
	 * @return A long description of this rooms
	 */
	public String getLongDescription() {
		return "You are " + description + ".\n" + getItemString() + "\n"
				+ getActionString() + "\n" + getExitString();
	}

	/**
	 * list of actions in the room
	 * 
	 * @return string of all actions
	 */
	public String getActionString() {
		if (actions.isEmpty())
			return "No actions.";
		String returnString = "Actions: ";
		for (String action : actions) {
			returnString += action + " - ";
		}
		return (returnString.length() > 3) ? returnString.substring(0,
				returnString.length() - 3) : returnString;
	}
	
	/**
	 * @author Adrien Boucher
	 * 
	 * @return an arraylist containing all the actions available in the room
	 */
	public ArrayList<String> getActions()	{	return actions;		}

	/**
	 * list of items in the room
	 * 
	 * @return string of all items
	 */
	public String getItemString() {
		if (items.isEmpty())
			return "No items.";
		String returnString = "Items: ";
		for (Item item : items) {
			returnString += item.getName() + " - ";
		}
		return (returnString.length() > 3) ? returnString.substring(0,
				returnString.length() - 3) : returnString;
	}

	/**
	 * Return a string describing the rooms's exits, for example
	 * "Exits: north west".
	 * 
	 * @return Details of the rooms's exits.
	 */
	public String getExitString() {
		String returnString = "Exits: ";
		Set<Exits> keys = exits.keySet();
		for (Exits exit : keys) {
			if (!getExit(exit.getValue()).isHidden())
				returnString += exit.getValue() + " - ";
		}
		return returnString.substring(0, returnString.length() - 3);
	}

	/**
	 * Return the rooms that is reached if we go from this rooms in direction
	 * "direction". If there is no rooms in that direction, return null.
	 * 
	 * @param direction
	 *            The exit's direction.
	 * @return The rooms in the given direction.
	 */
	public Room getExit(String direction) {
		// return exits.get(direction);
		return exits.get(Exits.getAnExit(direction));
	}
	
	
	public String getRandomDirection()
	{
		ArrayList<Exits> disp = new ArrayList<Exits>();
		disp.addAll(exits.keySet());
		
		return disp.get(new Random().nextInt(disp.size())).getValue();
	}
	
	
	/**
	 * Return the list of all items present in the current room
	 * 
	 * @return Array list of items
	 */
	public ArrayList<Item> getItemsList() {
		return items;
	}

	/**
	 * Method allowing the class Game or the player to add Items in the current
	 * room
	 * 
	 * @param item
	 *            Item to add in the current room.
	 */
	public void addItem(Item item) {
		items.add(item);
	}

	public void addUsableItem(Item item) {
		usableItems.add(item);
	}

	/**
	 * Return a boolean telling if an item is in this room or not.
	 * 
	 * @param item
	 *            Item to check
	 * @return If the item is in the room, or not.
	 */
	public boolean hasItem(Item item) {
		return this.items.contains(item);
	}

	public boolean hasItem(String itemString) {
		for (Item item : items) {
			if (item.getName().equals(itemString))
				return true;
		}
		return false;
	}

	/**
	 * Return a String containing all items in this room.
	 * 
	 * @return Items in the room.
	 */
	public String getItems() {
		String res = "";
		for (Item i : items) {
			res += i.toString() + ", ";
		}

		return (res.length() > 2) ? res.substring(0, res.length() - 2) : res;
	}

	/**
	 * Method allowing you to use an Item
	 * 
	 * @param itemString
	 *            item name
	 * @return if the item is usable
	 */
	public boolean canUseItem(String itemString) {
		for (Item item : usableItems) {
			if (item.getName().equals(itemString))
				return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * Try to execute an action
	 * 
	 * @param player	Player that do the action
	 * @param action	Action to do
	 * @return string that describe what happened
	 */
	public String doSomething(Player player, String action) {
		if (actions.contains(action)) {
			Method method = null;
			try {
				method = this.getClass().getMethod(action, Player.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				return (String) method.invoke(this, player);
			} catch (Exception e) {
				System.err.println("Action erreur :" + action);
				e.printStackTrace();
			}
		} else {
			return "This action doesn't exist in this room.";
		}
		return null;
	}
	
	/**
	 * Tells if a player can use the answer command in the room
	 * -> Used for IAs
	 * @return true if the command is available
	 */
	public boolean canUseAnswerCommand(){ return false; }
	
	
	/**
	 * Tells if a player can use the "DO" command
	 * -> Used for IAs
	 * @return true if there is a least 1 action available
	 */
	public boolean canUseDoCommand() { return actions.size() != 0; }

	public void study(String answer) {
		Woz.writeMsg((String)Game.getConst().get("not_in_examroom"));
	}

	/**
	 * @author Adrien Boucher Define what happen when the player enter the room
	 */
	public void enter(Player player) {
		playersInRoom.add(player);
		if(player instanceof NPC)		
			Woz.writeMsg(player.getName() + " is " + description);
		else
			Woz.writeMsg(getLongDescription());
	}

	/**
	 * @author Adrien Boucher Check if the player can enter in the room
	 * @return true if player can enter, else it return false
	 */
	public boolean canEnter(Player player) {
		return true;
	}

	/**
	 * @author Adrien Boucher Check if the player can leave the room
	 * @return true if player can leave, else it return false
	 */
	public boolean leave(Player player) {
		if(playersInRoom.contains(player))
			playersInRoom.remove(player);
		
		return true;
	}
	

	/**
	 * @author Adrien Boucher Tells if the room is visible (eg: exits are shown
	 *         in the other rooms)
	 * @return whether the room is hidden or not
	 */
	public boolean isHidden() {
		return false;
	}

	public enum Exits {
		/**
		 * Enum of the possible exits !
		 */
		NORTH("north"), EAST("east"), SOUTH("south"), WEST("west");

		private String value;

		private Exits(String s) {
			this.value = s;
		}

		public String getValue() {
			return this.value;
		}

		public Exits getOpposite() {
			return Exits.values()[(this.ordinal() + 2) % 4];
		}

		public static Exits getAnExit(String value) {
			for (Exits e : values()) {
				if (e.getValue().equals(value)) {			
					return e;
				}
			}
			return null;
		}
		
		public static Exits getRandom()
		{
			return values()[new Random().nextInt(values().length)];
		}

	}

	public int getLevel() {
		return this.level;
	}

}
