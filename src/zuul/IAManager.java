package zuul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import zuul.entities.IA;
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
	
	public static final int IA_SLEEP_TIME = 100;
	
	private Game game;
	
	public IAManager(GameManager gm, ArrayList<Room> rooms)
	{
		this.manager = gm;
		this.rooms 		= rooms;
		this.listIA 	= new ArrayList<IA>();
		
		
		addIA(new IA("Sara"));
//		addIA(new IA("Dorian le violent"));
//		addIA(new IA("Toto"));
//		addIA(new IA("Jean Gui"));
//		addIA(new IA("Michelle"));
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
		boolean canAnswer = currentRoom.canUseAnswerCommand();
		Command learn = ia.learn();
		Command act = ia.generateDoCommand();
		Command go = ia.generateValidGoCommand();
		
		ArrayList<Command> possibleCmd = new ArrayList<Command>();

		if(learn != null)
			possibleCmd.add(learn);
		
		if(canAnswer)
			possibleCmd.add(ia.getAnswerCommand());
		
		if(act != null)
			possibleCmd.add(act);
		
		if(go != null)
			possibleCmd.add(go);		
			
		return 	possibleCmd.get(RandomGenerator.nextInt(possibleCmd.size()));
	}
	
	
	
	
	
	
	
	public boolean runIA()
	{		
		HashMap<IA, Command> toProcess = generateCommandForIAs();
		for(IA ia : toProcess.keySet())
		{
			if(!manager.addCommandToProcess(ia, toProcess.get(ia)))
				return false;
			
			try {	Thread.sleep(IA_SLEEP_TIME);	
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
		
		while((running = runIA()));

	}
}
