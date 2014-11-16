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
	}
	
	@Override
	public ActionReturn doWork(Path filePath)
	{
		//String cmd = "cmd /C C:\\test\\tt.bat \"AUTOFILE_FILENAME\"";

		cmd = cmd.replaceAll("AUTOFILE_PATH", filePath.toString().replace("\\", "\\\\"));
		cmd = cmd.replaceAll("AUTOFILE_FILENAME", filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.')));
		cmd = cmd.replaceAll("AUTOFILE_FILEEXT", filePath.toFile().getName().substring(filePath.toFile().getName().lastIndexOf('.') + 1));
		
		try
		{
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				System.out.println(inputLine);
			}
			in.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ActionReturn("", filePath, false);
		}
		return new ActionReturn("", filePath, true);
	}

}
