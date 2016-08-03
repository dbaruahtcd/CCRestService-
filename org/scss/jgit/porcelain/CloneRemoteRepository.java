package org.scss.jgit.porcelain;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.scss.jgit.helper.FixedParams;

public class CloneRemoteRepository {
	
	 //private static final String REMOTE_URL = "https://github.com/github/testrepo.git";
   // private static final String REMOTE_URL  = "https://github.com/facebook/css-layout.git" ;//"https://github.com/facebook/react-native.git";

    public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
    	
    	CloneRemoteRepository clone = new CloneRemoteRepository();
    	File res = clone.cloneRepo(null, false);
    	System.out.println("the git repo is :" + res.getParentFile());
        
    }

    // takes the url ending with .git. The second parameter is check if this method is to be skipped
    public File cloneRepo(String remoteUrl, Boolean check) throws IOException
    {
    	
		if (!check) {
			Git result = null;
			if (remoteUrl == null) {
				remoteUrl = FixedParams.getRemoteUrl();
			}

			File localPath = File.createTempFile("TestGitRepository", "");
			localPath.delete();

			System.out.println("Cloning from :" + remoteUrl + " to " + localPath);

			try {
				result = Git.cloneRepository().setURI(remoteUrl).setDirectory(localPath).call();

			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result.getRepository().getDirectory();
		}
		else
		{
			return null;
		}
    	
    	
    }
}
