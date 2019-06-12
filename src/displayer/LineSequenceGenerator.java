package displayer;

import java.util.ArrayList;

import main.*;

public class LineSequenceGenerator {
	
	public static ArrayList<ArrayList<Point>> makeAbsoluteLinesFrom (Passage p) {
		ArrayList<ArrayList<Point>> returnee = new ArrayList<ArrayList<Point>> ();
		
		if (p.passagePoints.size() < 2) return returnee;
		
		// Make the doors
		for (Point d : p.doors) {
			ArrayList<Point> door = new ArrayList<Point> ();
			
			door.add(new Point (p.origin.x + d.x, p.origin.y + d.y - 2));
			door.add(new Point (p.origin.x + d.x + 1, p.origin.y + d.y - 2));
			door.add(new Point (p.origin.x + d.x + 1, p.origin.y + d.y));
			door.add(new Point (p.origin.x + d.x, p.origin.y + d.y));
			door.add(new Point (p.origin.x + d.x, p.origin.y + d.y - 2));
			
			returnee.add(door);
		}
		
		int pl = p.passagePoints.size();
		
		// Make the roof of the passage
		ArrayList<Point> topSeq = new ArrayList<Point> ();
		
		for (int n = 0; n < pl; n++) {
			Point pas = p.passagePoints.get(n);
			int offset = 0;
			
			if (n < pl-1 && p.passagePoints.get(n+1).y > p.passagePoints.get(n).y) {
				offset = 1;
				if (Math.abs(pas.x - p.passagePoints.get(n+1).x) == 0) {
					offset = 3;
				}
			}
			if (n > 0 && p.passagePoints.get(n).y > p.passagePoints.get(n-1).y) {
				offset = 1;
				if (Math.abs(pas.x - p.passagePoints.get(n-1).x) == 0) {
					offset = 3;
				}
			}
			if (n < pl-1 && p.passagePoints.get(n+1).y < p.passagePoints.get(n).y) {
				offset = -1;
				if (Math.abs(pas.x - p.passagePoints.get(n+1).x) == 0) {
					offset = -3;
				}
			}
			if (n > 0 && p.passagePoints.get(n).y < p.passagePoints.get(n-1).y) {
				offset = -1;
				if (Math.abs(pas.x - p.passagePoints.get(n-1).x) == 0) {
					offset = -3;
				}
			}
			
			topSeq.add(new Point (p.origin.x + pas.x + offset, p.origin.y + pas.y - 3));
		}
		
		returnee.add(topSeq);
		
		// Make the floor of the passage
		ArrayList<Point> bottomSeq = new ArrayList<Point> ();
		
		for (int n = 0; n < pl; n++) {
			Point pas = p.passagePoints.get(n);
			

			if (n < pl-1) {
				float dif = (pas.y - p.passagePoints.get(n+1).y);
				if (dif != 0 && Math.abs(pas.x - p.passagePoints.get(n+1).x) > 0) {
					if (dif > 0) {
						for (int i = 0,j = 0; i <= dif*2; i++) {
							j = i + 1;
							if (i == dif*2) j = i;
							bottomSeq.add(new Point (p.origin.x + pas.x + ((float)i / 2), p.origin.y + pas.y - ((float)i / 2)));
							bottomSeq.add(new Point (p.origin.x + pas.x + ((float)i / 2), p.origin.y + pas.y - ((float)j / 2)));
						}
					} else {
						for (int i = 0,j = 0; i <= Math.abs(dif)*2; i++) {
							if (i > 0) j = i-1;
							bottomSeq.add(new Point (p.origin.x + pas.x + ((float)i / 2), p.origin.y + pas.y + ((float)j / 2)));
							bottomSeq.add(new Point (p.origin.x + pas.x + ((float)i / 2), p.origin.y + pas.y + ((float)i / 2)));
						}
					}
				} else {
					bottomSeq.add(new Point (p.origin.x + pas.x, p.origin.y + pas.y));
				}
			} else {
				bottomSeq.add(new Point (p.origin.x + pas.x, p.origin.y + pas.y));
			}
			
			
		}
		
		returnee.add(bottomSeq);
		
		// Add the closed left wall
		if (p.closedLeft) {
			ArrayList<Point> leftSeq = new ArrayList<Point> ();
			
			leftSeq.add(new Point (p.origin.x + p.passagePoints.get(0).x, p.origin.y + p.passagePoints.get(0).y - 3));
			leftSeq.add(new Point (p.origin.x + p.passagePoints.get(0).x, p.origin.y + p.passagePoints.get(0).y));
			
			returnee.add(leftSeq);
		}
		
		// Add the closed right wall
		if (p.closedRight) {
			ArrayList<Point> rightSeq = new ArrayList<Point> ();
			
			int lastInd = p.passagePoints.size()-1;
			
			rightSeq.add(new Point (p.origin.x + p.passagePoints.get(lastInd).x, p.origin.y + p.passagePoints.get(lastInd).y - 3));
			rightSeq.add(new Point (p.origin.x + p.passagePoints.get(lastInd).x, p.origin.y + p.passagePoints.get(lastInd).y));
			
			returnee.add(rightSeq);
		}
		
		return returnee;	
	}
}
