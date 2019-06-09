package displayer;

import main.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Display extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3383638120175546617L;

	public static void main(String[] args) {
		Display d = new Display ();
		d.setVisible(true);
		d.setSize(new Dimension (500, 500));
	}
	
	int size = 20;
	
	@Override
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		for (int i = 0; i < this.getWidth()/size; i++) {
			for (int j = 0; j < this.getHeight()/size; j++) {
				g2.drawOval((i*size) - 1, (j*size) - 1, 2, 2);
			}
		}
		
		Passage p = new Passage ();
		p.origin = new Point (1, 5);
		p.closedLeft = true;
		p.closedRight = false;
		Point[] ps = {new Point (0, 0), new Point (3, 0), new Point (6, 3), new Point (12, 3), new Point (15, 6), new Point (17, 6), new Point (20, 6), new Point (20, 12), new Point (25, 12)};
		Point[] ds = {new Point (1, 0)};
		p.passagePoints = ps;
		p.doors = ds;
		
		
		ArrayList<ArrayList<Point>> drawable = LineSequenceGenerator.makeAbsoluteLinesFrom(p);
		
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke (5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (ArrayList<Point> arr : drawable) {
			for (int i = 0; i < arr.size()-1; i++) {
				int m = i + 1;
				g2.drawLine((int)(arr.get(i).x * size), (int)(arr.get(i).y * size), (int)(arr.get(m).x * size), (int)(arr.get(m).y * size));
			}
		}
		
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke (1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = 0; i < p.passagePoints.length-1; i++) {
			Point pt = p.passagePoints[i];
			Point ptt = p.passagePoints[i+1];
			g2.drawLine((int)((pt.x + p.origin.x) * size), (int)((pt.y + p.origin.y) * size), (int)((ptt.x + p.origin.x) * size), (int)((ptt.y + p.origin.y) * size));
		}
	}

}
