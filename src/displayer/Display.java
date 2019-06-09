package displayer;

import main.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Display extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3383638120175546617L;

	public static void main(String[] args) {
		Display d = new Display ();
		d.setup();
	}
	
	Point mousePos = new Point (0,0);
	
	public void setup () {
		getContentPane().addMouseMotionListener(new MouseMotionListener () {
			@Override
			public void mouseDragged(MouseEvent e) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				int approxX = Math.round((float)e.getX() / (float)size);
				int approxY = Math.round((float)e.getY() / (float)size);
				mousePos.x = approxX;
				mousePos.y = approxY+1;
				Graphics2D g2 = (Graphics2D)getContentPane().getGraphics();
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 100, 11);
				g2.setColor(Color.black);
				g2.drawString(mousePos.toString(), 0, 10);
			}
		});
		
		getContentPane().addMouseListener(new MouseListener () {

			@Override
			public void mouseClicked(MouseEvent e) {
				p.appendPassagePoint(new Point (mousePos.x, mousePos.y));
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		setVisible(true);
		setSize(new Dimension (1000, 500));
	}
	
	int size = 20;
	
	Passage p = new Passage ();
	
	@Override
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		g2.setColor(Color.lightGray);
		for (int i = 0; i < this.getWidth()/size; i++) {
			for (int j = 0; j < this.getHeight()/size; j++) {
				g2.fillOval((i*size) - 1, (j*size) - 1, 2, 2);
			}
		}
		
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
		for (int i = 0; i < p.passagePoints.size()-1; i++) {
			Point pt = p.passagePoints.get(i);
			Point ptt = p.passagePoints.get(i + 1);
			g2.drawLine((int)((pt.x + p.origin.x) * size), (int)((pt.y + p.origin.y) * size), (int)((ptt.x + p.origin.x) * size), (int)((ptt.y + p.origin.y) * size));
		}
	}

}
