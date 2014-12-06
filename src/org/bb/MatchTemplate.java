package org.bb;

import java.nio.file.Path;

/**
 * MatchRule that checks using [...].
 * @author      firstname lastname	<email@domain.tld>
 * @version     0.0
 * @since       
 * @see					org.bb.MatchRule
 */
public class MatchTemplate implements MatchRule
{
	/**
	 * Put variables that holds the configuration here
	 */

	/**
	 * Constructor, assign configuration using this.
	 */
	public MatchTemplate()
	{
		LogHandler.out("Add | MatchTemplate", LogHandler.INFO);
	}

	/**
	 * Checks if file matches definition.
	 * @param	filePath	Path of file to be checked
	 * @return 					true if [...]
	 */
	public boolean isMatch(Path filePath)
	{
		LogHandler.out(String.format("MatchTemplate | %s | Matches? %s",
				filePath, (false ? "YES" : "NO")),
				LogHandler.MATCH);
		return false;
	}
}
