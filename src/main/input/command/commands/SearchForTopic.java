package main.input.command.commands;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import processor.pipe.QueryPipe;
import main.ApplicationStatus;
import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

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
		
		if(!ApplicationSetup.getInstance().getUseLucene()){
		String topicnr= params[0];
		pipe.setTopic(topicnr);
		ApplicationStatus.getInstance().setTopic(topicnr);
		System.out.println("parsing...");
		pipe.run();
		
		System.out.println("done...");
		}else{
			
			
			
			
			
		}
		return "";
	}

}
