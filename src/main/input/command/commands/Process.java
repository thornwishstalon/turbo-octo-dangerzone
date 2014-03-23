package main.input.command.commands;

import java.io.File;
import java.util.ArrayList;

import processor.TextDocument;
import processor.DocumentProcessor;
import reader.Reader;
import main.input.command.ICommand;
import main.input.settings.ApplicationSetup;

public class Process implements ICommand {

	@Override
	public int numberOfParams() {
		return 0;
	}

	@Override
	public String execute(String[] params) {
		
		String NEWSGROUP_PATH = ApplicationSetup.getInstance().getCorporaPath();
		
		try {
			//arrayList will hold all documents
			ArrayList<File> documents = new ArrayList<>();
			System.out.println("Reading directory: " + NEWSGROUP_PATH);
			Reader reader = new Reader(NEWSGROUP_PATH);
			
			//stores all files in the arrayList
			reader.readFiles(documents);
			//System.out.println(documents.get(0).getName() + "," + documents.get(0).getParent());
			
			//test read - just 1 file
			DocumentProcessor documentProcessor = new DocumentProcessor();
			//String text = documentProcessor.readDocument(documents.get(0));
			//save index as documentID
			//text holds information for later use - to be discussed 
			//Document doc = new Document(0, text);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		
		return "";
	}

}
