package zuul.rooms;

import java.util.ArrayList;

import zuul.Game;
import zuul.entities.Player;
import zuul.entities.items.ExamPaper;
import zuul.studies.Exam;


/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class ExamRoom extends Room {

	private Exam exam;

	public Exam getExam() {
		return this.exam;
	}

	/**
	 * Create a rooms described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The rooms's description.
	 */
	public ExamRoom(String description) {
		super(description);
		actions = new ArrayList<String>();
		exam = null;
	}

	public void enter(Player player) {
		exam = Game.getPlayer().getRandomExam();
		System.out.println(getLongDescription());
		if(exam == null)
			System.out.println("You must do the corresponding lab");
		else if (exam.isPoo()) {
			System.out.println("Exam of POO, you must do the exam!");
			exam(player);
		}else {
			actions.add("exam");
		}
	}

	/**
	 * method dynamically called action to perform in ClassRoom
	 * 
	 * @param player
	 * 
	 * @returnthe string of the lesson
	 */
	public boolean exam(Player player) {
		if (actions.contains("exam")) {
			actions.remove("exam");
			if (exam.takeAnExam() >= 10) {
				return player.addExam(new ExamPaper(exam, 0));
			}else
				System.out.println("You have not passed the exam");
		}
		System.out.println("This exam is complete, you can exit the room.");
		return false;
	}
}
