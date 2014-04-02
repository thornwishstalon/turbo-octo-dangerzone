package processor.pipe;


import index.InvertedIndex;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Indexing extends AbstractPipeStage {

	// private SPMIInvert index;
	private String currentDocID;
	private InvertedIndex index;
	private static Logger logger = LogManager.getLogger("Index");
	private ProcessPipe pipe;

	public Indexing(PipedReader in, PipedWriter out, ProcessPipe pipe) {
		super(in, out);
		index = new InvertedIndex();
		this.pipe= pipe;
	}

	public void setCurrentDocID(String newID) {
		currentDocID = newID;
	}

	@Override
	public String process(String input) {
		// index.doSPIMIInvert(currentDocID);
		// System.out.println("indexing processing: "+ input+ " _ ID: " +
		// currentDocID);

		index.addTermDuringCreation(input, currentDocID);

		return "";
	}

	@Override
	public void backup() {
		System.out.println("BACKUP");
		//index.mergeIndices();
		//
		// //System.out.println("\n BACKUP!!!++++++++++++\n");
		// logger.info("writing to disk!");
		//
		// String filename;
		// if(ApplicationSetup.getInstance().getUseBigrams())
		// {
		// filename= "./dictionary/bigram_index.txt";
		// }else filename= "./dictionary/index.txt";
		//
		// File outputfile= new File(filename);
		// writeBlockToDisk(outputfile,sortTerms(dictionary), dictionary);
		//
		// ApplicationStatus.getInstance().setIndex(dictionary);
		//
		//
	}

	protected void success() {
		super.success();
		System.out.println("indexing done");
		pipe.mergeBlocks(index.getBlockList());
	}

	public ArrayList<String> getBlocks() {
		return index.getBlockList();
	}

}
