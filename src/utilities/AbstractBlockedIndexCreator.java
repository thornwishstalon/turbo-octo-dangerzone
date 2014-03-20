package utilities;

import java.util.ArrayList;

/***
 * 
 * @author F
 *
 */
public abstract class AbstractBlockedIndexCreator {
	private ArrayList<String> blocks;
	
	public AbstractBlockedIndexCreator() {
		blocks = new ArrayList<>();
	}
	
	protected void createIndex(){ //input TOKENSTREAM
		/*	while tokenstream is alive{
		 * 		buildIndexForBlock(tokenstream);
		 * 	}
		 * 	mergeBlocks();
		 * 
		 */
		
		
	}   
	
	protected  abstract void buildIndexForBlock(Object tokenstream); //input TOKENSTREAM TODO
	
	
	protected void mergeBlocks()
	{
		//binary merging?
		
	}
	
	protected void pushBlockForMerge(String blockFileName)
	{
		blocks.add(blockFileName);
	}
	
}
