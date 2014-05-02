package main.input.command.commands;

import main.ApplicationStatus;
import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class EnableBM25L implements ICommand {

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String execute(String[] params) {
		ApplicationSetup setup = ApplicationSetup.getInstance();
		if (setup.getUseBM25()) {
			setup.setUseBM25(false);
			ApplicationStatus.getInstance().notifyObservers();
			
			return "!print disabled bm25 silmilarity!";
		} else {
			setup.setUseBM25(true);
			ApplicationStatus.getInstance().notifyObservers();
			return "!print enabled bm25 silmilarity!";
		}
	}

}
