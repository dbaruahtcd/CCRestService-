package org.scss.jgit.porcelain;


import org.scss.jgit.helper.*;
import org.scss.jgit.helper.*;
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
    	FixedParams.setProjectUrl("https://github.com/facebook/rocksdb.git");
    	File res = clone.cloneRepo("https://github.com/facebook/rocksdb.git", false);
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
			String fileName = GetFileName.getFileNameFromUrl(FixedParams.getProjectUrl());
			FixedParams.setLocalDirName(fileName);
			System.out.println("File name : " + fileName);
			
			File localPath = File.createTempFile(fileName, "");
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
			return new File(FixedParams.getLocalGitDir());
		}
    	
    	
    }
}
