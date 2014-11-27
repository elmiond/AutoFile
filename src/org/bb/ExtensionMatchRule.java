package org.bb;

import java.nio.file.Path;

/**
 * MatchRule that checks the file-extension.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 * @see					org.bb.MatchRule
 */
public class ExtensionMatchRule implements MatchRule
{
	/**
	 * extension the file must have.
	 */
	public String extension = "";

	/**
	 * Constructor.
	 * @param	extension	extension the file must have
	 */
	public ExtensionMatchRule(String extension)
	{
		this.extension = extension;
		LogHandler.out(
				String.format("Add | ExtensionMatchRule | Extension: %s", extension),
				LogHandler.INFO);
	}

	/**
	 * Checks if file has desired extension.
	 * @param	filePath	Path of file to be checked
	 * @return 					true if file has desired extension
	 */
	public boolean isMatch(Path filePath)
	{
		LogHandler.out(
				String.format("ExtensionMatch | %s | Extension[%s] | Matches? %s",
						filePath, extension, ((filePath.toString().toLowerCase()
								.endsWith(extension)) ? "YES" : "NO")), LogHandler.MATCH);
		return filePath.toString().toLowerCase().endsWith(extension);
	}

}
