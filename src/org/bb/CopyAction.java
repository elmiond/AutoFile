package org.bb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Action used to copy a file to a new destination.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 * @see					org.bb.Action
 */
public class CopyAction implements Action
{
	/**
	 * destination to put the copy in.
	 */
	public Path destination;

	/**
	 * Constructor.
	 * @param	destination	destination to put the copy in
	 * @see								org.bb.Action 
	 */
	public CopyAction(Path destination)
	{
		this.destination = destination;
		LogHandler.out(String.format("Add | CopyAction | Destination: %s", destination), LogHandler.INFO);
	}

	/**
	 * Copies the file to the configured destination.
	 * @param	filePath	Path of file to run operation on
	 * @return 					Path to the copy and whether operation was a success
	 */
	public ActionReturn doWork(Path filePath)
	{
		destination = Paths.get(VarHandler.replace(destination.toString(), filePath));
		
		Path newFilePath = destination.resolve(filePath.getFileName());
		try
		{
			int i = 1;
			while (newFilePath.toFile().exists())
			{
				String name = filePath.getFileName().toString();
				int index = name.contains(".") ? name.lastIndexOf('.') : name.length();
				newFilePath = destination.resolve(name.substring(0, index) + " (" + i
						+ ")" + name.substring(index));
				i++;
			}

			destination.toFile().mkdirs();
			Files.copy(filePath, newFilePath);
			LogHandler.out(String.format("CopyAction | Successfully copied: %s to: %s", filePath, newFilePath), LogHandler.ACTION);
			return new ActionReturn(newFilePath, true);
		} catch (IOException e)
		{
			LogHandler.out(String.format("CopyAction | Couldn't copy: %s to: %s", filePath, newFilePath), LogHandler.ACTION);
			return new ActionReturn(filePath, false);
		}
	}
}