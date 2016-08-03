package org.scss.metrics;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * handles all the functionalities associated with a file 
 */

public class FileHandler {

	ArrayList<String> absPath = new ArrayList<String>();

	
	// returns an absolute list of all the files that match the pattern java.
	@SuppressWarnings("unused")
	public ArrayList<String> fileWalker(File fr) {

		if (fr == null || !fr.exists())
			return null;

		String pattern = ".*\\.java.*"; // "([a-zA-Z])([\\.java])";

		Pattern p = Pattern.compile(pattern);
		Matcher m = null;
		int dirCount = 0;

		if (fr.isDirectory()) {
			// System.out.println("Directory name :" + fr.getName());
			dirCount++;
		}

		File[] children = fr.listFiles();

		if (children == null)
			return null;

		for (File f : children) {
			if (f.isDirectory()) {
				fileWalker(f);
			}

			m = p.matcher(f.getName());

			if (m.matches()) {
				// System.out.println("the name of the file is :" + f.getName()
				// + " path :" + f.getAbsolutePath());
				absPath.add(f.getAbsolutePath());
			}

		}
		return absPath;

	}
	
	// returns the relative path for a file. Required for getting the individual commits to a file
	public static String getRelativePath(String path, String base)
	    {
	        return new File(base).toURI().relativize(new File(path).toURI()).getPath();
	    }
	    
	    // returns the filename from the total absolute path
	    public static String getFileName(String path)
	    {
	        Path p = Paths.get(path);
	        return p.getFileName().toString();
	    }
}
