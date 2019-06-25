package main;

public class Ladder {
	public Point origin;
	public int length;
	
	public Ladder (Point o, int l) {
		origin = o;
		length = l;
	}
	
	public String toString () {
		return origin.toString() + ", " + length;
	}
}
