package zuul.entities;

/**
 * La classe Badge permet de stocker les informations du joueur en rapport avec le jeu,
 * c'est à dire son énergie, son niveau, ses cours et ses activités gagnées.
 * Le badge permet aussi au joueur d'accèder aux salles de son niveau ou inférieures.
 * @author © 2014 Jihane ASRI
 */

import java.util.*;
import java.io.Serializable;

import zuul.rooms.Room;
import zuul.studies.Course;
import zuul.studies.Exam;
import zuul.studies.Lab;

public class Badge implements Serializable {

	private static int NB = 50; // On augmente le niveau du joueur tous les NB
								// pts d'énergie -> doit être le même que dans
								// Salle!

	private int energy; // l'énergie totale
	private int level; // le niveau du joueur - dépend de l'énergie!
	private ArrayList<Course> knowledges;
	private ArrayList<Lab> abilities;
	private int currentPOOLevel;

	/**
	 * Constructeur de badges: prend en argument son énergie de départ. Les
	 * listes de cours et de TD et d'exams réussis sont initialisées vides.
	 */
	public Badge(int energy) {
		this.knowledges = new ArrayList<>();
		this.abilities = new ArrayList<>();
		this.currentPOOLevel = 0;
		this.energy = energy;
		level = energy / NB;
	}

	public Badge() {
		this(0);
	}

	/**
	 * Les accesseurs en lecture.
	 */
	public int getEnergy() {
		return energy;
	}

	public int getCurrentPOOLevel() {
		return currentPOOLevel;
	}
	
	public int getLevel() {
		return level;
	}

	public ArrayList<Course> getKnowledges() {
		return knowledges;
	}

	public ArrayList<Lab> getAbilities() {
		return abilities;
	}

	/**
	 * Les fonctions addEnergy et removeEnergy permettent de modifier l'énergie
	 * (et donc le niveau) du joueur.
	 * 
	 * @param n
	 *            de type int -- le nombre de points d'énergie ajoutés ouretirés
	 */
	public void addEnergy(int n) {
		energy = energy + n;
		level = energy / NB;
	}

	public void removeEnergy(int n) {
		energy = energy - n;
		level = energy / NB;
	}

	/**
	 * addCourse et removeCourse ajoutent ou retirent un cours donné au joueur.
	 * 
	 * @param newCours
	 *            de type Cours
	 */
	public void addCourse(Course course) {
		if (!knowledges.contains(course))
			knowledges.add(course);
	}

	public void removeCourse(Course course) {
		knowledges.remove(course);
	}
	
	public boolean forgetACourse() {
		if (!knowledges.isEmpty()) {
			int k = new Random().nextInt(this.knowledges.size());
			this.knowledges.remove(k);
			return true;
		}
		return false;
	}

	public void addLab(Lab lab) {
		if (!abilities.contains(lab))
			abilities.add(lab);
	}

	public void removeLab(Lab lab) {
		abilities.remove(lab);
	}

	/**
	 * La fonction entrerSalle indique au joueur s'il peut entrer dans une salle
	 * donnée grâce aux informations contenu dans ce badge: s'il a le niveau
	 * requis il peut entrer.
	 * 
	 * @param salle
	 *            de type Salle
	 */
	public boolean entrerSalle(Room room) {
		if (room.getLevel() <= level)
			return true;
		return false;
	}

	/**
	 * La fonction toString permet d'afficher les informations contenues dans le
	 * badge.
	 */
	public String toString() {
		return "";
	}

	public void setCurrentPOOLevel(int currentPOOLevel) {
		this.currentPOOLevel = currentPOOLevel;
	}

}
