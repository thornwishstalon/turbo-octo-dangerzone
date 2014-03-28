package index;

import index.entities.Posting;
import index.entities.PostingList;
import index.entities.Token;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author F
 *
 */
public class SPMIInvert extends AbstractBlockedIndexCreator{
	private HashMap<String, PostingList> dictionary;
	private static Logger logger = LogManager.getLogger("SPIMIInvert");
	private PipedReader reader=null;
	
	public SPMIInvert(PipedReader reader)
	{		
		super();
		this.reader= reader;
		//BasicConfigurator.configure();
	}

	public void doSPIMIInvert( String ID){ 

		System.out.println("SPIMIInvert");
		//token = (term,docID)
	
		dictionary= new HashMap<>();

		Token token=null;
		PostingList list=null;
		Posting posting= null;
		BufferedReader bufferedReader = new BufferedReader(reader);

		//while(memoryIsFree()){
			try {
				String in=null;
				
				while((in = bufferedReader.readLine() )!=null){
					
					token= new Token(in, ID);
					
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
				}
				
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				//break;
			} //TODO read item from tokenStream
		//}
		//sortTerms(dictionary)
		//WRITEBLOCKTODISK
	}
	
	

	public void safe()
	{
		File outputfile= new File("./dictionary/1.txt"); //TODO add filename for block
		writeBlockToDisk(outputfile,sortTerms(dictionary), dictionary);
		
	}
	

	private boolean memoryIsFree() {
		// TODO Auto-generated method stub
		long usableFreeMemory= Runtime.getRuntime().maxMemory()
				-Runtime.getRuntime().totalMemory()
				+Runtime.getRuntime().freeMemory();

		return usableFreeMemory > 0 ; //TODO not good solution
	}

	private Set<String> sortTerms(HashMap<String, PostingList> dictionary)
	{
		Map<String, PostingList> copy= new TreeMap<String, PostingList>(dictionary);

		return copy.keySet();

	}

	private File writeBlockToDisk(File file, Set<String> sortedTerms, HashMap<String, PostingList> dictionary){
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
		pushBlockForMerge(file.getName());

		return file;
	}

	@Override
	protected void buildIndexForBlock(PipedReader tokenstream) {
		// TODO Auto-generated method stub
		
	}

	
	
/*
	@Override
	protected void buildIndexForBlock(PipedReader reader) {
		//
		//System.out.println("building index");
		doSPIMIInvert(reader);
		//
		//System.out.println("done");
		done();
	}
*/



}
