package main.input.command.commands;

import lucene.LuceneIndexer;
import main.ApplicationStatus;
import main.input.command.ICommand;

public class LucBuildVoc implements ICommand{

	@Override
	public int numberOfParams() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String execute(String[] params) {
		System.out.println("testing....");
		LuceneIndexer t= new LuceneIndexer();
		t.test();
		ApplicationStatus.getInstance().notifyObservers();
		return "";
	}

}
