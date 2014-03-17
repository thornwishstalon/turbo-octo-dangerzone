package main;

import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;

import reader.Reader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final String NEWSGROUP_PATH = "C:\\Users\\Feanor\\Desktop\\Information Retrieval\\Exercises\\Exercise 1\\20_newsgroups_subset";
		final String TOPICS_PATH = "C:\\Users\\Feanor\\Desktop\\Information Retrieval\\Exercises\\Exercise 1\\topics";
		
		try {
			ArrayList<File> documents = new ArrayList<>();
			System.out.println("Reading directory: " + NEWSGROUP_PATH);
			Reader reader = new Reader(NEWSGROUP_PATH);
			reader.readFiles(documents);
			System.out.println(documents.get(0).getName() + "," + documents.get(0).getParent());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

}
