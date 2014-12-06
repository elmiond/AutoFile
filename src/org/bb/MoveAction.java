package org.bb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Action used to move a file to a new destination.
 * 
 * @author Ask Bisgaard <Elmiond@gmail.com>
 * @version 1.0
 * @since 2014-11-27
 * @see org.bb.Action
 */
public class MoveAction implements Action
{
	/**
	 * destination to move the file to.
	 */
	public Path destination;

	/**
	 * Constructor.
	 * 
	 * @param destination
	 *          destination to move the file to
	 * @see org.bb.Action
	 */
	public MoveAction(Path destination)
	{
		this.destination = destination;
		LogHandler.out(
				String.format("Add | MoveAction | Destination: %s", destination),
				LogHandler.INFO);
	}

	/**
	 * Moves the file to the configured destination.
	 * 
	 * @param filePath
	 *          Path of file to run operation on
	 * @return new Path to the file and whether operation was a success
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
			
			Files.move(filePath, newFilePath);
			LogHandler.out(String.format(
					"MoveAction | Successfully moved: %s to: %s", filePath, newFilePath),
					LogHandler.ACTION);
			return new ActionReturn(newFilePath, true);
		} catch (IOException e)
		{
			LogHandler.out(String.format("MoveAction | Couldn't move: %s to: %s",
					filePath, newFilePath), LogHandler.ACTION);
			return new ActionReturn(filePath, false);
		}
	}
}
