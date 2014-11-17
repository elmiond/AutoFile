package org.bb;

import java.nio.file.Path;

public class RegexMatchRule implements MatchRule
{
	public String pattern = "";

	public RegexMatchRule(String pattern)
	{
		this.pattern = pattern;
		LogHandler.out(
				String.format("Add | RegexMatchRule | Pattern: %s", pattern),
				LogHandler.INFO);
	}

	public boolean isMatch(Path filePath)
	{
		LogHandler.out(String.format("RegexMatch | %s | Pattern[%s] | Matches? %s",
				filePath, pattern,
				((filePath.getFileName().toString().matches(pattern)) ? "YES" : "NO")),
				LogHandler.MATCH);
		return filePath.getFileName().toString().matches(pattern);
	}

}
