package main.input.command.commands;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class EnableBigrams implements ICommand {

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String execute(String[] params) {
		ApplicationSetup setup= ApplicationSetup.getInstance();
		if(setup.getUseBigrams())
		{
			setup.setUseBigrams(false);
			return "!print disabled bigrams!";
		}
		else
		{
			setup.setUseBigrams(true);
			return "!print enabled bigrams!";
		}
	}

}
