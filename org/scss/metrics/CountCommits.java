package org.scss.metrics;
/*
 * counts the number of commits for individual files and directories
 */

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class CountCommits {
	
	
	private Integer projectLogCount = 0;
	Repository repo;
	
	public CountCommits(Repository repo)
	{
		this.repo = repo;
	}
	
	@SuppressWarnings("unused")
	public int countCommitsForFile(String fileLoc) throws NoHeadException, GitAPIException {
		try (Git git = new Git(repo)) {
			Iterable<RevCommit> logs = git.log().call();

			String fn = fileLoc; // start from src "src/main/java/com/facebook/ads/sdk/GitFileCheck.java";
			logs = git.log()
					// for all log.all()
					.addPath(fn).call();
			int count = 0;
			for (RevCommit rev : logs) {
				// System.out.println("Commit: " + rev /* + ", name: " +
				// rev.getName() + ", id: " + rev.getId().getName() */);
				count++;
				projectLogCount+= count;
			}
			
			System.out.println("Had " + count + " commits on " + fn);

			return count;
		}
	}
	
	
	public int getTotalCommitsForProjects()
	{
		return projectLogCount;
	}
	
	public int countDirectoryCommits() throws NoHeadException, GitAPIException, IOException
	{
		int count;
		try (Git git = new Git(repo)) {
            Iterable<RevCommit> logs = git.log()
                    .call();
        logs = git.log()
                .all()
                .call();
        count = 0;
        for (@SuppressWarnings("unused") RevCommit rev : logs) {
            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
            count++;
        }
        //System.out.println("Had " + count + " commits overall in repository");
      
    }
        return count;
	}
	
	public int getCyclomaticComplexity(File fr) throws IOException
    {
        int complexity = 1;
        String[] keywords = {"if", "for", "do", "while", "switch", "case", "default", "continue", "break", "&&", "||", "?", "else", "return"};
        String words = "";
        String line = null;
        FileReader f;
        BufferedReader br = null;
        
        try {
            f = new FileReader(fr);
            br = new BufferedReader(f);
            line = br.readLine();
            
            while(line != null)
            {
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreTokens())
                {
                    words = st.nextToken();
                    for(int i = 0; i < keywords.length; i++)
                    {
                        if(keywords[i].equals(words))
                        {
                            //System.out.println(keywords[i]);
                            complexity++;
                        }
                    }
                }
                line = br.readLine();
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            br.close();
        }
        return (complexity);
}
}
