package zuul.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import zuul.Game;
import zuul.GameManager;
import zuul.io.Command;
import zuul.rooms.Room;


/**
 * 
 * @author Adrien Boucher
 * 
 * Manage the IA's
 *	-> Make them act like a player
 */
public class IAManager implements Runnable{
	private ArrayList<IA> listIA;
	private ArrayList<Room> rooms;	
	private GameManager manager;
	
	private Game game;
	
	public IAManager(GameManager gm, ArrayList<Room> rooms)
	{
		this.manager = gm;
		this.rooms 		= rooms;
		this.listIA 	= new ArrayList<IA>();
		
		//addIA(new IA("Dorian le violent"));
		addIA(new IA("Toto"));
		//addIA(new IA("Jean Gui"));
	}
	
	public void addIA(IA npc){ listIA.add(npc); }
	
	public void initIA(Room r)
	{
		for(int i=0; i<listIA.size(); i++)
			listIA.get(i).enter(r);
	}
	
	
	public int getNbIA(){ return listIA.size(); }
	
	public IA getNextIA()
	{
		return listIA.get(new Random().nextInt(listIA.size()));
	}
	
	public HashMap<IA, Command> generateCommandForIAs()
	{		
		HashMap<IA, Command> result = new HashMap<IA, Command>();
		HashSet<Integer> iaNum = getRandomIANum();
		
		for(int i : iaNum)
		{
			IA ia = listIA.get(i);
			result.put(ia, getCommandForIA(ia));
		}
		
		return result;
	}
	
	public HashSet<Integer> getRandomIANum()
	{
		Random r = new Random();
		int nbIA = listIA.size(), nbAct = r.nextInt(nbIA)+1;
		HashSet<Integer> num = new HashSet<Integer>();
		
		for(int i=1; i<=nbAct; i++)
		{
			int n;
			do{ n = r.nextInt(nbIA);}while(num.contains(n));
			num.add(n);			
		}
		
		return num;
	}
	
	public Command getCommandForIA(IA ia)
	{
		Room currentRoom = ia.getCurrentRoom();
		boolean canDo = currentRoom.canUseDoCommand();
		//boolean canAnwser = currentRoom.canUseAnswerCommand();
		
//		if(canDo && Math.random() < 0.3)
//			return generateDoCommand(ia);
		
		return generateValidGoCommand(ia);		
	}
	
	public Command generateDoCommand(IA ia)
	{
		ArrayList<String> actions = ia.getCurrentRoom().getActions();
		String action = actions.get(new Random().nextInt(actions.size()));
		
		return new Command("action", action);
	}
	
	public Command generateValidGoCommand(IA ia)
	{
		return new Command("go", ia.getCurrentRoom().getRandomExitDirection());
	}
	
	public boolean runIA()
	{		
		HashMap<IA, Command> toProcess = generateCommandForIAs();
		for(IA ia : toProcess.keySet())
		{
			if(!manager.addCommandToProcess(ia, toProcess.get(ia)))
				return false;
			
			try {	Thread.sleep(100);	
			} catch (InterruptedException e) {	
				e.printStackTrace();	
			}
		}	
		
		return true;
	}

	@Override
	public void run() {
		boolean running = true;
		
		initIA(rooms.get(0));
		
		while((running = runIA()))
		{
//			//System.out.println("YOLO");
//			try {	Thread.sleep(100);	
//			} catch (InterruptedException e) {	
//				e.printStackTrace();	
//			}
		}
	}
}
