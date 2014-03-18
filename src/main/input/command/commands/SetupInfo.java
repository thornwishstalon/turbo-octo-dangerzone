package main.input.command.commands;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

/**
 * 
 * @author f
 *
 */
public class SetupInfo implements ICommand{

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		return "!print "+ ApplicationSetup.getInstance().getInfo();
	}
	
}
