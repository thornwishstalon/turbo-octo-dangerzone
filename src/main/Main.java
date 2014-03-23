package main;

import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;

import processor.TextDocument;
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
			
			//test read - just 1 file
			List<TextDocument> documentList = new ArrayList<TextDocument>();
			DocumentProcessor documentProcessor = new DocumentProcessor();
			documentProcessor.process(documents, documentList);
			
			System.out.println("Total number of documents to be processed: " + documentList.size());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

}
