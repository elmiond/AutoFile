package src.org.bb;

import java.util.ArrayList;

public interface MatchRuleCollection extends MatchRule
{
	ArrayList<MatchRule> MatchRules = new ArrayList<MatchRule>();
	public void add(MatchRule matchRule);
	public void remove(int index);
}
