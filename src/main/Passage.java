package main;

import java.util.ArrayList;

public class Passage {
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
	
	private ArrayList<Point> cloneArray (ArrayList<Point> pts) {
		ArrayList<Point> newArr = new ArrayList<Point> ();
		
		for (Point p : pts) {
			newArr.add(p.duplicate());
		}
		
		return newArr;
	}
	
	public Passage duplicate () {
		Passage dup = new Passage ();
		dup.closedLeft = closedLeft;
		dup.closedRight = closedRight;
		dup.doors = cloneArray (doors);
		dup.passagePoints = cloneArray (passagePoints);
		
		return dup;
	}
}
