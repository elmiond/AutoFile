package org.bb;

import java.nio.file.Path;

/**
 * Struct like class used by Action to return the new filepath and operation result.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 */
public class ActionReturn
{
	/**
	 * new Path to the file.
	 */
	public final Path newFilePath;
	
	/**
	 * whether operation was a success.
	 */
	public final boolean wasSuccess;
	
	/**
	 * Constructor.
	 * @param	newFilePath	new Path to the file
	 * @param	wasSuccess	whether operation was a success
	 * @see								org.bb.Action 
	 */
	public ActionReturn(Path newFilePath, boolean wasSuccess)
	{
		this.newFilePath = newFilePath;
		this.wasSuccess = wasSuccess;
	}
}
	
