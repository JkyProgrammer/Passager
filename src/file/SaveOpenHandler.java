package file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import main.Ladder;
import main.Passage;
import main.Point;

public class SaveOpenHandler {
	public static String convertToText (ArrayList<Passage> arr) {
		String output = "";
		for (Passage p : arr) {
			if (p.passagePoints.size() > 0) {
				for (Point point : p.passagePoints) {
					output += (point.x + " " + point.y + ",");
				}
				output = output.substring(0, output.length()-1);
			}
			output += ":";
			
			if (p.doors.size() > 0) {
				for (Point point : p.doors) {
					output += (point.x + " " + point.y + ",");
				}
				output = output.substring(0, output.length()-1);
			}
			
			output += ":";
			
			if (p.ladders.size() > 0) {
				for (Ladder point : p.ladders) {
					output += (point.origin.x + " " + point.origin.y + " " + point.length + ",");
				}
				output = output.substring(0, output.length()-1);
			}
			output += ":";
			output += p.closedLeft;
			output += ":";
			output += p.closedRight;
			output += "\n";
		}
		output = output.substring(0, output.length()-1);
		return output;
	}
	
	public static void writeToFile (String path, ArrayList<Passage> arr) {
		try {
			String pth = path;
			if (pth.lastIndexOf('.') == -1) pth += ".pas";
			FileWriter wr = new FileWriter (pth);
			wr.write(convertToText (arr));
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Passage> openFromFile (String path) {
		try {
			Scanner re = new Scanner (new File (path));
			String in = "";
			while (re.hasNextLine()) in += re.nextLine() + "\n";
			re.close();
			return convertToArray (in);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Passage> ();
		}
	}
	
	public static ArrayList<Passage> convertToArray (String s) {
		ArrayList<Passage> output = new ArrayList<Passage> ();
		String[] lines = s.split("\n");
		for (String line : lines) {
			if (line.length() < 1) continue;
			Passage p = new Passage ();
			String[] parts = line.split(":");
			
			String[] passagePoints = parts[0].split(",");
			for (String passagePoint : passagePoints) p.appendPassagePoint(Point.fromString(passagePoint));
			
			if (parts[1].length() > 0) {
				String[] doorPoints = parts[1].split(",");
				for (String doorPoint : doorPoints) p.addDoor(Point.fromString(doorPoint));
			}
			
			if (parts[2].length() > 0) {
				String[] ladders = parts[2].split(",");
				for (String ladder : ladders) {
					int sep = ladder.lastIndexOf(" ");
					Point pnt = Point.fromString(ladder.substring(0, sep));
					p.addLadder (pnt, Integer.parseInt(ladder.substring(sep+1)));
				}
			}
			
			try {
				p.closedLeft = Boolean.parseBoolean(parts[3]);
			} catch (Exception e) {}
			
			try {
				p.closedRight = Boolean.parseBoolean(parts[4]);
			} catch (Exception e) {}
			
			output.add(p);
		}
		
		return output;
	}
}
