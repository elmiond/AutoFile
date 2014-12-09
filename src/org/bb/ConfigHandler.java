package org.bb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

/**
 * Handles all interaction with the config file
 * 
 * @author Ask Bisgaard <Elmiond@gmail.com>
 * @version 1.0
 * @since 2014-11-28
 */
final public class ConfigHandler
{
	/**
	 * name of the config file.
	 */
	static String configFile = "config.xml";

	/**
	 * {@link org.apache.commons.configuration.XMLConfiguration} instance.
	 */
	static XMLConfiguration config;

	/**
	 * Constructor, also creates config file if missing.
	 */
	private ConfigHandler()
	{
	}

	static private void verifyConfigFile()
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
	 * 
	 * @see org.bb.WatchedFolder
	 * @return an ArrayList of {@link org.bb.WatchedFolder}, including their
	 *         {@link org.bb.RuleSet}s
	 */
	static public ArrayList<WatchedFolder> getFolders()
			throws ConfigurationException
	{
		verifyConfigFile();

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
				while (true)
				{
					rules.add(new RuleSet(getMatchset(mat[i]), getActionset(act[i])));
					i++;
					if (i >= mat.length || i >= act.length)
					{
						break;
					}
				}

				folders.add(new WatchedFolder(Paths.get(s), rules));
			} else
			{
				LogHandler.out(String.format("Could not find folder: %s\n", s),
						LogHandler.WARNING);
			}
		}
		LogHandler.out("====Configuration loaded====", LogHandler.INFO);
		return folders;
	}

	/**
	 * Gets a MatchSet by name.
	 * 
	 * @param name
	 *          name of MatchSet to get
	 * @return MatchRule identified by name
	 */
	static private MatchRule getMatchset(String name)
	{
		return getMatch("matchsets/matchset[name = '" + name + "']/match");
	}

	/**
	 * Gets the MatchRule and it's configuration. Runs recursively for
	 * MatchRuleCollections
	 * 
	 * @param xpath
	 *          name of the ActionSet
	 * @return the configured MatchRule
	 */
	static private MatchRule getMatch(String xpath)
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
				orRule.add(getMatch(xpath + "/matches/match[" + i + "]"));
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
				andRule.add(getMatch(xpath + "/matches/match[" + i + "]"));
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
	 * 
	 * @param name
	 *          name of ActionSet to get
	 * @return ArrayList of Actions identified by name
	 */
	static private ArrayList<Action> getActionset(String name)
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
	 * 
	 * @param name
	 *          name of the ActionSet
	 * @param index
	 *          index of the Action to get
	 * @param kind
	 *          the kind of the Action
	 * @return the configured Action
	 */
	static private Action getAction(String name, int index, String kind)
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
			return new CmdAction(config.getString("actionsets/actionset[name = '"
					+ name + "']/actions/action[" + index + "]/command"));

		case "unpack":
			return new UnpackAction();

		case "package":
			int compression = 0;
			try
			{
				compression = Integer.parseInt(config
						.getString("actionsets/actionset[name = '" + name
								+ "']/actions/action[" + index + "]/compression"));
			} catch (NumberFormatException e)
			{
				return new PackagerAction(Paths.get(config
						.getString("actionsets/actionset[name = '" + name
								+ "']/actions/action[" + index + "]/destination")));
			}
			return new PackagerAction(Paths.get(config
					.getString("actionsets/actionset[name = '" + name
							+ "']/actions/action[" + index + "]/destination")), compression);

		case "meta":
			String action = config.getString("actionsets/actionset[name = '" + name
					+ "']/actions/action[" + index + "]/tag");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date time;
			try
			{
				time = formatter.parse(config.getString("actionsets/actionset[name = '"
						+ name + "']/actions/action[" + index + "]/date"));
			} catch (ParseException e)
			{
				time = new Date();
			}
			boolean value = Boolean.parseBoolean(config
					.getString("actionsets/actionset[name = '" + name
							+ "']/actions/action[" + index + "]/value"));
			return new FileMetaEditorAction(action, time, value);

		case "delete":
			return new DeleteAction();

		default:
			return new UnpackAction();
		}
	}

	public static int getFrequency()
	{
		// TODO Auto-generated method stub
		verifyConfigFile();

		return config.getInt("/@frequency", 60);
	}
}
