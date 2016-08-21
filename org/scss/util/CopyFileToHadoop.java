package org.scss.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.*;
import org.scss.jgit.helper.FixedParams;
import java.io.*;
import java.util.Date;

public class CopyFileToHadoop {

	File target;
	FileWriter fw;
	BufferedWriter bw;
	File hadoopDir;
	private int totalValidFiles;
	private int totalFileSize;
	ArrayList<Integer> sizes = new ArrayList<Integer>();

	public CopyFileToHadoop(File fr) throws IOException {
		target = new File("E:/Dissertation_Files/mergedFiles/" + fr.getName() + "_" + System.currentTimeMillis() + ".txt");
		System.out.println("target: " + target);
		fw = new FileWriter(target);
		// bw = new BufferedWriter(fw);
		hadoopDir = new File("E:/Dissertation_Files/hadoop/" + fr.getName());
		// hadoopDir.getParentFile().mkdirs();
		hadoopDir.mkdir();
		// hadoopDir = new File(f);
	}

	public int getValidFilesCount() {
		return totalValidFiles;
	}

	public int getTotalFileSize() {
		return totalFileSize;
	}

	public File getHadoopDir() {
		return hadoopDir;
	}

	public String fileWalker(File fr) throws IOException {

		if (fr == null || !fr.exists())
			return null;

		String pattern = FixedParams.getFilePattern(); // ".*\\.java.*"; //
														// "([a-zA-Z])([\\.java])";

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
				totalValidFiles++;
				// System.out.println("length of file : " + f.length());
				long size1 = f.length();
				sizes.add((int) size1);
				totalFileSize += size1;
				moveFileToHadoopDir(f);
				mergeFile(f);
			}

		}
		return "";

	}

	public void moveFileToHadoopDir(File src) throws IOException {
		// System.out.println("dest :" + getHadoopDir());

		FileUtils.copyFileToDirectory(src, getHadoopDir());

	}
	
	public ArrayList<Integer> getSize()
	{
		return sizes;
	}

	public void mergeFile(File f) throws IOException {
		FileReader fr = new FileReader(f);
		BufferedReader bf = new BufferedReader(fr);
		String line;
		while ((line = bf.readLine()) != null) {
			// System.out.println("Line :" + line);
			fw.write(line + "\n");
		}

		fw.write("======================= End of file " + f.getName() + " count " + getValidFilesCount()+ "===================");
		
		bf.close();

	}
	
	public static void main(String[] args) throws IOException {
		File new1 = new File("C:/Users/Dan/AppData/Local/Temp/AndroidSocialNetworks7233014196103236333/");
		CopyFileToHadoop fileName = new CopyFileToHadoop(new1);
		long start = System.currentTimeMillis();
		fileName.fileWalker(new1);
		long end = System.currentTimeMillis();
		
		
		double duration = ((end - start) / 1000.0) ;
		
		//System.out.println("duration is : " + duration + " total valid files :" + fileName.getValidFilesCount() + " total file size " + fileName.getTotalFileSize() +
			//	" avg size of files : " +( (fileName.getValidFilesCount() != 0) ? (fileName.getTotalFileSize()/fileName.getValidFilesCount())/1024 : 1) + "KB");
		File resultFile = new File("E:/Dissertation_Files/results/output.txt");
		FileWriter fr = new FileWriter(resultFile, true);
		
		ArrayList<Integer> res = fileName.getSize();
		Collections.sort(res);
		int max = res.get(res.size() -1 );
		String res1 = "CurrentTime :" + new Date() + "\n File name : " + new1.getName() + "\n duration is : " + duration + "\n total valid files :" + fileName.getValidFilesCount() + "\n total file size :" + fileName.getTotalFileSize() +
				"\n avg size of files : " +( (fileName.getValidFilesCount() != 0) ? (fileName.getTotalFileSize()/fileName.getValidFilesCount())/1024 : 1) + "KB \n max file size : " +  max/1024 + "\n";
	
		String newLine = "==========================" + "\n";
		fr.append(res1 + newLine);
		fr.flush();
		fr.close();
	
	}
}

	
