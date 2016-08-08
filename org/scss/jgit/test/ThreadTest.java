package org.scss.jgit.test;

public class ThreadTest implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Currently inside the second thread: " + Thread.currentThread().getId());
		
	}
	
	public static void main(String[] args)
	{
		System.out.println("Currently running on first thread :" + Thread.currentThread().getId());
		ThreadTest worker = new ThreadTest();
		Thread thread = new Thread(worker);
		thread.start();
	}

}
