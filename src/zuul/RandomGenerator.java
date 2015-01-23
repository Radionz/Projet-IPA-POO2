package zuul;

import java.util.Random;

public class RandomGenerator {
	private static Random rand;
	
	public static void init()
	{
		rand = new Random(System.currentTimeMillis());
	}
	
	public static int nextInt(int max)
	{
		return rand.nextInt(max);
	}

}
