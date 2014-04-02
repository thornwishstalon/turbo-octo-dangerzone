package main.input.command.commands;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class EnableStopwords implements ICommand {

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		ApplicationSetup setup = ApplicationSetup.getInstance();
		if (setup.getUseStopwords()) {
			setup.setUseStopwords(false);
			return "!print disabled stopwords!";
		} else {
			setup.setUseStopwords(true);
			return "!print enabled stopwords!";
		}

	}

}
