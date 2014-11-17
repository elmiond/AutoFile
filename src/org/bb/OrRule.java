package org.bb;

import java.nio.file.Path;

public class OrRule extends MatchRuleCollection
{
	public OrRule()
	{
		super();
		LogHandler.out("Add | OrMatchRuleCollection", LogHandler.INFO);
		LogHandler.out("====OrMatchRuleCollection Start====", LogHandler.INFO);
	}
	public boolean isMatch(Path filePath)
	{
		LogHandler.out("====OrCollectionMatch====", LogHandler.MATCH);
		for (MatchRule matchRule : matchRules)
		{
			//LogHandler.out("ISMATCHRULE: "+((Boolean)(matchRule instanceof MatchRule)).toString(), LogHandler.EVENT);
			//LogHandler.out("ISMATCHRULECOLLECTION: "+((Boolean)(matchRule instanceof MatchRuleCollection)).toString(), LogHandler.EVENT);
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
	
	public void add(MatchRule matchRule)
	{
		matchRules.add(matchRule);
	}
}
