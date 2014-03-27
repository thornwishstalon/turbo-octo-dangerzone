package main.input.command;

import java.util.HashMap;

import main.input.command.commands.BuildVoc;
import main.input.command.commands.Print;
import main.input.command.commands.SetupInfo;
import main.input.command.commands.SetupSetPath;

/**
 * 
 * @author f
 *
 */
public class ApplicationCommandList implements ICommandList {
	private HashMap<String, ICommand> commands;
	
	
	public ApplicationCommandList()
	{
		commands= new HashMap<>();
		
		commands.put("!info", new SetupInfo());
		commands.put("!setPath", new SetupSetPath());
		commands.put("!print", new Print());
		
		//commands.put("!process", new main.input.command.commands.Process());
		commands.put("!buildVoc", new BuildVoc());
		
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
