/**
 * 
 */
/**
 * @author Dan
 *
 */
package org.scss.metrics.multithreaded1;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.scss.jgit.porcelain.*;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
//import java.util.FileInputStream;


public class GetMetrics {

    String keyWords;
    Integer count;
    File fr ;
    //FileInputStream in;
    
    
   /* public GetMetrics(File fr)
    {
        this.fr = fr;
    }*/
    
   public GetMetrics()
   {
       
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
    
    
    private void FileWalker(File fr) throws IOException, NoHeadException, GitAPIException
    {
        
        JgitFunc jg1 = new JgitFunc();
        if(fr.isDirectory())
            System.out.print("Directory :" + fr.getName() + " has ");
        
        File[] children = fr.listFiles();
        
        
        if(children ==  null)
            return;
        
        System.out.print("children: " + children.length + "\n");
        
        //if(children.length == 1)
            //System.out.println(children[0]);
        //else
            for(File f : children)
            {
               if(!f.isDirectory() && getCyclomaticComplexity(f) != 1)// ignore files with complexity = 1 
                {   
                   if(f.getName().matches(".*\\.java.*"))
                   { 
                    System.out.println("cyclometric complexity of file : " + f.getName() + " is :" +getCyclomaticComplexity(f));
                    String rel = JgitFunc.getRelativePath(f.getAbsolutePath(), "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk");
                   }
                  //  jg1.countLogsForFile(rel);
                                     
                }
               FileWalker(f);
            }
    }
    
    public static void main(String[] args) throws IOException, NoHeadException, GitAPIException
    {
        
        //File new1 = new File("C:\\Users\\Dan\\Dropbox\\NDS\\dissertation\\data\\");
        File new1 = new File("C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/");
        GetMetrics fileName = new GetMetrics();
        fileName.FileWalker(new1);
        String loc = "C:/Users/Dan/Desktop/code optimization papers/code/facebook-java-ads-sdk/.git";
        JgitFunc jf = new JgitFunc(loc);
        
        
        
        //System.out.println(fileName.getCyclomaticComplexity());
    }
    
}
