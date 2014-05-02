package main.input.command.commands;

import main.ApplicationStatus;
import main.input.command.ICommand;

/**
 * 
 * @author f
 * 
 */
public class Print implements ICommand {

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String execute(String[] params) {
		System.out.println(params[0]);
		ApplicationStatus.getInstance().notifyObservers();
		return "";
	}

}
