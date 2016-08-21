package org.scss.metrics.multithreaded1;

import org.scss.jgit.porcelain.*;
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

//concurrent access
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Future;

public class RestRequestHandlerThreaded {

	
	static JSONObject header = new JSONObject();
	static JSONArray fileInfoArray = new JSONArray();
	static FileWriter jsonFile ;
	// need for the rerun between head the previous head 	
	private static String gitParentDirStr = null;
	
	// in invoked by the client facing web service
	public static String getParentDir()
	{
		return gitParentDirStr;
	}
	
	public static void setParentDir( String loc)
	{
		gitParentDirStr = loc;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void serverRequest(String url, Boolean isLocallyPresent, Boolean isReRun) throws IOException, NoHeadException, GitAPIException, InterruptedException, ExecutionException
	{
		//Arraylist for storing the file urls
		ArrayList<String>  listFiles = new ArrayList<String>();
		//Declared in case a different url is passed in
		//String url1;
		//setting the project url value for the json file
		//if(!isLocallyPresent)
			FixedParams.setProjectUrl(url);
		//else
			//url = FixedParams.getRemoteUrl();
		
		// download a remote repository
		CloneRemoteRepository  cloneRemote = new CloneRemoteRepository();
		File dotGitDir = cloneRemote.cloneRepo(url, isLocallyPresent);
		gitParentDirStr = dotGitDir.getParent();
		//setParentDir(gitParentDirStr);
		File gitParentDir = new File(gitParentDirStr);
		
		// initialize  a repository
		OpenRepository newRepo = new OpenRepository(dotGitDir);
		Repository repository = newRepo.initRepo();
		
		// Get a list of all the files with their abs for which the commit has to be calculated
		FileHandler fileHandler = new FileHandler();
		
		if(!isReRun)
		{
			listFiles = fileHandler.fileWalker(gitParentDir);
		}
		else
		{
			ShowChangedFilesBetweenCommits changed = new ShowChangedFilesBetweenCommits();
			listFiles = changed.changedFiles(repository);
		}
		
		// create an getmetrics class
		CountCommits metrics = new CountCommits(repository);
		
		
		 //headerObj.put("Project :", ""+new File(gitParentDirStr).getName()+"");
		createJsonHeader("Project", new File(gitParentDirStr).getName());
		 
		 
	     //headerObj.put("Total project commit :", ""+jf.countDirectoryCommits()+"");
		 createJsonHeader("Total", "" + metrics.countDirectoryCommits()); 
	    
		 //headerObj.put("Date :", ""+LocalDateTime.now()+"");
		 createJsonHeader("Date", ""+ LocalDateTime.now());
		 
		 createJsonHeader("ProjectURL", FixedParams.getProjectUrl());
		 createJsonHeader("Commiter",metrics.getCommiterIdentity() );
		 createJsonHeader("LCommit",WalkAllCommits.getLastCommitId(repository));
		 
	     createJsonFile(gitParentDir.getName());
		 
		long start = System.currentTimeMillis();
		
		ExecutorService executor = Executors.newFixedThreadPool(30);
		for(String str : listFiles)
		{
			
			// creating a new JSONbody object
			JSONObject jsonBodyObj = new JSONObject();
			
			// single threaded implementation
			//int complexityCountFile = metrics.getCyclomaticComplexity(new File(str));
			
			CalculateCC worker = new CalculateCC(new File(str));
			Future<Integer> complexityCountFile = (Future<Integer>) executor.submit(worker);
			//System.out.print("complexity is : " + complexityCountFile.get() + "\t");
			
            String relPath = FileHandler.getRelativePath(str, gitParentDirStr);
            int commitCount = metrics.countCommitsForFile(relPath);
            
            
           /* String fileName = "FileName : " + ""+FileHandler.getFileName(str)+"";
            String complexity = "Complexity : " + ""+complexityCountFile+"";
            String commits = "#Commits : " + ""+commitCount+"";*/
            
            
            
            jsonBodyObj.put("FileName ", FileHandler.getFileName(str));
            jsonBodyObj.put("Complexity", complexityCountFile.get());
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
		executor.shutdown();
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
				
			case "LCommit" :
				header.put("Last commit id", value);
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
		jsonFile = new FileWriter(workingDir + "/"+fileName+"_"+"mThread"+"_"+System.currentTimeMillis()+".json",true);
	 
	}
	
	public static void main(String[] args) throws NoHeadException, IOException, GitAPIException, InterruptedException, ExecutionException
	{
		RestRequestHandlerThreaded request = new RestRequestHandlerThreaded();
		//request.serverRequest("https://github.com/hackedteam/vector-rmi.git",false, false);
		
		// 1. https://github.com/google/guava.git
		// 2. https://github.com/gameclosure/facebook.git
		// 3. https://github.com/facebook/facebook-android-sdk.git 
		//4. https://github.com/Wizcorp/phonegap-facebook-plugin.git
		// 5. https://github.com/antonkrasov/AndroidSocialNetworks.git
		
		request.serverRequest("https://github.com/antonkrasov/AndroidSocialNetworks.git",false, false);
	}
	
	
	
	
}
