package displayer;

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

import file.SaveOpenHandler;
import file.TextFilter;
import main.Bounds;
import main.Ladder;
import main.Passage;
import main.Point;

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
	int editingMode = 0; // 0 = Passage
						 // 1 = Doors
						 // 2 = Ladders
						 // 3 = Decorations
						 // 4 = Rooms
	
	JMenuItem mousePosLabel;
	Point tmpLadderStartPos;
	
	public void setup () {
		passages.add (new Passage());
		getContentPane().addMouseMotionListener(new MouseMotionListener () {
			@Override public void mouseDragged(MouseEvent e) {
				int approxX = Math.round((float)e.getX() / (float)size);
				int approxY = Math.round((float)e.getY() / (float)size);
				mousePos.x = approxX;
				mousePos.y = approxY+2;
				mousePosLabel.setText(mousePos.toString());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int approxX = Math.round((float)e.getX() / (float)size);
				int approxY = Math.round((float)e.getY() / (float)size);
				mousePos.x = approxX;
				mousePos.y = approxY+2;
				mousePosLabel.setText(mousePos.toString());
			}
		});
		
		getContentPane().addMouseListener(new MouseListener () {
			@Override public void mouseClicked(MouseEvent e) {
				if (editingMode == 0) {
					Passage p = passages.get(editingIndex);
					if (p.passagePoints.size() < 1 || mousePos.x >= (p.passagePoints.get(p.passagePoints.size()-1).x)) {
						passages.get(editingIndex).appendPassagePoint(new Point (mousePos.x, mousePos.y));
						repaint();
					}
				} else if (editingMode == 1) {
					passages.get(editingIndex).addDoor(new Point (mousePos.x, mousePos.y));
					repaint();
				}
				
			}

			@Override public void mousePressed(MouseEvent e) {
				if (editingMode == 2) {
					tmpLadderStartPos = new Point (mousePos.x, mousePos.y);
				}
			}
			@Override public void mouseReleased(MouseEvent e) {
				if (editingMode == 2) {
					passages.get(editingIndex).addLadder (tmpLadderStartPos.duplicate(), (int) (mousePos.y-tmpLadderStartPos.y));
					repaint ();
				}
			}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		addKeyListener(new KeyListener () {
			// TODO: Proper deleting for different elements
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					if (editingMode == 0) {
						if (passages.get(editingIndex).passagePoints.size() > 0) {
							passages.get(editingIndex).passagePoints.remove(passages.get(editingIndex).passagePoints.size()-1);
							repaint ();
						}
					} else if (editingMode == 1) {
						if (passages.get(editingIndex).doors.size() > 0) {
							passages.get(editingIndex).doors.remove(passages.get(editingIndex).doors.size()-1);
							repaint ();
						}
					} else if (editingMode == 2) {
						if (passages.get(editingIndex).ladders.size() > 0) {
							passages.get(editingIndex).ladders.remove(passages.get(editingIndex).ladders.size()-1);
							repaint ();
						}
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
		
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension (500, 500));
		setVisible(true);
	}
	
	public void repackPassages () {
		// Remove empty passages
		int x = 0;
		while (x < passages.size()) {
			if (passages.get(x).passagePoints.size() < 2) passages.remove(x);
			else x++;
		}
		
		// Kill disconnected doors
		for (Passage p : passages) {
			Bounds b = p.getBounds();
			int d = 0;
			while (d < p.doors.size()) {
				if (!b.contains (p.doors.get(d))) p.doors.remove(d);
				else d++;
			}
		}
		
		// Simplify passages
		for (Passage p : passages) {
			int l = 0;
			while (l < p.passagePoints.size()-2) {
				Point p1 = p.passagePoints.get(l);
				Point p2 = p.passagePoints.get(l+1);
				Point p3 = p.passagePoints.get(l+2);
				
				if (p1.y == p2.y && p2.y == p3.y) p.passagePoints.remove(p2);
				else if ((p1.y-p2.y)/(p1.x-p2.x) == (p2.y-p3.y)/(p2.x-p3.x)) p.passagePoints.remove (p2);
				else l++;
			}
		}
		
		if (passages.size() < 1) passages.add(new Passage ());
		editingIndex = 0;
		
		repaint ();
	}

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
		
		JMenuItem cleanupButton = new JMenuItem ("Clean up");
		cleanupButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				repackPassages();
			}
		});
		toolsMenu.add(cleanupButton);
		
		JMenu editingModeMenu = new JMenu ("Editing Mode");
		toolsMenu.add(editingModeMenu);
		
		JMenuItem emPassages = new JMenuItem ("Passages");
		emPassages.addActionListener(new ActionListener () { @Override public void actionPerformed(ActionEvent e) { editingMode = 0; } });
		editingModeMenu.add(emPassages);
		
		JMenuItem emDoors = new JMenuItem ("Doors");
		emDoors.addActionListener(new ActionListener () { @Override public void actionPerformed(ActionEvent e) { editingMode = 1; } });
		editingModeMenu.add(emDoors);
		
		JMenuItem emLadders = new JMenuItem ("Ladders");
		emLadders.addActionListener(new ActionListener () { @Override public void actionPerformed(ActionEvent e) { editingMode = 2; } });
		editingModeMenu.add(emLadders);
		
		JMenuItem emDecorations = new JMenuItem ("Decorations");
		emDecorations.addActionListener(new ActionListener () { @Override public void actionPerformed(ActionEvent e) { editingMode = 3; } });
		editingModeMenu.add(emDecorations);
		
		JMenuItem emRooms = new JMenuItem ("Rooms");
		emRooms.addActionListener(new ActionListener () { @Override public void actionPerformed(ActionEvent e) { editingMode = 4; } });
		editingModeMenu.add(emRooms);
		
		mousePosLabel = new JMenuItem (mousePos.toString());
		
		
		mb.add(fileMenu);
		mb.add(toolsMenu);
		mb.add(mousePosLabel);
		
		setJMenuBar (mb);
	}
	
	public void save (boolean as) {
		repackPassages();
		if (filePath == null || as) {
			JFileChooser chooser = new JFileChooser ();
			// TODO: Fix filtering
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
	
	// TODO: Scrolling
	
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
			
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			paintDots (g2, new Bounds (0, 0, getWidth()/size, getHeight()/size));
			
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
		paintLadders (g2);
		this.getJMenuBar().repaint();
	}
	
	public void paintDots (Graphics2D g2, Bounds area) {
		g2.setColor(Color.lightGray);
		int x1 = (int) (Math.ceil(area.x) * 20);
		int x2 = (int) (Math.floor(area.width) * 20) + x1;
		int y1 = (int) (Math.ceil(area.y) * 20);
		int y2 = (int) (Math.floor(area.height) * 20) + y1;
		
		for (int i = x1; i < x2/size; i++) {
			for (int j = y1; j < y2/size; j++) {
				g2.fillOval((i*size) - 1, (j*size) - 1, 2, 2);
			}
		}
	}
	
	public void paintLadders (Graphics2D g2) {
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke (5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (Passage pas : passages) {
			if (pas.ladders.size() < 1) continue;
			for (Ladder lad : pas.ladders) {
				if (lad.length < 1) continue;
				g2.setColor(Color.white);
				g2.fillRect((int)lad.origin.x * size, (int)lad.origin.y * size, size, lad.length * size);
				g2.setColor(Color.black);
				g2.drawRect((int)lad.origin.x * size, (int)lad.origin.y * size, size, lad.length * size);
				
				for (float i = 0.5f; i < lad.length; i+= 0.5) {
					g2.drawLine((int)((lad.origin.x + 0.3) * size), (int)((lad.origin.y + i) * size), (int)((lad.origin.x + 0.7) * size), (int)((lad.origin.y + i) * size));
				}
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
