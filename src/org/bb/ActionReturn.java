package src.org.bb;

import java.nio.file.Path;

public class ActionReturn
{
	public final Path newFilePath;
	public final boolean wasSuccess;
	

	public ActionReturn(String action, Path newFilePath, boolean wasSuccess)
	{
		this.newFilePath = newFilePath;
		this.wasSuccess = wasSuccess;
	}
}
	
