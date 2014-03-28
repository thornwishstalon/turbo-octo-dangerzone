package main.input.command.commands;

import processor.VocabularyBuilder;
import main.input.command.ICommand;

public class BuildVoc implements ICommand {

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		
		VocabularyBuilder builder= new VocabularyBuilder("");
		builder.buildWithPipe();
		
		
		return "";
	}

}
