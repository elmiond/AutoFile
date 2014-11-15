package org.bb;

import java.nio.file.Path;

public class ExtensionMatchRule implements MatchRule
{
	public String extension = "";
	
	public ExtensionMatchRule(String extension)
	{
		LogHandler.out(extension, LogHandler.EVENT);
		this.extension = extension;
	}
	
	public boolean isMatch(Path filePath)
	{
		LogHandler.out(extension+" || " + filePath.toString().toLowerCase(), LogHandler.EVENT);
		return filePath.toString().toLowerCase().endsWith(extension);
	}

}
