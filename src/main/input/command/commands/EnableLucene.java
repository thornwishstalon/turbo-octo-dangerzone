package main.input.command.commands;

import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class EnableLucene implements ICommand{

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String execute(String[] params) {
		ApplicationSetup setup = ApplicationSetup.getInstance();
		if (setup.getUseLucene()) {
			setup.setUseLucene(false);
			return "!print disabled Lucene!";
		} else {
			setup.setUseLucene(true);
			return "!print enabled Lucene!";
		}
	}

}
