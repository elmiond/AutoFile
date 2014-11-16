package org.bb;

import java.nio.file.Path;
import java.util.ArrayList;

public class RuleSet
{
	MatchRule matchRule;
	ArrayList<Action> actions = new ArrayList<Action>();
	
	public RuleSet(MatchRule matchRule, ArrayList<Action> actions)
	{
		this.matchRule = matchRule;
		this.actions = actions;
	}

	public void doWork(Path path)
	{
		if (matchRule.isMatch(path))
		{
			System.out.println("Matched: " + path);
			for (Action a : actions)
			{
				ActionReturn actionreturn = a.doWork(path);
				if (actionreturn.wasSuccess)
				{
					path = actionreturn.newFilePath;
				}
				else
				{
					break;
				}
			}
		}
	}
}
