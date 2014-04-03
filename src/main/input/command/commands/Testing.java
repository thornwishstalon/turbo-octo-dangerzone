package main.input.command.commands;

import processor.pipe.TestPipe;
import main.input.command.ICommand;

public class Testing implements ICommand {
	private TestPipe pipe;
	
	public Testing() {
		pipe = new TestPipe();
	}
	
	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		
		if (pipe.isRunning())
			return "!print Still processing! have some patience!";
		else {
			pipe = new TestPipe();
			pipe.start();
			return "!print testing....";
		}
		
		/*
		 * 
		 * TODO 1. testing mit current index.. search for stuff
		 * 		2. reader benutzen um topics einzulesen
		 * 			-> docProcessor -> filtering -> alle 'relevanten' wörter aus der anfrage 
		 */

	}

}
