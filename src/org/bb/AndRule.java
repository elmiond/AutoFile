package org.bb;

import java.nio.file.Path;

/**
 * MatchRuleCollection in which all MatchRules must return true.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 * @see					org.bb.MatchRuleCollection
 */
public class AndRule extends MatchRuleCollection
{
	/**
	 * Constructor
	 */
	public AndRule()
	{
		super();
		LogHandler.out("Add | AndMatchRuleCollection", LogHandler.INFO);
		LogHandler.out("====AndMatchRuleCollection Start====", LogHandler.INFO);
	}
	
	/**
	 * Checks MatchRules in collection to see all match definitions.
	 * @param	filePath	Path of file to be checked
	 * @return					true only if all MatchRules in collection return true, otherwise false
	 */
	public boolean isMatch(Path filePath)
	{
		LogHandler.out("====AndCollectionMatch====", LogHandler.MATCH);
		for (MatchRule matchRule : matchRules)
		{
			if (matchRule instanceof MatchRuleCollection)
			{
				MatchRuleCollection matchRuleCollection = (MatchRuleCollection)matchRule;
				if (!matchRuleCollection.isMatch(filePath))
				{
					LogHandler.out("====AndCollectionMatch | Matched? NO====", LogHandler.MATCH);
					return false;
				}
			}
			else
			{
				if (!matchRule.isMatch(filePath))
				{
					LogHandler.out("====AndCollectionMatch | Matched? NO====", LogHandler.MATCH);
					return false;
				}
			}
		}
		LogHandler.out("====AndCollectionMatch | Matched? YES====", LogHandler.MATCH);
		return true;
	}
}
