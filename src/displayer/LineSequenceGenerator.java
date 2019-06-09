package displayer;

import java.util.ArrayList;

import main.*;

public class LineSequenceGenerator {
	
	public static ArrayList<ArrayList<Point>> makeAbsoluteLinesFrom (Passage p) {
		ArrayList<ArrayList<Point>> returnee = new ArrayList<ArrayList<Point>> ();
		
		// Make the doors
		for (Point d : p.doors) {
			ArrayList<Point> door = new ArrayList<Point> ();
			
			door.add(new Point (p.origin.x + d.x, p.origin.y + d.y + 1));
			door.add(new Point (p.origin.x + d.x + 1, p.origin.y + d.y + 1));
			door.add(new Point (p.origin.x + d.x + 1, p.origin.y + d.y + 3));
			door.add(new Point (p.origin.x + d.x, p.origin.y + d.y + 3));
			door.add(new Point (p.origin.x + d.x, p.origin.y + d.y + 1));
			
			returnee.add(door);
		}
		
		int pl = p.passagePoints.length;
		
		// Make the roof of the passage
		ArrayList<Point> topSeq = new ArrayList<Point> ();
		
		for (int n = 0; n < pl; n++) {
			Point pas = p.passagePoints[n];
			int offset = 0;
			
			if (n < pl-1 && p.passagePoints[n+1].y > p.passagePoints[n].y) offset = 1;
			if (n > 0 && p.passagePoints[n].y > p.passagePoints[n-1].y) offset = 1;
			if (n < pl-1 && p.passagePoints[n+1].y < p.passagePoints[n].y) offset = -1;
			if (n > 0 && p.passagePoints[n].y < p.passagePoints[n-1].y) offset = -1;
			
			topSeq.add(new Point (p.origin.x + pas.x + offset, p.origin.y + pas.y));
		}
		
		returnee.add(topSeq);
		
		// Make the floor of the passage
		ArrayList<Point> bottomSeq = new ArrayList<Point> ();
		
		for (int n = 0; n < pl; n++) {
			Point pas = p.passagePoints[n];
			

			if (n < pl-1) {
				float dif = Math.abs(pas.y - p.passagePoints[n+1].y);
				if (dif > 0) {
					for (int i = 0,j = 0; i <= dif*2; i++) {
						if (i > 0) j = i-1;
						bottomSeq.add(new Point (p.origin.x + pas.x + ((float)i / 2), p.origin.y + pas.y + 3 + ((float)j / 2)));
						bottomSeq.add(new Point (p.origin.x + pas.x + ((float)i / 2), p.origin.y + pas.y + 3 + ((float)i / 2)));
					}
				} else {
					bottomSeq.add(new Point (p.origin.x + pas.x, p.origin.y + pas.y + 3));
				}
			} else {
				bottomSeq.add(new Point (p.origin.x + pas.x, p.origin.y + pas.y + 3));
			}
			
			
		}
		
		returnee.add(bottomSeq);
		
		// Add the closed left wall
		if (p.closedLeft) {
			ArrayList<Point> leftSeq = new ArrayList<Point> ();
			
			leftSeq.add(new Point (p.origin.x + p.passagePoints[0].x, p.origin.y + p.passagePoints[0].y));
			leftSeq.add(new Point (p.origin.x + p.passagePoints[0].x, p.origin.y + p.passagePoints[0].y + 3));
			
			returnee.add(leftSeq);
		}
		
		// Add the closed right wall
		if (p.closedRight) {
			ArrayList<Point> rightSeq = new ArrayList<Point> ();
			
			int lastInd = p.passagePoints.length-1;
			
			rightSeq.add(new Point (p.origin.x + p.passagePoints[lastInd].x, p.origin.y + p.passagePoints[lastInd].y));
			rightSeq.add(new Point (p.origin.x + p.passagePoints[lastInd].x, p.origin.y + p.passagePoints[lastInd].y + 3));
			
			returnee.add(rightSeq);
		}
		
		return returnee;	
	}
}
