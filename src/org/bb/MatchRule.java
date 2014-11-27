package org.bb;

import java.nio.file.Path;

/**
 * Interface for MatchRules to allow checks via isMatch().
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 */
public interface MatchRule
{
	/**
	 * Checks if file matches definition.
	 * @param	filePath	Path of file to be checked
	 * @return 					true if file matches definition
	 */
	public boolean isMatch(Path filePath);
}
