package org.bb;

import java.util.ArrayList;

public interface MatchRuleCollection extends MatchRule
{
	ArrayList<MatchRule> matchRules = new ArrayList<MatchRule>();
	public void add(MatchRule matchRule);
	public void remove(int index);
}
