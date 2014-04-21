package index;

import java.util.ArrayList;
import java.util.TreeMap;

import main.input.settings.ApplicationSetup;
import index.entities.IndexFileWriter;
import index.entities.Posting;
import index.entities.PostingList;
import index.entities.Token;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class InvertedIndex {
	private ArrayList<String> indices;
	private TreeMap<String, PostingList> currentIndex;
	private static Logger logger = LogManager.getLogger("InvertedIndex");

	private int count = 0;
	private int block = 0;
	private final int MAX_SIZE = 1024 * 16;

	public InvertedIndex() {
		indices = new ArrayList<String>();
		currentIndex = new TreeMap<>();
	}

	private void addTerm(String term, String docID){
		PostingList list = null;
		Posting posting = null;

		Token token = new Token(term, docID);

		logger.info("received: " + token.toString());
		posting = new Posting(token.getDocID());
		logger.info("posting: " + posting.toString());

		if (!currentIndex.containsKey(token.getTerm())) {
			logger.info("term not in dictionary");
			list = new PostingList(term);
			currentIndex.put(token.getTerm(), list);
			count++;
		} else {
			logger.info("term already in dictionary");
			list = currentIndex.get(token.getTerm());
		}

		list.addToList(posting);

		logger.info("posting list: " + list.toString());
	}
	
	public void addTermDuringCreation(String term, String docID) {

		addTerm(term, docID);

		if (count > MAX_SIZE) {

			// System.out.println("\n\nSWITCHING BLOCK\n\n"+ indices.size());
			//System.out.println("\n\nSWITCHING BLOCK\n\n" + block);
			String filename;
			filename= IndexFileWriter.writeToDisk(currentIndex, block++);

			indices.add(filename);
			
			currentIndex = new TreeMap<>();
			count = 0;

			// write to disk?
		}
		
	}
	
	public void addTermDuringMerge(String term, String docID) {
		addTerm(term, docID);
	}

	


	public ArrayList<String> getBlockList() {
		return indices;
	}

	public void writeCurrentToDisk() {
		String filename;
		if(ApplicationSetup.getInstance().getUseBigrams()) {
			filename= "./dictionary/bigram_index.txt";
		}
		else filename= "./dictionary/index.txt";
		
		IndexFileWriter.writeToDisk(currentIndex, block++ );
		
	}
}
