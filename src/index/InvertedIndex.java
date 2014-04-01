package index;

import index.entities.Posting;
import index.entities.PostingList;
import index.entities.Token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import main.input.settings.ApplicationSetup;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class InvertedIndex {
	private ArrayList<TreeMap<String , PostingList>> indices;
	private static Logger logger= LogManager.getLogger("InvertedIndex");
	private TreeMap<String, PostingList> currentIndex;
	private int count=0;
	private int block=0;
	private final int MAX_SIZE=1024*16;
	
	public InvertedIndex()
	{
		indices = new ArrayList<TreeMap<String,PostingList>>();
		currentIndex = new TreeMap<>();
	}
	
	public void addTerm(String term, String docID)
	{
		try{
		PostingList list=null;
		Posting posting= null;
		
		Token token= new Token(term, docID);
		
		logger.info("received: "+token.toString());
		posting =  new Posting(token.getDocID());
		logger.info("posting: "+ posting.toString());
		
		if(!currentIndex.containsKey(token.getTerm()))
		{
			logger.info("term not in dictionary");
			list=new PostingList(token.getTerm());
			currentIndex.put(token.getTerm(), list );
			count++;
		}else{
			logger.info("term already in dictionary");
			list= currentIndex.get(token.getTerm());						
		}
		
		list.addToList(posting);
		
		logger.info("posting list: "+list.toString());
		
		if(count > MAX_SIZE)
		{
			
			//System.out.println("\n\nSWITCHING BLOCK\n\n"+ indices.size());
			System.out.println("\n\nSWITCHING BLOCK\n\n"+ block);
			writeToDisk(currentIndex, block++);
			
			//indices.add(currentIndex);
			currentIndex = new TreeMap<>();
			count=0;
		
			
			
			//write to disk?
		}
		}catch(Exception e){
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}
	
	private void writeToDisk(TreeMap<String, PostingList> block, int blockID) {
		//System.out.println("write to block");
				PrintWriter out=null;
				try{
					String filename;
					if(ApplicationSetup.getInstance().getUseBigrams())
					{
						filename= "./dictionary/bigram_index_b"+blockID+".txt";
					}else filename= "./dictionary/index_b"+blockID+".txt";
					
					File outputfile= new File(filename); 
					
					out = new PrintWriter(new BufferedWriter(new FileWriter(outputfile, false)));


					Iterator<String> it= block.keySet().iterator();
					String term;
					String s="";
					while(it.hasNext()){

						term= it.next();
						//s="{"+term+ block.get(term).toString()+"}";
						s= block.get(term).toJSONString();
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

	public void mergeIndices()
	{
		logger.info("merge indices");
		System.out.println("index n: "+indices.size());
//		for(int i= 0; i<indices.size(); i++)
//		{
//			
//		}
	}
}
