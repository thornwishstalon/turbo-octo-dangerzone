package main;

import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;

import processor.Document;
import processor.DocumentProcessor;
import reader.Reader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final String NEWSGROUP_PATH = "C:\\Users\\Feanor\\Desktop\\Information Retrieval\\Exercises\\Exercise 1\\20_newsgroups_subset";
		final String TOPICS_PATH = "C:\\Users\\Feanor\\Desktop\\Information Retrieval\\Exercises\\Exercise 1\\topics";
		
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
			String text = documentProcessor.readDocument(documents.get(0));
			//save index as documentID
			//text holds information for later use - to be discussed 
			Document doc = new Document(0, text);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

}
