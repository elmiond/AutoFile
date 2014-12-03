package org.bb;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import org.bb.OSValidator;
import org.bb.ActionReturn;

public class FileMetaEditorAction {
	public Path destination;
	public String action;
	public Date date;
	public boolean value;

	public FileMetaEditorAction(Path destination, String action, Date time) {
		super();
		this.destination = destination;
		this.action = action;
		this.date = time;
	}

	public FileMetaEditorAction(Path destination, String action, boolean value) {
		super();
		this.destination = destination;
		this.action = action;
		this.date = null;
		this.value = value;
	}

	public ActionReturn doWork(Path destination) {
		if (action.equals("creationTime")) {
			/* Change Created Time Stamp */
			try {
				Files.setAttribute(destination, "basic:creationTime",
						FileTime.fromMillis(date.getTime()), NOFOLLOW_LINKS);
			} catch (IOException e) {
				return new ActionReturn(destination, false);
			}
			return new ActionReturn(destination, true);
		} else if (action.equals("lastModifiedTime")) {
			/* Change Created Time Stamp */
			try {
				Files.setAttribute(destination, "basic:lastModifiedTime",
						FileTime.fromMillis(date.getTime()), NOFOLLOW_LINKS);
			} catch (IOException e) {
				return new ActionReturn(destination, false);
			}
			return new ActionReturn(destination, true);
		} else if (action.equals("lastAccessTime")) {
			/* Change Created Time Stamp */
			try {
				Files.setAttribute(destination, "basic:lastAccessTime",
						FileTime.fromMillis(date.getTime()), NOFOLLOW_LINKS);
			} catch (IOException e) {
				return new ActionReturn(destination, false);
			}
			return new ActionReturn(destination, true);
		} else if (action.equals("hidden")) {
			/* Change windows attribute to hidden */
			try {
				if (OSValidator.isWindows())
					Files.setAttribute(destination, "dos:hidden", value);
				else { /* Change other filesystems to hidden */
					if (!Files.isDirectory(destination)) {
						if (value && !destination.getFileName().startsWith(".")) {
							Path newPath = Paths.get((destination.getParent()
									+ "." + destination.getFileName())
									.toString());
							Files.move(destination, newPath, REPLACE_EXISTING,
									COPY_ATTRIBUTES);
						} else if (!value
								&& destination.getFileName().startsWith(".")) {
							Path newPath = Paths
									.get((destination.getParent() + destination
											.getFileName().toString()
											.substring(1)).toString());
							Files.move(destination, newPath, REPLACE_EXISTING,
									COPY_ATTRIBUTES);
						}
					}
				}
			} catch (IOException e) {
				return new ActionReturn(destination, false);
			}
			return new ActionReturn(destination, true);
		} else if (action.equals("protected")) {
			try {
				Files.setAttribute(destination, "dos:readonly", value);
			} catch (IOException e) {
				return new ActionReturn(destination, false);
			}
		}
		return new ActionReturn(destination, true);
	}
}
