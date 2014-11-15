package org.bb;

import java.nio.file.Path;

public class RegexMatchRule implements MatchRule
{
	public String pattern = "";
	
	public RegexMatchRule(String pattern)
	{
		this.pattern = pattern;
	}
	
	public boolean isMatch(Path filePath)
	{
		LogHandler.out("Regex: " + filePath, LogHandler.EVENT);
		LogHandler.out("RegexPattern: " + pattern, LogHandler.EVENT);
		LogHandler.out("RegexMatch: " +  filePath.getFileName().toString().matches(pattern), LogHandler.EVENT);
		return filePath.getFileName().toString().matches(pattern);
	}

}
