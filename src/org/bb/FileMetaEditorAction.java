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

public class FileMetaEditorAction implements Action
{
	public String action;
	public Date date;
	public boolean value;

	public FileMetaEditorAction(String action, Date time, boolean value) {
		//super();
		this.action = action;
		this.date = time;
		this.value = value;
	}

	/*public FileMetaEditorAction(String action, boolean value) {
		//super();
		this.action = action;
		this.value = value;
	}*/

	public ActionReturn doWork(Path filePath) {
		if (action.equals("creationTime")) {
			/* Change Created Time Stamp */
			try {
				Files.setAttribute(filePath, "basic:creationTime",
						FileTime.fromMillis(date.getTime()), NOFOLLOW_LINKS);
			} catch (IOException e) {
				return new ActionReturn(filePath, false);
			}
			return new ActionReturn(filePath, true);
		} else if (action.equals("lastModifiedTime")) {
			/* Change Created Time Stamp */
			try {
				Files.setAttribute(filePath, "basic:lastModifiedTime",
						FileTime.fromMillis(date.getTime()), NOFOLLOW_LINKS);
			} catch (IOException e) {
				return new ActionReturn(filePath, false);
			}
			return new ActionReturn(filePath, true);
		} else if (action.equals("lastAccessTime")) {
			/* Change Created Time Stamp */
			try {
				Files.setAttribute(filePath, "basic:lastAccessTime",
						FileTime.fromMillis(date.getTime()), NOFOLLOW_LINKS);
			} catch (IOException e) {
				return new ActionReturn(filePath, false);
			}
			return new ActionReturn(filePath, true);
		} else if (action.equals("hidden")) {
			/* Change windows attribute to hidden */
			try {
				if (OSValidator.isWindows())
					Files.setAttribute(filePath, "dos:hidden", value);
				else { /* Change other filesystems to hidden */
					if (!Files.isDirectory(filePath)) {
						if (value && !filePath.getFileName().startsWith(".")) {
							Path newPath = Paths.get((filePath.getParent()
									+ "." + filePath.getFileName())
									.toString());
							Files.move(filePath, newPath, REPLACE_EXISTING,
									COPY_ATTRIBUTES);
						} else if (!value
								&& filePath.getFileName().startsWith(".")) {
							Path newPath = Paths
									.get((filePath.getParent() + filePath
											.getFileName().toString()
											.substring(1)).toString());
							Files.move(filePath, newPath, REPLACE_EXISTING,
									COPY_ATTRIBUTES);
						}
					}
				}
			} catch (IOException e) {
				return new ActionReturn(filePath, false);
			}
			return new ActionReturn(filePath, true);
		} else if (action.equals("protected")) {
			try {
				Files.setAttribute(filePath, "dos:readonly", value);
			} catch (IOException e) {
				return new ActionReturn(filePath, false);
			}
		}
		return new ActionReturn(filePath, true);
	}
}
