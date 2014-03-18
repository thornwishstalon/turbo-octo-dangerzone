package main.input.command;

/**
 * 
 * @author f
 *
 */
public interface ICommandList {

	public boolean containsKey(String commandKey);
	public ICommand get(String commandKey);
}
