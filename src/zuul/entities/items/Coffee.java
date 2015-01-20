package zuul.entities.items;

import zuul.Game;
/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Coffee extends Item{

	/**
	 * Class describing how coffee works
	 */

	/**
	 * public constructor for coffee
	 */
	public Coffee() {
		super("coffee", 1);
	}
	
	@Override
	public String use(){
		Game.getPlayer().gainAmountEnergy(this.energy);
		return "You won " + this.energy + " Energy !";
	}
}
