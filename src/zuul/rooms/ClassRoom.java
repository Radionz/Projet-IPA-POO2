package zuul.rooms;


import java.util.ArrayList;

import zuul.Game;
import zuul.Woz;
import zuul.entities.NPC;
import zuul.entities.Player;
import zuul.entities.items.LessonPaper;
import zuul.studies.Lesson;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class ClassRoom extends Room {

	private Lesson lesson;

	public Lesson getLesson() {
		return this.lesson;
	}

	/**
	 * Create a rooms described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The rooms's description.
	 */
	public ClassRoom(String description) {
		super(description);
		actions = new ArrayList<String>();
		lesson = null;
	}

	public void enter(Player player) {
		if (!actions.contains("learn")) {
			actions.add("learn");
		}
		lesson = Game.getRadomLesson();
		Woz.writeMsg(getLongDescription());
		if (lesson.isPoo()) {
			Woz.writeMsg("Lesson of POO, you must listen to this lesson !");
			learn(player);
		}
	}

	/**
	 * method dynamically called action to perform in ClassRoom
	 * 
	 * @param player
	 * 
	 * @returnthe string of the lesson
	 */
	public boolean learn(Player player) {
		if (actions.contains("learn")) {
			actions.remove("learn");
			if(!(player instanceof NPC))
				lesson.printLesson();
			else
				System.out.println("IA: " + player.getName() + " is Studying");
			return player.addLesson(new LessonPaper(lesson, 0));
		}
		Woz.writeMsg("This lesson is complete, you can exit the room.");
		return false;
	}
}
