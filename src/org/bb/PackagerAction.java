package org.bb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Action that compresses file into an archive.
 * @author      Morten Bondo	<Bondo.Morten@gmail.com>
 * @version     1.0
 * @since       2014-12-10
 * @see					org.bb.Action
 */
public class PackagerAction implements Action
{
	/**
	 * destination to put the zipfile.
	 */
	public Path destination;
	/**
	 * the compressionlevel.
	 */
	public int compressionlevel = Deflater.DEFAULT_COMPRESSION;

	/**
	 * Constructor.
	 * 
	 * @param destination
	 *          destination to put the zipfile in
	 * @see org.bb.Action
	 */
	public PackagerAction(Path destination)
	{
		this.destination = destination;
		LogHandler.out(String.format(
				"Add | PackagerAction | Destination: %s | compressionlevel: Default",
				destination), LogHandler.INFO);
	}

	/**
	 * Constructor.
	 * 
	 * @param destination
	 *          destination to put the zipfile in
	 * @param compressionlevel
	 *          compression level
	 * @see org.bb.Action
	 */
	public PackagerAction(Path destination, int compressionlevel)
	{
		this.destination = destination;
		this.compressionlevel = compressionlevel;
		LogHandler.out(String.format(
				"Add | PackagerAction | Destination: %s | compressionlevel: %s",
				destination, compressionlevel), LogHandler.INFO);
	}

	/**
	 * Compresses the file to the configured destination.
	 * 
	 * @param filePath
	 *          Path of file to run operation on
	 * @return Path to the zip and whether operation was a success
	 * @throws IOException
	 */
	public ActionReturn doWork(Path filePath)
	{
		destination = Paths.get(VarHandler.replace(destination.toString(), filePath));
		destination.getParent().toFile().mkdirs();
		return new ActionReturn(destination, packZip(destination.toFile(), filePath.toFile(),
				compressionlevel));
	}

	/**
	 * Packs as zip.
	 * 
	 * @param output
	 *          where to put the file
	 * @param source
	 *          Path of file to run operation on
	 * @param compressionlevel
	 *          compression level
	 * @return whether operation was a success
	 */
	static boolean packZip(File output, File source, int compressionlevel)
	{
		try
		{
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output));
			zipOut.setLevel(compressionlevel);
			zipFile(zipOut, "", source);
			zipOut.flush();
			zipOut.close();
			LogHandler.out(String.format(
					"PackagerAction | Successfully packed: %s to: %s", source, output),
					LogHandler.ACTION);
			return true;
		} catch (IOException e)
		{
			LogHandler.out(String.format("PackagerAction | Couldn't pack: %s to: %s",
					source, output), LogHandler.ACTION);
			return false;
		}
	}

	/**
	 * Builds destination string.
	 * 
	 * @param path
	 *          filepath
	 * @param file
	 *          filename
	 * @return destination string
	 */
	private static String buildPath(String path, String file)
	{
		if (path == null || path.isEmpty())
		{
			return file;
		} else
		{
			return path + "/" + file;
		}
	}
	/**
	 * Compresses using zip.
	 * 
	 * @param zos
	 *          ZipOutputStream to compress through
	 * @param path
	 *          filepath
	 * @param file
	 *          filename
	 * @throws IOException
	 */
	private static void zipFile(ZipOutputStream zos, String path, File file)
			throws IOException
	{
		if (!file.canRead())
		{
			LogHandler.out(
					String.format("PackagerAction | Couldn't read file: %s",
							(path + file.getName())), LogHandler.ACTION);
			return;
		}
		zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

		FileInputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[4092];
		int byteCount = 0;
		while ((byteCount = fis.read(buffer)) != -1)
		{
			zos.write(buffer, 0, byteCount);
		}

		fis.close();
		zos.closeEntry();
	}
}
