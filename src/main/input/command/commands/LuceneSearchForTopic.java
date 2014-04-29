package main.input.command.commands;

import org.apache.lucene.queryparser.classic.ParseException;

import lucene.LuceneSearch;
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

}