package main;

import index.entities.IndexFileReader;
import index.entities.PostingList;


import java.util.ArrayList;
import java.util.TreeMap;

import main.input.settings.ApplicationSetup;

public class ApplicationStatus {
	private static ApplicationStatus instance = null;
	private TreeMap<String, PostingList> index;
	private boolean indexIsSet=false;


	private ArrayList<PostingList> lists;

	private ApplicationStatus() {
		lists = new ArrayList<PostingList>();
	}

	public static ApplicationStatus getInstance() {
		if (instance == null)
			instance = new ApplicationStatus();
		return instance;
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
			System.out.println("WARNING: you need to initialize your indices first! use the '!buildVoc' command");
		}else{
			System.out.println("index-file read. ready for queries!");
			indexIsSet=true;
		}

	}

	public synchronized void doSearch(String term) {
		//TODO
		System.out.println("searching...");

		//retrieve posting lists
		PostingList list=null;

		list= ApplicationStatus.getInstance().getPostingsFor(term);
		if(list!=null){
			lists.add(list);
			System.out.println("posting list found");
		}


		for(PostingList p:lists){
			System.out.println(p.toString()+"\n");
		}

	}


	public boolean indexIsSet(){
		return indexIsSet;
	}
	
	public PostingList getPostingsFor(String term){
		if(index.containsKey(term.trim()))
		{
			return index.get(term);
		}else return null;

	}


}
