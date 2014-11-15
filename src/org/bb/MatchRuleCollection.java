package org.bb;

import java.util.ArrayList;

public abstract class MatchRuleCollection implements MatchRule
{
	ArrayList<MatchRule> matchRules = new ArrayList<MatchRule>();
	ArrayList<MatchRuleCollection> matchRuleCollections = new ArrayList<MatchRuleCollection>();
	public void add(MatchRule matchRule)
	{
		
	}
}
