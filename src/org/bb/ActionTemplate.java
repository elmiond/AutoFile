package org.bb;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * MatchRule that checks using [...].
 * @author      firstname lastname	<email@domain.tld>
 * @version     0.0
 * @since       
 * @see					org.bb.MatchRule
 */
public class ActionTemplate implements Action
{
	/**
	 * Put variables that holds the configuration here
	 */

	/**
	 * Constructor, assign configuration using this.
	 */
	public ActionTemplate()
	{
		LogHandler.out("Add | MoveAction ", LogHandler.INFO);
	}

	/**
	 * Does something.
	 * 
	 * @param filePath
	 *          Path of file to run operation on
	 * @return new Path to the file and whether operation was a success
	 */
	public ActionReturn doWork(Path filePath)
	{
		Path newFilePath = Paths.get("");
		if (true)
		{
			LogHandler.out("ActionTemplate ", LogHandler.ACTION);
			return new ActionReturn(newFilePath, true);
		}
		else
		{
			LogHandler.out("ActionTemplate ", LogHandler.ACTION);
			return new ActionReturn(filePath, false);
		}
		
	}
}
