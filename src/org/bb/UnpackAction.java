package org.bb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.*;

/**
 * Action that uncompresses file.
 * @author      Morten Bondo	<Bondo.Morten@gmail.com>
 * @version     1.0
 * @since       2014-12-10
 * @see					org.bb.Action
 */
public class UnpackAction implements Action
{
	/**
	 * Unpacks the file.
	 * 
	 * @param filePath
	 *          Path of file to run operation on
	 * @return Path to the file and whether operation was a success
	 */
	@Override
	public ActionReturn doWork(Path filePath)
	{

		if (Files.isDirectory(filePath))
		{
			File[] folder = listFilesForFolder(filePath.toFile());
			for (File file : folder)
			{
				if (getFileExtension(file).equals("zip"))
				{
					if (unZip(file.toString(), file.getParent()))
						return new ActionReturn(file.toPath(), true);
					else
						return new ActionReturn(file.toPath(), false);
				}
			}
		} else
		{
			if (unZip(filePath.toString(), filePath.getParent().toString()))
				return new ActionReturn(filePath, true);
		}
		return new ActionReturn(filePath, false);

	}
	/**
	 * Gets all files in folder.
	 * 
	 * @param folder
	 *          Path to the folder
	 * @return files in folder
	 */
	public File[] listFilesForFolder(final File folder)
	{
		File[] filelist = folder.listFiles();
		return filelist;

	}

	/**
	 * Gets file extension.
	 * 
	 * @param file
	 *          Path of file to extension of
	 * @return File extension
	 */
	private String getFileExtension(File file)
	{
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1)
		{
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	/**
	 * Unpacks the file.
	 * 
	 * @param zipFile
	 *          file to unpack
	 * @param outputFolder
	 *          Where to upack to
	 * @return Whether operation was a success
	 */
	private boolean unZip(String zipFile, String outputFolder)
	{
		try
		{
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry ze = zis.getNextEntry();
			while (ze != null)
			{
				String entryName = ze.getName();
				System.out.print("Extracting " + entryName + " -> " + outputFolder
						+ File.separator + entryName + "...");
				File f = new File(outputFolder + File.separator + entryName);
				// create all folder needed to store in correct relative path.
				f.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(f);
				int len;
				byte buffer[] = new byte[1024];
				while ((len = zis.read(buffer)) > 0)
				{
					fos.write(buffer, 0, len);
				}
				fos.close();
				System.out.println("OK!");
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();

		} catch (Exception e)
		{
			return false;
		}

		return true;
	}

}
