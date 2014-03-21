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

/**
 * @author F
 *
 */
public class SPMIInvert extends AbstractBlockedIndexCreator{
	private HashMap<String, PostingList> dictionary;
	
	
	public SPMIInvert()
	{
		super();
	}

	private File doSPIMIInvert(PipedReader reader){ 

		System.out.println("SPIMIInvert");
		//token = (term,docID)
		File outputfile= new File("./dictionary/1.txt"); //TODO add filename for block
		
		dictionary= new HashMap<>();

		Token token=null;
		PostingList list=null;
		Posting posting= null;
		BufferedReader bufferedReader = new BufferedReader(reader);

		//while(memoryIsFree()){
			try {
				String in=null;
				
				while((in = bufferedReader.readLine() )!=null){
					
					token= new Token(in);
					System.out.println("received: "+token.toString());
					posting =  new Posting(token.getDocID());
					System.out.println("posting: "+ posting.toString());
					
					if(!dictionary.containsKey(token.getTerm()))
					{
						System.out.println("term not in dictionary");
						list=new PostingList();
						dictionary.put(token.getTerm(), list );
					}else{
						System.out.println("term already in dictionary");
						list= dictionary.get(token.getTerm());					
					}
					
					list.addToList(posting);
					System.out.println("posting list: "+list.toString());
				}
				
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				//break;
			} //TODO read item from tokenStream
		//}
		//sortTerms(dictionary)
		//WRITEBLOCKTODISK

		return  writeBlockToDisk(outputfile,sortTerms(dictionary), dictionary);

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
	protected void buildIndexForBlock(PipedReader reader) {
		//
		System.out.println("building index");
		doSPIMIInvert(reader);
		//
		System.out.println("done");
		done();
	}




}
