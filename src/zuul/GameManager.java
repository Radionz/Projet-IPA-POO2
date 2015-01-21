package zuul;

import java.util.ArrayList;
import java.util.concurrent.locks.*;

import zuul.entities.Player;
import zuul.io.Command;
import zuul.io.CommandWord;
import zuul.io.Parser;
import zuul.rooms.Room;

public class GameManager {
	private ArrayList<CommandToProcess> todo;
	private boolean running;
	public Lock lock;
	private Parser parser;

	public GameManager() {
		todo = new ArrayList<CommandToProcess>();
		lock = new ReentrantLock();
		parser = new Parser();
		running = true;
		new Thread(new Processor()).start();
	}

	private void executeOne() {
		CommandToProcess action = null;
		lock.lock();
		try {
			if (todo.size() > 0) {
				action = todo.remove(0);
				processCommand(action.command, action.player);
			}
		} finally {
			lock.unlock();
		}

	}

	public boolean addCommandToProcess(Player p, Command c) {
		boolean quit = true;
		if(c.getCommandWord() == CommandWord.QUIT)
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

	private void processCommand(Command command, Player p) {

		CommandWord commandWord = command.getCommandWord();

		switch (commandWord) {
		case UNKNOWN:
			Woz.writeMsg(Game.getConst().get("dont_understand"));
			break;

		case HELP:
			printHelp();
			break;

		case GO:
			goRoom(command, p);
			break;

		case DO:
			doSomething(command, p);
			break;

		case DROP:
			dropItem(command, p);
			break;

		case PICK:
			pickItem(command, p);
			break;

		case USE:
			useItem(command, p);
			break;

		case INVENTORY:
			printInventory(command, p);
			break;

		case ANSWER:
			answerQuestion(command, p);
			break;

		case QUIT:
			lock.lock();
			try{
				running = quit(command, p);
			}finally{
				lock.unlock();
			}			
			break;

		}
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
		Woz.writeMsg(Game.getConst().get("help_intro"));
		Woz.writeMsg(Game.getConst().get("your_command_word"));
		parser.showCommands();
	}

	/**
	 * Try to go in one direction. If there is an exit, enter the new rooms,
	 * otherwise print an error message.
	 */
	private void goRoom(Command command, Player p) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg(Game.getConst().get("go_where"));
			Woz.writeMsg(p.getCurrentRoom().getExitString());
			return;
		}
		String direction = command.getSecondWord();

		// Try to leave current rooms.
		Room nextRoom = p.getCurrentRoom().getExit(direction);

		if (nextRoom == null) {
			Woz.writeMsg(p.getName() + ", "
					+ Game.getConst().get("no_door"));
		} else if (p.getCurrentRoom().isHidden()
				|| (nextRoom.canEnter(p) && p.leaveRoom())) {
			p.enter(nextRoom);
		}
	}

	/**
	 * Method allowing the player to drop an item in the current room.
	 * 
	 * @param command
	 */
	private void dropItem(Command command, Player p) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg(Game.getConst().get("what_drop"));
			return;
		}

		String itemName = command.getSecondWord();

		// Try to drop item in the current rooms.
		if (p.dropItem(p.getCurrentRoom(), itemName)) {
			System.out
					.println(Game.getConst().get("ok_drop") + ": " + itemName);
		} else {
			Woz.writeMsg(Game.getConst().get("you_not_carry") + ": "
					+ itemName);
			Woz.writeMsg(Game.getConst().get("you_carry") + ": "
					+ p.getInventoryContent());
		}
	}

	/**
	 * Method allowing you to pick an item in the current room
	 * 
	 * @param command
	 *            item name
	 */
	private void pickItem(Command command, Player p) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg(Game.getConst().get("what_pick"));
			Woz.writeMsg(p.getCurrentRoom().getItemString());
			return;
		}

		String itemName = command.getSecondWord();

		// Try to pick item in the current rooms.
		if (p.getCurrentRoom().hasItem(itemName)) {
			p.pickUp(p.getCurrentRoom(), itemName);
			System.out
					.println(Game.getConst().get("ok_pick") + ": " + itemName);
		} else {
			Woz.writeMsg(Game.getConst().get("no") + itemName);
			Woz.writeMsg(p.getCurrentRoom().getItemString());
		}
	}

	/**
	 * Method allowing you to use an item in the current room
	 * 
	 * @param command
	 *            item name
	 */
	private void useItem(Command command, Player p) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg(Game.getConst().get("what_use"));
			return;
		}

		String itemName = command.getSecondWord();

		// Try to use item in the current rooms.
		if (p.getCurrentRoom().canUseItem(itemName)) {
			Woz.writeMsg(p.use(itemName));
		} else {
			Woz.writeMsg(Game.getConst().get("no_use_item_here"));
		}
	}

	/**
	 * method allowing you to do some actions in different rooms
	 * 
	 * @param command
	 *            full command to parse
	 */
	private void doSomething(Command command, Player p) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			Woz.writeMsg(Game.getConst().get("what_do"));
			Woz.writeMsg(p.getCurrentRoom().getActionString());
			return;
		}

		String mehtod = command.getSecondWord();

		// Try to use item in the current rooms.
		Woz.writeMsg(p.getCurrentRoom().doSomething(p, mehtod));
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
			Woz.writeMsg(Game.getConst().get("what_answer"));
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
	private boolean quit(Command command, Player p) {
		if (command.hasSecondWord()) {
			Woz.writeMsg(Game.getConst().get("what_quit"));
			return false;
		} else {
			return true; // signal that we want to quit
		}
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
					//System.out.println("FOO");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
					
			
		}

	}

}
