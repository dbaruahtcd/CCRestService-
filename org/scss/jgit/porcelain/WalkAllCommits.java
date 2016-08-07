package org.scss.jgit.porcelain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.scss.jgit.helper.CookbookHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;



/**
 * Simple snippet which shows how to use RevWalk to quickly iterate over all available commits,
 * not just the ones on the current branch
 */
public class WalkAllCommits {
	
	
	
	public static String getLastCommitId(Repository repository) throws NoHeadException, GitAPIException, IOException
	{
		
		ArrayList<String> commitIds = new ArrayList<String>();
		try (Git git = new Git(repository)) {
            Iterable<RevCommit> commits = git.log().all().call();
            int count = 0;
            for (RevCommit commit : commits) {
                System.out.println("LogCommit: " + commit);
                count++;
                commitIds.add(commit.toString());
            }
            
            System.out.println(count);
            String result =  commitIds.get(commitIds.size()-1);
            return result.substring(result.indexOf(" "), result.indexOf(" ", result.indexOf(" ") + 1));
        }
		
	}
    
	public static void main(String[] args) throws IOException, InvalidRefNameException, GitAPIException {
        WalkAllCommits commit = new WalkAllCommits();
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(new File("C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git")).readEnvironment().findGitDir().build()) {
            System.out.println("last id : " +  commit.getLastCommitId(repository));
        }
    }
}

