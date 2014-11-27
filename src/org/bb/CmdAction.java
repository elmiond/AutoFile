package org.bb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class CmdAction implements Action
{
	public String cmd;

	public CmdAction(String command)
	{
		super();
		this.cmd = command;
		LogHandler.out(String.format("Add | CommandAction | Command: %s", command), LogHandler.INFO);
	}
	
	@Override
	public ActionReturn doWork(Path filePath)
	{
		cmd = VarHandler.replace(cmd, filePath);
		
		try
		{
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			LogHandler.out("====Output Start====", LogHandler.INFO);
			while ((inputLine = in.readLine()) != null)
			{
				//System.out.println(inputLine);
				LogHandler.out(inputLine, LogHandler.INFO);
			}
			in.close();
			LogHandler.out("====Output End====", LogHandler.INFO);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogHandler.out("CommandAction | Error", LogHandler.ACTION);
			LogHandler.out("CommandAction | Error", LogHandler.ERROR);
			return new ActionReturn(filePath, false);
		}
		LogHandler.out("CommandAction | Success", LogHandler.ACTION);
		return new ActionReturn(filePath, true);
	}

}
