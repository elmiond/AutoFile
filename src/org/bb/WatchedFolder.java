package org.bb;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Ties one or more RuleSets to a folder.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-28
 * @see					org.bb.RuleSet
 */
public class WatchedFolder
{
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
	 * @param	folderPath	what criteria to match on
	 * @param	ruleSets		what actions to run on matching files
	 * @see								org.bb.RuleSet
	 */
	public WatchedFolder(Path folderPath, ArrayList<RuleSet> ruleSets)
	{
		this.folderPath = folderPath;
		this.ruleSets = ruleSets;
	}
	
	/**
	 * Tell all contained RuleSets to run checks and actions.
	 * @param	filePath	Path of file to be checked and possibly affected
	 */
	public void doWork(Path path)
	{
		for (RuleSet ruleSet : ruleSets)
		{
			ruleSet.doWork(path);
		}
	}
}