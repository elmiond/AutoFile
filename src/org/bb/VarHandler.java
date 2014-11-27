package org.bb;

import java.nio.file.Path;

class VarHandler
{

	public static String replace(String string, Path filePath)
	{
		string = string.replaceAll("AUTOFILE_PATH", filePath.toString().replace("\\", "\\\\"));
		string = string.replaceAll("AUTOFILE_FILENAME", filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.')));
		string = string.replaceAll("AUTOFILE_FILEEXT", filePath.toFile().getName().substring(filePath.toFile().getName().lastIndexOf('.') + 1));
		
		return string;
	}

}
