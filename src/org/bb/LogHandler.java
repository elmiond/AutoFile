package org.bb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class LogHandler
{
	static final int EVENT = 0;
	static final int ERROR = 1;
	static final int WARNING = 2;

	static final String eventLog = "events.log";
	static final String errorLog = "errors.log";

	public static void out(String message, int kind)
	{
		PrintWriter writer;
		try
		{
			if (kind > 0)
			{
				checkFile(errorLog);
				writer = new PrintWriter(errorLog, "UTF-8");
				writer.println(message);
				writer.close();
				System.out.println(message);
			} else
			{
				checkFile(eventLog);
				writer = new PrintWriter(eventLog, "UTF-8");
				writer.println(message);
				writer.close();
				System.out.println(message);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e)
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
				Files.createFile(Paths.get(file));
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
