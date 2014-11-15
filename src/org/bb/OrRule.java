package src.org.bb;

import java.nio.file.Path;

public class OrRule implements MatchRuleCollection
{

	public boolean isMatch(Path filePath)
	{
		for (MatchRule matchRule : matchRules)
		{
			if (matchRule.isMatch(filePath))
			{
				return true;
			}
		}
		return false;
	}
	
	public void add(MatchRule matchRule)
	{
		matchRules.add(matchRule);
	}

	public void remove(int index)
	{
		matchRules.remove(index);
	}
}
