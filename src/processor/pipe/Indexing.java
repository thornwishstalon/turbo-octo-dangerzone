package processor.pipe;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import main.ApplicationStatus;
import main.input.settings.ApplicationSetup;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import index.InvertedIndex;
import index.SPMIInvert;
import index.entities.Posting;
import index.entities.PostingList;
import index.entities.Token;

public class Indexing extends AbstractPipeStage {
	
	//private SPMIInvert index;
	private String currentDocID;
	private InvertedIndex index;
	private static Logger logger = LogManager.getLogger("Index");
	
	
	
	public Indexing(PipedReader in, PipedWriter out) {
		super(in,out);
		index= new InvertedIndex();
		
	}
	
	public void setCurrentDocID(String newID)
	{
		currentDocID= newID;
	}

	@Override
	public String process(String input) {		
		//index.doSPIMIInvert(currentDocID);
		//System.out.println("indexing processing: "+ input+ " _ ID: " + currentDocID);
		
		index.addTerm(input, currentDocID);
		
		return "";
	}
	
	@Override
	public void backup(){
		System.out.println("BACKUP");
		index.mergeIndices();
//		
//		//System.out.println("\n BACKUP!!!++++++++++++\n");
//		logger.info("writing to disk!");
//		
//		String filename;
//		if(ApplicationSetup.getInstance().getUseBigrams())
//		{
//			filename= "./dictionary/bigram_index.txt";
//		}else filename= "./dictionary/index.txt";
//		
//		File outputfile= new File(filename); 
//		writeBlockToDisk(outputfile,sortTerms(dictionary), dictionary);
//		
//		ApplicationStatus.getInstance().setIndex(dictionary);
//		
//		
	}
	
	private Set<String> sortTerms(HashMap<String, PostingList> dictionary)
	{
		logger.info("sorting");
		//System.out.println("sorting");
		Map<String, PostingList> copy= new TreeMap<String, PostingList>(dictionary);

		return copy.keySet();

	}

	private void writeBlockToDisk(File file, Set<String> sortedTerms, HashMap<String, PostingList> dictionary){
		
	}
	
	@Override
	protected void success() {
		super.success();
		System.out.println("indexing done");
		
	}

}
