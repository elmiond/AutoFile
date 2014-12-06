package org.bb;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		string = string.replaceAll("AUTOFILE_PATH", filePath.getParent().toAbsolutePath().toString().replace("\\", "\\\\"));
		string = string.replaceAll("AUTOFILE_FULLFILENAME", filePath.toFile().getName().toString());
		string = string.replaceAll("AUTOFILE_FILENAME", filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.')));
		string = string.replaceAll("AUTOFILE_FILEEXT", filePath.toFile().getName().substring(filePath.toFile().getName().lastIndexOf('.') + 1));
		
		Date now = new Date();
		string = string.replaceAll("AUTOFILE_YEAR", new SimpleDateFormat( "yyyy" ).format(now));
		string = string.replaceAll("AUTOFILE_MONTH", new SimpleDateFormat( "MMM" ).format(now));
		string = string.replaceAll("AUTOFILE_WEEK", new SimpleDateFormat( "'W'ww" ).format(now));
		string = string.replaceAll("AUTOFILE_WEEKDAY", new SimpleDateFormat( "EEE" ).format(now));
		string = string.replaceAll("AUTOFILE_DAY", new SimpleDateFormat( "dd" ).format(now));
		string = string.replaceAll("AUTOFILE_DATE", new SimpleDateFormat( "yyyy.MM.dd" ).format(now));
		
		return string;
	}

}
