package zuul;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Woz {

    public static void main(String[] args){
    	GameManager gm = new GameManager();
    	
    	Game game = new Game(gm);
		game.play();
		
    }
}