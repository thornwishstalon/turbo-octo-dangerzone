package main;

import index.entities.PostingList;

import java.util.HashMap;

public class ApplicationStatus {
	private static ApplicationStatus instance = null;
	private HashMap<String, PostingList> index;

	private ApplicationStatus() {

	}

	public static ApplicationStatus getInstance() {
		if (instance == null)
			instance = new ApplicationStatus();
		return instance;
	}

	public HashMap<String, PostingList> getIndex() {
		return index;
	}

	public void setIndex(HashMap<String, PostingList> index) {
		this.index = index;
	}

}
