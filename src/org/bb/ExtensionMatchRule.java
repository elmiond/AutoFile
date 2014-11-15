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
		LogHandler.out("Extension: " + filePath, LogHandler.EVENT);
		LogHandler.out("Extension: " + extension, LogHandler.EVENT);
		LogHandler.out(
				"ExtensionMatch: " + filePath.toString().toLowerCase().endsWith(extension),
				LogHandler.EVENT);
		return filePath.toString().toLowerCase().endsWith(extension);
	}

}
