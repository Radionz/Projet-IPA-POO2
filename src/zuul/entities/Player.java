package zuul.entities;

import zuul.Game;
import zuul.entities.items.*;
import zuul.rooms.Room;
import zuul.studies.Exam;
import zuul.studies.Lab;
import zuul.studies.Lesson;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Player {

	protected final String name;
	protected ArrayList<Item> inventory;
	protected ArrayList<LessonPaper> knowledges;
	protected ArrayList<LabPaper> abilities;
	protected ArrayList<ExamPaper> examsPassed;
	protected Badge badge;
	protected Room currentRoom;
	
	protected boolean studying;

	public String getName() {
		return name;
	}

	public Player(String playerName, Room room) {
		this.name = playerName;
		this.badge = new Badge();
		this.inventory = new ArrayList<Item>();
		this.currentRoom = room;
		this.studying = false;
		knowledges = new ArrayList<LessonPaper>();
		abilities = new ArrayList<LabPaper>();
		examsPassed = new ArrayList<ExamPaper>();
	}
	
	public boolean isStudying(){ return studying; }
	public void setStudying(boolean state) { studying = state; }

	public boolean addItem(Item item) {
		if (!inventory.contains(item)) {
			inventory.add(item);
			return true;
		}
		return false;
	}

	public boolean addLesson(LessonPaper lesson) {
		if (!knowledges.contains(lesson)) {
			knowledges.add(lesson);
			return true;
		}
		return false;
	}

	public boolean addLab(LabPaper lab) {
		if (!abilities.contains(lab)) {
			abilities.add(lab);
			return true;
		}
		return false;
	}

	public boolean addExam(ExamPaper exam) {
		if (!examsPassed.contains(exam)) {
			examsPassed.add(exam);
			return true;
		}
		return false;
	}

	/**
	 * Method allowing the player to pickup an item on the ground and to put it
	 * in is own inventory
	 * 
	 * @param item
	 *            Item being pick up
	 * @return If the item has successfully been added or not
	 */
	public boolean pickUp(Room room, Item item) {
		if (room != null && item != null) {
			room.getItemsList().remove(item);
			return this.inventory.add(item);
		}
		return false;
	}

	/**
	 * Method allowing the player to pick up an item
	 * 
	 * @param room
	 *            the current room where to pick the item
	 * @param itemString
	 *            the item name
	 * @return boolean if the action succeed
	 */
	public boolean pickUp(Room room, String itemString) {
		if (room != null && itemString != null) {
			for (Item item : room.getItemsList()) {
				if (item.getName().equals(itemString)) {
					room.getItemsList().remove(item);
					return this.inventory.add(item);
				}
			}
		}
		return false;
	}

	public boolean forgetALesson() {
		Random random = new Random();
		int aRetirer = knowledges.get(random.nextInt(knowledges.size()))
				.getId();
		removeLessonPaper(aRetirer);
		removeLabPaper(aRetirer);
		removeExamPaper(aRetirer);
		return false;
	}

	protected void removeExamPaper(int aRetirer) {
		for (ExamPaper exam : examsPassed) {
			if (exam.getId() == aRetirer)
				examsPassed.remove(aRetirer);
		}

	}

	protected void removeLabPaper(int aRetirer) {
		for (LabPaper lab : abilities) {
			if (lab.getId() == aRetirer)
				abilities.remove(aRetirer);
		}

	}

	protected void removeLessonPaper(int aRetirer) {
		for (LessonPaper lesson : knowledges) {
			if (lesson.getId() == aRetirer)
				knowledges.remove(aRetirer);
		}
	}

	public Lab getRandomLab() {
		if (!knowledges.isEmpty()) {
			Random random = new Random();
			int aPrendre = knowledges.get(random.nextInt(knowledges.size()))
					.getId();
			for (Lab lab : Game.getLabs()) {
				if (lab.getId() == aPrendre) {
					return lab;
				}
			}
		}
		return null;
	}

	public Exam getRandomExam() {
		if (!examsPassed.isEmpty()) {
			Random random = new Random();
			int aPrendre = examsPassed.get(random.nextInt(examsPassed.size()))
					.getId();
			for (Exam exam : Game.getExams()) {
				if (exam.getId() == aPrendre) {
					return exam;
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Player))
			return false;
		return ((Player) obj).getName().equals(name);

	}

	public String use(String itemString) {
		for (Item item : inventory) {
			if (item.getName().equals(itemString)) {
				String effect = inventory.get(inventory.indexOf(item)).use();
				inventory.remove(inventory.indexOf(item));
				return effect;
			}
		}
		return "Player don't have this item.";
	}

	/**
	 * method adding a lab to players abilities
	 * 
	 * @param lab
	 *            Lab object to add
	 */
	public void improveAbilities(Lab lab) {
		badge.addLab(lab);
	}

	public void gainAmountEnergy(int energy) {
		badge.addEnergy(energy);

	}

	/**
	 * show player's inventory
	 * 
	 * @return String of the inventory
	 */
	public String getInventoryContent() {
		String res = "";
		for (Item item : inventory) {
			res += item.getName() + " - ";
		}
		return (res.length() > 3) ? res.substring(0, res.length() - 3) : res;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	/**
	 * Method allowing you to drop an item
	 * 
	 * @param room
	 *            the current room
	 * @param itemString
	 *            String name of the item
	 * @return
	 */
	public boolean dropItem(Room room, String itemString) {
		if (room != null && itemString != null) {
			for (Item item : inventory) {
				if (item.getName().equals(itemString)) {
					room.addItem(item);
					return this.inventory.remove(item);
				}
			}
		}
		return false;
	}
	
	public void enter(Room room){ 
		currentRoom = room; 
		room.enter(this);
	}
	
	public boolean leaveRoom()
	{
		return currentRoom.leave(this);			
	}

}
