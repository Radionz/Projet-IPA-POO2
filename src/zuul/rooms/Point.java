package zuul.rooms;

public class Point {
	private int x;
	private int y;
	
	public Point() {
		this(0,0);
	}
	
	public Point(int x, int y) {
		this.setLocation(x,y);
	}
	
	public Point(double x, double y) {
		this((int) x,(int) y);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setLocation(double x, double y) {
		this.setLocation((int) x,(int) y);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
}
