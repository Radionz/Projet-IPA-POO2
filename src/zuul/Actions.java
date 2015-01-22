package zuul;

import zuul.entities.Player;

public interface Actions {
	boolean go(String command, Player p);
	boolean action(String command, Player p);
	boolean dropItem(String command, Player p);
	boolean pickItem(String command, Player p);
	boolean use(String command, Player p);
	boolean quit(String command, Player p);
}
