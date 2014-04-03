package main;

import index.entities.IndexFileReader;
import index.entities.PostingList;

import java.util.HashMap;
import java.util.TreeMap;

import main.input.settings.ApplicationSetup;

public class ApplicationStatus {
	private static ApplicationStatus instance = null;
	private TreeMap<String, PostingList> index;

	private ApplicationStatus() {

	}

	public static ApplicationStatus getInstance() {
		if (instance == null)
			instance = new ApplicationStatus();
		return instance;
	}


	public void setIndex(TreeMap<String, PostingList> index) {
		this.index = index;
	}
	
	public void readIndex()
	{
		String filename;
		
		if(ApplicationSetup.getInstance().getUseBigrams())
		{
			filename= "./dictionary/bigram_index.txt";
		}else filename= "./dictionary/index.txt";
	
		index= IndexFileReader.readBlock(filename);
		if(index.size() == 0)
		{
			System.out.println("you need to initialize your indices first! use the '!buildVoc' command");
		}
		
	}

}
