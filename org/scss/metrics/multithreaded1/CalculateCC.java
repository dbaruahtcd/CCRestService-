package org.scss.metrics.multithreaded1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.scss.jgit.porcelain.GetFileName;
import org.scss.jgit.helper.*;

import java.util.concurrent.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CalculateCC implements Callable<Integer>{
	
	File fr = null;
	//int complexity;
	static int  iRun = 0;
	
	public CalculateCC(File fr)
	{
		this.fr = fr;
		//Thread thread = new Thread(this);
		//thread.run();
		
		
	}
	
	public CalculateCC()
	{
		
	}
	
	
	// return the file name
	private File getFileName()
	{
		return this.fr;
	}
	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		
		int complexity = 0;
		complexity = getCyclomaticComplexity(getFileName());
		return complexity;
	}

	
	/*@Override
	public void run()
	{
		
		
		try {
			complexity = getCyclomaticComplexity(getFileName());
			System.out.println("Complexity is : " + complexity);
			iRun++;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//getComplexity();
	}*/
	
	
	
	
	public int getCyclomaticComplexity(File fr) throws IOException
    {
        int complexity = 1;
        String[] keywords = FixedParams.getKeywords();//{"if", "for", "do", "while", "switch", "case", "default", "continue", "break", "&&", "||", "?", "else", "return"};
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
	
	// returns the complexity set my the thread's run method
	
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException
	{
		
		FileHandler fileHandler = new FileHandler();
		ArrayList<String> fileList = fileHandler.fileWalker(new File("C:/Users/Dan/AppData/Local/Temp/guava7584461915963236433/"));
		System.out.println(fileList.size());
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		long start = System.currentTimeMillis();
		int i = 0;
		for(String str : fileList)
		{
			
			CalculateCC worker = new CalculateCC(new File(str));
			Future<Integer> res = (Future<Integer>) executor.submit(worker); 
			System.out.println("complexity of :" + str + " is " + res.get());
			System.out.println(i + " complexity is :" + worker.getCyclomaticComplexity(new File(str)));
			i++;
			
		}
		executor.shutdown();
		long end = System.currentTimeMillis();
		System.out.println("Multithreaded run time : " + (end - start)/1000.0 + " sec");
		
		
		System.out.println("+++++++++++++++++++++ Single threaded run ++++++++++++++++++++++++++");
		
		
		long start1 = System.currentTimeMillis();
		CalculateCC cc = new CalculateCC();
		for(String s : fileList)
		{
			try {
				cc.getCyclomaticComplexity(new File(s));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end1 = System.currentTimeMillis();
		System.out.println("Single threaded  run time : " + (end1 - start1)/1000.0 + " sec");
		
	}

	
}
