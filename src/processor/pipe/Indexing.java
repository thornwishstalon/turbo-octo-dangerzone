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
		logger.info("indexer active");
	}

	public void setCurrentDocID(String newID) {
		currentDocID = newID;
	}

	@Override
	public String process(String input) {
		//System.out.println("term added");
		index.addTermDuringCreation(input, currentDocID);

		return "";
	}

	@Override
	public void backup() {

		index.writeCurrentToDisk();
		System.out.println("BACKUP");

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
