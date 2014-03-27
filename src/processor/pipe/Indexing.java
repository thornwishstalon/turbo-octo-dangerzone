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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import index.SPMIInvert;
import index.entities.Posting;
import index.entities.PostingList;
import index.entities.Token;

public class Indexing extends AbstractPipeStage {
	
	//private SPMIInvert index;
	private String currentDocID;
	private HashMap<String, PostingList> dictionary;
	private static Logger logger = LogManager.getLogger("SPIMIInvert");
	
	
	
	public Indexing(PipedReader in, PipedWriter out) {
		super(in,out);
		dictionary = new HashMap<>();
		
	}
	
	public void setCurrentDocID(String newID)
	{
		currentDocID= newID;
	}

	@Override
	public String process(String input) {		
		//index.doSPIMIInvert(currentDocID);
		System.out.println("indexing processing: "+ input);
		
		PostingList list=null;
		Posting posting= null;
		
		Token token= new Token(input, currentDocID);
		
		logger.info("received: "+token.toString());
		posting =  new Posting(token.getDocID());
		logger.info("posting: "+ posting.toString());
		
		if(!dictionary.containsKey(token.getTerm()))
		{
			logger.info("term not in dictionary");
			list=new PostingList();
			dictionary.put(token.getTerm(), list );
		}else{
			logger.info("term already in dictionary");
			list= dictionary.get(token.getTerm());						
		}
		
		list.addToList(posting);
		logger.info("posting list: "+list.toString());
		
		
		return "";
	}
	
	@Override
	public void backup(){
		
		System.out.println("BACKUP");
		File outputfile= new File("./dictionary/1.txt"); //TODO add filename for block
		writeBlockToDisk(outputfile,sortTerms(dictionary), dictionary);
		
	}
	
	private Set<String> sortTerms(HashMap<String, PostingList> dictionary)
	{
		Map<String, PostingList> copy= new TreeMap<String, PostingList>(dictionary);

		return copy.keySet();

	}

	private void writeBlockToDisk(File file, Set<String> sortedTerms, HashMap<String, PostingList> dictionary){
		//System.out.println("write to blocl");
		PrintWriter out=null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));


			Iterator<String> it= sortedTerms.iterator();
			String term;
			String s="";
			while(it.hasNext()){

				term= it.next();
				s="{"+term+ dictionary.get(term).toString()+"}";
				//for debug reason currently
				//System.out.println(s);

				//out.println(s); //append to files
				out.println(s);
			}

		}catch (IOException e) {
			//exception handling left as an exercise for the reader
			e.printStackTrace();
		}
		finally
		{
			if(out!=null){
				out.close();
			}
		}
		//mark for future merge
		//pushBlockForMerge(file.getName());

		//return file;
	}

}
