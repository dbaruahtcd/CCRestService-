package org.scss.metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.simple.JSONObject;
import org.scss.jgit.porcelain.OpenRepository;
import org.json.simple.JSONArray;

/*
 * @ date - 19th july, 2016
 * handles all the call to jgit functionalities
 * 
 */
public class JgitFunc {
    
    ArrayList<String> absPath = new ArrayList<String>();
    FileRepositoryBuilder builder = null;
    OpenRepository repo;
    Repository repository;
    //String gitRepo;
    
    public JgitFunc(String fileLoc)
    {
        //gitRepo = fileLoc;
        try {
            repo = new OpenRepository(new File(fileLoc));
            repository = repo.initRepo();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public JgitFunc() throws IOException
    {
        
    }
    
    // initialize the git repository
    
    public ArrayList<String> fileWalker(File fr)
    {
        
        if(fr == null || !fr.exists())
            return null;
        
        String pattern =  ".*\\.java.*";  //"([a-zA-Z])([\\.java])";
       
        Pattern p = Pattern.compile(pattern);
        Matcher m = null; 
        int dirCount = 0;
        
        if(fr.isDirectory())
        {
           // System.out.println("Directory name :" + fr.getName());
            dirCount++;
        }
        
        File[] children = fr.listFiles();
        
        if (children == null)
            return null;
        
        for(File f : children)
        {
            if(f.isDirectory())
            {
                fileWalker(f);
            }
            
            m = p.matcher(f.getName());
             
            if(m.matches())
            {
                //System.out.println("the name of the file is :" + f.getName() + " path :" + f.getAbsolutePath());
                absPath.add(f.getAbsolutePath());
            }
                
        }
        return absPath;
        
    }
    private int countDirectoryCommits() throws NoHeadException, GitAPIException, IOException
    {
        int count;
		try (Git git = new Git(repository)) {
            Iterable<RevCommit> logs = git.log()
                    .call();
        logs = git.log()
                .all()
                .call();
        count = 0;
        for (RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        //System.out.println("Had " + count + " commits overall in repository");
      
    }
        return count;
    }

    //returns the number of commits made to a file
    @SuppressWarnings("unused")
    public Integer countLogsForFile(String fileLoc) throws IOException, NoHeadException, GitAPIException
    {
       
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> logs = git.log()
                    .call();
            
            String fn =  fileLoc; //"src/main/java/com/facebook/ads/sdk/GitFileCheck.java";
            logs = git.log()
                    // for all log.all()
                    .addPath(fn)
                    .call();
            int count = 0;
            for (RevCommit rev : logs) {
                //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                count++;
            }
            System.out.println("Had " + count + " commits on " + fn);
            return count;
        }
    }
    
    // gets the user info related to a remote repository
    public String getCommiterIdentity()
    {
       		Config config = repository.getConfig();
            String name = config.getString("user", null, "name");
            String email = config.getString("user", null, "email");
            if (name == null || email == null) {
                System.out.println("User identity is unknown!");
            } else {
               // System.out.println("User identity is " + name + " <" + email + ">");
            	return name + " <" + email + ">";
            }

            /*String url = config.getString("remote", "origin", "url");
            if (url != null) {
                    System.out.println("Origin comes from " + url);
            }*/
    	 return null;
    }
    
    
    //returns the relative file as needed for finding the git commit
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
    
    //writes the result to a file
    public static FileWriter createJsonFile(String fileName) throws IOException
    {
        FileWriter file = new FileWriter("C:/Users/Dan/workspace/Json/"+fileName+"_"+System.currentTimeMillis()+".json",true);
        return file;
    }
    
    public static void writeJson(FileWriter file, JSONObject ob) throws IOException
    {
        
        
        file.write(ob.toJSONString());
    }
    
    /*public static void closeFile(File f)
    {
        f.flush();
        f.close();
    }
    */
    public static void closeFile(FileWriter fr) throws IOException
    {
        fr.flush();
        fr.close();
    }
    
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws NoHeadException, IOException, GitAPIException
    {
        
        JSONObject headerObj = new JSONObject();
        JSONArray fileInfo = new JSONArray();
        
        String loc = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git";
        JgitFunc jf = new JgitFunc(loc);
        GetMetrics gm = new GetMetrics();
        String base = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/";
        
        headerObj.put("Project :", ""+new File(base).getName()+"");
        headerObj.put("Total project commit :", ""+jf.countDirectoryCommits()+"");
        headerObj.put("Date :", ""+LocalDateTime.now()+"");
        FileWriter file = createJsonFile(new File(base).getName());
       // writeJson(file, headerObj);
        //headerObj.clear();
      //  System.out.println("Directory name :" + new File(base).getName());
        ArrayList<String> resAbsList = jf.fileWalker(new File(base));
        
        System.out.println("Lenght of the list : " + resAbsList.size() + " relative paths areL : ");
        for(String str : resAbsList)
        {
            //JSONObject body = new JSONObject();
            int complexityCount = gm.getCyclomaticComplexity(new File(str));
            System.out.print("complexity is : " + complexityCount + "\t");
            String relPath = getRelativePath(str, base);
            int commitCount = jf.countLogsForFile(relPath);
            
            
            fileInfo.add("FileName : " + ""+getFileName(str)+"");
            fileInfo.add("Complexity :" + ""+complexityCount+"");
            fileInfo.add("#Commits :" + "" +commitCount+"");
           
           
        }
        headerObj.put("FileInfo", fileInfo);
        writeJson(file,headerObj);
        headerObj.clear();
        fileInfo.clear();
        
       // writeJson(file, headerObj);
       // headerObj.clear();
        
        closeFile(file);
    }   
}
    
