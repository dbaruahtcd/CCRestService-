package org.scss.jgit.test;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

// creating multiple worker instances and seeing how it fare in comparision with the linear approach

public class Worker implements Runnable {
	
	public boolean running = false;
	
	public Worker()
	{
		Thread thread = new Thread(this);
		thread.start();
	}
	
	
	@Override
	public void run()
	{
		this.running = true;
		System.out.println("Currently running inside a separate thread :" + Thread.currentThread().getId());
		
		try
		{
				Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		this.running = false;
	}
	
	
	public static void main(String[] args) throws InterruptedException
	{
		List<Worker> workers = new ArrayList<Worker>();
		
		System.out.println("this is the main thread : " + Thread.currentThread().getId());
		// instantiate new workers
		for(int i = 0; i < 5 ; i ++)
		{
			workers.add(new Worker());
		}
		
		
		Date start = new Date();
		for(Worker w : workers)
		{
			System.out.println("Inside for");
			while(w.running)
			{
				Thread.sleep(100);
			}
		}
		
		Date end = new Date();
		long diff = end.getTime() - start.getTime();
		
		System.out.println("the whole process took : " + diff/1000 + " secs");
		// the idea here is the distribute the work among a number of threads so that the total run time 
		// comes down compared to the linear running time
		
	}

}
