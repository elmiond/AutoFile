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
		return filePath.getFileName().toString().matches(pattern);
	}

}
