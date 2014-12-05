package org.bb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackagerAction implements Action {
	/**
	 * destination to put the zipfile in and the compressionlevel.
	 */
	public Path output;
	public int compressionlevel = Deflater.DEFAULT_COMPRESSION;

	/**
	 * Constructor.
	 * 
	 * @param destination
	 *            destination to put the zipfile in
	 * @see org.bb.Action
	 */
	public PackagerAction(Path destination) {
		super();
		this.output = destination;
		LogHandler.out(String.format("Add | PackagerAction | Destination: %s | compressionlevel: Default",
				destination), LogHandler.INFO);
	}
	public PackagerAction(Path destination, int compressionlevel) {
		super();
		this.output = destination;
		this.compressionlevel = compressionlevel;
		LogHandler.out(String.format("Add | PackagerAction | Destination: %s | compressionlevel: %s",
				destination, compressionlevel), LogHandler.INFO);
	}

	/**
	 * Copies the file to the configured destination.
	 * 
	 * @param filePath
	 *            Path of file to run operation on
	 * @return Path to the zip and whether operation was a success
	 * @throws IOException 
	 */
	public ActionReturn doWork(Path filePath) {
		return new ActionReturn(output, packZip(output.toFile(), filePath.toFile(), compressionlevel));
	}

	static boolean packZip(File output, File source, int compressionlevel){
		try {
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
					output));
			zipOut.setLevel(compressionlevel);
			zipFile(zipOut, "", source);
			zipOut.flush();
			zipOut.close();
			LogHandler.out(String.format("PackagerAction | Successfully packed: %s to: %s", source, output), LogHandler.ACTION);
			return true;
		} catch (IOException e) {
			LogHandler.out(String.format("PackagerAction | Couldn't pack: %s to: %s", source, output), LogHandler.ACTION);
			return false;
		}
	}

	private static String buildPath(String path, String file) {
		if (path == null || path.isEmpty()) {
			return file;
		} else {
			return path + "/" + file;
		}
	}

	private static void zipFile(ZipOutputStream zos, String path, File file)
			throws IOException {
		if (!file.canRead()) {
			LogHandler.out(String.format("PackagerAction | Couldn't read file: %s", (path + file.getName())), LogHandler.ACTION);
			return;
		}
		zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

		FileInputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[4092];
		int byteCount = 0;
		while ((byteCount = fis.read(buffer)) != -1) {
			zos.write(buffer, 0, byteCount);
		}

		fis.close();
		zos.closeEntry();
	}
}
