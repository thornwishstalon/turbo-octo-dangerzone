package main.input.command;

import main.input.parser.CommandParser;

/**
 * 
 * @author f
 * 
 */
public interface ICommandList {

	public boolean containsKey(String commandKey);

	public ICommand get(String commandKey);
	
	public void addScript(CommandParser parser);
}
