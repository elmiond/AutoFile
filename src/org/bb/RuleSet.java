package org.bb;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Container for a single MatchSet and ActionSet, used by {@link org.bb.WatchedFolder} to tie them together.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-28
 * @see					org.bb.Action
 * @see					org.bb.MatchRule 
 * @see					org.bb.WatchedFolder
 */
public class RuleSet
{
	/**
	 * definition to judge the file by.
	 */
	MatchRule matchSet;
	
	/**
	 * list of actions to run on a matched file.
	 */
	ArrayList<Action> actionsSet = new ArrayList<Action>();
	
	/**
	 * Constructor.
	 * @param	matchSet		what criteria to match on
	 * @param	actionsSet	what actions to run on matching files
	 * @see								org.bb.Action
	 * @see								org.bb.MatchRule 
	 */
	public RuleSet(MatchRule matchSet, ArrayList<Action> actionsSet)
	{
		this.matchSet = matchSet;
		this.actionsSet = actionsSet;
	}

	/**
	 * Runs checks and possibly operations on file.
	 * @param	filePath	Path of file to be checked and possibly affected
	 */
	public void doWork(Path filePath)
	{
		if (matchSet.isMatch(filePath))
		{
			System.out.println("Matched: " + filePath);
			for (Action a : actionsSet)
			{
				ActionReturn actionreturn = a.doWork(filePath);
				if (actionreturn.wasSuccess)
				{
					filePath = actionreturn.newFilePath;
				}
				else
				{
					break;
				}
			}
		}
	}
}
