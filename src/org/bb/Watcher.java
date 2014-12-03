/**
 * 
 */
package org.bb;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;

/**
 * Requests the operating system notify on folder events for configured paths.
 * Upon notification it runs the {@link org.bb.RuleSet} chain for that folder.
 * Reloads upon changes to the config file.
 * 
 * @author Ask Bisgaard <Elmiond@gmail.com>
 * @version 1.0
 * @since 2014-11-28
 * @see org.bb.WatchedFolder
 * @see org.bb.ConfigHandler
 */
public class Watcher implements Runnable
{
	int frequency, counter;

	/**
	 * {@link java.nio.file.WatchService} instance.
	 */
	private final WatchService watcher;

	/**
	 * ties event keys to the {@link org.bb.WatchedFolder} for lookup purposes.
	 */
	private HashMap<WatchKey, WatchedFolder> watchedfolders = new HashMap<WatchKey, WatchedFolder>();

	/**
	 * Path to config file.
	 */
	private final Path config = Paths.get("config.xml");

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event)
	{
		return (WatchEvent<T>) event;
	}

	/**
	 * Register the given directory with the WatchService.
	 * 
	 * @param dir
	 *          Path to the folder to be watched
	 * @param ruleSets
	 *          the associated RuleSets
	 * @throws IOException
	 * @see org.bb.RuleSet
	 */
	private void register(Path dir, ArrayList<RuleSet> ruleSets)
			throws IOException
	{
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
				ENTRY_MODIFY);
		if (this.watchedfolders.get(key) == null)
		{
			LogHandler.out(String.format("Registered folder: %s", dir),
					LogHandler.INFO);
		} else
		{
			Path prev = this.watchedfolders.get(key).folderPath;
			if (!dir.equals(prev))
			{
				LogHandler.out(String.format("Updated folder: %s -> %s", prev, dir),
						LogHandler.INFO);
			}
		}
		this.watchedfolders.put(key, new WatchedFolder(dir, ruleSets));
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 * 
	 * @param watchedfolder
	 *          the folder and it's associated RuleSets that is to be watched
	 * @see org.bb.WatchedFolder
	 */
	public void registerAll(final WatchedFolder watchedfolder) throws IOException
	{
		// register directory and sub-directories
		Files.walkFileTree(watchedfolder.folderPath, new SimpleFileVisitor<Path>()
			{
				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException
				{
					register(dir, new ArrayList<RuleSet>(watchedfolder.ruleSets));
					return FileVisitResult.CONTINUE;
				}
			});
	}

	/**
	 * Registers the WatchService
	 */
	Watcher() throws IOException
	{
		this.watcher = FileSystems.getDefault().newWatchService();
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	public void run()
	{
		LogHandler.out("====Starting service====", LogHandler.INFO);

		loadConfig();

		while (true)
		{

			// wait for key to be signalled
			WatchKey key;
			try
			{
				key = watcher.take();
			} catch (InterruptedException x)
			{
				return;
			}
			WatchedFolder watchedfolder = this.watchedfolders.get(key);
			if (watchedfolder == null || watchedfolder.folderPath == null)
			{
				LogHandler.out("WatchKey not recognized!!", LogHandler.WARNING);
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents())
			{
				WatchEvent.Kind kind = event.kind();

				// TODO decide how to handle OVERFLOW events
				if (kind == OVERFLOW)
				{
					fullRun(true);
				}

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = watchedfolder.folderPath.resolve(name);

				// if directory is AutoFile root directory
				if (child.toAbsolutePath().equals(config.toAbsolutePath()))
				{
					// discard events for AutoFile root dir not pertaining to the config
					// file
					if (child.getFileName().toString().equals("config.xml"))
					{
						LogHandler.out("====Configuration changed====", LogHandler.INFO);
						reload();
					}
				} else if (!child.getFileName().toString().startsWith("~")) // discard
																																		// windows
																																		// systemfiles
				{
					if (kind == ENTRY_CREATE)
					{
						try
						{
							if (Files.isDirectory(child, NOFOLLOW_LINKS)) // if event is
																														// creation of new
																														// directory,
																														// register it.
							{
								registerAll(watchedfolder);
							} else
							{
								watchedfolder.doWork(child);
							}
						} catch (IOException x)
						{
							// ignore to keep sample readable
						}
					}
					if (kind == ENTRY_MODIFY)
					{
						if (!Files.isDirectory(child, NOFOLLOW_LINKS))
						{
							watchedfolder.doWork(child);
						}
					}

				}
			}

			// reset key and remove from set if directory no longer accessible
			if (!key.reset())
			{
				this.watchedfolders.remove(key);

				// all directories are inaccessible
				if (this.watchedfolders.isEmpty())
				{
					break;
				}
			}
		}
	}

	/**
	 * clears service and reloads.
	 */
	public void reload()
	{
		this.watchedfolders.clear();

		// short sleep to avoid accessing the file before the operating system has
		// finished with it
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loadConfig();
	}

	/**
	 * loads the configuration via {@link org.bb.ConfigHandler}.
	 * 
	 * @see org.bb.ConfigHandler
	 */
	public void loadConfig()
	{
		frequency = ConfigHandler.getFrequency();

		ArrayList<WatchedFolder> watchedfolders;
		try
		{
			// register AutoFile root directory
			register(config.toAbsolutePath().getParent(), new ArrayList<RuleSet>());

			watchedfolders = ConfigHandler.getFolders();
			for (WatchedFolder watchedfolder : watchedfolders)
			{
				registerAll(watchedfolder);
			}
		} catch (ConfigurationException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fullRun(true);
	}

	public void fullRun(boolean forceRun)
	{
		if (forceRun || counter >= frequency)
		{
			counter = 0;
			LogHandler.out("====FULL RUN!!====", LogHandler.INFO);
			for (Entry<WatchKey, WatchedFolder> entry : watchedfolders.entrySet())
			{
				if (!entry.getValue().folderPath.toAbsolutePath().startsWith(
						Paths.get("").toAbsolutePath()))
				{
					ArrayList<Path> files = new ArrayList<Path>();

					try
					{
						files = listFiles(entry.getValue().folderPath);
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Path file : files)
					{
						entry.getValue().doWork(file);
						// LogHandler.out(file.toAbsolutePath().toString(),
						// LogHandler.INFO);
					}
				}
			}
		} else
		{
			counter++;
		}
	}

	private ArrayList<Path> listFiles(Path path) throws IOException
	{
		ArrayList<Path> files = new ArrayList<Path>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path))
		{
			for (Path entry : stream)
			{
				if (!Files.isDirectory(entry))
				{
					files.add(entry);
				}
			}
		}
		return files;
	}
}
