package main.input.command.commands;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import lucene.LuceneSearch;
import main.ApplicationStatus;
import main.input.command.ICommand;

public class LuceneSearchForTopic implements ICommand {

	
	@Override
	public int numberOfParams() {
		
		return 1;
	}

	@Override
	public String execute(String[] params) {
		LuceneSearch search= new LuceneSearch();
		
		try {
			search.search(params[0]);
			ApplicationStatus.getInstance().notifyObservers();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

}
