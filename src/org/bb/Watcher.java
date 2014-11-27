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
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;

/**
 * @author Elmiond
 *
 */
public class Watcher implements Runnable
{

	private final WatchService watcher;
	private Map<WatchKey, WatchedFolder> watchedfolders = new HashMap<WatchKey, WatchedFolder>();
	private final Path config = Paths.get("config.xml");

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event)
	{
		return (WatchEvent<T>) event;
	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void register(Path dir, ArrayList<RuleSet> ruleSets)
			throws IOException
	{
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
				ENTRY_MODIFY);
		if (this.watchedfolders.get(key) == null)
		{
			LogHandler.out(String.format("Registered folder: %s", dir), LogHandler.INFO);
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
	 * Creates a WatchService and registers the given directory
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
				//System.err.println("WatchKey not recognized!!");
				LogHandler.out("WatchKey not recognized!!", LogHandler.WARNING);
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents())
			{
				WatchEvent.Kind kind = event.kind();

				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW)
				{
					continue;
				}

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = watchedfolder.folderPath.resolve(name);

				// print out event
				//System.out.format("%s: %s\n", event.kind().name(), child);
				//LogHandler.out(String.format("%s: %s\n", event.kind().name(), child), LogHandler.WARNING);

				// if directory is created, and watching recursively, then
				// register it and its sub-directories

				if (child.toAbsolutePath().equals(config.toAbsolutePath()))
				{
					// discard events for AutoFile root dir not pertaining to the config
					// file
					if (child.getFileName().toString().equals("config.xml"))
					{
						//System.out.println("====config changed====");
						LogHandler.out("====Configuration changed====", LogHandler.INFO);
						clearWatchlist();
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
				} else if (!child.getFileName().toString().startsWith("~"))
				{
					if (kind == ENTRY_CREATE)
					{
						try
						{
							if (Files.isDirectory(child, NOFOLLOW_LINKS))
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

	public void clearWatchlist()
	{
		this.watchedfolders.clear();
	}

	public void loadConfig()
	{
		ConfigHandler ch = new ConfigHandler();
		ArrayList<WatchedFolder> watchedfolders;
		try
		{
			register(config.toAbsolutePath().getParent(), new ArrayList<RuleSet>());
			watchedfolders = ch.getFolders();
			for (WatchedFolder watchedfolder : watchedfolders)
			{
				registerAll(watchedfolder);
			}
		} catch (ConfigurationException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
