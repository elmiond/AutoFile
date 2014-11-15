package org.bb;

import java.nio.file.Path;

public interface MatchRule
{
	public boolean isMatch(Path filePath);
}
