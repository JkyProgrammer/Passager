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
	
	public static Point fromString (String s) {
		Point p = new Point (0, 0);
		String[] bits = s.split(" ");
		
		try {
			p.x = Float.parseFloat(bits[0]);
		} catch (Exception e) {
			
		}
		
		try {
			p.y = Float.parseFloat(bits[1]);
		} catch (Exception e) {
			
		}
		
		return p;
	}
}
