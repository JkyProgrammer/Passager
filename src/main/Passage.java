package main;

import java.util.ArrayList;

public class Passage {
	public ArrayList<Point> doors = new ArrayList<Point>();
	public ArrayList<Point> passagePoints = new ArrayList<Point>();
	public ArrayList<Ladder> ladders = new ArrayList<Ladder> ();
	
	public boolean closedLeft = true;
	public boolean closedRight = true;
	
	public void appendPassagePoint (Point element) {
		if (!passagePoints.contains(element))
			passagePoints.add(element);
	}
	
	public void addDoor (Point p) {
		if (!passagePoints.contains(p))
			doors.add (p);
	}
	
	private ArrayList<Point> cloneArray (ArrayList<Point> pts) {
		ArrayList<Point> newArr = new ArrayList<Point> ();
		
		for (Point p : pts) {
			newArr.add(p.duplicate());
		}
		
		return newArr;
	}
	
	// TODO: Custom decorations
	// TODO: Rooms
	
	public Passage duplicate () {
		Passage dup = new Passage ();
		dup.closedLeft = closedLeft;
		dup.closedRight = closedRight;
		dup.doors = cloneArray (doors);
		dup.passagePoints = cloneArray (passagePoints);
		
		return dup;
	}
	
	public Bounds getBounds () {
		Bounds b = new Bounds (0, 0, 0, 0);
		b.x = (int) passagePoints.get(0).x;
		b.y = (int) passagePoints.get(0).y - 3;
		b.width = (int) passagePoints.get(passagePoints.size()-1).x - b.x;
		b.height = (int) passagePoints.get(passagePoints.size()-1).y - b.y;
		
		for (Point p : passagePoints) {
			if (p.y-3 < b.y) b.y = (int) (p.y-3);
			if (p.y - b.y > b.height) b.height = (int) (p.y-b.y);
		}
		
		
		
		return b;
	}

	public void addLadder (Point point, int i) {
		ladders.add(new Ladder (point, i));
	}
	
	public void removeLadder (Point p) {
		for (int i = 0; i < ladders.size(); i++) {
			Point pnt = ladders.get(i).origin;
			if (pnt.equals(p)) ladders.remove (i);
		}
	}
	
	public void removePassagePoint (Point p) {
		for (int i = 0; i < passagePoints.size(); i++) {
			Point pnt = passagePoints.get(i);
			if (pnt.equals(p)) passagePoints.remove (i);
		}
	}
	
	public void removeDoor (Point p) {
		for (int i = 0; i < doors.size(); i++) {
			Point pnt = doors.get(i);
			if (pnt.equals(p)) doors.remove (i);
		}
	}
}
