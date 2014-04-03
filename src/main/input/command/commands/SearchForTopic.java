package main.input.command.commands;

import processor.pipe.QueryPipe;
import main.input.command.ICommand;

public class SearchForTopic implements ICommand {

	private QueryPipe pipe;
	
	
	public SearchForTopic()
	{
		pipe= new QueryPipe();
	}
	
	@Override
	public int numberOfParams() {
		return 1;
	}

	@Override
	public String execute(String[] params) {
		String topicnr= params[0];
		pipe.setTopic(topicnr);
		
		System.out.println("parsing...");
		pipe.run();
		System.out.println("done...");
		
		return "";
	}

}
