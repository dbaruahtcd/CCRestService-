package org.scss.jgit.porcelain;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class OpenRepository {
        
    File repoFile;
    FileRepositoryBuilder builder;
    
    public OpenRepository(File f)// throws IOException
    {
        this.repoFile = f;
        
    }
    
    public Repository initRepo() throws IOException
    {
        Repository repository;
        builder = new FileRepositoryBuilder();
        repository = builder.setGitDir(repoFile)
                .readEnvironment()
                .findGitDir()
                .build();
        //System.out.println("Having repository :" + repository.getDirectory());
       
        
       // Ref head = repository.exactRef("refs/heads/master");
       // System.out.println("Ref of refs/heads/master: " + head);
        
        
        return repository;
    }
    

    public static void main(String[] main) throws IOException
    {
        
        String repoLoc = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git";
        OpenRepository repo = new OpenRepository(new File(repoLoc));
        repo.initRepo();
    }

}
