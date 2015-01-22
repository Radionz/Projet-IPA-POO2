package zuul;

import zuul.gui.Window;
import zuul.io.Parser;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Woz {
	private static Window w;
	private static boolean debug = false;
	private static String name = "";
	
	public static void writeMsg()
	{
		writeMsg("\n");
	}
	
	public static void writeMsg(boolean b)
	{
		if(b)
			writeMsg("true");
		else
			writeMsg("false");
	}
	
	public static void writeMsg(String msg)
	{
		if(debug)
			System.out.println(msg);
		else
			w.writeLine(msg);
	}

	public static String getPlayerName() {
		System.out.println((String) Game.getConst().get("welcome"));
		System.out.println((String) Game.getConst().get("invite_enter_name"));
		return new Parser().getPlayerName();
	}
	
	public static String getName()
	{
		return name;
	}
	
    public static void main(String[] args){
    	
    	
    	
    	GameManager gm = new GameManager();    	
    	Game game = new Game(gm);
    	
    	name = getPlayerName();
    	
    	w = new Window(game);    	
    	w.setVisible(true);
    	
    	
		game.play();
		
		w.dispose();
	}
}