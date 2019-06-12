package main;

public class Point {
	public float x;
	public float y;
	
	public Point (float xa, float ya) {
		x = xa;
		y = ya;
	}
	
	public String toString () {
		return "X: " + x + " Y: " + y;
	}
	
	public Point duplicate () {
		return new Point (x, y);
	}
}
