package org.bb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CopyAction implements Action
{
	public Path destination;

	public CopyAction(Path destination)
	{
		super();
		this.destination = destination;
		LogHandler.out(String.format("Add | CopyAction | Destination: %s", destination), LogHandler.INFO);
	}

	public ActionReturn doWork(Path filePath)
	{
		Path newFilePath = destination.resolve(filePath.getFileName());
		// System.out.println(newFilePath);
		try
		{
			int i = 0;
			while (!filePath.toFile().exists())
			{
				if (i >= 120)
				{
					break;
				}
				Thread.sleep(100);
				i++;
			}
			if (!Files.notExists(newFilePath))
			{
				try
				{
					Files.createDirectory(newFilePath);
				} catch (Exception e)
				{
					// TODO: handle exception
				}
			}

			i = 1;
			while (newFilePath.toFile().exists())
			{
				String name = filePath.getFileName().toString();
				int index = name.contains(".") ? name.lastIndexOf('.') : name.length();
				newFilePath = destination.resolve(name.substring(0, index) + " (" + i
						+ ")" + name.substring(index));
				i++;
			}

			Files.copy(filePath, newFilePath);
			LogHandler.out(String.format("CopyAction | Successfully copied: %s to: %s", filePath, newFilePath), LogHandler.EVENT);
			return new ActionReturn(newFilePath, true);
		} catch (IOException | InterruptedException e)
		{
			LogHandler.out(String.format("CopyAction | Couldn't copy: %s to: %s", filePath, newFilePath), LogHandler.ERROR);
			return new ActionReturn(filePath, false);
		}
	}
}