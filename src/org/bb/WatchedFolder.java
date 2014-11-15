package org.bb;

import java.nio.file.Path;
import java.util.ArrayList;

public class WatchedFolder
{
	Path folderPath;
	ArrayList<RuleSet> ruleSets;

	public WatchedFolder(Path folderPath, ArrayList<RuleSet> ruleSets)
	{
		this.folderPath = folderPath;
		this.ruleSets = ruleSets;
	}
	
	public void doWork(Path path)
	{
		for (RuleSet ruleSet : ruleSets)
		{
			ruleSet.doWork(path);
		}
	}
}