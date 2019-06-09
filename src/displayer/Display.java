package displayer;

import main.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Display extends JFrame {

	public static void main(String[] args) {
		Display d = new Display ();
		d.setVisible(true);
		d.setSize(new Dimension (500, 500));
	}
	
	int size = 20;
	
	@Override
	public void paint (Graphics g) {
		g.setColor(Color.BLACK);
		
		Passage p = new Passage ();
		p.origin = new Point (4, 4);
		p.closedLeft = true;
		p.closedRight = false;
		Point[] ps = {new Point (0, 0), new Point (3, 0), new Point (6, 3), new Point (12, 3), new Point (15, 6), new Point (17, 6)};
		Point[] ds = {new Point (1, 0)};
		p.passagePoints = ps;
		p.doors = ds;
		
		ArrayList<ArrayList<Point>> drawable = LineSequenceGenerator.makeAbsoluteLinesFrom(p);
		
		for (ArrayList<Point> arr : drawable) {
			for (int i = 0; i < arr.size()-1; i++) {
				int m = i + 1;
				g.drawLine((int)(arr.get(i).x * size), (int)(arr.get(i).y * size), (int)(arr.get(m).x * size), (int)(arr.get(m).y * size));
			}
		}
	}

}
