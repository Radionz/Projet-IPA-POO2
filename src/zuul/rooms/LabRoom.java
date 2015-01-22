package zuul.rooms;

import java.util.ArrayList;

import zuul.Game;
import zuul.entities.Player;
import zuul.entities.items.LabPaper;
import zuul.studies.Lab;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class LabRoom extends Room {

	private Lab lab;

	public Lab getLab() {
		return this.lab;
	}

	/**
	 * Create a rooms described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The rooms's description.
	 */
	public LabRoom(String description) {
		super(description);
		actions = new ArrayList<String>();
		lab = null;
	}

	public void enter(Player player) {
		lab = player.getRandomLab();
		System.out.println(getLongDescription());

		if (lab == null) {
			System.out.println("You must learn the proper lesson");
			return;
		} else if (lab.isPoo()) {
			System.out.println("Lab of POO, you must do the lab!");
			lab(player);
		} else {
			actions.add("lab");
		}

	}

	/**
	 * method dynamically called action to perform in ClassRoom
	 * 
	 * @param player
	 * 
	 * @returnthe string of the lesson
	 */
	public boolean lab(Player player) {
		if (actions.contains("lab")) {
			actions.remove("lab");
			if (lab.takeALab() >= 10) {
				return player.addLab(new LabPaper(lab, 0));
			} else
				System.out.println("You have not passed the lab");
		}
		System.out.println("This lab is complete, you can exit the room.");
		return false;
	}
}
