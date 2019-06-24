package main;

public class Bounds {
	public int x;
	public int y;
	public int width;
	public int height;

	public Bounds (int xx, int yy, int w, int h) {
		x = xx;
		y = yy;
		width = w;
		height = h;
	}

	public boolean contains (Point point) {
		if (point.x < x) return false;
		if (point.y < y) return false;
		if (point.x > x + width) return false;
		if (point.y > y + height) return false;
		return true;
	}
	
	public String toString () {
		return "X: " + x + " Y: " + y + " W: " + width + " H: " + height;
	}
}