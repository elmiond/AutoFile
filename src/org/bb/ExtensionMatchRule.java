package org.bb;

import java.nio.file.Path;

public class ExtensionMatchRule implements MatchRule
{
	public String extension = "";

	public ExtensionMatchRule(String extension)
	{
		this.extension = extension;
		LogHandler.out(
				String.format("Add | ExtensionMatchRule | Extension: %s", extension),
				LogHandler.INFO);
	}

	public boolean isMatch(Path filePath)
	{
		LogHandler.out(
				String.format("ExtensionMatch | %s | Extension[%s] | Matches? %s",
						filePath, extension, ((filePath.toString().toLowerCase()
								.endsWith(extension)) ? "YES" : "NO")), LogHandler.EVENT);
		return filePath.toString().toLowerCase().endsWith(extension);
	}

}
