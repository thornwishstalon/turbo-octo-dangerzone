package index;

import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedReader;
import java.util.ArrayList;



/***
 * 
 * @author F
 *
 */
public abstract class AbstractBlockedIndexCreator extends Thread{
	private ArrayList<String> blocks;
	private PipedReader reader= null;
	private boolean isDone=false;
	
	public AbstractBlockedIndexCreator() {
		blocks = new ArrayList<>();
		reader = new PipedReader();
	}
	
	@Override
	public void run()
	{
		buildIndexForBlock(reader);
		mergeBlocks();
		
	}
	
	protected void createIndex(){ //input TOKENSTREAM
		/*	while tokenstream is alive{
		 * 		buildIndexForBlock(tokenstream);
		 * 	}
		 * 	mergeBlocks();
		 * 
		 */
		run();
		
	}   
	
	protected  abstract void buildIndexForBlock(PipedReader tokenstream); //input TOKENSTREAM TODO
	
	
	protected void mergeBlocks()
	{
		//TODO
		
	}
	
	private void mergeBlock(String n, String m)
	{
		//load M and N from disk
		
		//merge M and N to U
		
		//write U to disk
		
	}
	
	protected void pushBlockForMerge(String blockFileName)
	{
		blocks.add(blockFileName);
	}
	
	protected void done()
	{
		isDone=true;
	}
	
	public PipedReader getReader()
	{
		return reader;
	}
}
