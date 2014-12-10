package org.bb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles all interactions with the logfiles.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-28
 */
public final class LogHandler
{
	public static final int MATCH = 0;
	public static final int ACTION = 1;
	public static final int ERROR = 2;
	public static final int WARNING = 3;
	public static final int INFO = 4;

	private static final String eventLog = "events.log";
	private static final String errorLog = "errors.log";

	/**
	 * Writes to log.
	 * @param	message	message
	 * @param	kind	type of log
	 */
	public static void out(String message, int kind)
	{
		PrintWriter writer;
		try
		{
			if (kind > 1)
			{
				checkFile(errorLog);
				switch (kind)
				{
				case ERROR:
					message = "ERROR   | " + message;
					break;
					
				case WARNING:
					message = "WARNING | " + message;
					break;
					
				case INFO:
					message = "INFO    | " + message;
					break;

				default:
					break;
				}

				writer = new PrintWriter(new BufferedWriter(new FileWriter(errorLog,
						true)));
				writer.println(message);
				writer.close();
				System.out.println(message);
			} else
			{
				checkFile(eventLog);
				switch (kind)
				{
				case MATCH:
					message = "MATCH   | " + message;
					break;
					
				case ACTION:
					message = "ACTION  | " + message;
					break;

				default:
					break;
				}
				writer = new PrintWriter(new BufferedWriter(new FileWriter(eventLog,
						true)));
				writer.println(message);
				writer.close();
				System.out.println(message);
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new logfile if it does not already exist.
	 * @param	file	file
	 */
	private static void checkFile(String file)
	{

		if (Files.notExists(Paths.get(file)))
		{
			try
			{
				//System.out.println("Creating " + file);
				Files.createFile(Paths.get(file));
				LogHandler.out(String.format("Creating: %s", file), LogHandler.INFO);
			} catch (IOException e)
			{
				LogHandler.out(String.format("Creating: %s", file), LogHandler.ERROR);
				e.printStackTrace();
			}
		}
	}
}
