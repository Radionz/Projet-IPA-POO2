package zuul;

import zuul.gui.Window;

/**
 * @author Nicolas Sarroche, Dorian Blanc
 */
public class Woz {
	private static Window w;
	private static boolean debug = false;
	
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
	
    public static void main(String[] args){
    	
    	GameManager gm = new GameManager();    	
    	Game game = new Game(gm);
    	
    	w = new Window(game);    	
    	w.setVisible(true);
    	
    	
		game.play();
		
		w.dispose();
	}
}