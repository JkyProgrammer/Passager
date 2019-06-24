package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.*;
import java.io.*;

public class SaveOpenHandler {
	public static String convertToText (ArrayList<Passage> arr) {
		String output = "";
		for (Passage p : arr) {
			for (Point point : p.passagePoints) {
				output += (point.x + " " + point.y + ",");
			}
			output = output.substring(0, output.length()-1);
			output += ":";
			for (Point point : p.doors) {
				output += (point.x + " " + point.y + ",");
			}
			output = output.substring(0, output.length()-1);
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
			FileWriter wr = new FileWriter (path);
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
			
			String[] doorPoints = parts[1].split(",");
			for (String doorPoint : doorPoints) p.addDoor(Point.fromString(doorPoint));
			
			try {
				p.closedLeft = Boolean.parseBoolean(parts[2]);
			} catch (Exception e) {}
			
			try {
				p.closedRight = Boolean.parseBoolean(parts[3]);
			} catch (Exception e) {}
			
			output.add(p);
		}
		
		return output;
	}
}
