package org.bb;

import java.nio.file.Path;

public interface Action
{
	public ActionReturn doWork(Path filePath);
}
