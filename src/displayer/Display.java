package displayer;

import main.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

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
	
	JMenuItem mousePosLabel;
	
	public void setup () {
		passages.add (new Passage());
		getContentPane().addMouseMotionListener(new MouseMotionListener () {
			@Override
			public void mouseDragged(MouseEvent e) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				int approxX = Math.round((float)e.getX() / (float)size);
				int approxY = Math.round((float)e.getY() / (float)size);
				mousePos.x = approxX;
				mousePos.y = approxY+2;
//				Graphics2D g2 = (Graphics2D)getContentPane().getGraphics();
//				g2.setColor(Color.white);
//				g2.fillRect(0, 0, 100, 11);
//				g2.setColor(Color.black);
//				g2.drawString(mousePos.toString(), 0, 10);
				mousePosLabel.setText(mousePos.toString());
			}
		});
		
		getContentPane().addMouseListener(new MouseListener () {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
					passages.get(editingIndex).appendPassagePoint(new Point (mousePos.x, mousePos.y));
				if (SwingUtilities.isRightMouseButton(e))
					passages.get(editingIndex).addDoor(new Point (mousePos.x, mousePos.y));
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
		
		addKeyListener(new KeyListener () {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					if (passages.get(editingIndex).passagePoints.size() > 0) {
						passages.get(editingIndex).passagePoints.remove(passages.get(editingIndex).passagePoints.size()-1);
						repaint ();
					}
				}
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					passages.add (new Passage ());
					doPaintPassage (passages.get(editingIndex), false, true);
					editingIndex = passages.size()-1;
					repaint ();
					System.out.println("New path made");
				}
				
				if (e.getKeyChar() == '[') {
					if (editingIndex > 0) {
						doPaintPassage (passages.get(editingIndex), false, true);
						editingIndex -= 1;
						doPaintPassage (passages.get(editingIndex), true, true);
						repaint ();
					}
					
				}
				if (e.getKeyChar() == ']') {
					if (editingIndex < passages.size()-1) {
						doPaintPassage (passages.get(editingIndex), false, true);
						editingIndex += 1;
						doPaintPassage (passages.get(editingIndex), true, true);
						repaint ();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
		});
		
		
		// TODO: More tools!
		setupMenus ();
		
		setVisible(true);
		setSize(new Dimension (1000, 500));
		
		
	}
	
	// TODO: Passage repacker

	public void setupMenus () {
		JMenuBar mb = new JMenuBar ();
		JMenu fileMenu = new JMenu ("File");
		JMenu toolsMenu = new JMenu ("Tools");
		
		JMenuItem saveButton = new JMenuItem ("Save");
		saveButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				save (false);
			}
		});
		fileMenu.add(saveButton);
		
		JMenuItem saveAsButton = new JMenuItem ("Save As");
		saveAsButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				save (true);
			}
		});
		fileMenu.add(saveAsButton);
		
		JMenuItem openButton = new JMenuItem ("Open");
		openButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				open ();
			}
		});
		fileMenu.add(openButton);
		
		mousePosLabel = new JMenuItem (mousePos.toString());
		
		
		mb.add(fileMenu);
		mb.add(toolsMenu);
		mb.add(mousePosLabel);
		
		setJMenuBar (mb);
	}
	
	public void save (boolean as) {
		if (filePath == null || as) {
			JFileChooser chooser = new JFileChooser ();
			chooser.setFileFilter(new TextFilter ());
			chooser.showSaveDialog(this);
			File f = chooser.getSelectedFile();
			if (f == null) return;
			filePath = f.getAbsolutePath();
		}
		SaveOpenHandler.writeToFile(filePath, passages);
		setTitle (filePath);
	}
	
	public void open () {
		JFileChooser chooser = new JFileChooser ();
		chooser.setFileFilter(new TextFilter ());
		chooser.showOpenDialog(this);
		File f = chooser.getSelectedFile();
		if (f == null) return;
		filePath = f.getAbsolutePath();
		passages = SaveOpenHandler.openFromFile (filePath);
		editingIndex = 0;
		setTitle (filePath);
		repaint ();
	}
	
	
	String filePath = null;
	
	int size = 20;
	int editingIndex = 0;
		
	ArrayList<Passage> passages = new ArrayList<Passage> ();
	ArrayList<PassagePaintQueueItem> paintQueue = new ArrayList<PassagePaintQueueItem> ();
	
	
	public void doPaintPassage (Passage p, boolean d, boolean n) {
		paintQueue.add(new PassagePaintQueueItem (p, d, n));
	}
	
	
	@Override
	public void paint (Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// TODO: Further paint efficiency improvements
		if (paintQueue.isEmpty()) {
			System.out.println("Starting full repaint");
			g2.setColor(Color.white);
			
			g2.fillRect(0, 40, getWidth(), getHeight());
			
			g2.setColor(Color.lightGray);
			for (int i = 0; i < this.getWidth()/size; i++) {
				for (int j = 0; j < this.getHeight()/size; j++) {
					g2.fillOval((i*size) - 1, (j*size) - 1, 2, 2);
				}
			}
			
			for (int i = 0; i < passages.size(); i++) {
				boolean x = false;
				if (i == editingIndex) x = true;
				paintPassage (passages.get(i), g2, x, true);
			}
		} else {
			while (!paintQueue.isEmpty()) {
				PassagePaintQueueItem it = paintQueue.remove(0);
				paintPassage (it.passage, g2, it.wantsDebug, it.wantsNormal);
			}
		}
	}
	
	public void paintPassage (Passage p, Graphics2D g2, boolean debug, boolean normal) {
		if (p.passagePoints.size() < 1) return;
		
		if (normal) {
			ArrayList<ArrayList<Point>> drawable = LineSequenceGenerator.makeAbsoluteLinesFrom(p);
			
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke (5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			for (ArrayList<Point> arr : drawable) {
				for (int i = 0; i < arr.size()-1; i++) {
					int m = i + 1;
					g2.drawLine((int)(arr.get(i).x * size), (int)(arr.get(i).y * size), (int)(arr.get(m).x * size), (int)(arr.get(m).y * size));
				}
			}
		}
		
		if (!debug) return;

		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke (1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = 0; i < p.passagePoints.size()-1; i++) {
			Point pt = p.passagePoints.get(i);
			Point ptt = p.passagePoints.get(i + 1);
			g2.drawLine((int)(pt.x * size), (int)(pt.y * size), (int)(ptt.x * size), (int)(ptt.y * size));
			g2.fillOval(((int)(pt.x * size) - 2), ((int)(pt.y * size) - 2), 4, 4);
		}
		
		int n = p.passagePoints.size()-1;
		g2.fillOval((int)(p.passagePoints.get(n).x * size) - 2, (int)(p.passagePoints.get(n).y * size) - 2, 4, 4);
	}

}
