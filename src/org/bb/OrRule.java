package org.bb;

import java.nio.file.Path;

/**
 * MatchRuleCollection in which at least one MatchRule must return true.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 * @see					org.bb.MatchRuleCollection
 */
public class OrRule extends MatchRuleCollection
{
	/**
	 * Constructor
	 */
	public OrRule()
	{
		super();
		LogHandler.out("Add | OrMatchRuleCollection", LogHandler.INFO);
		LogHandler.out("====OrMatchRuleCollection Start====", LogHandler.INFO);
	}
	
	/**
	 * Checks MatchRules in collection to see if at least one matches definition.
	 * @param	filePath	Path of file to be checked
	 * @return					true if at least one MatchRule in collection returns true, otherwise false
	 */
	public boolean isMatch(Path filePath)
	{
		LogHandler.out("====OrCollectionMatch====", LogHandler.MATCH);
		for (MatchRule matchRule : matchRules)
		{
			if (matchRule instanceof MatchRuleCollection)
			{
				MatchRuleCollection matchRuleCollection = (MatchRuleCollection)matchRule;
				if (matchRuleCollection.isMatch(filePath))
				{
					LogHandler.out("====OrCollectionMatch | Matched? YES====", LogHandler.MATCH);
					return true;
				}
			}
			else
			{
				if (matchRule.isMatch(filePath))
				{
					LogHandler.out("====OrCollectionMatch | Matched? YES====", LogHandler.MATCH);
					return true;
				}
			}
		}
		LogHandler.out("====OrCollectionMatch | Matched? NO====", LogHandler.MATCH);
		return false;
	}
}
