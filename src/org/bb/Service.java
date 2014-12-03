package org.bb;

/**
 * Starts the {@link org.bb.Watcher} thread, is intended to restart it in case
 * it crashes.
 * 
 * @author Ask Bisgaard <Elmiond@gmail.com>
 * @version 1.0
 * @since 2014-11-28
 * @see org.bb.Watcher
 */
public class Service
{
	/**
	 * Main, starts the entire service.
	 * 
	 * @param args
	 *          list of runtime arguments, currently unused
	 * @see org.bb.RuleSet
	 */
	public static void main(String[] args) throws Exception
	{
		Watcher watcher = new Watcher();

		// TODO Add loop to check and restart service if it crashes

		Thread thread = new Thread(watcher);

		while (true)
		{
			if (!thread.isAlive())
			{
				thread.start();
			}
			watcher.fullRun(false);

			Thread.sleep(1000);
		}
	}

}