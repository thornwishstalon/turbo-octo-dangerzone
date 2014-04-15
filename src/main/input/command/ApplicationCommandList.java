package main.input.command;

import java.util.HashMap;

import main.input.command.commands.BuildVoc;
import main.input.command.commands.EnableBigrams;
import main.input.command.commands.EnableStemmer;
import main.input.command.commands.EnableStopwords;
import main.input.command.commands.LucBuildVoc;
import main.input.command.commands.Print;
import main.input.command.commands.SearchForTopic;
import main.input.command.commands.SetupInfo;
import main.input.command.commands.SetupSetPath;
import main.input.command.commands.Testing;

/**
 * 
 * @author f
 * 
 */
public class ApplicationCommandList implements ICommandList {
	private HashMap<String, ICommand> commands;

	public ApplicationCommandList() {
		commands = new HashMap<>();

		commands.put("!info", new SetupInfo());
		commands.put("!setPath", new SetupSetPath());
		commands.put("!print", new Print());

		commands.put("!bigram", new EnableBigrams());
		commands.put("!stemmer", new EnableStemmer());
		commands.put("!stopwords", new EnableStopwords());

		// commands.put("!process", new main.input.command.commands.Process());
		commands.put("!buildVoc", new BuildVoc());
		commands.put("!test", new Testing());
        commands.put("!search", new SearchForTopic());
        
        // Lucene commands!!!!
        commands.put("!luceneBouildVoc", new LucBuildVoc());

	}

	@Override
	public boolean containsKey(String commandKey) {
		return commands.containsKey(commandKey);
	}

	@Override
	public ICommand get(String commandKey) {
		return commands.get(commandKey);
	}

}
