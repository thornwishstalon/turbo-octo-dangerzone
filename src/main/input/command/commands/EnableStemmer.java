package main.input.command.commands;

import main.ApplicationStatus;
import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class EnableStemmer implements ICommand {

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String execute(String[] params) {
		ApplicationSetup setup = ApplicationSetup.getInstance();
		if (setup.getUseStemmer()) {
			setup.setUseStemmer(false);
			ApplicationStatus.getInstance().notifyObservers();
			return "!print disabled stemmer!";
		} else {
			setup.setUseStemmer(true);
			ApplicationStatus.getInstance().notifyObservers();
			return "!print enabled stemmer!";
		}
	}

}
