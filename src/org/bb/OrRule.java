package org.bb;

import java.nio.file.Path;

public class OrRule extends MatchRuleCollection
{

	public boolean isMatch(Path filePath)
	{
		for (MatchRule matchRule : matchRules)
		{
			//LogHandler.out("ISMATCHRULE: "+((Boolean)(matchRule instanceof MatchRule)).toString(), LogHandler.EVENT);
			//LogHandler.out("ISMATCHRULECOLLECTION: "+((Boolean)(matchRule instanceof MatchRuleCollection)).toString(), LogHandler.EVENT);
			if (matchRule instanceof MatchRuleCollection)
			{
				MatchRuleCollection matchRuleCollection = (MatchRuleCollection)matchRule;
				if (matchRuleCollection.isMatch(filePath))
				{
					LogHandler.out("OrCollectionMatch: true", LogHandler.EVENT);
					return true;
				}
			}
			else
			{
				if (matchRule.isMatch(filePath))
				{
					LogHandler.out("OrCollectionMatch: true", LogHandler.EVENT);
					return true;
				}
			}
		}
		LogHandler.out("OrCollectionMatch: false", LogHandler.EVENT);
		return false;
	}
	
	public void add(MatchRule matchRule)
	{
		matchRules.add(matchRule);
	}
}
