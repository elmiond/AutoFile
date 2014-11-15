package org.bb;

public class Service
{

	public static void main(String[] args) throws Exception
	{
		Watcher w = new Watcher();

		
		
	// TODO Add loop to check and restart service if it crashes

		Thread t = new Thread(w);
		t.start();

	}

}