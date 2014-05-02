package main.input.command.commands;

import processor.pipe.ProcessPipe;
import main.ApplicationStatus;
import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class BuildVoc implements ICommand {
	private ProcessPipe pipe;

	public BuildVoc() {
		pipe = new ProcessPipe();
	}

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {

		// VocabularyBuilder builder= new VocabularyBuilder("");
		// builder.buildWithPipe();

		if (pipe.isRunning())
			return "!print Still processing! have some patience!";
		else {
			pipe = new ProcessPipe();
			pipe.run();
			
			ApplicationStatus.getInstance().notifyObservers();
			
			return "!print building index....";
		}
	}

}
