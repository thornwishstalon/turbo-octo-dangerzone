package main.input.command.commands;

import main.ApplicationStatus;
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
		ApplicationSetup setup = ApplicationSetup.getInstance();
		if (setup.getUseBigrams()) {
			setup.setUseBigrams(false);
			System.out.println("disabled bigrams!");
			ApplicationStatus.getInstance().readIndex();
			return "";
		} else {
			setup.setUseBigrams(true);
			System.out.println("enabled bigrams!");
			ApplicationStatus.getInstance().readIndex();
			return "";

		}
	}

}
