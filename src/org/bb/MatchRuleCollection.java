package org.bb;

import java.util.ArrayList;

/**
 * Collection of MatchRules.
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-27
 */
public abstract class MatchRuleCollection implements MatchRule
{
	/**
	 * ArrayList of MatchRules to run isMatch() on.
	 */
	protected ArrayList<MatchRule> matchRules = new ArrayList<MatchRule>();
	
	/**
	 * Adds a MatchRule to collection.
	 * @param	matchRule	MatchRule to add to collection
	 */
	public void add(MatchRule matchRule)
	{
		matchRules.add(matchRule);
	}
}
