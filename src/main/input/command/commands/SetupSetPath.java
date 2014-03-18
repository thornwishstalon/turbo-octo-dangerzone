package main.input.command.commands;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

/**
 * 
 * @author f
 *
 */
public class SetupSetPath implements ICommand {

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String execute(String[] params) {
		ApplicationSetup.getInstance().setCorporaPath(params[0]);
		
		return "!print corporate path changed to: "+params[0];
	}

}
