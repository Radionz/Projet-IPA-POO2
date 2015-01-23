package zuul;

import zuul.rooms.Point;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import zuul.GameManager.CommandToProcess;
import zuul.entities.IA;
import zuul.entities.Player;
import zuul.entities.items.Coffee;
import zuul.entities.items.Item;
import zuul.io.Command;
import zuul.io.IO;
import zuul.io.Parser;
import zuul.rooms.*;
import zuul.studies.Exam;
import zuul.studies.Lab;
import zuul.studies.Lesson;
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

	private static boolean finished;
	private static Player player;
	private Parser parser;
	private static HashMap<String, Object> constantes;
	private ArrayList<Room> rooms;
	private static IAManager managerIA;
	private static GameManager gameManager;
	private static ArrayList<Lesson> lessons;
	private static ArrayList<Lab> labs;
	private static ArrayList<Exam> exams;

	/**
	 * Create the game and initialise its internal map. all questions and
	 * lessons arrays and create a Player
	 */
	public Game(GameManager gm) {
		finished = false;
		try {
			constantes = IO.getFromFile(IO.getPossibleLanguagesPaths().get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lessons = new ArrayList<Lesson>();
		labs = new ArrayList<Lab>();
		exams = new ArrayList<Exam>();
		rooms = new ArrayList<Room>();

		parser = new Parser();
		createStudySupport();
		createRooms();

		managerIA = new IAManager(gm, rooms);
		gameManager = gm;
	}

	/* Basic getters */
	public static HashMap<String, Object> getConst() {
		return constantes;
	}

	public static Player getPlayer() {
		return player;
	}

	public static ArrayList<Exam> getExams() {
		return exams;
	}

	public static ArrayList<Lab> getLabs() {
		return labs;
	}

	public static ArrayList<Lesson> getLessons() {
		return lessons;
	}

	public static Lesson getRadomLesson() {
		Random random = new Random();
		return lessons.get(random.nextInt(lessons.size()));
	}

	private void createStudySupport() {
		for (String path : IO.getPossibleLessonsPaths()) {
			lessons.add(new Lesson(path));
		}
		for (Lesson lesson : lessons) {
			labs.add(new Lab(lesson, 3));
		}
		for (Lab lab : labs) {
			exams.add(new Exam(lab));
		}
	}

	private void createRooms() {
		Room outside, theater, pub, office, secretPassage;

		// create the rooms
		outside = new Room(
				(String) (String) constantes.get("outside_description"));
		ClassRoom classroom = new ClassRoom(
				(String) constantes.get("classroom_description"));
		ExamRoom examroom = new ExamRoom(
				(String) constantes.get("examroom_description"));
		LabRoom labroom = new LabRoom(
				(String) constantes.get("labroom_description"));
		Library library = new Library(
				(String) constantes.get("library_description"));
		LunchRoom lunchroom = new LunchRoom(
				(String) constantes.get("lunchroom_description"));
		Corridor corridor = new Corridor(
				(String) constantes.get("corridor_description"));
		Corridor corridor2 = new Corridor(
				(String) constantes.get("corridor_description"));
		theater = new Room((String) constantes.get("theater_description"));
		pub = new Room((String) constantes.get("pub_description"));
		office = new Room((String) constantes.get("office_description"));
		secretPassage = new SecretPassage(
				(String) constantes.get("secret_passage_description"));

		// initialise rooms exits
		outside.setExit(Room.Exits.EAST, library);
		outside.setExit(Room.Exits.SOUTH, corridor);
		outside.setExit(Room.Exits.WEST, lunchroom);
		outside.addItem(new Item((String) constantes
				.get("chocolate_bar_description"), 1));
		outside.addUsableItem(new Item((String) constantes
				.get("chocolate_bar_description"), 1));
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
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		player = new Player(Woz.getName(), null);
		printWelcome();
		player.enter(rooms.get(0));
		new Thread(managerIA).start();
		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = !gameManager.addCommandToProcess(player, command);
		}
		Woz.writeMsg((boolean) constantes.get("close_game"));
	}

	public void process(Command c) {
		gameManager.addCommandToProcess(player, c);
	}

	private void printWelcome() {
		Woz.writeMsg(this.player.getName() + ", "
				+ (String) constantes.get("intro"));
	}
	
	public void generateCoordinatesMap() {
		Room startingRoom = rooms.get(0);
		startingRoom.setCoordinates(new Point(0,0));
		generateCoordinates(startingRoom,"",new Point(0,0));
	}
	
	/**
	 * A recursive method that set coordinates for a room and all it's neighbours.
	 * @param room The room to add coordinates to
	 * @param fromWhere The cardinal orientation from which we came from
	 * @param previousLocation	The previous room's coordinate
	 * @param count	The count of the rooms who's coodinates have been set..
	 * @author ANASTAS Mazen
	 */
	private void generateCoordinates(Room room, String fromWhere,Point previousLocation) {
		Point coordinates, differential;
		int abscissa, ordinate;
				
		//If coordinates don't exist, create them
		coordinates = previousLocation;
		if (room.getCoordinates() == null) {
			differential = getDifferential(fromWhere);
			
			abscissa = (int) coordinates.getX() + (int) differential.getX();
			ordinate = (int) coordinates.getY() + (int) differential.getY();
			coordinates = new Point(abscissa, ordinate);
			
			room.setCoordinates(coordinates);
			System.out.println(room + " >>>> OK");
		}
		
		//if only one exit, it's the same exit from which we came from
		if (room.getNbExits()==1)	return;
		
		
		//generate coordinates for neighbors rooms
		for (String cardinal : cardinals()) {
			//that's where we came from. So no need to check
			if (areComplement(cardinal,fromWhere))	continue;	
							
			Room next = room.getExit(cardinal); 
			if (next == null || next.getCoordinates() != null) continue;		//if room doesn't exist, skip 1 iteration.s
			generateCoordinates(room.getExit(cardinal),cardinal,coordinates);	//recursively check neigbour
		}
	}

	private static boolean areComplement(String cardinal, String complement) {
		switch (cardinal) {
			case "north" : return (complement.equals("south"));
			case "south" : return (complement.equals("north"));
			case "west" : return (complement.equals("east"));
			case "east" : return (complement.equals("west"));
			default : return false;
		}
	}
	
	public static String[] cardinals() {
		return new String[]{"north","east","south","west"};
	}
	
	
	/**
	 * Returns the (x,y) pair that represents the differential
	 * based on the cardinal. The returned points' x will represent
	 * the differential judging the x axis (respectively y). 
	 * @param cardinal String representing the cardinal
	 * @return point differential
	 * @author ANASTAS Mazen
	 */
	private static Point getDifferential(String cardinal) {
		switch (cardinal) {
			case "north" : return new Point(0,1);
			case "south" : return new Point(0,-1);
			case "east" : return new Point(1,0);
			case "west" : return new Point(-1,0);
			default : return new Point(0,0);		//shouldn't ever need to get here.
		}
	}

}
