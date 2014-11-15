package org.bb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

public class ConfigHandler
{
	String configFile = "config.xml";
	XMLConfiguration config;

	public ConfigHandler()
	{
		try
		{
			if (Files.notExists(Paths.get(configFile)))
			{
				Files.createFile(Paths.get(configFile));

				// TODO tilføj skrivning af en tom basis struktur når når vi ved hvordan
				// den
				// skal være
			}
			config = new XMLConfiguration(configFile);
			config.setExpressionEngine(new XPathExpressionEngine());

		} catch (IOException | ConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<WatchedFolder> getFolders() throws ConfigurationException
	{
		ArrayList<WatchedFolder> folders = new ArrayList<WatchedFolder>();
		
		LogHandler.out("Reading configuration", LogHandler.EVENT);
		
		String[] ss = config.getStringArray("folders/folder/path");
		for (String s : ss)
		{
			if (Files.exists(Paths.get(s)))
			{
				ArrayList<RuleSet> rules = new ArrayList<RuleSet>();
				String[] mat = config.getStringArray("folders/folder[path = '" + s
						+ "']/rulesets/ruleset/matchsetname");
				String[] act = config.getStringArray("folders/folder[path = '" + s
						+ "']/rulesets/ruleset/actionsetname");
				int i = 0;
				boolean run = true;
				while (run)
				{

					System.out.println(mat[i]);
					System.out.println(act[i]);
					rules.add(new RuleSet(getMatchRule("matches/match[name = '" + mat[i] + "']"), getActions(act[i])));
					i++;
					if (i >= mat.length || i >= act.length)
					{
						run = false;
					}
				}

				folders.add(new WatchedFolder(Paths.get(s), rules));
			} else
			{
				System.out.format("Could not find folder: %s\n", s);
			}
		}
		return folders;
	}

	private MatchRule getMatchRule(String xpath)
	{
		String kind = config.getString(xpath + "/kind");
		String[] ss;
		int i;
		
		LogHandler.out("Matchkind: " + kind, LogHandler.EVENT);
		switch (kind)
		{
		case "or":
			OrRule orRule = new OrRule();
			LogHandler.out("path: " + xpath, LogHandler.EVENT);
			ss = config.getStringArray(xpath + "/matches/match/kind");
			i = 1;
			for (String s : ss)
			{
				orRule.add(getMatchRule(xpath + "/matches/match["+ i +"]"));
				i++;
			}
			return orRule;
			
		case "and":
			AndRule andRule = new AndRule();
			LogHandler.out("path: " + xpath, LogHandler.EVENT);
			ss = config.getStringArray(xpath + "/matches/match/kind");
			i = 1;
			for (String s : ss)
			{
				andRule.add(getMatchRule(xpath + "/matches/match["+ i +"]"));
				i++;
			}
			return andRule;
			
		case "regex":
			String pattern = config.getString(xpath + "/pattern");
			LogHandler.out("Pattern: " + pattern, LogHandler.EVENT);
			return new RegexMatchRule(pattern);
			
		case "extension":
			String extension = config.getString(xpath + "/extension");
			LogHandler.out("Extension: " + extension, LogHandler.EVENT);
			return new ExtensionMatchRule(extension);

		default:
			return new RegexMatchRule("");
		}
	}

	private ArrayList<Action> getActions(String name)
	{
		ArrayList<Action> al = new ArrayList<Action>();

		String[] kinds = config.getStringArray("actionsets/actionset[name = '"
				+ name + "']/actions/action/kind");

		int i = 1;
		for (String kind : kinds)
		{
			System.out.println(kind);
			al.add(getAction(name, i, kind));
			i++;
		}
		return al;
	}

	private Action getAction(String name, int index, String kind)
	{
		switch (kind)
		{
		case "move":
			return new MoveAction(Paths.get(config
					.getString("actionsets/actionset[name = '" + name
							+ "']/actions/action[" + index + "]/destination")));

		case "copy":
			return new CopyAction(Paths.get(config
					.getString("actionsets/actionset[name = '" + name
							+ "']/actions/action[" + index + "]/destination")));

		default:
			return new MoveAction(Paths.get(""));
		}
	}
}
