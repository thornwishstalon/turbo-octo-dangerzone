package main.input.command;

/**
 * 
 * @author f
 *
 */
public interface ICommand {
	public int numberOfParams();
	public  String execute(String[] params);
}
