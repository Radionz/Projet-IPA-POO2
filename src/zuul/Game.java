package zuul;

import java.util.ArrayList;
import java.util.HashMap;

import zuul.GameManager.CommandToProcess;
import zuul.entities.IA;
import zuul.entities.IAManager;
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
	private static HashMap<String,String> constantes;
	private ArrayList<Room> rooms;
	private IAManager managerIA;
	private GameManager gameManager;
	
	private static Question[] questions;
	private static Course[] lessons;

	/**
	 * Create the game and initialise its internal map.
	 * all questions and lessons arrays
	 * and create a Player
	 */
	public Game(GameManager gm) {
		try{
			constantes = IO.getFromFile(IO.PossibleFiles.ENGLISH.getPath());
		} catch(Exception e) {e.printStackTrace();}
		questions = new Question[15];
		lessons = new Course[10];		
		init();
		rooms = new ArrayList<Room>();
		createRooms();
		// Set the player in the first room
		player = new Player("", new Item(""), null);
		parser = new Parser();
		
		managerIA = new IAManager(gm, rooms);
		gameManager = gm;
	}

	/* Basic getters */
	public static HashMap<String, String> getConst() {
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
	
	public ArrayList<Room> getRooms(){ return rooms; }



	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms() {
		Room outside, theater, pub, office, secretPassage;

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

		rooms.add(outside);
		rooms.add(theater);
		rooms.add(pub);
		rooms.add(office);
		rooms.add(secretPassage);
		rooms.add(labroom);		
		rooms.add(classroom);
		rooms.add(examroom);
		rooms.add(library);
		rooms.add(lunchroom);
		rooms.add(corridor);
		rooms.add(corridor2);
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
		printWelcome();
		
		new Thread(managerIA).start();
		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished) {			
			Command command = parser.getCommand();
			finished = gameManager.addCommandToProcess(player, command);	
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
		System.out.println(this.player.getName() + ", " + constantes.get("intro"));
		System.out.println(constantes.get("need_help") + " : " + CommandWord.HELP);
		System.out.println();
		// Add the player to the first room -> print room description
		player.enter(rooms.get(0));
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * 
	 * @param command
	 *            The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	
}
