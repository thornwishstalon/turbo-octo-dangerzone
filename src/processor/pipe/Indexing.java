package processor.pipe;


import index.InvertedIndex;

import java.io.PipedReader;
import java.io.PipedWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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

	
	@Override
	protected void success() {
		super.success();
		System.out.println("indexing done");
		
	}

}
