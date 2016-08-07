package org.scss.metrics;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.scss.jgit.helper.FixedParams;
import org.scss.jgit.porcelain.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RestRequestHandler {

	
	static JSONObject header = new JSONObject();
	static JSONArray fileInfoArray = new JSONArray();
	static FileWriter jsonFile ;
	
	// in invoked by the client facing web service
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void serverRequest(String url, Boolean val) throws IOException, NoHeadException, GitAPIException
	{
			
		//setting the project url value for the json file
		FixedParams.setProjectUrl(url);
		
		// download a remote repository
		CloneRemoteRepository  cloneRemote = new CloneRemoteRepository();
		File dotGitDir = cloneRemote.cloneRepo(url, val);
		String gitParentDirStr = dotGitDir.getParent();
		File gitParentDir = new File(gitParentDirStr);
		
		// initialize  a repository
		OpenRepository newRepo = new OpenRepository(dotGitDir);
		Repository repository = newRepo.initRepo();
		
		// Get a list of all the files with their abs for which the commit has to be calculated
		FileHandler fileHandler = new FileHandler();
		ArrayList<String> listFiles = fileHandler.fileWalker(gitParentDir);
		
		// create an getmetrics class
		CountCommits metrics = new CountCommits(repository);
		
		
		 //headerObj.put("Project :", ""+new File(gitParentDirStr).getName()+"");
		createJsonHeader("Project", new File(gitParentDirStr).getName());
		 
		 
	     //headerObj.put("Total project commit :", ""+jf.countDirectoryCommits()+"");
		 createJsonHeader("Total", "" + metrics.countDirectoryCommits());
	    
		 //headerObj.put("Date :", ""+LocalDateTime.now()+"");
		 createJsonHeader("Date", ""+ LocalDateTime.now());
		 
		 createJsonHeader("ProjectURL", FixedParams.getRemoteUrl());
		 createJsonHeader("Commiter",metrics.getCommiterIdentity() );
		 
	     createJsonFile(gitParentDir.getName());
		 
		long start = System.currentTimeMillis();
		for(String str : listFiles)
		{
			
			// creating a new JSONbody object
			JSONObject jsonBodyObj = new JSONObject();
			
			int complexityCountFile = metrics.getCyclomaticComplexity(new File(str));
			System.out.print("complexity is : " + complexityCountFile + "\t");
            String relPath = FileHandler.getRelativePath(str, gitParentDirStr);
            int commitCount = metrics.countCommitsForFile(relPath);
            
            
           /* String fileName = "FileName : " + ""+FileHandler.getFileName(str)+"";
            String complexity = "Complexity : " + ""+complexityCountFile+"";
            String commits = "#Commits : " + ""+commitCount+"";*/
            
            
            
            jsonBodyObj.put("FileName ", FileHandler.getFileName(str));
            jsonBodyObj.put("Complexity", complexityCountFile);
            jsonBodyObj.put("#Commits", commitCount);
            
            //adding the individual json object to the array
            fileInfoArray.add(jsonBodyObj);
            
            /*addJsonArray("FileName", fileName);
            addJsonArray("Complexity", complexity);
            addJsonArray("Commits", commits);*/
            
            
           /* fileInfo.add("FileName : " + ""+getFileName(str)+"");
            fileInfo.add("Complexity :" + ""+complexityCount+"");
            fileInfo.add("#Commits :" + "" +commitCount+"");*/
           
           
        }
		long end = System.currentTimeMillis();
		
		double minutes = (end - start)/1000.0;
       // headerObj.put("FileInfo", fileInfo);
		createJsonHeader("EndDate", ""+ LocalDateTime.now());
		createJsonHeader("RunTime", String.valueOf(minutes)); // Double.toString(double)
       // createJsonHeader("FileInfo", fileInfoArray.toJSONString());
		header.put("FileInfo", fileInfoArray);
        
        writeJson(jsonFile,header);
        header.clear();
        fileInfoArray.clear();
        
       // writeJson(file, headerObj);
       // headerObj.clear();
        
        closeFile(jsonFile);
	}
	
	
	@SuppressWarnings("unchecked")
	public void createJsonHeader(String keyword, String value)
	{
		
		switch(keyword) 
		{
			case "Project" :
				header.put("LocalProjectDir :", ""+ value+"");
				break;
			
			case "Total" :
				header.put("Total project commit :", ""+ value+"");
				break;
			
			case "Date" :
				header.put("Start time : ", ""+value+"");
				break;
			
			case "EndDate" :
				header.put("End time  ", ""+ value+"");
				break;
				
			case "FileInfo":
				header.put("FileInfo",""+value+"");
				break;
				
			case "RunTime":
				header.put("TotalRunTime",value);
				break;
				
			case "ProjectURL" :
				header.put("ProjectURL", value);
				break;
			
			case "Commiter":
				header.put("Commiter", value);
				break;
				
			default :
				System.out.println("None of the keywords matched :" + keyword);
		}
				
			
	}
	
	// creates a new structure for json body
	/*public void createJsonBody(String keyword, String value, JSONObject bodyObj)
	{
		switch(keyword)
		{
		case "FileName" :
			bodyObj.put("Filename ", value);
			break;
			
		case "Complexity" :
			bodyObj.put("Complexity ", value);
			break;
			
		case "Commits" :
			bodyObj.put("#Commits", value);
			break;
			
			default:
				System.out.println("Body keyword didn't match" + keyword);
				
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public void addJsonArray(String keyword, String value)
	{
		
		switch(keyword)
		{
		case "FileName" :
				fileInfoArray.add(value);
				break;
				
		case "Complexity":
			fileInfoArray.add(value);
			break;
			
		case "Commits" :
			fileInfoArray.add(value);
			break;
			
		default:
			System.out.println("None of the options matched!");
			break;
		}
	}
	
	public static void writeJson(FileWriter file, JSONObject ob) throws IOException
    {
        
        
        file.write(ob.toJSONString());
    }
	
	public static void closeFile(FileWriter fr) throws IOException
    {
        fr.flush();
        fr.close();
    }
	
	
	private static void createJsonFile(String fileName) throws IOException
	{
		String workingDir = System.getProperty("user.dir");
		//File newDir = new File(workingDir+"/Json");
		//newDir.mkdirs();
		jsonFile = new FileWriter(workingDir + "/"+fileName+"_"+System.currentTimeMillis()+".json",true);
	 
	}
	
	public static void main(String[] args) throws NoHeadException, IOException, GitAPIException
	{
		RestRequestHandler request = new RestRequestHandler();
		request.serverRequest("https://github.com/facebook/jscodeshift.git",false);
	}
	
	
	
	
}
