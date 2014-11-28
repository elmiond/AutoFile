package org.bb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;





import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

/**
 * Handles all interaction with the config file
 * @author      Ask Bisgaard	<Elmiond@gmail.com>
 * @version     1.0
 * @since       2014-11-28
 */
public class ConfigHandler
{
	/**
	 * name of the config file.
	 */
	String configFile = "config.xml";
	
	/**
	 * {@link org.apache.commons.configuration.XMLConfiguration} instance.
	 */
	XMLConfiguration config;

	/**
	 * Constructor, also creates config file if missing.
	 */
	public ConfigHandler()
	{
		try
		{
			if (Files.notExists(Paths.get(configFile)))
			{
				Files.createFile(Paths.get(configFile));

				// TODO Creation of skeleton config file
			}
			config = new XMLConfiguration(configFile);
			config.setExpressionEngine(new XPathExpressionEngine());

		} catch (IOException | ConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Get folders and their associated {@link org.bb.RuleSet}s.
	 * @see									org.bb.WatchedFolder
	 * @return 							an ArrayList of {@link org.bb.WatchedFolder}, including their {@link org.bb.RuleSet}s
	 */
	public ArrayList<WatchedFolder> getFolders() throws ConfigurationException
	{
		ArrayList<WatchedFolder> folders = new ArrayList<WatchedFolder>();
		
		LogHandler.out("====Reading configuration====", LogHandler.INFO);
		
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
					rules.add(new RuleSet(getMatchset(mat[i]), getActionset(act[i])));
					i++;
					if (i >= mat.length || i >= act.length)
					{
						run = false;
					}
				}

				folders.add(new WatchedFolder(Paths.get(s), rules));
			} else
			{
				LogHandler.out(String.format("Could not find folder: %s\n", s), LogHandler.WARNING);
			}
		}
		LogHandler.out("====Configuration loaded====", LogHandler.INFO);
		return folders;
	}
	
	/**
	 * Gets a MatchSet by name.
	 * @param	name					name of MatchSet to get
	 * @return 							MatchRule identified by name
	 */
	private MatchRule getMatchset(String name)
	{
		return getMatch("matchsets/matchset[name = '" + name + "']/match");
	}
	
	/**
	 * Gets the MatchRule and it's configuration.
	 * Runs recursively for MatchRuleCollections
	 * @param	xpath					name of the ActionSet
	 * @return							the configured MatchRule
	 */
	private MatchRule getMatch(String xpath)
	{
		String kind = config.getString(xpath + "/kind");
		String[] ss;
		int i;
		
		
		switch (kind)
		{
		case "or":
			OrRule orRule = new OrRule();
			ss = config.getStringArray(xpath + "/matches/match/kind");
			i = 1;
			for (String s : ss)
			{
				orRule.add(getMatch(xpath + "/matches/match["+ i +"]"));
				i++;
			}
			LogHandler.out("====OrMatchRuleCollection End====", LogHandler.INFO);
			return orRule;
			
		case "and":
			AndRule andRule = new AndRule();
			ss = config.getStringArray(xpath + "/matches/match/kind");
			i = 1;
			for (String s : ss)
			{
				andRule.add(getMatch(xpath + "/matches/match["+ i +"]"));
				i++;
			}
			LogHandler.out("====AndMatchRuleCollection End====", LogHandler.INFO);
			return andRule;
			
		case "regex":
			String pattern = config.getString(xpath + "/pattern");
			return new RegexMatchRule(pattern);
			
		case "extension":
			String extension = config.getString(xpath + "/extension");
			return new ExtensionMatchRule(extension);

		default:
			return new RegexMatchRule("");
		}
	}

	/**
	 * Gets an ActionSet by name.
	 * @param	name					name of ActionSet to get
	 * @return 							ArrayList of Actions identified by name
	 */
	private ArrayList<Action> getActionset(String name)
	{
		ArrayList<Action> al = new ArrayList<Action>();

		String[] kinds = config.getStringArray("actionsets/actionset[name = '"
				+ name + "']/actions/action/kind");

		int i = 1;
		for (String kind : kinds)
		{
			al.add(getAction(name, i, kind));
			i++;
		}
		return al;
	}

	/**
	 * Get a specific Action, including it's individual settings.
	 * @param	name					name of the ActionSet
	 * @param	index					index of the Action to get
	 * @param	kind					the kind of the Action
	 * @return							the configured Action
	 */
	private Action getAction(String name, int index, String kind)
	{
		// TODO streamline method akin to what exists in Configeditor project
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
			
		case "cmd":
			return new CmdAction(config.getString("actionsets/actionset[name = '" + name + "']/actions/action[" + index + "]/command"));

		default:
			return new MoveAction(Paths.get(""));
		}
	}
}
