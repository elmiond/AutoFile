package org.bb;

import java.nio.file.Path;

/**
 * Package for replacing AutoFile variables with value in strings.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 */
class VarHandler
{

	/**
	 * Checks if file matches definition.
	 * @param	string	String to replace variables in
	 * @param	string	Path of file to get values from
	 * @return 				string with variables replaced with values
	 */
	public static String replace(String string, Path filePath)
	{
		string = string.replaceAll("AUTOFILE_PATH", filePath.toString().replace("\\", "\\\\"));
		string = string.replaceAll("AUTOFILE_FILENAME", filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.')));
		string = string.replaceAll("AUTOFILE_FILEEXT", filePath.toFile().getName().substring(filePath.toFile().getName().lastIndexOf('.') + 1));
		
		return string;
	}

}
