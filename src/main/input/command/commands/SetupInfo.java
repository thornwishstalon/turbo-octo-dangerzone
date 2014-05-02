package main.input.command.commands;

import main.ApplicationStatus;
import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

/**
 * 
 * @author f
 * 
 */
public class SetupInfo implements ICommand {

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		ApplicationStatus.getInstance().notifyObservers();
		return "!print " + ApplicationSetup.getInstance().getInfo();
	}

}
