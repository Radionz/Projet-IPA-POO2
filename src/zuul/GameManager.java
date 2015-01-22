package zuul;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.locks.*;

import zuul.entities.NPC;
import zuul.entities.Player;
import zuul.io.Command;
import zuul.io.Parser;
import zuul.rooms.Room;

public class GameManager implements Actions{
	private ArrayList<CommandToProcess> todo;
	private boolean running;
	public Lock lock;
	private Parser parser;
	
	private static ArrayList<String> actions;

	public GameManager() {
		todo = new ArrayList<CommandToProcess>();
		lock = new ReentrantLock();
		parser = new Parser();
		running = true;
		
		
		actions = new ArrayList<String>();
		for (Method method : Actions.class.getDeclaredMethods()) {
			actions.add(method.getName());			
			
		}
		
		new Thread(new Processor()).start();
	}

	private void executeOne() {
		CommandToProcess action = null;
		boolean ia;
		lock.lock();
		try {
			if (todo.size() > 0) {
				action = todo.remove(0);
				ia = (action.player instanceof NPC);
				if(!ia)	
					Woz.writeMsg("####");
				processCommand(action.command, action.player);
				if(!ia)	
					Woz.writeMsg("####");
			}
		} finally {
			lock.unlock();
		}

	}

	public boolean addCommandToProcess(Player p, Command c) {
		boolean quit = true;
		if(c.getCommandWord().equals("quit"))
		{
			quit = true;
			lock.lock();
			try {
				running = false;
			} finally {
				lock.unlock();
			}
		}else{
			CommandToProcess ctp = new CommandToProcess(p, c);
			lock.lock();
			try {
				todo.add(ctp);
			} finally {
				lock.unlock();
			}
		}
		return quit;
	}

	
	private boolean processCommand(Command command, Player p) {
		String commandWord = command.getCommandWord();
		String paramWord = command.getSecondWord();
		
		
		if (actions.contains(commandWord) && !commandWord.equals("unknown")) {
			Method method = null;
			try {
				method = Actions.class.getDeclaredMethod(commandWord,
						String.class, Player.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				return (boolean) method.invoke(this, paramWord, p);
			} catch (Exception e) {
				System.err.println("Action :" + commandWord + " " + paramWord);
				e.printStackTrace();
			}
		} else
			System.err.println("I don't know what you mean." + "\n"
					+ "Availbe actions : " + stringAvaibleActions());
		return false;
	}

	private String stringAvaibleActions() {
		String result = "";
		;
		for (String action : actions) {
			result += action + ", ";
		}
		return (result.length() > 2) ? result.substring(0, result.length() - 2)
				: result;
	}


	// implementations of user commands:
	private void printInventory(Command command, Player p) {
		Woz.writeMsg(Game.getConst().get("you_carry") + ": "
				+ p.getInventoryContent());
	}

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void printHelp() {
		Woz.writeMsg((String)Game.getConst().get("help_intro"));
		Woz.writeMsg((String)Game.getConst().get("your_command_word"));
		
		
		System.err.println("TODO : show command");//parser.showCommands();
	}

	/**
	 * Try to go in one direction. If there is an exit, enter the new rooms,
	 * otherwise print an error message.
	 */
	public boolean go(String command, Player p) {		
		if (command == null) {
			Woz.writeMsg((String) Game.getConst().get("go_where"));
			Woz.writeMsg(p.getCurrentRoom().getExitString());
			return false;
		}

		Room nextRoom = p.getCurrentRoom().getExit(command);
		

		
		if (nextRoom == null) {
			Woz.writeMsg((String) Game.getConst().get("no_door"));
		} else if (nextRoom.canEnter(p)) {
			if(p.getCurrentRoom().leave(p))
			{
				p.enter(nextRoom);
				return true;
			}
			
		}
		
		return false;
	}

	/**
	 * Method allowing the player to drop an item in the current room.
	 * 
	 * @param command
	 */
	public boolean dropItem(String command, Player p) {
		if (command == null) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg((String)Game.getConst().get("what_drop"));
			return false;
		}
		// Try to drop item in the current rooms.
		if (p.dropItem(p.getCurrentRoom(), command)) {
			Woz.writeMsg(Game.getConst().get("ok_drop") + ": " + command);
		} else {
			Woz.writeMsg(Game.getConst().get("you_not_carry") + ": "
					+ command);
			Woz.writeMsg(Game.getConst().get("you_carry") + ": "
					+ p.getInventoryContent());
		}
		
		return true;
	}

	/**
	 * Method allowing you to pick an item in the current room
	 * 
	 * @param command
	 *            item name
	 */
	public boolean pickItem(String command, Player p) {
		Room r = p.getCurrentRoom();
		
		if (command == null) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg((String)Game.getConst().get("what_pick"));
			Woz.writeMsg(r.getItemString());
			return false;
		}

		// Try to pick item in the current rooms.
		if (r.hasItem(command)) {
			p.pickUp(r, command);
			Woz.writeMsg(Game.getConst().get("ok_pick") + ": " + command);
		} else {
			Woz.writeMsg(Game.getConst().get("no") + command);
			Woz.writeMsg(r.getItemString());
		}
		
		return true;
	}

	/**
	 * Method allowing you to use an item in the current room
	 * 
	 * @param command
	 *            item name
	 */
	public boolean use(String command, Player p) {
		if (command == null) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg((String)Game.getConst().get("what_use"));
			return false;
		}

		// Try to use item in the current rooms.
		if (p.getCurrentRoom().canUseItem(command)) {
			Woz.writeMsg(p.use(command));
		} else {
			Woz.writeMsg((String)Game.getConst().get("no_use_item_here"));
		}
		
		return false;
	}

	/**
	 * method allowing you to do some actions in different rooms
	 * 
	 * @param command
	 *            full command to parse
	 */
	
	public boolean action(String command, Player p) {
		if (command == null) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg((String) Game.getConst().get("what_do"));
			Woz.writeMsg(p.getCurrentRoom().getActionString());
			return false;
		}
		return addCommandToProcess(p, new Command("action", command));
	}

	/**
	 * method allowing you to answer question in some room as LabRoom or
	 * ExamRoom
	 * 
	 * @param command
	 *            the full command to parse
	 */
	private void answerQuestion(Command command, Player p) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know...
			Woz.writeMsg((String)Game.getConst().get("what_answer"));
			return;
		}

		String answer = command.getSecondWord();
		p.getCurrentRoom().study(answer);
	}

	/**
	 * "Quit" was entered. Check the rest of the command to see whether we
	 * really quit the game.
	 * 
	 * @return true, if this command quits the game, false otherwise.
	 */
	public boolean quit(String command, Player p) {
		//finished = true;
		return true;
	}

	public class CommandToProcess {
		private Player player;
		private Command command;

		CommandToProcess(Player p, Command c) {
			player = p;
			command = c;
		}

		public Player getPlayer() {
			return player;
		}

		public Command getCommand() {
			return command;
		}
	}

	private class Processor implements Runnable {
		
		@Override
		public void run() {
			boolean ok = true;
			
			while(ok)
			{
				lock.lock();
				try {
					executeOne();
					ok = running;
				} finally {
					lock.unlock();
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}			
		}
	}
}
