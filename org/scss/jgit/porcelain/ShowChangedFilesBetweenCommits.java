package org.scss.jgit.porcelain;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.scss.jgit.helper.FixedParams;
import org.scss.metrics.RestRequestHandler;

public class ShowChangedFilesBetweenCommits {
	
	public ArrayList<String> changedFiles(Repository repository) throws IncorrectObjectTypeException, IOException, GitAPIException
	{
		ArrayList<String> fileUrls = new ArrayList<String>(); 
		String absPathToGitParent =  RestRequestHandler.getParentDir();
				
				//"C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk";
		
		
				
		
		ObjectId oldHead = repository.resolve("HEAD^^{tree}"); 
        //repository.
        ObjectId head = repository.resolve("HEAD^{tree}");
        
       /*ObjectId oldHead = repository.resolve("70ec1225a1ae0020af2a24231f171fd786c7fc75");
       ObjectId head = repository.resolve("0f2a0d27366ecec92f9f5a03fd4e3843f30cbe47");*/
        
        /*ObjectId oldHead = ObjectId.fromString("70ec1225a1ae0020af2a24231f171fd786c7fc75");
        ObjectId head = ObjectId.fromString("0f2a0d27366ecec92f9f5a03fd4e3843f30cbe47");*/

        System.out.println("Printing diff between tree: " + oldHead + " and " + head);

        // prepare the two iterators to compute the diff between
		try (ObjectReader reader = repository.newObjectReader()) {
    		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
    		oldTreeIter.reset(reader, oldHead);
    		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
    		newTreeIter.reset(reader, head);

    		// finally get the list of changed files
    		try (Git git = new Git(repository)) {
                List<DiffEntry> diffs= git.diff()
        		                    .setNewTree(newTreeIter)
        		                    .setOldTree(oldTreeIter)
        		                    .call();
                for (DiffEntry entry : diffs) {
                    //System.out.println("Entry: " + entry);
                	
                    String relPath = getChangedFiles(entry.toString());
                    if(checkFileSuffix(relPath))
                	{
                		String absPath = absPathToGitParent.concat("/"+relPath);
                		fileUrls.add(absPath);
                		//System.out.println("abspath :" + absPath);
                		
                	}
                	
                		//continue;
                }
    		}
		}
		//System.out.println("Done");
		return fileUrls;
	  
	}
	
	// gets just the relative file path names from the diffentry
	public static String getChangedFiles(String path)
	{
		
		int indexOfSpace = path.indexOf(" ");
		return path.substring(indexOfSpace + 1 , path.length()-1);
		
	}
	
	// checks if the file falls into the valid categories
	public static boolean checkFileSuffix(String str)
	{
		String pattern = FixedParams.getFilePattern();
		Pattern p = Pattern.compile(pattern);
		Matcher m = null;
		
		
		m = p.matcher(str.replace("]", ""));
		
		//System.out.println("str :" + str + " matcher :" + m.matches());
		if(m.matches())
		{
			//System.out.println("str :" + str);
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException, GitAPIException
	{
		/*Entry: DiffEntry[MODIFY pom2.xml]
		Entry: DiffEntry[ADD pom3.xml]
		Entry: DiffEntry[ADD src/main/java/com/facebook/ads/sdk/GitFileCheck.java]*/
		
		/*String url = "DiffEntry[ADD src/main/java/com/facebook/ads/sdk/GitFileCheck.java]";
		int indexOfSpace = url.indexOf(" ");
		System.out.println("Index : " + indexOfSpace);
		String res = url.substring(indexOfSpace + 1, url.length() - 1);
		System.out.println("result string :" + res);*/
		
		////////////////////////
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
	       // try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
	    Repository repository = builder.setGitDir(new File("C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git")).readEnvironment().findGitDir().build();
		
		ShowChangedFilesBetweenCommits newfiles = new ShowChangedFilesBetweenCommits();
		ArrayList<String > result = newfiles.changedFiles(repository);
		for(String s : result)
		{
			System.out.println("path :" + s);
		}
		
		/*String str = "src/main/java/com/facebook/ads/sdk/User.java";
		checkFileEnd(str);*/
		
	}

}
