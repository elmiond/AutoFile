package org.bb;

import java.nio.file.Path;

public class AndRule extends MatchRuleCollection
{

	public boolean isMatch(Path filePath)
	{
		LogHandler.out("AndCollection: " + filePath, LogHandler.EVENT);
		for (MatchRule matchRule : matchRules)
		{
			//LogHandler.out("ISMATCHRULE: "+((Boolean)(matchRule instanceof MatchRule)).toString(), LogHandler.EVENT);
			//LogHandler.out("ISMATCHRULECOLLECTION: "+((Boolean)(matchRule instanceof MatchRuleCollection)).toString(), LogHandler.EVENT);
			if (matchRule instanceof MatchRuleCollection)
			{
				MatchRuleCollection matchRuleCollection = (MatchRuleCollection)matchRule;
				if (!matchRuleCollection.isMatch(filePath))
				{
					LogHandler.out("AndCollectionMatch: false", LogHandler.EVENT);
					return false;
				}
			}
			else
			{
				if (!matchRule.isMatch(filePath))
				{
					LogHandler.out("AndCollectionMatch: false", LogHandler.EVENT);
					return false;
				}
			}
		}
		LogHandler.out("AndCollectionMatch: true", LogHandler.EVENT);
		return true;
	}

	public void add(MatchRule matchRule)
	{
		matchRules.add(matchRule);
	}
}
