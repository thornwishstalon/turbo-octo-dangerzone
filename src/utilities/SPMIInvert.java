package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import utilities.entities.Posting;
import utilities.entities.PostingList;
import utilities.entities.Token;

/**
 * @author F
 *
 */
public class SPMIInvert extends AbstractBlockedIndexCreator{


	
	private File doSPIMIInvert(Object tokenStream){ //TODO TOKENSTREAM as input!!

		//token = (term,docID)



		File outputfile= new File(""); //TODO add filename for block
		HashMap<String, PostingList> dictionary= new HashMap<>();

		Token token=null;
		PostingList list=null;
		Posting posting= null;

		while(memoryIsFree())
		{

			token= new Token(1); //TODO read item from tokenStream 
			posting =  new Posting(token.getDocID());

			if(!dictionary.containsKey(token.getTerm()))
			{
				list=new PostingList();
				dictionary.put(token.getTerm(), list );
			}else{
				list= dictionary.get(token.getTerm());					
			}

			list.addToList(posting);


		}
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

		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {

			Iterator<String> it= sortedTerms.iterator();
			String term;
			while(it.hasNext()){
				//out.println("the text");
				term= it.next();
				System.out.println("{"+term+ dictionary.get(term).toString()+"}\n");
			}

		}catch (IOException e) {
			//exception handling left as an exercise for the reader
		}

		//mark for future merge
		pushBlockForMerge(file.getName());

		return file;
	}


	@Override
	protected void buildIndexForBlock(Object tokenStream) {
		doSPIMIInvert(tokenStream);

	}




}
