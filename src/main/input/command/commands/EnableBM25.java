package main.input.command.commands;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class EnableBM25 implements ICommand {

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
			return "!print disabled bm25 silmilarity!";
		} else {
			setup.setUseBM25(true);
			return "!print enabled bm25 silmilarity!";
		}
	}

}
