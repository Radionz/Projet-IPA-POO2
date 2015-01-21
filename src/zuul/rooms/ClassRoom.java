package zuul.rooms;

import java.util.ArrayList;

import zuul.Game;
import zuul.entities.Player;
import zuul.studies.Course;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class ClassRoom extends Room {

	private Course[] lessons;
	private Course lesson;
	private int sentenceNb;

	/**
	 * Create a rooms described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The rooms's description.
	 */
	public ClassRoom(String description, boolean isPOO, int playerLevel) {
		super(description);
		this.sentenceNb = 0;
		this.lessons = Game.getLessons();
		this.lesson = lessons[0];
		this.actions = new ArrayList<String>();
		actions.add("learn");
	}

	public Course getLesson() {
		return this.lesson;
	}

	public void setLesson(Course lesson) {
		this.lesson = lesson;
	}

	/**
	 * method dynamically called action to perform in ClassRoom
	 * 
	 * @returnthe string of the lesson
	 */
	public String learn(Player player) {
		String returned = "";
		if (lesson.isPOO()) {
			returned += "You have to learn the course to the bitter end.\n";
		}
		actions.add("nextSentence");
		actions.remove("learn");
		return returned += displaySentences();
	}

	/**
	 * method dynamically called action to perform when the player ask for the
	 * next sentences
	 * 
	 * @return String of the lesson
	 */
	public String nextSentence() {
		return displaySentences();
	}

	/**
	 * specific exit for classRoom : depends on if it's a poo lesson, or not.
	 * 
	 * @param direction
	 *            The exit's direction.
	 * @return a room in a certain direction
	 */
	@Override
	public Room getExit(String direction) {
		if (lesson.isDone() || actions.contains("learn")) {
			if (lesson.isPOO() && lesson.isDone()) {
				Game.getPlayer().learn(lesson);
				sentenceNb = 0;
				this.lesson = lessons[Game.getPlayer().getKnowledges().size()];
				// Game.getPlayer().setCurrentPOOLevel(Game.getPlayer().getCurrentPOOLevel()+1);
			}
			if (!actions.contains("learn")) {
				actions.add("learn");
			}

			actions.remove("nextSentence");
			return exits.get(Exits.getAnExit(direction));
		}
		displaySentences();
		return null;
	}

	/**
	 * Return the n'the phrase of the lesson, to be display in the game part.
	 * 
	 * @return String of the n'th phrase of the lesson
	 */
	public String displaySentences() {
		String res = lesson.getSentence(sentenceNb);
		sentenceNb++;
		return res;
	}



}
