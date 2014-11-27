package org.bb;

import java.nio.file.Path;

/**
 * MatchRule that checks using regEx pattern.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 * @see					org.bb.MatchRule
 */
public class RegexMatchRule implements MatchRule
{
	/**
	 * regEx string to match on.
	 */
	public String pattern = "";

	/**
	 * Constructor.
	 * @param	pattern	regEx string to match on
	 */
	public RegexMatchRule(String pattern)
	{
		this.pattern = pattern;
		LogHandler.out(
				String.format("Add | RegexMatchRule | Pattern: %s", pattern),
				LogHandler.INFO);
	}

	/**
	 * Checks if file matches definition.
	 * @param	filePath	Path of file to be checked
	 * @return 					true if file matches regEx pattern
	 */
	public boolean isMatch(Path filePath)
	{
		LogHandler.out(String.format("RegexMatch | %s | Pattern[%s] | Matches? %s",
				filePath, pattern,
				((filePath.getFileName().toString().matches(pattern)) ? "YES" : "NO")),
				LogHandler.MATCH);
		return filePath.getFileName().toString().matches(pattern);
	}

}
