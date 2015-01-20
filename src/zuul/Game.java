package zuul;

import java.util.HashMap;

import zuul.entities.Player;
import zuul.entities.items.Coffee;
import zuul.entities.items.Item;
import zuul.io.Command;
import zuul.io.CommandWord;
import zuul.io.IO;
import zuul.io.Parser;
import zuul.rooms.*;
import zuul.studies.Lab;
import zuul.studies.Course;
import zuul.studies.Question;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game. Users can walk
 * around some scenery. That's all. It should really be extended to make it more
 * interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates and executes the
 * commands that the parser returns.
 * 
 * @author Nicolas Sarroche, Dorian Blanc
 */

public class Game {

	private static Player player;
	private Parser parser;
	private Room currentRoom;
	private static HashMap<String,String> constantes;

	private static Question[] questions;
	private static Course[] lessons;

	/**
	 * Create the game and initialise its internal map.
	 * all questions and lessons arrays
	 * and create a Player
	 */
	public Game() {
		try{
			constantes = IO.getFromFile(IO.PossibleFiles.ENGLISH.getPath());
		} catch(Exception e) {e.printStackTrace();}
		questions = new Question[15];
		lessons = new Course[10];
		player = new Player("", new Item(""));
		init();
		createRooms();
		parser = new Parser();
	}

	/* Basic getters */
	public static HashMap<String, String> getConstantes() {
		return constantes;
	}

	public static Player getPlayer() {
		return player;
	}

	public static Question[] getQuestions() {
		return questions;
	}

	public static Course[] getLessons() {
		return lessons;
	}
	/* Basics getters */


	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms() {
		Room outside, theater, pub, lab, office, secretPassage;

		// create the rooms
		outside = new Room(constantes.get("outside_description"));
		ClassRoom classroom = new ClassRoom( constantes.get("classroom_description"), false, 0);
		ExamRoom examroom = new ExamRoom(constantes.get("examroom_description"));
		LabRoom labroom = new LabRoom(constantes.get("labroom_description"));
		Library library = new Library(constantes.get("library_description"));
		LunchRoom lunchroom = new LunchRoom(constantes.get("lunchroom_description"));
		Corridor corridor = new Corridor(constantes.get("corridor_description"));
		Corridor corridor2 = new Corridor(constantes.get("corridor_description"));
		theater = new Room(constantes.get("theater_description"));
		pub = new Room(constantes.get("pub_description"));
		office = new Room(constantes.get("office_description"));
		secretPassage = new SecretPassage(constantes.get("secret_passage_description"));

		// initialise rooms exits
		outside.setExit(Room.Exits.EAST, library);
		outside.setExit(Room.Exits.SOUTH, corridor);
		outside.setExit(Room.Exits.WEST, lunchroom);
		outside.addItem(new Item(constantes.get("chocolate_bar_description"),1));
		outside.addUsableItem(new Item(constantes.get("chocolate_bar_description"),1));
		outside.addItem(new Coffee());
		
		pub.setExit(Room.Exits.WEST, secretPassage);
		secretPassage.setExit(Room.Exits.WEST, theater);


		lunchroom.setExit(Room.Exits.WEST, pub);
		library.setExit(Room.Exits.EAST, theater);

		corridor.setExit(Room.Exits.EAST, labroom);
		corridor.setExit(Room.Exits.WEST, classroom);
		corridor.setExit(Room.Exits.SOUTH, corridor2);

		corridor2.setExit(Room.Exits.EAST, office);
		corridor2.setExit(Room.Exits.WEST, examroom);
		corridor2.setExit(Room.Exits.SOUTH, outside);

		currentRoom = outside; // start game outside
	}


	/**
	 * init questions arrays
	 */
	private void init() {

		for (int i = 0; i < 15; i++) {
			questions[i] = new Question(i + 1);
		}
		for (int i = 0; i < 5; i++) {
			lessons[i] = new Course(true, i+1);
		}
		for (int i = 0; i < 5; i++) {
			lessons[i+5] = new Course(false, i+1);
		}
	}


	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		String playerName = getPlayerName();
		player = new Player(playerName);

		printWelcome();

		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println(constantes.get("close_game"));
	}

	/**
	 * Print the beginning of the welcome message and allows the player to type his name.
	 * @return String of player's name.
	 */
	private String getPlayerName() {
		System.out.println();
		System.out.println(constantes.get("welcome"));
		System.out.println(constantes.get("invite_enter_name"));
		return parser.getPlayerName();
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println(this.player.getName() + ", " + constantes.get("intro"));
		System.out.println(constantes.get("need_help") + " : " + CommandWord.HELP);
		System.out.println();
		System.out.println(currentRoom.getLongDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * 
	 * @param command
	 *            The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private boolean processCommand(Command command) {
		boolean wantToQuit = false;

		CommandWord commandWord = command.getCommandWord();

		switch (commandWord) {
		case UNKNOWN:
			System.out.println(constantes.get("dont_understand"));
			break;

		case HELP:
			printHelp();
			break;

		case GO:
			goRoom(command);
			break;

		case DO:
			doSomething(command);
			break;

		case DROP:
			dropItem(command);
			break;

		case PICK:
			pickItem(command);
			break;

		case USE:
			useItem(command);
			break;

		case INVENTORY:
			printInventory(command);
			break;

		case ANSWER:
			answerQuestion(command);
			break;

		case QUIT:
			wantToQuit = quit(command);
			break;

		}
		return wantToQuit;
	}

	// implementations of user commands:
	private void printInventory(Command command) {
		System.out.println(constantes.get("you_carry") + ": " + player.getInventoryContent());
	}

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void printHelp() {
		System.out.println(constantes.get("help_intro"));
		System.out.println(constantes.get("your_command_word"));
		parser.showCommands();
	}

	/**
	 * Try to go in one direction. If there is an exit, enter the new rooms,
	 * otherwise print an error message.
	 */
	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println(constantes.get("go_where"));
			System.out.println(currentRoom.getExitString());
			return;
		}

		String direction = command.getSecondWord();

		// Try to leave current rooms.
		Room nextRoom = currentRoom.getExit(direction);		

		if (nextRoom == null) {
			System.out.println(constantes.get("no_door"));
		}else if(currentRoom.isHidden() || (currentRoom.canLeave() && nextRoom.canEnter())){
			currentRoom = nextRoom;
			currentRoom.enter();
		}		
	}

	/**
	 * Method allowing the player to drop an item in the current room.
	 * @param command
	 */
	private void dropItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println(constantes.get("what_drop"));
			return;
		}

		String itemName = command.getSecondWord();

		// Try to drop item in the current rooms.
		if(player.dropItem(currentRoom,itemName)){
			System.out.println(constantes.get("ok_drop") + ": " + itemName);
		}else{
			System.out.println(constantes.get("you_not_carry")+ ": " + itemName);
			System.out.println(constantes.get("you_carry")+ ": " + player.getInventoryContent());
		}
	}

	/**
	 * Method allowing you to pick an item in the current room
	 * @param command item name
	 */
	private void pickItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println(constantes.get("what_pick"));
			System.out.println(currentRoom.getItemString());
			return;
		}

		String itemName = command.getSecondWord();

		// Try to pick item in the current rooms.
		if(currentRoom.hasItem(itemName)){
			player.pickUp(currentRoom,itemName);
			System.out.println(constantes.get("ok_pick") + ": " + itemName);
		}else{
			System.out.println(constantes.get("no") + itemName);
			System.out.println(currentRoom.getItemString());
		}
	}

	/**
	 * Method allowing you to use an item in the current room
	 * @param command item name
	 */
	private void useItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println(constantes.get("what_use"));
			return;
		}

		String itemName = command.getSecondWord();

		// Try to use item in the current rooms.
		if (currentRoom.canUseItem(itemName)) {
			System.out.println(player.use(itemName));
		}
		else {
			System.out.println(constantes.get("no_use_item_here"));
		}
	}

	/**
	 * method allowing you to do some actions in different rooms
	 * @param command full command to parse
	 */
	private void doSomething(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println(constantes.get("what_do"));
			System.out.println(currentRoom.getActionString());
			return;
		}

		String mehtod = command.getSecondWord();

		// Try to use item in the current rooms.
		System.out.println(currentRoom.doSomething(mehtod));
	}

	/**
	 * method allowing you to answer question in some room as
	 * LabRoom or ExamRoom
	 * @param command the full command to parse
	 */
	private void answerQuestion(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know...
			System.out.println(constantes.get("what_answer"));
			return;
		}

		String answer = command.getSecondWord();
		currentRoom.study(answer);		
	}

	/**
	 * "Quit" was entered. Check the rest of the command to see whether we
	 * really quit the game.
	 * 
	 * @return true, if this command quits the game, false otherwise.
	 */
	private boolean quit(Command command) {
		if (command.hasSecondWord()) {
			System.out.println(constantes.get("what_quit"));
			return false;
		} else {
			return true; // signal that we want to quit
		}
	}

	/**
	 * @author Adrien Boucher
	 * @return Constantes du jeu (textes)
	 */
	public static HashMap<String, String> getConst() { return constantes; }
}
