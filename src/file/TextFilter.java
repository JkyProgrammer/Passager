package file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

public class TextFilter extends FileFilter {

	ArrayList<String> allowedEndings = new ArrayList<String> (Arrays.asList("pas", "passage", "passages", "txt"));
	
	@Override
	public boolean accept(File f) {
		String[] parts = f.getName().split(".");
		if (parts.length == 0) return true;
		System.out.println(parts);
		if (allowedEndings.contains (parts[parts.length-1])) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		String desc = "Passage Files (";
		for (String str : allowedEndings) {
			desc += "." + str + ", ";
		}
		desc = desc.substring(0, desc.length()-2);
		desc += ")";
		return desc;
	}

}
