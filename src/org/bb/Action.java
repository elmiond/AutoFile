package org.bb;

import java.nio.file.Path;

/**
 * Interface for Actions to allow custom actions via doWork().
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 */
public interface Action
{
	/**
	 * Runs operations on file.
	 * @param	filePath	Path of file to be affected
	 */
	public ActionReturn doWork(Path filePath);
}
