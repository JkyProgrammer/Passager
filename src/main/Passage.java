package main;

import java.util.ArrayList;

public class Passage {
	public Point origin = new Point (0, 0);
	public ArrayList<Point> doors = new ArrayList<Point>();
	public ArrayList<Point> passagePoints = new ArrayList<Point>();
	
	public boolean closedLeft = true;
	public boolean closedRight = true;
	
	public void appendPassagePoint (Point element, int place) {
	    passagePoints.add(place, element);
	}
	
	public void appendPassagePoint (Point element) {
	    passagePoints.add(element);
	}
	
	public void addDoor (Point p) {
		doors.add (p);
	}
}
