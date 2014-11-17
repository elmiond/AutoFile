package org.bb;

import java.nio.file.Path;

public class AndRule extends MatchRuleCollection
{
	public AndRule()
	{
		super();
		LogHandler.out("Add | AndMatchRuleCollection", LogHandler.INFO);
		LogHandler.out("====AndMatchRuleCollection Start====", LogHandler.INFO);
	}
	public boolean isMatch(Path filePath)
	{
		LogHandler.out("====AndCollectionMatch====", LogHandler.MATCH);
		for (MatchRule matchRule : matchRules)
		{
			//LogHandler.out("ISMATCHRULE: "+((Boolean)(matchRule instanceof MatchRule)).toString(), LogHandler.EVENT);
			//LogHandler.out("ISMATCHRULECOLLECTION: "+((Boolean)(matchRule instanceof MatchRuleCollection)).toString(), LogHandler.EVENT);
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

	public void add(MatchRule matchRule)
	{
		matchRules.add(matchRule);
	}
}
