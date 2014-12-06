package org.bb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * Action used to run a custom commandline.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 * @see					org.bb.Action
 */
public class CmdAction implements Action
{
	/**
	 * command to be run.
	 */
	public String cmd;

	/**
	 * Constructor.
	 * @param	command			command to be run
	 * @see								org.bb.Action 
	 */
	public CmdAction(String command)
	{
		this.cmd = command;
		LogHandler.out(String.format("Add | CommandAction | Command: %s", command), LogHandler.INFO);
	}
	
	/**
	 * Runs the configured commandline, uses AutoFile variables.
	 * @param	filePath	Path of file to run operation on
	 * @return 					filePath and whether operation was a success
	 */
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
				LogHandler.out(inputLine, LogHandler.INFO);
			}
			in.close();
			LogHandler.out("====Output End====", LogHandler.INFO);
		} catch (IOException e)
		{
			e.printStackTrace();
			LogHandler.out("CommandAction | Error", LogHandler.ACTION);
			LogHandler.out("CommandAction | Error", LogHandler.ERROR);
			return new ActionReturn(filePath, false);
		}
		LogHandler.out("CommandAction | Success", LogHandler.ACTION);
		return new ActionReturn(filePath, true);
	}

}
