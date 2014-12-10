package org.bb;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Ties one or more RuleSets to a folder.
 * 
 * @author Ask Bisgaard <Elmiond@gmail.com>
 * @version 1.0
 * @since 2014-11-28
 * @see org.bb.RuleSet
 */
public class WatchedFolder extends Thread
{
	private ArrayList<Path> paths;
	Thread t;
	/**
	 * path to the watched folder.
	 */
	Path folderPath;

	/**
	 * list of rulesets to judge and work by.
	 */
	ArrayList<RuleSet> ruleSets;

	/**
	 * Constructor.
	 * 
	 * @param folderPath
	 *          what criteria to match on
	 * @param ruleSets
	 *          what actions to run on matching files
	 * @see org.bb.RuleSet
	 */
	public WatchedFolder(Path folderPath, ArrayList<RuleSet> ruleSets)
	{
		this.folderPath = folderPath;
		this.ruleSets = ruleSets;
		paths = new ArrayList<Path>();
		t = new Thread(this);
	}

	/**
	 * Tell all contained RuleSets to run checks and actions.
	 * 
	 * @param filePath
	 *          Path of file to be checked and possibly affected
	 */
	public void doWork(Path path)
	{
		this.paths.add(path);
		if (!t.isAlive())
		{
			t = new Thread(this);
			t.start();
		}
	}
	
	/**
	 * Asynchronously process all events queued by the watcher
	 */
	public void run()
	{
		while (paths.size() > 0)
		{
			ArrayList<Path> temp = new ArrayList<Path>();
			temp.addAll(paths);
			for (Path path : temp)
			{
				if (Calendar.getInstance().getTimeInMillis() > path.toFile()
						.lastModified() + 2000L && path.toFile().length() > 0)
				{
					if (path.toFile().exists())
					{
						for (RuleSet ruleSet : ruleSets)
						{
							ruleSet.doWork(path);
						}
					}
					paths.remove(path);
				}
			}
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}