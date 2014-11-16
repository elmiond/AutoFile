package org.bb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class LogHandler
{
	public static final int EVENT = 0;
	public static final int ERROR = 1;
	public static final int WARNING = 2;
	public static final int INFO = 3;

	private static final String eventLog = "events.log";
	private static final String errorLog = "errors.log";

	public static void out(String message, int kind)
	{
		PrintWriter writer;
		try
		{
			if (kind > 0)
			{
				checkFile(errorLog);
				switch (kind)
				{
				case 1:
					message = "ERROR   | " + message;
					break;
					
				case 2:
					message = "WARNING | " + message;
					break;
					
				case 3:
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
